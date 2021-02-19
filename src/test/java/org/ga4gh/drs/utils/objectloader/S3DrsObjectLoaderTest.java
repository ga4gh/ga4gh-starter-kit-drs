package org.ga4gh.drs.utils.objectloader;

import org.ga4gh.drs.App;
import org.ga4gh.drs.AppConfig;
import org.ga4gh.drs.model.AccessMethod;
import org.ga4gh.drs.model.AccessType;
import org.ga4gh.drs.model.Checksum;
import org.ga4gh.drs.model.ContentsObject;
import org.ga4gh.drs.utils.datasource.S3DataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.time.LocalDateTime;
import java.util.List;

@Test(groups = {"secure"})
@SpringBootTest
@ContextConfiguration(classes = {App.class, AppConfig.class})
public class S3DrsObjectLoaderTest extends AbstractTestNGSpringContextTests {

    @Autowired
    private DrsObjectLoaderFactory factory;

    private static final String PROFILE = "ga4gh-serviceaccount-basic";


    /**
     * GA4GH Demo Test Data, identical to the local file test data, but hosted on an s3 bucket
     */
    private static final String REGION_1 = "us-east-2";
    private static final String BUCKET_1 = "ga4gh-demo-data";
    private static final String PREFIX_1 = "S3DemoData.";
    private static final String ROOT_DIR_1 = "drs/phenopackets/";

    private static final S3DataSource GA4GH_SERVICE_SOURCE =
        new S3DataSource(PREFIX_1, REGION_1, BUCKET_1, ROOT_DIR_1, PROFILE);

    private static final String TEST_KEY_1 = PREFIX_1 + "Cao/Patient1.json";
    private static final String TEST_KEY_2 = PREFIX_1 + "Zapata/Patient1.json";
    private static final String TEST_DIR_1 = PREFIX_1 + "Cao/";

    private static final long TEST_SIZE_1 = 4257;
    private static final long TEST_SIZE_2 = 15826;

    private static final String TEST_NAME_1 = (ROOT_DIR_1 + TEST_KEY_1.replaceFirst(PREFIX_1, ""))
        .replace('/', '_');
    private static final String TEST_NAME_2 = (ROOT_DIR_1 + TEST_KEY_2.replaceFirst(PREFIX_1, ""))
        .replace('/', '_');
    private static final String TEST_DIR_NAME_1 = (ROOT_DIR_1 + TEST_DIR_1.replaceFirst(PREFIX_1, ""))
        .replaceAll("/$", "")
        .replace('/', '_');

    /* DATA PROVIDERS */

    @DataProvider(name = "existsCases")
    public Object[][] existsCases() {
        return new Object[][]{
            {GA4GH_SERVICE_SOURCE, TEST_KEY_1, true},
            {GA4GH_SERVICE_SOURCE, TEST_KEY_2, true},
            {GA4GH_SERVICE_SOURCE, "nonexistent.bai", false},
        };
    }

    @DataProvider(name = "isBundleCases")
    public Object[][] isBundleCases() {
        return new Object[][]{
            {GA4GH_SERVICE_SOURCE, TEST_DIR_1, true},
            {GA4GH_SERVICE_SOURCE, TEST_KEY_1, false},
        };
    }

    @DataProvider(name = "generateIdCases")
    public Object[][] generateIdCases() {
        return new Object[][]{
            {GA4GH_SERVICE_SOURCE, TEST_KEY_1, TEST_KEY_1}
        };
    }

    @DataProvider(name = "generateSelfURICases")
    public Object[][] generateSelfURICases() {
        return new Object[][]{
            {GA4GH_SERVICE_SOURCE, TEST_KEY_1, "drs://localhost:8080/" + TEST_KEY_1},
        };
    }

    @DataProvider(name = "generateAccessMethodsCases")
    public Object[][] generateAccessMethodsCases() {
        return new Object[][]{
            {GA4GH_SERVICE_SOURCE, TEST_KEY_1},
        };
    }

    @DataProvider(name = "imputeChecksumsCases")
    public Object[][] imputeChecksumsCases() {
        return new Object[][]{
            {GA4GH_SERVICE_SOURCE, TEST_KEY_1, false, "f81ea43c74824cc72c77a39a92bf7b71"},
            {GA4GH_SERVICE_SOURCE, TEST_DIR_1, false, "9ff5785bce736fe030a805f1fc02ac72"},
        };
    }

    @DataProvider(name = "imputeSizeCases")
    public Object[][] imputeSizeCases() {
        return new Object[][]{
            {GA4GH_SERVICE_SOURCE, TEST_KEY_1, TEST_SIZE_1},
            {GA4GH_SERVICE_SOURCE, TEST_KEY_2, TEST_SIZE_2},
        };
    }

    @DataProvider(name = "imputeNameCases")
    public Object[][] imputeNameCases() {
        return new Object[][]{
            {GA4GH_SERVICE_SOURCE, TEST_KEY_1, TEST_NAME_1},
            {GA4GH_SERVICE_SOURCE, TEST_KEY_2, TEST_NAME_2},
            {GA4GH_SERVICE_SOURCE, TEST_DIR_1, TEST_DIR_NAME_1},
        };
    }


