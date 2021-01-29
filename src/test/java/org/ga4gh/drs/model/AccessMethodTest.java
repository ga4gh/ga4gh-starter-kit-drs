package org.ga4gh.drs.model;

import java.net.MalformedURLException;
import java.net.URI;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

@Test
public class AccessMethodTest {

    @DataProvider(name = "cases")
    public Object[][] getData() throws MalformedURLException {
        return new Object[][] {
            {
                "access_id:00001",
                new AccessURL(URI.create("https://s3.somebucket.com/00001.bam")),
                AccessType.HTTPS,
                "us-east-2"
            }
        };
    }

    private void assertions(AccessMethod accessMethod, String accessId, AccessURL accessURL, AccessType type, String region) {
        Assert.assertEquals(accessMethod.getAccessId(), accessId);
        Assert.assertEquals(accessMethod.getAccessUrl(), accessURL);
        Assert.assertEquals(accessMethod.getType(), type);
        Assert.assertEquals(accessMethod.getRegion(), region);
    }

    @Test(dataProvider = "cases")
    public void testAccessMethodConstructorA(String accessId, AccessURL accessURL, AccessType type, String region) {
        AccessMethod accessMethod = new AccessMethod(accessId, type);
        accessMethod.setAccessUrl(accessURL);
        accessMethod.setRegion(region);
        assertions(accessMethod, accessId, accessURL, type, region);
    }

    @Test(dataProvider = "cases")
    public void testAccessMethodConstructorB(String accessId, AccessURL accessURL, AccessType type, String region) {
        AccessMethod accessMethod = new AccessMethod(accessURL, type);
        accessMethod.setAccessId(accessId);
        accessMethod.setRegion(region);
        assertions(accessMethod, accessId, accessURL, type, region);
    }

    @Test(dataProvider = "cases")
    public void testAccessMethodConstructorC(String accessId, AccessURL accessURL, AccessType type, String region) {
        AccessMethod accessMethod = new AccessMethod(accessId, type, region);
        accessMethod.setAccessUrl(accessURL);
        assertions(accessMethod, accessId, accessURL, type, region);
    }

    @Test(dataProvider = "cases")
    public void testAccessMethodConstructorD(String accessId, AccessURL accessURL, AccessType type, String region) {
        AccessMethod accessMethod = new AccessMethod(accessURL, type, region);
        accessMethod.setAccessId(accessId);
        accessMethod.setType(type);
        assertions(accessMethod, accessId, accessURL, type, region);
    }
}
