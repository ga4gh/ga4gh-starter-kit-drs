package org.ga4gh.drs.utils.objectloader;

import java.net.URI;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import org.ga4gh.drs.App;
import org.ga4gh.drs.AppConfig;
import org.ga4gh.drs.model.AccessMethod;
import org.ga4gh.drs.model.AccessType;
import org.ga4gh.drs.model.Checksum;
import org.ga4gh.drs.model.DrsObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

@Test
@SpringBootTest
@ContextConfiguration(classes={App.class, AppConfig.class})
public class FileDrsObjectLoaderTest extends AbstractTestNGSpringContextTests {

    @Autowired
    private DrsObjectLoaderFactory factory;

    /* DATA PROVIDERS */

    @DataProvider(name = "existsCases")
    public Object[][] existsCases() {
        return new Object[][] {
            {"test.phenopackets.Zapata.1", "./src/test/resources/data/phenopackets/Zapata/Zapata-Patient-1.json", true},
            {"test.phenopackets.Zapata", "./src/test/resources/data/phenopackets/Zapata", true},
            {"test.phenopackets.Zapata.3", "./src/test/resources/data/phenopackets/Zapata/Zapata-Patient-3.json", false},
        };
    }

    @DataProvider(name = "isBundleCases")
    public Object[][] isBundleCases() {
        return new Object[][] {
            {"test.phenopackets.Zapata.1", "./src/test/resources/data/phenopackets/Zapata/Zapata-Patient-1.json", false},
            {"test.phenopackets.Zapata", "./src/test/resources/data/phenopackets/Zapata", true},
            {"test.phenopackets.Zapata.2", "./src/test/resources/data/phenopackets/Zapata/Zapata-Patient-2.json", false},
        };
    }

    @DataProvider(name = "generateIdCases")
    public Object[][] generateIdCases() {
        return new Object[][] {
            {"test.id.00001", "/path/to/bam00001.bam", "test.id.00001"}
        };
    }

    @DataProvider(name = "generateSelfURICases")
    public Object[][] generateSelfURICases() {
        return new Object[][] {
            {"test.id.00001", "/path/to/bam00001.bam", "drs://localhost:8080/test.id.00001"}
        };
    }

    @DataProvider(name = "generateAccessMethodsCases")
    public Object[][] generateAccessMethodsCases() {
        return new Object[][] {
            {"test.phenopackets.Cao.1", "./src/test/resources/data/phenopackets/Cao/Cao-Patient-1.json"}
        };
    }

    @DataProvider(name = "generateCustomDrsObjectPropertiesCases")
    public Object[][] generateCustomDrsObjectPropertiesCases() {
        return new Object[][] {
            {
                "test.phenopackets.Zapata.1",
                "./src/test/resources/data/phenopackets/Zapata/Zapata-Patient-1.json",
                false,
                new ArrayList<String>() {{add("Zapata1");}},
                "11.12.13",
                new ArrayList<Checksum>() {{add(new Checksum("67e8dabdcc47969974bfb48855cea9ff", "md5"));}}
            },
            {
                "test.phenopackets.Zapata.4",
                "./src/test/resources/data/phenopackets/Zapata/Zapata-Patient-4.json",
                true,
                null,
                null,
                null
            }
        };
    }

    @DataProvider(name = "imputeChecksumsCases")
    public Object[][] imputeChecksumsCases() {
        return new Object[][] {
            {"test.phenopackets.Cao.1", "./src/test/resources/data/phenopackets/Cao/Cao-Patient-1.json", false, "f81ea43c74824cc72c77a39a92bf7b71"},
            {"test.phenopackets.Zapata.1", "./src/test/resources/data/phenopackets/Zapata/Zapata-Patient-1.json", false, "67e8dabdcc47969974bfb48855cea9ff"},
            {"test.phenopackets.Mundhofir.2", "./src/test/resources/data/phenopackets/Mundhofir/Mundhofir-Patient-2.json", false, "09d05a76060f87af2006a5e5be332f99"},
            {"test.phenopackets.Mundhofir.4", "./src/test/resources/data/phenopackets/Mundhofir/Mundhofir-Patient-4.json", true, null},
        };
    }

