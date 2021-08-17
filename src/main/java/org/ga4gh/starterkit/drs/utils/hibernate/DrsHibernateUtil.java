package org.ga4gh.starterkit.drs.utils.hibernate;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.persistence.PersistenceException;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import org.ga4gh.starterkit.drs.model.Checksum;
import org.ga4gh.starterkit.drs.model.DrsObject;
import org.apache.commons.lang3.StringUtils;
import org.ga4gh.starterkit.common.hibernate.HibernateEntity;
import org.ga4gh.starterkit.common.hibernate.HibernateUtil;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.codec.digest.MessageDigestAlgorithms;

/**
 * Provides access to DRS entities/tables in the database, enabling access, creation,
 * updating, and deleting of DRSObjects and associated entities
 */
public class DrsHibernateUtil extends HibernateUtil {

    /**
     * Fully loads a DRS Object, performing recursive inspection/loading of child
     * objects (in the case of bundles)
     * @param id DRSObject identifier
     * @param recursiveChildLoad if true, recursively load the children of each child until termini DRSObjects have been reached 
     * @return DRSObject with all necessary attributes loaded
     * @throws HibernateException if problem encountered while interacting with db
     */
    public DrsObject loadDrsObject(String id, boolean recursiveChildLoad) throws HibernateException {
        Session session = newTransaction();
        DrsObject drsObject = null;
        try {
            drsObject = session.get(DrsObject.class, id);
            if (drsObject != null) {
                drsObject.loadRelations();

                // detach entity so computed fields (size, checksum)
                // aren't saved to db
                session.evict(drsObject);
                if (recursiveChildLoad) {
                    recursiveDrsObjectChildLoad(drsObject);
                    drsObject.setSize(recursiveSize(drsObject));
                    drsObject.setChecksums(checksumMapToList(recursiveChecksums(drsObject)));
                }
            }
        } catch (PersistenceException e) {
            throw new HibernateException(e.getMessage());
        } catch (Exception e) {
            throw new HibernateException(e.getMessage());
        } finally {
            endTransaction(session);
        }
        return drsObject;
    }

    /**
     * Recursive function that loads all the children associated to a parent DrsObject.
     * Recursively calls this function again if a child object has children itself.
     * @param parentDrsObject root DRSObject to load all children for
     * @return byte size sum of all recursive objects under the DrsObject node
     */
    private void recursiveDrsObjectChildLoad(DrsObject parentDrsObject) {
        List<DrsObject> childrenDrsObjects = parentDrsObject.getDrsObjectChildren();

        if (childrenDrsObjects != null) {
            if (childrenDrsObjects.size() != 0) {
                for (int i = 0; i < childrenDrsObjects.size(); i ++) {
                    childrenDrsObjects.get(i).loadRelations();
                    recursiveDrsObjectChildLoad(childrenDrsObjects.get(i));
                }
            }
        }
    }

    private Long recursiveSize(DrsObject parentDrsObject) {
        Long sizeSum = 0L;
        List<DrsObject> childrenDrsObjects = parentDrsObject.getDrsObjectChildren();

        if (childrenDrsObjects != null) {
            if (childrenDrsObjects.size() == 0) {
                sizeSum = parentDrsObject.getSize();
                sizeSum = sizeSum == null ? 0L : sizeSum;
            } else {
                for (int i = 0; i < childrenDrsObjects.size(); i ++) {
                    sizeSum += recursiveSize(childrenDrsObjects.get(i));
                }
            }
        }
        return sizeSum;
    }

    private Map<String, String> recursiveChecksums(DrsObject parentDrsObject) {
        Map<String, String> checksumMap = null;
        List<DrsObject> childrenDrsObjects = parentDrsObject.getDrsObjectChildren();

        if (childrenDrsObjects != null) {
            if (childrenDrsObjects.size() == 0) {
                checksumMap = createChecksumMapFromDrsObjectBlob(parentDrsObject);
            } else {
                List<Map<String, String>> childChecksumMaps = new ArrayList<>();
                for (int i = 0; i < childrenDrsObjects.size(); i++) {
                    childChecksumMaps.add(recursiveChecksums(childrenDrsObjects.get(i)));
                }
                checksumMap = mergeChecksumMaps(childChecksumMaps);
            }
        }
        return checksumMap;
    }

    private List<Checksum> checksumMapToList(Map<String, String> checksumMap) {
        List<Checksum> checksumList = new ArrayList<>();
        for (String key : checksumMap.keySet()) {
            Checksum checksum = new Checksum();
            checksum.setType(key);
            checksum.setChecksum(checksumMap.get(key));
            checksumList.add(checksum);
        }
        return checksumList;
    }

    private Map<String, String> createChecksumMapFromDrsObjectBlob(DrsObject drsObject) {
        Map<String, String> checksumMap = new HashMap<>();
        for (Checksum checksum: drsObject.getChecksums()) {
            checksumMap.put(checksum.getType(), checksum.getChecksum());
        }
        return checksumMap;
    }

    private Map<String, String> mergeChecksumMaps(List<Map<String, String>> checksumMaps) {
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

    /**
     * Retrieve a list of objects from the database
     * @param <T> The entity class to be retrieved
     * @param entityClass The entity class to be retrieved
     * @return List of entity objects
     */
    public <T extends HibernateEntity<? extends Serializable>> List<T> getEntityList(Class<T> entityClass) {
        Session session = newTransaction();
        List<T> entities = null;
        try {
            CriteriaBuilder builder = session.getCriteriaBuilder();
            CriteriaQuery<T> criteria = builder.createQuery(entityClass);
            criteria.from(entityClass);
            entities = session.createQuery(criteria).getResultList();
        } finally {
            endTransaction(session);
        }
        return entities;
    }
}
