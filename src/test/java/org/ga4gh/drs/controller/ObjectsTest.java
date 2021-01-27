package org.ga4gh.drs.controller;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.ga4gh.drs.App;
import org.ga4gh.drs.AppConfig;
import org.ga4gh.drs.model.DrsObject;
import org.ga4gh.drs.testutils.ResourceLoader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ContextConfiguration(classes={App.class, AppConfig.class, Objects.class})
@WebAppConfiguration
public class ObjectsTest extends AbstractTestNGSpringContextTests {

    @Autowired
    private WebApplicationContext webAppContext;

    private MockMvc mockMvc;

    @BeforeMethod
    public void setup() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webAppContext).build();
    }

    @DataProvider(name = "cases")
    public Object[][] getData() {
        return new Object[][] {
            {
                "test.invalidId",
                false,
                status().isNotFound(),
                null
            },
            {
                "test.phenopackets.Zapata.4",
                false,
                status().isNotFound(),
                null
            },
            {
                "test.phenopackets.Zapata.1",
                true,
                status().isOk(),
                "00.json"
            },
            {
                "test.phenopackets.Chebly.1",
                true,
                status().isOk(),
                "01.json"
            },
            {
                "test.phenopackets.Vajro.3",
                true,
                status().isOk(),
                "02.json"
            }
        };
    }

    private static final String responseDir = "/responses/objects/getObjectById/";

    @Test(dataProvider = "cases")
    public void testGetObjectById(String objectId, boolean expSuccess, ResultMatcher expStatus, String expFilename) throws Exception {
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/objects/" + objectId))
            .andExpect(expStatus)
            .andReturn();
        if (expSuccess) {
            String responseBody = result.getResponse().getContentAsString();

            // deserialize response, set access id to a hardcoded value, and
            // reserialize (access id is meant to be randomly generated)
            ObjectMapper mapper = new ObjectMapper();
            DrsObject interimDrsObject = mapper.readValue(responseBody, DrsObject.class);
            interimDrsObject.getAccessMethods().get(0).setAccessId("00000000-0000-0000-0000-000000000000");
            String finalResponseBody = mapper.writeValueAsString(interimDrsObject);

            // load local file containing expected response and assert
            String drsObjectExpFile = responseDir + expFilename;
            String expResponseBody = ResourceLoader.load(drsObjectExpFile);
            Assert.assertEquals(finalResponseBody, expResponseBody);
        }
    }
}
