package org.ga4gh.starterkit.drs.utils.hibernate;

import org.ga4gh.starterkit.common.hibernate.HibernateEntity;
import org.ga4gh.starterkit.common.hibernate.HibernateUtil;
import org.ga4gh.starterkit.common.util.logging.LoggingUtil;
import org.ga4gh.starterkit.drs.model.Checksum;
import org.ga4gh.starterkit.drs.model.DrsObject;
import org.ga4gh.starterkit.drs.model.FileAccessObject;
import org.ga4gh.starterkit.drs.model.PassportVisa;
import org.ga4gh.starterkit.drs.utils.BundleRecursiveChecksumCalculator;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.PersistenceException;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.lang.reflect.Method;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Provides access to DRS entities/tables in the database, enabling access, creation,
 * updating, and deleting of DRSObjects and associated entities
 */
public class DrsHibernateUtil extends HibernateUtil {

    /**
     * Fully loads a DRS Object, performing recursive inspection/loading of child
     * objects (in the case of bundles)
     *
     * @param id DRSObject identifier
     * @param recursiveChildLoad if true, recursively load the children of each child until termini DRSObjects have been reached
     * @return DRSObject with all necessary attributes loaded
     * @throws HibernateException if problem encountered while interacting with db
     */

    @Autowired
    private LoggingUtil loggingUtil;

    @Autowired
    private HibernateUtil hibernateUtil;