    @DataProvider(name = "imputeSizeCases")
    public Object[][] imputeSizeCases() {
        return new Object[][] {
            {"test.phenopackets.Cao.1", "./src/test/resources/data/phenopackets/Cao/Cao-Patient-1.json", 4257},
            {"test.phenopackets.Zapata.1", "./src/test/resources/data/phenopackets/Zapata/Zapata-Patient-1.json", 15826},
            {"test.phenopackets.Mundhofir.2", "./src/test/resources/data/phenopackets/Mundhofir/Mundhofir-Patient-2.json", 7634},
        };
    }

    @DataProvider(name = "imputeNameCases")
    public Object[][] imputeNameCases() {
        return new Object[][] {
            {"test.phenopackets.Cao.1", "./src/test/resources/data/phenopackets/Cao/Cao-Patient-1.json", "Cao-Patient-1.json"},
            {"test.phenopackets.Zapata.1", "./src/test/resources/data/phenopackets/Zapata/Zapata-Patient-1.json", "Zapata-Patient-1.json"},
            {"test.phenopackets.Mundhofir.2", "./src/test/resources/data/phenopackets/Mundhofir/Mundhofir-Patient-2.json", "Mundhofir-Patient-2.json"},
        };
    }

    @DataProvider(name = "imputeMimeTypeCases")
    public Object[][] imputeMimeTypeCases() {
        return new Object[][] {
            {"test.phenopackets.Zapata.1", "./src/test/resources/data/phenopackets/Zapata/Zapata-Patient-1.json", "application/json"},
            {"test.phenopackets.Zapata.1", "./src/test/resources/data/phenopackets/Zapata/Zapata-Patient-1.xml", null},
        };
    }

    @DataProvider(name = "imputeCreatedTimeCases")
    public Object[][] imputeCreatedTimeCases() {
        return new Object[][] {
            {"test.phenopackets.Zapata.1", "./src/test/resources/data/phenopackets/Zapata/Zapata-Patient-1.json", false},
            {"test.phenopackets.Zapata.2", "./src/test/resources/data/phenopackets/Zapata/Zapata-Patient-2.json", false},
            {"test.phenopackets.Zapata.4", "./src/test/resources/data/phenopackets/Zapata/Zapata-Patient-4.json", true},
        };
    }

    @DataProvider(name = "generateDrsObjectCases")
    public Object[][] generateDrsObjectCases() {
        return new Object[][] {
            {
                "test.phenopackets.Zapata.1",
                "./src/test/resources/data/phenopackets/Zapata/Zapata-Patient-1.json",
                "test.phenopackets.Zapata.1",
                URI.create("drs://localhost:8080/test.phenopackets.Zapata.1"),
                15826,
                "Zapata-Patient-1.json",
                "application/json"
            },
            {
                "test.phenopackets.Zapata.2",
                "./src/test/resources/data/phenopackets/Zapata/Zapata-Patient-2.json",
                "test.phenopackets.Zapata.2",
                URI.create("drs://localhost:8080/test.phenopackets.Zapata.2"),
                9561,
                "Zapata-Patient-2.json",
                "application/json"
            }
        };
    }

    /* TEST METHODS */

    @Test(dataProvider = "existsCases")
    public void testExists(String objectId, String objectPath, boolean expExists) {
        boolean exists = factory.createDrsObjectLoader(AccessType.FILE, objectId, objectPath).exists();
        Assert.assertEquals(exists, expExists);
    }

    @Test(dataProvider = "isBundleCases")
    public void testIsBundle(String objectId, String objectPath, boolean expIsBundle) {
        boolean isBundle = factory.createDrsObjectLoader(AccessType.FILE, objectId, objectPath).isBundle();
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
        List<AccessMethod> accessMethods = factory.createDrsObjectLoader(AccessType.FILE, objectId, objectPath).generateAccessMethods();
        Assert.assertEquals(accessMethods.size(), 1);
        Assert.assertEquals(accessMethods.get(0).getType(), AccessType.FILE);
    }

