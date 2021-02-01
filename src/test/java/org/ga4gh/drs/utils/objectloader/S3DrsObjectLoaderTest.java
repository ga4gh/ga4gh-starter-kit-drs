package org.ga4gh.drs.utils.objectloader;

import org.ga4gh.drs.App;
import org.ga4gh.drs.AppConfig;
import org.ga4gh.drs.model.AccessMethod;
import org.ga4gh.drs.model.AccessType;
import org.ga4gh.drs.model.Checksum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;

@Test
@SpringBootTest
@ContextConfiguration(classes = {App.class, AppConfig.class})
public class S3DrsObjectLoaderTest extends AbstractTestNGSpringContextTests {

    @Autowired
    private DrsObjectLoaderFactory factory;

    private static final String BUCKET = "s3://czbiohub-tabula-muris/";

    private static final String TEST_DIR_1 = "facs_bam_files/";

    private static final String TEST_KEY_1 = "A1-B000126-3_39_F-1-1_R1.mus.Aligned.out.sorted.bam.bai";
    private static final String TEST_KEY_2 = "A1-B000126-3_39_F-1-1_R2.mus.Aligned.out.sorted.bam.bai";

    private static final String TEST_MD5_1 = "b8948758178275cbeb755f4c5ae1a741";
    private static final String TEST_MD5_2 = "3409dfe62b87843a0bedc8f9008fac97";

    private static final long TEST_SIZE_1 = 1430792;
    private static final long TEST_SIZE_2 = 1434896;

    private static final LocalDateTime TEST_CREATED_DATE_1 = LocalDateTime.parse("2018-09-12T19:55:15");
    private static final LocalDateTime TEST_CREATED_DATE_2 = TEST_CREATED_DATE_1;

    /* DATA PROVIDERS */

    @DataProvider(name = "existsCases")
    public Object[][] existsCases() {
        return new Object[][]{
            {TEST_KEY_1, BUCKET + TEST_DIR_1 + TEST_KEY_1, true},
            {TEST_KEY_2, BUCKET + TEST_DIR_1 + TEST_KEY_2, true},
            {"nonexistent.bai", BUCKET + TEST_DIR_1 + "nonexistent.bai", false},
        };
    }

    @DataProvider(name = "isBundleCases")
    public Object[][] isBundleCases() {
        return new Object[][]{
            {TEST_DIR_1, BUCKET + TEST_DIR_1, true},
            {TEST_KEY_1, BUCKET + TEST_DIR_1 + TEST_KEY_1, false},
        };
    }

    @DataProvider(name = "generateIdCases")
    public Object[][] generateIdCases() {
        return new Object[][]{
            {TEST_KEY_1, BUCKET + TEST_DIR_1 + TEST_KEY_1, TEST_KEY_1}
        };
    }

    @DataProvider(name = "generateSelfURICases")
    public Object[][] generateSelfURICases() {
        return new Object[][]{
            {TEST_KEY_1, BUCKET + TEST_DIR_1 + TEST_KEY_1, "drs://localhost:8080/" + TEST_KEY_1}
        };
    }

    @DataProvider(name = "generateAccessMethodsCases")
    public Object[][] generateAccessMethodsCases() {
        return new Object[][]{
            {TEST_KEY_1, BUCKET + TEST_DIR_1 + TEST_KEY_1}
        };
    }

    @DataProvider(name = "imputeChecksumsCases")
    public Object[][] imputeChecksumsCases() {
        return new Object[][]{
            {TEST_KEY_1, BUCKET + TEST_DIR_1 + TEST_KEY_1, false, TEST_MD5_1},
            {TEST_KEY_2, BUCKET + TEST_DIR_1 + TEST_KEY_2, false, TEST_MD5_2},
            // TODO: add bundle cases, the Tabula Muris bundles are too large to feasibly run tests on
        };
    }

    @DataProvider(name = "imputeSizeCases")
    public Object[][] imputeSizeCases() {
        return new Object[][]{
            {TEST_KEY_1, BUCKET + TEST_DIR_1 + TEST_KEY_1, TEST_SIZE_1},
            {TEST_KEY_2, BUCKET + TEST_DIR_1 + TEST_KEY_2, TEST_SIZE_2},
            // TODO: add bundle cases, the Tabula Muris bundles are too large to feasibly run tests on
        };
    }

    @DataProvider(name = "imputeNameCases")
    public Object[][] imputeNameCases() {
        return new Object[][]{
            {TEST_KEY_1, BUCKET + TEST_DIR_1 + TEST_KEY_1, TEST_KEY_1},
            {TEST_KEY_2, BUCKET + TEST_DIR_1 + TEST_KEY_2, TEST_KEY_2},
            {TEST_DIR_1, BUCKET + TEST_DIR_1, TEST_DIR_1.substring(0, TEST_DIR_1.length() - 1)},
        };
    }