    private SessionFactory sessionFactory;

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
                    drsObject.setChecksums(BundleRecursiveChecksumCalculator.getChecksums(drsObject));
                }
            }
        } catch (PersistenceException e) {
            loggingUtil.error("Exception occurred: persistence exception" + e.getMessage());
            throw new HibernateException(e.getMessage());
        } catch (Exception e) {
            loggingUtil.error("Exception occurred: persistence exception" + e.getMessage());
            throw new HibernateException(e.getMessage());
        } finally {
            endTransaction(session);
        }
        return drsObject;
    }

    /**
     * Recursive function that loads all the children associated to a parent DrsObject.
     * Recursively calls this function again if a child object has children itself.
     *
     * @param parentDrsObject root DRSObject to load all children for
     * @return byte size sum of all recursive objects under the DrsObject node
     */
    private void recursiveDrsObjectChildLoad(DrsObject parentDrsObject) {
        List<DrsObject> childrenDrsObjects = parentDrsObject.getDrsObjectChildren();

        if (childrenDrsObjects != null) {
            if (childrenDrsObjects.size() != 0) {
                for (int i = 0; i < childrenDrsObjects.size(); i++) {
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
                for (int i = 0; i < childrenDrsObjects.size(); i++) {
                    sizeSum += recursiveSize(childrenDrsObjects.get(i));
                }
            }
        }
        return sizeSum;
    }

    /**
     * Retrieve a list of objects from the database
     *
     * @param <T>         The entity class to be retrieved
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

    public PassportVisa findPassportVisa(String visaName, String visaIssuer) {
        Session session = newTransaction();
        PassportVisa visa = null;
        try {
            CriteriaBuilder cb = session.getCriteriaBuilder();
            CriteriaQuery<PassportVisa> cq = cb.createQuery(PassportVisa.class);
            Root<PassportVisa> root = cq.from(PassportVisa.class);
            Predicate[] predicates = new Predicate[2];
            predicates[0] = cb.equal(root.get("name"), visaName);
            predicates[1] = cb.equal(root.get("issuer"), visaIssuer);
            cq.select(root).where(predicates);
            Query<PassportVisa> query = session.createQuery(cq);
            List<PassportVisa> results = query.getResultList();
            if (results.size() != 1) {
                String exceptionMessage = "no unique visa found";
                loggingUtil.error("Exception occurred: " + exceptionMessage);
                throw new Exception(exceptionMessage);
            }
            visa = results.get(0);
        } catch (Exception ex) {
            loggingUtil.error("Exception occurred: " + ex.getMessage());
        } finally {
            endTransaction(session);
        }
        return visa;
    }


    public void insertBulkDrsObjects(MultipartFile file) throws Exception {
        List<DrsObject> dataToInsert = new ArrayList<DrsObject>();
        int totalSize = 0;
        int processedRecords = 0;
        int successfulRecords = 0;
        List<DrsObject> failedRecords = new CopyOnWriteArrayList<>();

        try {
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream()))) {
                dataToInsert = prepareDataForInsert(reader);
                totalSize = dataToInsert.size();
            } catch (IOException ex) {
                loggingUtil.error("Exception occurred during read: " + ex);
            }

            int batchSize = 1000;
            loggingUtil.info("Starting bulk insert for "+totalSize+" records.");
            for (int i = 0; i < totalSize; i += batchSize) {
                int toIndex = Math.min(i + batchSize, totalSize);
                List<DrsObject> batch = dataToInsert.subList(i, toIndex);
                failedRecords = performBulkInsertWithExecutor(batch, 8, batchSize, failedRecords);

                int batchFailures = failedRecords.size(); // Capture failed count before next batch
                successfulRecords += (batch.size() - batchFailures);
                processedRecords += batch.size();
                loggingUtil.info("Processed "+processedRecords+" records out of "+totalSize);
            }
            loggingUtil.info("Completed bulk insert for "+totalSize+" records.");
            loggingUtil.info("Inserted: "+ (totalSize - failedRecords.size()));
            loggingUtil.info("Failed to insert: "+ failedRecords.size());
        } catch (Exception ex) {
            loggingUtil.error("Exception during bulk insert: " + ex.getMessage()+ ex);
            throw ex;
        }
    }

    private List<DrsObject> prepareDataForInsert(BufferedReader reader) throws IOException {
        List<DrsObject> dataToInsert = new ArrayList<>();
        String row;
        reader.readLine();
        DrsObject drsObject = null;
        while ((row = reader.readLine()) != null) {
            drsObject = createAndReturnDrsObject(row);
            dataToInsert.add(drsObject);
        }
        return dataToInsert;
    }

    private DrsObject createAndReturnDrsObject(String line) {
        String[] fields = line.split(",");
        String description = Arrays.asList(fields[1].split("/")).get(fields[1].split("/").length-1);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'Z'");

        DrsObject drsObject = new DrsObject();
        drsObject.setId(fields[0]);
        drsObject.setDescription(description);
        drsObject.setName(description);
        drsObject.setMimeType(fields[2]);
        drsObject.setSize(Long.valueOf(fields[3]));
        drsObject.setUpdatedTime(LocalDateTime.parse(fields[4], formatter));
        drsObject.setCreatedTime(LocalDateTime.parse(fields[5], formatter));
        if(fields[1] != null) {
            List<FileAccessObject> files = createAndReturnFileObjects(drsObject, fields[1]);
            drsObject.setFileAccessObjects(files);
        }
        if(fields[6] != null || fields[7] != null || fields[8] != null) {
            List<Checksum> checksums = createAndReturnChecksums(drsObject, fields[6], fields[7], fields[8]);
            drsObject.setChecksums(checksums);
        }

        return drsObject;
    }

    private List<Checksum> createAndReturnChecksums(DrsObject drsObject, String cMD5, String cSHA1, String cSHA256) {
        Checksum md5 = new Checksum(cMD5,"md5", drsObject);
        Checksum sha1 = new Checksum(cSHA1,"sha1", drsObject);
        Checksum sha256 = new Checksum(cSHA256,"sha256", drsObject);
        /*
        if(drsObject.getId().equals("")) {
            sha256 = new Checksum(cSHA256,"sha512", drsObject);
        }
        */

        return List.of(md5, sha1, sha256);
    }

    private List<FileAccessObject> createAndReturnFileObjects(DrsObject drsObject, String field) {
        String[] filePaths = field.split(",");
        return Stream.of(filePaths)
                .map(path -> new FileAccessObject(drsObject, path))
                .collect(Collectors.toList());
    }

    public <I extends Serializable, T extends HibernateEntity<I>> List<DrsObject> performBulkInsertWithExecutor(
            List<DrsObject> objectList, int numThreads, int batchSize, List<DrsObject> failedRecords) throws Exception {

        ExecutorService executor = Executors.newFixedThreadPool(numThreads);
        List<Future<List<DrsObject>>> futures = new ArrayList<>();
        int totalSize = objectList.size();

        try {
            for (int i = 0; i < totalSize; i += batchSize) {
                List<DrsObject> batch = objectList.subList(i, Math.min(i + batchSize, totalSize));

                futures.add(executor.submit(() -> {
                    List<DrsObject> failedBatch = new ArrayList<>();

                    Session session = null;
                    Transaction tx = null;

                    try {
                        Method method = HibernateUtil.class.getDeclaredMethod("getSessionFactory");
                        method.setAccessible(true);  // Bypass private access
                        SessionFactory sessionFactory = (SessionFactory) method.invoke(hibernateUtil);

                        session = sessionFactory.openSession();
                        tx = session.beginTransaction();
                        for (DrsObject object : batch) {
                            try {
                                session.save(object);
                            } catch (HibernateException ex) {
                                loggingUtil.error("HibernateException occurred: " + ex);
                                failedBatch.add(object);
                            }
                        }
                        session.flush();
                        session.clear();
                        tx.commit();
                    } catch (Exception ex) {
                        if (tx != null) {
                            tx.rollback();
                        }
                        loggingUtil.error("Transaction rolled back due to: " + ex);
                        throw ex;

                    } finally {
                        if (session != null && session.isOpen()) {
                            session.close();
                        }
                    }
                    return failedBatch;
                }));
            }

            List<DrsObject> allFailedRecords = new ArrayList<>();
            for (Future<List<DrsObject>> future : futures) {
                allFailedRecords.addAll(future.get());
            }
            return allFailedRecords;
        } finally {
            executor.shutdown();
        }
    }
}
