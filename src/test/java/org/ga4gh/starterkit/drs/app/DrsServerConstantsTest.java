package org.ga4gh.starterkit.drs.app;

import org.testng.Assert;
import org.testng.annotations.Test;

public class DrsServerConstantsTest {

    @Test
    public void instantiateClass() {
        DrsServerConstants drsServerConstants = new DrsServerConstants();
        Assert.assertEquals(drsServerConstants.getClass().getSimpleName(), "DrsServerConstants");
    }
    
}