    @DataProvider(name = "imputeMimeTypeCases")
    public Object[][] imputeMimeTypeCases() {
        return new Object[][]{
            {GA4GH_SERVICE_SOURCE, TEST_KEY_1, "application/json"},
            {GA4GH_SERVICE_SOURCE, TEST_KEY_2, "application/json"},
            {GA4GH_SERVICE_SOURCE, TEST_DIR_1, null},
        };
    }

    @DataProvider(name = "imputeCreatedTimeCases")
    public Object[][] imputeCreatedTimeCases() {
        return new Object[][]{
            {GA4GH_SERVICE_SOURCE, TEST_KEY_1, false},
            {GA4GH_SERVICE_SOURCE, TEST_KEY_2, false},
            {GA4GH_SERVICE_SOURCE, "nonexistent_dir/", false},
        };
    }

    /* TEST METHODS */

    @Test(dataProvider = "existsCases")
    public void testExists(S3DataSource source, String objectId, boolean expExists) {
        boolean exists = factory.createS3DrsObjectLoader(source, objectId).exists();
        Assert.assertEquals(exists, expExists);
    }

    @Test(dataProvider = "isBundleCases")
    public void testIsBundle(S3DataSource source, String objectId, boolean expIsBundle) {
        boolean isBundle = factory.createS3DrsObjectLoader(source, objectId).isBundle();
        Assert.assertEquals(isBundle, expIsBundle);
    }

    @Test(dataProvider = "generateIdCases")
    public void testGenerateId(S3DataSource source, String objectId, String expId) {
        String actualId = factory.createS3DrsObjectLoader(source, objectId).generateId();
        Assert.assertEquals(actualId, expId);
    }

    @Test(dataProvider = "generateSelfURICases")
    public void testGenerateSelfURI(S3DataSource source, String objectId, String expSelfURI) {
        String selfURI = factory.createS3DrsObjectLoader(source, objectId).generateSelfURI().toString();
        Assert.assertEquals(selfURI, expSelfURI);
    }

    @Test(dataProvider = "generateAccessMethodsCases")
    public void testGenerateAccessMethods(S3DataSource source, String objectId) {
        List<AccessMethod> accessMethods = factory.createS3DrsObjectLoader(source, objectId).generateAccessMethods();
        Assert.assertEquals(accessMethods.size(), 1);
        Assert.assertEquals(accessMethods.get(0).getType(), AccessType.HTTPS);
    }

    @Test(dataProvider = "imputeChecksumsCases")
    public void testImputeChecksums(S3DataSource source, String objectId, boolean expError, String expDigest) {
        List<Checksum> checksums = factory.createS3DrsObjectLoader(source, objectId).imputeChecksums();
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
    public void testImputeSize(S3DataSource source, String objectId, long expSize) {
        long size = factory.createS3DrsObjectLoader(source, objectId).imputeSize();
        Assert.assertEquals(size, expSize);
    }

    @Test(dataProvider = "imputeNameCases")
    public void testImputeName(S3DataSource source, String objectId, String expName) {
        String name = factory.createS3DrsObjectLoader(source, objectId).imputeName();
        Assert.assertEquals(name, expName);
    }

    @Test(dataProvider = "imputeMimeTypeCases")
    public void testImputeMimeType(S3DataSource source, String objectId, String expMimeType) {
        String mimeType = factory.createS3DrsObjectLoader(source, objectId).imputeMimeType();
        Assert.assertEquals(mimeType, expMimeType);
    }

    @Test(dataProvider = "imputeCreatedTimeCases")
    public void testImputeCreatedTime(S3DataSource source, String objectId, boolean expNull) {
        // cannot assert to an actual time, assert that createdTime is not null (loaded successfully)
        LocalDateTime createdTime = factory.createS3DrsObjectLoader(source, objectId).imputeCreatedTime();
        if (expNull) {
            Assert.assertNotNull(createdTime);
            Assert.assertEquals(createdTime.getYear(), 1970);
            Assert.assertEquals(createdTime.getMonthValue(), 1);
            Assert.assertEquals(createdTime.getDayOfMonth(), 1);
        } else {
            Assert.assertNotNull(createdTime);
        }
    }

    @Test
    public void testGenerateContents() {
        S3DrsObjectLoader loader = factory.createS3DrsObjectLoader(GA4GH_SERVICE_SOURCE, "");
        loader.setExpand(true);
        List<ContentsObject> contents = loader.generateContents();
        // All the top level contents objects should be bundles with more contents
        Assert.assertTrue(contents.stream().noneMatch(c -> c.getContents().isEmpty()));

        // The further nested contents should all be files and not bundles
        Assert.assertTrue(contents.stream().flatMap(c -> c.getContents().stream()).allMatch(c -> c.getContents() == null));
    }

    @Test
    public void testGenerateContentsNoExpand() {
        S3DrsObjectLoader loader = factory.createS3DrsObjectLoader(GA4GH_SERVICE_SOURCE, "");
        loader.setExpand(false);
        List<ContentsObject> contents = loader.generateContents();
        // All the top level contents objects should not have further contents because expand has been set to false
        Assert.assertTrue(contents.stream().allMatch(c -> c.getContents() == null));
    }
}
