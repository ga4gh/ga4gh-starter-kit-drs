package org.ga4gh.starterkit.drs.utils.cache;

import org.ga4gh.starterkit.drs.model.AccessType;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

@Test
public class AccessCacheItemTest {

    @DataProvider(name = "cases")
    public Object[][] getData() {
        return new Object[][] {
            {
                "00001",
                "2bc20749-bdfc-489d-8234-9eaea158b918",
                "./path/to/00001.bam",
                AccessType.file,
                "application/bam"
            }
        };
    }

    @Test(dataProvider = "cases")
    public void testAccessCacheItem(String objectId, String accessId, String objectPath, AccessType accessType, String mimeType) {
        AccessCacheItem accessCacheItem = new AccessCacheItem();
        accessCacheItem.setObjectId(objectId);
        accessCacheItem.setAccessId(accessId);
        accessCacheItem.setObjectPath(objectPath);
        accessCacheItem.setAccessType(accessType);
        accessCacheItem.setMimeType(mimeType);
        Assert.assertEquals(accessCacheItem.getObjectId(), objectId);
        Assert.assertEquals(accessCacheItem.getAccessId(), accessId);
        Assert.assertEquals(accessCacheItem.getObjectPath(), objectPath);
        Assert.assertEquals(accessCacheItem.getAccessType(), accessType);
        Assert.assertEquals(accessCacheItem.getMimeType(), mimeType);
    }
}
