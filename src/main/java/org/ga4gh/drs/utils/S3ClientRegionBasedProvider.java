package org.ga4gh.drs.utils;

import org.springframework.beans.factory.annotation.Autowired;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.HeadBucketRequest;
import software.amazon.awssdk.services.s3.model.S3Exception;

import java.util.HashMap;
import java.util.Map;

public class S3ClientRegionBasedProvider {

    @Autowired
    private static Region DEFAULT_REGION;

    public S3ClientRegionBasedProvider() {

    }

    private static S3Client mainClient = S3Client.builder()
        .region(DEFAULT_REGION)
        .build();

    private static Map<Region, S3Client> clients = new HashMap<>();

    public static S3Client getClient(String bucket) {
        Region region = S3ClientRegionBasedProvider.getRegion(bucket);
        return clients.computeIfAbsent(region, r ->
            S3Client.builder()
                .region(region)
                .build());
    }

    private static Region getRegion(String bucket) {
        // Request metadata about bucket
        HeadBucketRequest request = HeadBucketRequest.builder()
            .bucket(bucket)
            .build();

        try {
            mainClient.headBucket(request);
        } catch (S3Exception e) {
            // If permanently redirected, get correct region from http header
            if (e.statusCode() == 301) {
                return Region.of(
                    e.awsErrorDetails().sdkHttpResponse().headers().get("x-amz-bucket-region").get(0)
                );
            } else {
                throw e;
            }
        }
        // Default US_EAST_1
        return DEFAULT_REGION;
    }
}
