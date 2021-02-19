package org.ga4gh.drs.utils;

import org.ga4gh.drs.App;
import org.ga4gh.drs.AppConfig;
import org.ga4gh.drs.utils.objectloader.DrsObjectLoader;
import org.ga4gh.drs.utils.objectloader.FileDrsObjectLoader;
import org.ga4gh.drs.utils.objectloader.S3DrsObjectLoader;
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
public class DataSourceLookupTest extends AbstractTestNGSpringContextTests {

    @Autowired
    DataSourceLookup dataSourceLookup;

    @DataProvider(name = "cases")
    public Object[][] getData() {
        return new Object[][]{
            {
                "unregisteredid.id00001",
                false,
                null,
                null
            },
            {
                "TestDataHtsjdkSamtools.compressed.bam",
                true,
                FileDrsObjectLoader.class,
                "./src/test/resources/data/htsjdk/samtools/compressed.bam"
            },
            {
                "TestDataHtsjdkSamtools.BAMFileIndexTest-index_test.bam",
                true,
                FileDrsObjectLoader.class,
                "./src/test/resources/data/htsjdk/samtools/BAMFileIndexTest/index_test.bam"
            },
            {
                "TestDataPhenopackets.Volpi-Patient2.json",
                true,
                FileDrsObjectLoader.class,
                "./src/test/resources/data/phenopackets/Volpi/Patient2.json"
            },
            {
                "TestDataPhenopackets.Tamhankar-Patient1.json",
                true,
                FileDrsObjectLoader.class,
                "./src/test/resources/data/phenopackets/Tamhankar/Patient1.json"
            },
            {
                "TestDataPhenopackets.Zapata",
                true,
                FileDrsObjectLoader.class,
                "./src/test/resources/data/phenopackets/Zapata"
            },
            {
                "TestTabulaMurisFacs.A1-B000126-3_39_F-1-1_R1.mus.Aligned.out.sorted.bam.bai",
                true,
                S3DrsObjectLoader.class,
                "s3://czbiohub-tabula-muris/facs_bam_files/A1-B000126-3_39_F-1-1_R1.mus.Aligned.out.sorted.bam.bai"
            },
            {
                "TestTabulaMurisFacs.",
                true,
                S3DrsObjectLoader.class,
                "s3://czbiohub-tabula-muris/facs_bam_files/"
            },
        };
    }

    @Test(dataProvider = "cases")
    public void testDataSourceLookup(String objectId, boolean expLoaded, Class<?> expClass, String expObjectPath) {
        DrsObjectLoader drsObjectLoader = dataSourceLookup.getDrsObjectLoaderFromId(objectId);
        if (expLoaded) {
            Assert.assertNotNull(drsObjectLoader);
            Assert.assertEquals(drsObjectLoader.getClass(), expClass);
            Assert.assertEquals(drsObjectLoader.getObjectPath(), expObjectPath);
        } else {
            Assert.assertNull(drsObjectLoader);
        }
    }
}
