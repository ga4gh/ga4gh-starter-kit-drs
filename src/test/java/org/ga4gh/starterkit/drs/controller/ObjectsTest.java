package org.ga4gh.starterkit.drs.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.ga4gh.starterkit.drs.app.DrsServer;
import org.ga4gh.starterkit.drs.app.DrsServerSpringConfig;
import org.ga4gh.starterkit.drs.model.DrsObject;
import org.ga4gh.starterkit.drs.testutils.ResourceLoader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.util.DigestUtils;
import org.springframework.web.context.WebApplicationContext;
import org.testng.Assert;
import org.testng.ITestContext;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Ignore;
import org.testng.annotations.Test;

import static org.ga4gh.starterkit.drs.constant.DrsApiConstants.DRS_API_V1;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@ContextConfiguration(classes={
    DrsServer.class,
    DrsServerSpringConfig.class,
    Objects.class
})
@WebAppConfiguration
public class ObjectsTest extends AbstractTestNGSpringContextTests {

    static final String API_PREFIX = DRS_API_V1;

    @Autowired
    private WebApplicationContext webAppContext;

    private MockMvc mockMvc;

    @BeforeMethod
    public void setup() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webAppContext).build();
    }

    @DataProvider(name = "getObjectByIdCases")
    public Object[][] getObjectByIdCases() {
        return new Object[][] {
            // invalid id
            {
                "00000000-0000-0000-0000-000000000000",
                false,
                null,
                false,
                status().isNotFound(),
                null
            },
            // single blob - zhang proband
            {
                "697907bf-d5bd-433e-aac2-1747f1faf366",
                false,
                "true",
                true,
                status().isOk(),
                "00.json"
            },
            // single blob - mundhofir patient 1
            {
                "2506f0e1-29e4-4132-9b37-f7452dc8a89b",
                false,
                "true",
                true,
                status().isOk(),
                "01.json"
            },
            // single blob - lalani patient 1
            {
                "456e9ee0-5b60-4f38-82b5-83ba5d338038",
                false,
                "true",
                true,
                status().isOk(),
                "02.json"
            },
            // bundle - cao family, expand not provided
            {
                "a1dd4ae2-8d26-43b0-a199-342b64c7dff6",
                false,
                null,
                true,
                status().isOk(),
                "03.json"
            },
            // bundle - cao family, expand false
            {
                "a1dd4ae2-8d26-43b0-a199-342b64c7dff6",
                true,
                "false",
                true,
                status().isOk(),
                "03.json"
            },
            // bundle - cao family, expand true
            {
                "a1dd4ae2-8d26-43b0-a199-342b64c7dff6",
                true,
                "true",
                true,
                status().isOk(),
                "03.json"
            },
            // bundle - phenopackets - expand not provided
            {
                "b8cd0667-2c33-4c9f-967b-161b905932c9",
                false,
                "true",
                true,
                status().isOk(),
                "04.json"
            },
            // bundle - phenopackets - expand false
            {
                "b8cd0667-2c33-4c9f-967b-161b905932c9",
                true,
                "false",
                true,
                status().isOk(),
                "04.json"
            },
            // bundle - phenopackets - expand true
            {
                "b8cd0667-2c33-4c9f-967b-161b905932c9",
                true,
                "true",
                true,
                status().isOk(),
                "05.json"
            },
        };
    }

    @DataProvider(name = "getAccessURLByIdCases")
    public Object[][] getAccessURLByIdCases() {
        return new Object[][] {
            // single blob - mundhofir patient 2
            {
                "c00c264a-8f17-471f-8ded-1a1f10e965ac",
                false,
                status().isNotFound()
            },
            // single blob - zhang proband
            {
                "697907bf-d5bd-433e-aac2-1747f1faf366",
                true,
                status().isOk()
            },
            // single blob - mundhofir patient 1
            {
                "2506f0e1-29e4-4132-9b37-f7452dc8a89b",
                true,
                status().isOk()
            },
            // single blob - lalani patient 1
            {
                "456e9ee0-5b60-4f38-82b5-83ba5d338038",
                true,
                status().isOk()
            }
        };
    }

    @DataProvider(name = "streamFileCases")
    public Object[][] streamFileCases() {
        return new Object[][] {
            // single blob - mundhofir patient 2
            {
                "c00c264a-8f17-471f-8ded-1a1f10e965ac",
                false,
                status().isNotFound(),
                null
            },
            // single blob - zhang proband
            {
                "697907bf-d5bd-433e-aac2-1747f1faf366",
                true,
                status().isOk(),
                "71611ed3a3246fea6ce80916924c0722"
            },
            // single blob - mundhofir patient 1
            {
                "2506f0e1-29e4-4132-9b37-f7452dc8a89b",
                true,
                status().isOk(),
                "ed8dc271295fdea0f6df1fcb92f19d01"
            },
            // single blob - lalani patient 1
            {
                "456e9ee0-5b60-4f38-82b5-83ba5d338038",
                true,
                status().isOk(),
                "2262c06d95790324a76f09e2ee0ec418"
            }
        };
    }

    private static final String responseDir = "/responses/objects/getObjectById/";

    @Test(dataProvider = "getObjectByIdCases", groups= "object")
    public void testGetObjectById(String objectId, boolean includeExpand, String expand, boolean expSuccess, ResultMatcher expStatus, String expFilename, ITestContext context) throws Exception {
        MvcResult result;

        // construct mock mvc request with expand parameter if requested by test case
        if (includeExpand) {
            result = mockMvc.perform(get(API_PREFIX + "/objects/" + objectId).param("expand", expand))
            .andExpect(expStatus)
            .andReturn();
        } else {
            result = mockMvc.perform(get(API_PREFIX + "/objects/" + objectId))
            .andExpect(expStatus)
            .andReturn();
        }

        // evaluate content of successful response
        if (expSuccess) {
            String responseBody = result.getResponse().getContentAsString();

            // deserialize response, set access id to a hardcoded value, and
            // reserialize (access id is meant to be randomly generated)
            ObjectMapper mapper = new ObjectMapper();
            DrsObject interimDrsObject = mapper.readValue(responseBody, DrsObject.class);

            // set access id to constant to evaluate response string
            if (interimDrsObject.getAccessMethods() != null) {
                String accessId = interimDrsObject.getAccessMethods().get(0).getAccessId();
                context.setAttribute(objectId, accessId);
                interimDrsObject.getAccessMethods().get(0).setAccessId("00000000-0000-0000-0000-000000000000");
            }
            String finalResponseBody = mapper.writeValueAsString(interimDrsObject);

            // load local file containing expected response and assert
            String drsObjectExpFile = responseDir + expFilename;
            String expResponseBody = ResourceLoader.load(drsObjectExpFile);
            Assert.assertEquals(finalResponseBody, expResponseBody);
        }
    }

    @Test(dataProvider = "getAccessURLByIdCases", groups = "access", dependsOnGroups = "object")
    public void testGetAccessURLById(String objectId, boolean expSuccess, ResultMatcher expStatus, ITestContext context) throws Exception {
        String accessId = (String) context.getAttribute(objectId);
        MvcResult result = mockMvc.perform(get(API_PREFIX + "/objects/" + objectId + "/access/" + accessId))
            .andExpect(expStatus)
            .andReturn();
        if (expSuccess) {
            String responseBody = result.getResponse().getContentAsString();
            String expResponseBody = "{\"url\":\"http://localhost:4500" + DRS_API_V1 + "/stream/" + objectId + "/" + accessId + "\"}";
            Assert.assertEquals(responseBody, expResponseBody);
        }
    }

    @Test(dataProvider = "streamFileCases")
    @Ignore("Skipping the test temporarily")
    public void testStreamFile(String objectId, boolean expSuccess, ResultMatcher expStatus, String expChecksum, ITestContext context) throws Exception {
        String accessId = (String) context.getAttribute(objectId);
        MvcResult result = mockMvc.perform(get(API_PREFIX + "/stream/" + objectId + "/" + accessId))
            .andExpect(expStatus)
            .andReturn();
        if (expSuccess) {
            String responseBody = result.getResponse().getContentAsString();
            byte[] responseBodyBytes = responseBody.getBytes();
            String checksum = DigestUtils.md5DigestAsHex(responseBodyBytes);
            Assert.assertEquals(checksum, expChecksum);
        }
    }
}
