package org.ga4gh.starterkit.drs.constant;

import org.testng.Assert;
import org.testng.annotations.Test;

public class DrsServiceInfoDefaultsTest {

    @Test
    public void instantiateClass() {
        DrsServiceInfoDefaults drsServiceInfoDefaults = new DrsServiceInfoDefaults();
        Assert.assertEquals(drsServiceInfoDefaults.getClass().getSimpleName(), "DrsServiceInfoDefaults");
    }
}
