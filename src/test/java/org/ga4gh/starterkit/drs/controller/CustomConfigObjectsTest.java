package org.ga4gh.starterkit.drs.controller;

import org.ga4gh.starterkit.drs.app.DrsStandaloneServer;
import org.ga4gh.starterkit.drs.app.DrsStandaloneSpringConfig;
import org.ga4gh.starterkit.drs.beanconfig.StarterKitDrsSpringConfig;
import org.ga4gh.starterkit.drs.testutils.ResourceLoader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import static org.ga4gh.starterkit.drs.constant.DrsApiConstants.DRS_API_V1;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Testing of bespoke handling of the /objects/{object_id} endpoint when 
 * non-default config parameters are provided to the program
 */
@SpringBootTest(args = {"--config", "./src/test/resources/config/test-config-00.yml"})
@ContextConfiguration(classes={
    DrsStandaloneServer.class,
    DrsStandaloneSpringConfig.class,
    StarterKitDrsSpringConfig.class,
    Objects.class
})
@WebAppConfiguration
public class CustomConfigObjectsTest extends AbstractTestNGSpringContextTests {

    private static final String API_PREFIX = DRS_API_V1;

    private static final String RESPONSE_DIR = "/responses/objects/getObjectById/";

    @Autowired
    private WebApplicationContext webAppContext;

    private MockMvc mockMvc;

    @BeforeMethod
    public void setup() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webAppContext).build();
    }

    @DataProvider(name = "serveFileURLCases")
    public Object[][] getServeFileURLCases() {
        return new Object[][] {
            {
                "1275f896-4c8f-47e1-99a1-873a6b2ef5fb",
                "custom-yaml-00.json"
            },
            {
                "2506f0e1-29e4-4132-9b37-f7452dc8a89b",
                "custom-yaml-01.json"
            }
        };
    }

    @Test(dataProvider = "serveFileURLCases")
    public void testServeFileURL(String objectId, String expFilename) throws Exception {

        MvcResult result = mockMvc.perform(get(API_PREFIX + "/objects/" + objectId))
            .andExpect(status().isOk())
            .andReturn();
        
        String responseBody = result.getResponse().getContentAsString();

        String drsObjectExpFile = RESPONSE_DIR + expFilename;
        String expResponseBody = ResourceLoader.load(drsObjectExpFile);
        Assert.assertEquals(responseBody, expResponseBody);
    }
}
