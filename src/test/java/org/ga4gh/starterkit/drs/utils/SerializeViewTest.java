package org.ga4gh.starterkit.drs.utils;

import org.testng.Assert;
import org.testng.annotations.Test;

@Test
public class SerializeViewTest {

    public void testSerializeViewClasses() {

        SerializeView serializeView = new SerializeView();
        SerializeView.Always alwaysView = new SerializeView.Always();
        SerializeView.Admin adminView = new SerializeView.Admin();
        SerializeView.Public publicView = new SerializeView.Public();
        SerializeView.Never neverView = new SerializeView.Never();

        Assert.assertEquals(serializeView.getClass().getSimpleName(), "SerializeView");
        Assert.assertEquals(alwaysView.getClass().getSimpleName(), "Always");
        Assert.assertEquals(adminView.getClass().getSimpleName(), "Admin");
        Assert.assertEquals(publicView.getClass().getSimpleName(), "Public");
        Assert.assertEquals(neverView.getClass().getSimpleName(), "Never");
    }
}
