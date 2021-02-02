package org.ga4gh.drs.utils;

import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

@Test
public class MimeTypeImputerTest {

    @DataProvider(name = "cases")
    public Object[][] getData() {
        return new Object[][] {
            {
                "json",
                "application/json"
            }
        };
    }

    @Test(dataProvider = "cases")
    public void testImputeMimeType(String extension, String expMimeType) {
        new MimeTypeImputer(); // no value, only for test coverage
        String actualMimeType = MimeTypeImputer.imputeMimeType(extension);
        Assert.assertEquals(actualMimeType, expMimeType);
    }
}
