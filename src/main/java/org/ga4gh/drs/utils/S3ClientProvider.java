package org.ga4gh.drs.utils;

import org.ga4gh.drs.utils.datasource.S3DataSource;
import software.amazon.awssdk.auth.credentials.ProfileCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.S3ClientBuilder;

import java.util.HashMap;
import java.util.Map;

/**
 * Utility class to create and cache expensive S3Clients configured according to
 * region and profile of data source
 */
public class S3ClientProvider {

    private static Map<S3DataSource, S3Client> clients = new HashMap<>();

    public static S3Client getClient(S3DataSource source) {
        return clients.computeIfAbsent(source, s -> {
            Region region = Region.of(s.getRegion());
            String profile = s.getProfile();
            S3ClientBuilder builder = S3Client.builder().region(region);
            if (profile != null) {
                builder.credentialsProvider(ProfileCredentialsProvider.builder()
                    .profileName(profile)
                    .build()
                );
            }
            return builder.build();
        });
    }
}
