package org.ga4gh.starterkit.drs.app;

import org.ga4gh.starterkit.common.config.DatabaseProps;
import org.ga4gh.starterkit.common.config.ServerProps;
import org.ga4gh.starterkit.drs.config.DrsServiceProps;
import org.ga4gh.starterkit.drs.model.DrsServiceInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * Tests the state of the loaded YAML config container singleton when a 
 * YAML config file is provided on the command line (some user-specified 
 * properties overriding defaults)
 */
@SpringBootTest(args = {"--config", "./src/test/resources/config/test-config-00.yml"})
@ContextConfiguration(classes = {
    DrsServer.class,
    DrsServerSpringConfig.class
})
public class DrsServerYamlConfigContainerUserTest extends AbstractTestNGSpringContextTests {

    @Autowired
    @Qualifier(DrsServerConstants.FINAL_DRS_CONFIG_CONTAINER)
    private DrsServerYamlConfigContainer drs;

    @Test
    public void testYamlConfigContainer() {
        ServerProps serverProps = drs.getDrs().getServerProps();
        Assert.assertEquals(serverProps.getScheme(), "https");
        Assert.assertEquals(serverProps.getHostname(), "starterkit.ga4gh.org");
        Assert.assertEquals(serverProps.getPort(), "80");

        DatabaseProps databaseProps = drs.getDrs().getDatabaseProps();
        Assert.assertEquals(databaseProps.getUrl(), "jdbc:sqlite:./ga4gh-starter-kit.dev.db");
        Assert.assertEquals(databaseProps.getUsername(), "ga4gh-user");
        Assert.assertEquals(databaseProps.getPassword(), "password01234");
        Assert.assertEquals(databaseProps.getPoolSize(), "1");

        DrsServiceInfo drsServiceInfo = drs.getDrs().getServiceInfo();
        Assert.assertEquals(drsServiceInfo.getId(), "org.ga4gh.starterkit.drs.test");
        Assert.assertEquals(drsServiceInfo.getName(), "GA4GH Starter Kit DRS server test deployment");
        Assert.assertEquals(drsServiceInfo.getDescription(), "An open source, community-driven implementation of the GA4GH Data Repository Service (DRS) API specification.");
        Assert.assertEquals(drsServiceInfo.getContactUrl(), "mailto:info@ga4gh.org");

        DrsServiceProps drsServiceProps = drs.getDrs().getDrsServiceProps();
        Assert.assertEquals(drsServiceProps.getServeFileURLForFileObjects(), true);
        Assert.assertEquals(drsServiceProps.getServeStreamURLForFileObjects(), false);
    }
}
