package org.ga4gh.drs.model;

import org.testng.Assert;
import org.testng.annotations.Test;

public class ServiceTypeTest {

    @Test
    public void testServiceType() {
        new ServiceType();
        Assert.assertEquals(ServiceType.GROUP, "org.ga4gh");
        Assert.assertEquals(ServiceType.ARTIFACT, "drs");
        Assert.assertEquals(ServiceType.VERSION, "1.1.0");
    }
}
