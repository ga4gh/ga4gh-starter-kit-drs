package org.ga4gh.starterkit.drs.utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.ga4gh.starterkit.drs.model.Checksum;
import org.ga4gh.starterkit.drs.model.DrsObject;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.codec.digest.MessageDigestAlgorithms;
import org.apache.commons.lang3.StringUtils;

/* Recursively calculates checksum values for bundle-based DrsObjects based on 
 * the checksums of the blob-based DrsObject children it has
 */
public class BundleRecursiveChecksumCalculator {

    public static List<Checksum> getChecksums(DrsObject drsObject) {
        return checksumMapToList(recursiveCalculateChecksums(drsObject));
    }

    public static Map<String, String> recursiveCalculateChecksums(DrsObject parentDrsObject) {
        Map<String, String> checksumMap = null;
        List<DrsObject> childrenDrsObjects = parentDrsObject.getDrsObjectChildren();

        if (childrenDrsObjects != null) {
            if (childrenDrsObjects.size() == 0) {
                checksumMap = createChecksumMapFromDrsObjectBlob(parentDrsObject);
            } else {
                List<Map<String, String>> childChecksumMaps = new ArrayList<>();
                for (int i = 0; i < childrenDrsObjects.size(); i++) {
                    childChecksumMaps.add(recursiveCalculateChecksums(childrenDrsObjects.get(i)));
                }
                checksumMap = mergeChecksumMaps(childChecksumMaps);
            }
        }
        return checksumMap;
    }

    private static List<Checksum> checksumMapToList(Map<String, String> checksumMap) {
        List<Checksum> checksumList = new ArrayList<>();
        for (String key : checksumMap.keySet()) {
            Checksum checksum = new Checksum();
            checksum.setType(key);
            checksum.setChecksum(checksumMap.get(key));
            checksumList.add(checksum);
        }
        return checksumList;
    }

    private static Map<String, String> createChecksumMapFromDrsObjectBlob(DrsObject drsObject) {
        Map<String, String> checksumMap = new HashMap<>();
        for (Checksum checksum: drsObject.getChecksums()) {
            checksumMap.put(checksum.getType(), checksum.getChecksum());
        }
        return checksumMap;
    }

    private static Map<String, String> mergeChecksumMaps(List<Map<String, String>> checksumMaps) {
        // initial setup
        Map<String, String> mergedChecksumMap = new HashMap<>();
        Map<String, Integer> checksumTypeCounts = new HashMap<>();
        int nMaps = checksumMaps.size();

        // first loop, determine the checksum types shared by all children
        for (Map<String, String> checksumMap : checksumMaps) {
            for (String key : checksumMap.keySet()) {
                if (!checksumTypeCounts.containsKey(key)) {
                    checksumTypeCounts.put(key, 0);
                }
                checksumTypeCounts.put(key, checksumTypeCounts.get(key) + 1);
            }
        }

        // second loop, for each final checksum type, sort and concatenate values
        for (String key : checksumTypeCounts.keySet()) {
            if (checksumTypeCounts.get(key) == nMaps) {
                List<String> checksumValues = new ArrayList<>();
                for (Map<String, String> checksumMap : checksumMaps) {
                    checksumValues.add(checksumMap.get(key));
                }
                Collections.sort(checksumValues);
                String concatenated = StringUtils.join(checksumValues, "");

                String algorithm = null;
                switch (key) {
                    case "md5":
                        algorithm = MessageDigestAlgorithms.MD5;
                        break;
                    case "sha1":
                        algorithm = MessageDigestAlgorithms.SHA_1;
                        break;
                    case "sha256":
                        algorithm = MessageDigestAlgorithms.SHA_256;
                        break;
                }
                String concatDigest = new DigestUtils(algorithm).digestAsHex(concatenated);
                mergedChecksumMap.put(key, concatDigest);
            }
        }
        return mergedChecksumMap;
    }
}