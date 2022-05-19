package org.ga4gh.starterkit.drs.model;

import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class AwsS3AccessObjectTest {

    @DataProvider(name = "cases")
    public Object[][] getData() {
        return new Object[][] {
            {
                Long.valueOf(1L),
                "us-east-1",
                "ga4gh-demo-data",
                "/path/to/the/file.bam",
                new DrsObject() {{
                    setId("12345");
                    setSize(Long.valueOf(1L));
                }},
                "12345"
            },
            {
                Long.valueOf(200L),
                "us-east-2",
                "ga4gh-production-data",
                "/data/phenopackets/0001.json",
                new DrsObject() {{
                    setId("98765");
                    setSize(Long.valueOf(1L));
                }},
                "98765"
            },
        };
    }

    @Test(dataProvider = "cases")
    public void testAwsS3AccessObject(Long id, String region, String bucket, String key, DrsObject drsObject, String expDrsObjectId) {
        AwsS3AccessObject awsS3AccessObject = new AwsS3AccessObject();
        awsS3AccessObject.loadRelations();
        
        awsS3AccessObject.setId(id);
        awsS3AccessObject.setRegion(region);
        awsS3AccessObject.setBucket(bucket);
        awsS3AccessObject.setKey(key);
        awsS3AccessObject.setDrsObject(drsObject);

        Assert.assertEquals(awsS3AccessObject.getId(), id);
        Assert.assertEquals(awsS3AccessObject.getRegion(), region);
        Assert.assertEquals(awsS3AccessObject.getBucket(), bucket);
        Assert.assertEquals(awsS3AccessObject.getKey(), key);
        Assert.assertEquals(awsS3AccessObject.getDrsObject().getId(), expDrsObjectId);
    }    
}