    @DataProvider(name = "imputeMimeTypeCases")
    public Object[][] imputeMimeTypeCases() {
        return new Object[][]{
            {TEST_KEY_1, BUCKET + TEST_DIR_1 + TEST_KEY_1, "binary/octet-stream"},
            {TEST_KEY_2, BUCKET + TEST_DIR_1 + TEST_KEY_2, "binary/octet-stream"},
            {TEST_DIR_1, BUCKET + TEST_DIR_1, null},
        };
    }

    @DataProvider(name = "imputeCreatedTimeCases")
    public Object[][] imputeCreatedTimeCases() {
        return new Object[][]{
            {TEST_KEY_1, BUCKET + TEST_DIR_1 + TEST_KEY_1, TEST_CREATED_DATE_1},
            {TEST_KEY_2, BUCKET + TEST_DIR_1 + TEST_KEY_2, TEST_CREATED_DATE_2},
            {"nonexistent_dir/", BUCKET + "nonexistent_dir/", LocalDateTime.ofEpochSecond(0, 0, ZoneOffset.UTC)},
            // TODO: add bundle cases, the Tabula Muris bundles are too large to feasibly run tests on
        };
    }

    /* TEST METHODS */

    @Test(dataProvider = "existsCases")
    public void testExists(String objectId, String objectPath, boolean expExists) {
        boolean exists = factory.createDrsObjectLoader(AccessType.S3, objectId, objectPath).exists();
        Assert.assertEquals(exists, expExists);
    }

    @Test(dataProvider = "isBundleCases")
    public void testIsBundle(String objectId, String objectPath, boolean expIsBundle) {
        boolean isBundle = factory.createDrsObjectLoader(AccessType.S3, objectId, objectPath).isBundle();
        Assert.assertEquals(isBundle, expIsBundle);
    }

    @Test(dataProvider = "generateIdCases")
    public void testGenerateId(String objectId, String objectPath, String expId) {
        String actualId = factory.createDrsObjectLoader(AccessType.FILE, objectId, objectPath).generateId();
        Assert.assertEquals(actualId, expId);
    }

    @Test(dataProvider = "generateSelfURICases")
    public void testGenerateSelfURI(String objectId, String objectPath, String expSelfURI) {
        String selfURI = factory.createDrsObjectLoader(AccessType.FILE, objectId, objectPath).generateSelfURI().toString();
        Assert.assertEquals(selfURI, expSelfURI);
    }

    @Test(dataProvider = "generateAccessMethodsCases")
    public void testGenerateAccessMethods(String objectId, String objectPath) {
        List<AccessMethod> accessMethods = factory.createDrsObjectLoader(AccessType.S3, objectId, objectPath).generateAccessMethods();
        Assert.assertEquals(accessMethods.size(), 1);
        Assert.assertEquals(accessMethods.get(0).getType(), AccessType.HTTPS);
    }

    @Test(dataProvider = "imputeChecksumsCases")
    public void testImputeChecksums(String objectId, String objectPath, boolean expError, String expDigest) {
        List<Checksum> checksums = factory.createDrsObjectLoader(AccessType.S3, objectId, objectPath).imputeChecksums();
        if (expError) {
            Assert.assertEquals(checksums.size(), 0);
        } else {
            Assert.assertEquals(checksums.size(), 1);
            Checksum checksum = checksums.get(0);
            Assert.assertEquals(checksum.getType(), "md5");
            Assert.assertEquals(checksum.getChecksum(), expDigest);
        }
    }

    @Test(dataProvider = "imputeSizeCases")
    public void testImputeSize(String objectId, String objectPath, long expSize) {
        long size = factory.createDrsObjectLoader(AccessType.S3, objectId, objectPath).imputeSize();
        Assert.assertEquals(size, expSize);
    }

    @Test(dataProvider = "imputeNameCases")
    public void testImputeName(String objectId, String objectPath, String expName) {
        String name = factory.createDrsObjectLoader(AccessType.S3, objectId, objectPath).imputeName();
        Assert.assertEquals(name, expName);
    }

    @Test(dataProvider = "imputeMimeTypeCases")
    public void testImputeMimeType(String objectId, String objectPath, String expMimeType) {
        String mimeType = factory.createDrsObjectLoader(AccessType.S3, objectId, objectPath).imputeMimeType();
        Assert.assertEquals(mimeType, expMimeType);
    }

    @Test(dataProvider = "imputeCreatedTimeCases")
    public void testImputeCreatedTime(String objectId, String objectPath, LocalDateTime expTime) {
        LocalDateTime createdTime = factory.createDrsObjectLoader(AccessType.S3, objectId, objectPath).imputeCreatedTime();
        Assert.assertNotNull(createdTime);
        Assert.assertEquals(createdTime, expTime);
    }
}
