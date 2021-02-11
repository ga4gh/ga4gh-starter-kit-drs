package org.ga4gh.drs.utils;

import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;

import java.util.HashMap;
import java.util.Map;

public class S3ClientRegionBasedProvider {

    private static Map<Region, S3Client> clients = new HashMap<>();

    public static S3Client getClient(String regionString) {
        Region region = Region.of(regionString);
        return clients.computeIfAbsent(region, r ->
            S3Client.builder()
                .region(region)
                .build());
    }
}
