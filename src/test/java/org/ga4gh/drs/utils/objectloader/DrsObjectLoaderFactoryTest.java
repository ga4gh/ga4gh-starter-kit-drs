package org.ga4gh.drs.utils.objectloader;

import org.ga4gh.drs.App;
import org.ga4gh.drs.AppConfig;
import org.ga4gh.drs.model.AccessType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

@Test
@SpringBootTest
@ContextConfiguration(classes={App.class, AppConfig.class})
public class DrsObjectLoaderFactoryTest extends AbstractTestNGSpringContextTests {

    @Autowired
    DrsObjectLoaderFactory drsObjectLoaderFactory;

    @DataProvider(name = "cases")
    public Object[][] getData() {
        return new Object[][]{
            {AccessType.FILE, true, "FileDrsObjectLoader"},
            {AccessType.HTTPS, true, "HttpsDrsObjectLoader"},
            {null, false, null}
        };
    }

    @Test(dataProvider = "cases")
    public void testCreateDrsObjectLoader(AccessType accessType, boolean expLoaded, String expClassName) {
        DrsObjectLoader drsObjectLoader = drsObjectLoaderFactory.createDrsObjectLoader(accessType);
        if (expLoaded) {
            Assert.assertNotNull(drsObjectLoader);
            String className = drsObjectLoader.getClass().getSimpleName();
            Assert.assertEquals(className, expClassName);
        } else {
            Assert.assertNull(drsObjectLoader);
        }
    }
}
