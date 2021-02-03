package org.ga4gh.drs.utils.objectloader;

<<<<<<< HEAD
import java.time.LocalDateTime;
import java.util.List;

=======
>>>>>>> Add S3DrsObjectLoader and tests
import org.ga4gh.drs.model.AccessMethod;
import org.ga4gh.drs.model.AccessType;
import org.ga4gh.drs.model.Checksum;
import org.ga4gh.drs.model.ContentsObject;
import org.ga4gh.drs.model.DrsObject;
<<<<<<< HEAD

public class S3DrsObjectLoader extends AbstractDrsObjectLoader {

    public S3DrsObjectLoader(String objectId, String objectPath) {
        super(objectId, objectPath);
=======
import org.ga4gh.drs.utils.S3ClientRegionBasedProvider;
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
import java.util.UUID;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class S3DrsObjectLoader extends AbstractDrsObjectLoader {

    private S3Client client;

    private String bucket;
    private String key;

    private Supplier<HeadObjectResponse> headObjectResponse;

    // Store nested objects only if this object is a bundle to avoid repeat requests
    private Supplier<List<S3Object>> objects;

    public S3DrsObjectLoader(String objectId, String objectPath) {
        super(objectId, objectPath);
        URI uri = URI.create(objectPath);
        bucket = uri.getHost();
        // Strip leading /
        key = uri.getPath().replaceAll("^/+$", "");
        // Get client based on region
        client = S3ClientRegionBasedProvider.getClient(bucket);

        headObjectResponse = this::getHeadObjectResponse;
        objects = this::getObjects;
>>>>>>> Add S3DrsObjectLoader and tests
    }

    @Override
    public boolean exists() {
<<<<<<< HEAD
        // TODO implement stub method
        return false;
=======
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
>>>>>>> Add S3DrsObjectLoader and tests
    }

    @Override
    public boolean isBundle() {
<<<<<<< HEAD
        // TODO implement stub method
        return false;
=======
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
>>>>>>> Add S3DrsObjectLoader and tests
    }

    @Override
    public List<AccessMethod> generateAccessMethods() {
<<<<<<< HEAD
        // TODO implement stub method
        return null;
=======
        // TODO currently access ID has no meaning within the app, should be
        // used to populate a lookup cache
        String accessID = UUID.randomUUID().toString();
        AccessMethod accessMethod = new AccessMethod(accessID, AccessType.HTTPS);
        return Collections.singletonList(accessMethod);
>>>>>>> Add S3DrsObjectLoader and tests
    }

    @Override
    public List<ContentsObject> generateContents() {
<<<<<<< HEAD
        // TODO fill out stub method
        return null;
=======
        return objects.get().stream()
            .map(object -> new ContentsObject(object.key()))
            .collect(Collectors.toList());
>>>>>>> Add S3DrsObjectLoader and tests
    }

    @Override
    public DrsObject generateCustomDrsObjectProperties() {
<<<<<<< HEAD
        // TODO fill out stub method
        return null;
=======
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
>>>>>>> Add S3DrsObjectLoader and tests
    }

    @Override
    public List<Checksum> imputeChecksums() {
<<<<<<< HEAD
        // TODO implement stub method
        return null;
=======
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
>>>>>>> Add S3DrsObjectLoader and tests
    }

    @Override
    public long imputeSize() {
<<<<<<< HEAD
        // TODO implement stub method
        return 0;
=======
        if (isBundle()) {
            return objects.get().stream().mapToLong(S3Object::size).sum();
        } else {
            return headObjectResponse.get().contentLength();
        }
>>>>>>> Add S3DrsObjectLoader and tests
    }

    @Override
    public String imputeName() {
<<<<<<< HEAD
        // TODO implement stub method
        return null;
=======
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
>>>>>>> Add S3DrsObjectLoader and tests
    }

    @Override
    public String imputeMimeType() {
<<<<<<< HEAD
        // TODO implement stub method
        return null;
=======
        return isBundle()
            ? null
            : headObjectResponse.get().contentType();
>>>>>>> Add S3DrsObjectLoader and tests
    }

    @Override
    public LocalDateTime imputeCreatedTime() {
<<<<<<< HEAD
        // TODO implement stub method
        return null;
    }

    public AccessType getAccessType() {
        return AccessType.S3;
=======
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
>>>>>>> Add S3DrsObjectLoader and tests
    }
}