    @Test(dataProvider = "generateCustomDrsObjectPropertiesCases")
    public void testGenerateCustomDrsObjectProperties(String objectId, String objectPath, boolean expException, List<String> expAliases, String expVersion, List<Checksum> expChecksums) {
        DrsObject customDrsObject = factory.createDrsObjectLoader(AccessType.FILE, objectId, objectPath).generateCustomDrsObjectProperties();
        if (expException) {
            Assert.assertNull(customDrsObject.getAliases());
            Assert.assertNull(customDrsObject.getVersion());
            Assert.assertNull(customDrsObject.getChecksums());

        } else {
            Assert.assertEquals(customDrsObject.getAliases(), expAliases);
            Assert.assertEquals(customDrsObject.getVersion(), expVersion);

            // assert checksums list
            List<Checksum> actualChecksums = customDrsObject.getChecksums();
            Assert.assertEquals(actualChecksums.size(), expChecksums.size());
            for (int i = 0; i < actualChecksums.size(); i++) {
                Assert.assertEquals(actualChecksums.get(i).getChecksum(), expChecksums.get(i).getChecksum());
                Assert.assertEquals(actualChecksums.get(i).getType(), expChecksums.get(i).getType());
            }
        }
    }

    @Test(dataProvider = "imputeChecksumsCases")
    public void testImputeChecksums(String objectId, String objectPath, boolean expError, String expDigest) {
        List<Checksum> checksums = factory.createDrsObjectLoader(AccessType.FILE, objectId, objectPath).imputeChecksums();
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
    public void testImputeSize(String objectId, String objectPath, int expSize) {
        long size = factory.createDrsObjectLoader(AccessType.FILE, objectId, objectPath).imputeSize();
        Assert.assertEquals(size, expSize);
    }

    @Test(dataProvider = "imputeNameCases")
    public void testImputeName(String objectId, String objectPath, String expName) {
        String name = factory.createDrsObjectLoader(AccessType.FILE, objectId, objectPath).imputeName();
        Assert.assertEquals(name, expName);
    }

    @Test(dataProvider = "imputeMimeTypeCases")
    public void testImputeMimeType(String objectId, String objectPath, String expMimeType) {
        String mimeType = factory.createDrsObjectLoader(AccessType.FILE, objectId, objectPath).imputeMimeType();
        Assert.assertEquals(mimeType, expMimeType);
    }

    @Test(dataProvider = "imputeCreatedTimeCases")
    public void testImputeCreatedTime(String objectId, String objectPath, boolean expNull) {
        // cannot assert to an actual time, assert that createdTime is not null (loaded successfully)
        LocalDateTime createdTime = factory.createDrsObjectLoader(AccessType.FILE, objectId, objectPath).imputeCreatedTime();
        if (expNull) {
            Assert.assertNotNull(createdTime);
            Assert.assertEquals(createdTime.getYear(), 1970);
            Assert.assertEquals(createdTime.getMonthValue(), 1);
            Assert.assertEquals(createdTime.getDayOfMonth(), 1);
        } else {
            Assert.assertNotNull(createdTime);
        }
    }

    @Test(dataProvider = "generateDrsObjectCases")
    public void testGenerateDrsObject(String objectId, String objectPath, String expId, URI expSelfURI, int expSize, String expName, String expMimeType) {
        DrsObject drsObject = factory.createDrsObjectLoader(AccessType.FILE, objectId, objectPath).generateDrsObject();
        Assert.assertEquals(drsObject.getId(), expId);
        Assert.assertEquals(drsObject.getSelfURI(), expSelfURI);
        Assert.assertEquals(drsObject.getSize(), expSize);
        Assert.assertEquals(drsObject.getName(), expName);
        Assert.assertEquals(drsObject.getMimeType(), expMimeType);
    }
}
