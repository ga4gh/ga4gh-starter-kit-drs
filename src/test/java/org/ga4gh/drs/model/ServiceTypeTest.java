package org.ga4gh.drs.model;

import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class ServiceTypeTest {

    @DataProvider(name = "cases")
    public Object[][] getData() {
        return new Object[][]{
            {
                "org.ga4gh",
                "drs",
                "1.1.0"
            }
        };
    }

    public void assertions(ServiceType serviceType, String group, String artifact, String version) {
        Assert.assertEquals(serviceType.getGroup(), group);
        Assert.assertEquals(serviceType.getArtifact(), artifact);
        Assert.assertEquals(serviceType.getVersion(), version);
    }

    @Test(dataProvider = "cases")
    public void testServiceTypeNoArgsConstructor(String group, String artifact, String version) {
        ServiceType serviceType = new ServiceType();
        serviceType.setGroup(group);
        serviceType.setArtifact(artifact);
        serviceType.setVersion(version);
        assertions(serviceType, group, artifact, version);
    }

    @Test(dataProvider = "cases")
    public void testServiceTypeAllArgsConstructor(String group, String artifact, String version) {
        ServiceType serviceType = new ServiceType(group, artifact, version);
        assertions(serviceType, group, artifact, version);
    }
}
