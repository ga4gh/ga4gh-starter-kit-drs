package org.ga4gh.drs.utils.cache;

import org.ga4gh.drs.App;
import org.ga4gh.drs.AppConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.springframework.test.context.web.WebAppConfiguration;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

@SpringBootTest
@ContextConfiguration(classes={App.class, AppConfig.class})
@WebAppConfiguration
public class AccessCacheTest extends AbstractTestNGSpringContextTests {

    @Autowired
    private AccessCache accessCache;

    @DataProvider(name = "cases")
    public Object[][] getData() {
        return new Object[][] {
            {
                "DrsId00001",
                "AccessId00000",
                "DrsId00001",
                "AccessId00000",
                false,
                "./path/to/file/0001.bam"
            },
            {
                "DrsId00001",
                "AccessId00000",
                "DrsId00001",
                "AccessId00001",
                true,
                null
            }
        };
    }

    @Test(dataProvider = "cases")
    public void testGetAccessCacheItem(String putObjectId, String putAccessId, String getObjectId, String getAccessId, boolean expNull, String objectPath) {
        AccessCacheItem item = new AccessCacheItem();
        item.setObjectPath(objectPath);

        accessCache.put(putObjectId, putAccessId, item);
        AccessCacheItem retrievedItem = accessCache.get(getObjectId, getAccessId);

        if (expNull) {
            Assert.assertNull(retrievedItem);
        } else {
            Assert.assertNotNull(retrievedItem);
            Assert.assertEquals(retrievedItem.getObjectPath(), objectPath);
        }
    }
}
