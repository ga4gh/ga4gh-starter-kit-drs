package org.ga4gh.starterkit.drs.app;

import org.testng.Assert;
import org.testng.annotations.Test;

public class DrsStandaloneConstantsTest {

    @Test
    public void instantiateClass() {
        DrsStandaloneConstants drsStandaloneConstants = new DrsStandaloneConstants();
        Assert.assertEquals(drsStandaloneConstants.getClass().getSimpleName(), "DrsStandaloneConstants");
    }
    
}
