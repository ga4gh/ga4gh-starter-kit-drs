package org.ga4gh.drs.utils.objectloader;

import org.ga4gh.drs.model.AccessMethod;
import org.ga4gh.drs.model.AccessType;
import org.ga4gh.drs.model.AccessURL;
import org.ga4gh.drs.model.Checksum;
import org.ga4gh.drs.model.ContentsObject;
import org.ga4gh.drs.model.DrsObject;
import org.springframework.util.DigestUtils;
import software.amazon.awssdk.core.ResponseInputStream;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;
import software.amazon.awssdk.services.s3.model.HeadBucketRequest;
import software.amazon.awssdk.services.s3.model.HeadObjectRequest;
import software.amazon.awssdk.services.s3.model.HeadObjectResponse;
import software.amazon.awssdk.services.s3.model.ListObjectsV2Request;
import software.amazon.awssdk.services.s3.model.ListObjectsV2Response;
import software.amazon.awssdk.services.s3.model.S3Exception;
import software.amazon.awssdk.services.s3.model.S3Object;

import java.io.IOException;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class S3DrsObjectLoader extends AbstractDrsObjectLoader {

    private String bucket;
    private String key;
    private String region;
    private S3Client client;

    private Supplier<HeadObjectResponse> headObjectResponse;

    // Store nested objects only if this object is a bundle to avoid repeat requests
    private Supplier<List<S3Object>> objects;

    public S3DrsObjectLoader(String objectId, String region, String bucket, String key, S3Client client) {
        super(objectId, "s3://" + bucket + "/" + key);
        this.bucket = bucket;
        this.key = key;
        this.region = region;
        // Get client based on region
        this.client = client;

        headObjectResponse = this::getHeadObjectResponse;
        objects = this::getObjects;
    }

    @Override
    public boolean exists() {
        // Check if bucket exists
        try {
            HeadBucketRequest request = HeadBucketRequest.builder()
                .bucket(bucket)
                .build();
            client.headBucket(request);
        } catch (S3Exception e) {
            return false;
        }

        // Check if key exists inside bucket
        try {
            if (isBundle()) {
                ListObjectsV2Request request = ListObjectsV2Request.builder()
                    .bucket(bucket)
                    .prefix(key)
                    .build();

                ListObjectsV2Response response = client.listObjectsV2(request);
                // A directory exists if there are any keys with it as a prefix
                return !response.contents().isEmpty();
            } else {
                headObjectResponse.get();
                // A file exists if the head-object request returns successfully
                return true;
            }
        } catch (S3Exception e) {
            return false;
        }
    }

    @Override
    public boolean isBundle() {
        return key.endsWith("/");
    }

    private HeadObjectResponse getHeadObjectResponse() {
        HeadObjectRequest request = HeadObjectRequest.builder()
            .bucket(bucket)
            .key(key)
            .build();
        return client.headObject(request);
    }

    private List<S3Object> getObjects() {
        return StreamSupport.stream(
            Spliterators.spliteratorUnknownSize(
                new S3ContentsLazyIterator(key),
                Spliterator.ORDERED
            ),
            false
        ).collect(Collectors.toList());
    }

    @Override
    public List<AccessMethod> generateAccessMethods() {
        AccessURL accessURL = new AccessURL(URI.create(
            String.format("https://s3.%s.amazonaws.com/%s/%s", region, bucket, key)));
        AccessMethod accessMethod = new AccessMethod(accessURL, AccessType.HTTPS, region);
        return Collections.singletonList(accessMethod);
    }

    @Override
    public List<ContentsObject> generateContents() {
        return objects.get().stream()
            .map(object -> new ContentsObject(object.key()))
            .collect(Collectors.toList());
    }

    @Override
    public DrsObject generateCustomDrsObjectProperties() {
        DrsObject drsObject = new DrsObject();
        drsObject.setVersion(headObjectResponse.get().versionId());
        return drsObject;
    }

    private String md5FromKey(String key) {
        GetObjectRequest request = GetObjectRequest.builder()
            .bucket(bucket)
            .key(key)
            .build();

        try (ResponseInputStream<GetObjectResponse> response = client.getObject(request)) {
            return DigestUtils.md5DigestAsHex(response);
        } catch (IOException | S3Exception e) {
            throw new RuntimeException();
        }
    }

    @Override
    public List<Checksum> imputeChecksums() {
        try {
            String digest;
            if (isBundle()) {
                String concatenatedChecksums = objects.get().stream()
                    .map(o -> md5FromKey(o.key()))
                    .sorted()
                    .collect(Collectors.joining());
                digest = DigestUtils.md5DigestAsHex(concatenatedChecksums.getBytes(StandardCharsets.US_ASCII));
            } else {
                digest = md5FromKey(key);
            }
            return Collections.singletonList(new Checksum(digest, "md5"));
        } catch (RuntimeException e) {
            return Collections.emptyList();
        }
    }

    @Override
    public long imputeSize() {
        if (isBundle()) {
            return objects.get().stream().mapToLong(S3Object::size).sum();
        } else {
            return headObjectResponse.get().contentLength();
        }
    }

    @Override
    public String imputeName() {
        if (isBundle()) {
            // Strip trailing / from folders and replace internal / with _
            // to make name conform with schema on best effort basis
            return key
                .substring(0, key.length() - 1)
                .replace("/", "_");
        } else {
            // Only take last component of key if object is inside a directory
            int lastSlash = key.lastIndexOf("/");
            return lastSlash == -1
                ? key
                : key.substring(lastSlash + 1);
        }
    }

    @Override
    public String imputeMimeType() {
        return isBundle()
            ? null
            : headObjectResponse.get().contentType();
    }

    @Override
    public LocalDateTime imputeCreatedTime() {
        /*
        S3 does not support the concept of updating objects, only replacing them
        with new objects, so created time is indistinguishable from updated time
         */
        Instant lastModified;
        if (isBundle()) {
            // Directories are not real files in S3 so they don't have an associated creation date
            // Use the age of the oldest file inside or else Unix epoch
            lastModified = objects.get().stream()
                .map(S3Object::lastModified)
                .min(Instant::compareTo)
                .orElse(Instant.EPOCH);
        } else {
            lastModified = headObjectResponse.get().lastModified();
        }
        return LocalDateTime.ofInstant(lastModified, ZoneId.of("UTC"));
    }

    @Override
    public AccessType getAccessType() {
        return AccessType.S3;
    }

    /**
     * Iterate over the S3Objects inside a bundle using continuation tokens
     * if output is truncated
     */
    private class S3ContentsLazyIterator implements Iterator<S3Object> {

        private String key;
        private boolean truncated;
        private Iterator<S3Object> inner;

        public S3ContentsLazyIterator(String key) {
            this.key = key;
        }

        @Override
        public boolean hasNext() {
            // First iteration
            if (inner == null) {
                ListObjectsV2Request request = ListObjectsV2Request.builder()
                    .bucket(bucket)
                    .prefix(this.key)
                    .build();

                ListObjectsV2Response response = client.listObjectsV2(request);
                inner = response.contents().iterator();
                truncated = response.isTruncated();
                if (truncated) this.key = response.continuationToken();
            }

            if (inner.hasNext()) {
                return true;
            } else if (truncated) {
                ListObjectsV2Request request = ListObjectsV2Request.builder()
                    .bucket(bucket)
                    .continuationToken(this.key)
                    .build();

                ListObjectsV2Response response = client.listObjectsV2(request);
                inner = response.contents().iterator();
                truncated = response.isTruncated();
                if (truncated) this.key = response.continuationToken();
                return true;
            } else {
                return false;
            }
        }

        @Override
        public S3Object next() {
            return inner.next();
        }
    }
}
