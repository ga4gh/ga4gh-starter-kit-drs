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
 * Tests the state of the loaded YAML config container singleton when no
 * YAML config file is provided on the command line (all defaults used)
 */
@SpringBootTest
@ContextConfiguration(classes = {
    DrsServer.class,
    DrsServerSpringConfig.class
})
public class DrsServerYamlConfigContainerDefaultTest extends AbstractTestNGSpringContextTests {

    @Autowired
    @Qualifier(DrsServerConstants.FINAL_DRS_CONFIG_CONTAINER)
    private DrsServerYamlConfigContainer drs;

    @Test
    public void testYamlConfigContainer() {
        ServerProps serverProps = drs.getDrs().getServerProps();
        Assert.assertEquals(serverProps.getScheme(), "http");
        Assert.assertEquals(serverProps.getHostname(), "localhost");
        Assert.assertEquals(serverProps.getPublicApiPort(), "4500");
        Assert.assertEquals(serverProps.getAdminApiPort(), "4501");

        DatabaseProps databaseProps = drs.getDrs().getDatabaseProps();
        Assert.assertEquals(databaseProps.getUrl(), "jdbc:sqlite:./ga4gh-starter-kit.dev.db");
        Assert.assertEquals(databaseProps.getUsername(), "");
        Assert.assertEquals(databaseProps.getPassword(), "");
        Assert.assertEquals(databaseProps.getPoolSize(), "1");

        DrsServiceInfo drsServiceInfo = drs.getDrs().getServiceInfo();
        Assert.assertEquals(drsServiceInfo.getId(), "org.ga4gh.starterkit.drs");
        Assert.assertEquals(drsServiceInfo.getName(), "GA4GH Starter Kit DRS Service");
        Assert.assertEquals(drsServiceInfo.getDescription(), "An open source, community-driven implementation of the GA4GH Data Repository Service (DRS) API specification.");
        Assert.assertEquals(drsServiceInfo.getContactUrl(), "mailto:info@ga4gh.org");

        DrsServiceProps drsServiceProps = drs.getDrs().getDrsServiceProps();
        Assert.assertEquals(drsServiceProps.getServeFileURLForFileObjects(), true);
        Assert.assertEquals(drsServiceProps.getServeStreamURLForFileObjects(), false);
    }
}
