package org.ga4gh.starterkit.drs.constant;

import org.testng.Assert;
import org.testng.annotations.Test;

public class DrsApiConstantsTest {

    @Test
    public void instantiateClass() {
        DrsApiConstants drsApiConstants = new DrsApiConstants();
        Assert.assertEquals(drsApiConstants.getClass().getSimpleName(), "DrsApiConstants");
    }
}
