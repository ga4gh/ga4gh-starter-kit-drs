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
import org.springframework.util.DigestUtils;
import org.springframework.web.context.WebApplicationContext;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import org.testng.ITestContext;
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

    @DataProvider(name = "getObjectByIdCases")
    public Object[][] getObjectByIdCases() {
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

    @DataProvider(name = "getAccessURLByIdCases")
    public Object[][] getAccessURLByIdCases() {
        return new Object[][] {
            {
                "test.phenopackets.Zapata.4",
                false,
                status().isNotFound()
            },
            {
                "test.phenopackets.Zapata.1",
                true,
                status().isOk()
            },
            {
                "test.phenopackets.Chebly.1",
                true,
                status().isOk()
            },
            {
                "test.phenopackets.Vajro.3",
                true,
                status().isOk()
            }
        };
    }

    @DataProvider(name = "streamFileCases")
    public Object[][] streamFileCases() {
        return new Object[][] {
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
                "67e8dabdcc47969974bfb48855cea9ff"
            },
            {
                "test.phenopackets.Chebly.1",
                true,
                status().isOk(),
                "b0e87051955eab5cecd75a9cdf73c618"
            },
            {
                "test.phenopackets.Vajro.3",
                true,
                status().isOk(),
                "545ac7eb31f493097a929f4f62a146b9"
            }
        };
    }

    private static final String responseDir = "/responses/objects/getObjectById/";

    @Test(dataProvider = "getObjectByIdCases", groups= "object")
    public void testGetObjectById(String objectId, boolean expSuccess, ResultMatcher expStatus, String expFilename, ITestContext context) throws Exception {
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/objects/" + objectId))
            .andExpect(expStatus)
            .andReturn();
        if (expSuccess) {
            String responseBody = result.getResponse().getContentAsString();

            // deserialize response, set access id to a hardcoded value, and
            // reserialize (access id is meant to be randomly generated)
            ObjectMapper mapper = new ObjectMapper();
            DrsObject interimDrsObject = mapper.readValue(responseBody, DrsObject.class);

            String accessId = interimDrsObject.getAccessMethods().get(0).getAccessId();
            context.setAttribute(objectId, accessId);

            // set access id to constant to evaluate response string
            interimDrsObject.getAccessMethods().get(0).setAccessId("00000000-0000-0000-0000-000000000000");
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
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/objects/" + objectId + "/access/" + accessId))
            .andExpect(expStatus)
            .andReturn();
        if (expSuccess) {
            String responseBody = result.getResponse().getContentAsString();
            String expResponseBody = "{\"url\":\"http://localhost:8080/stream/" + objectId + "/" + accessId + "\"}";
            Assert.assertEquals(responseBody, expResponseBody);
        }
    }

    @Test(dataProvider = "streamFileCases")
    public void testStreamFile(String objectId, boolean expSuccess, ResultMatcher expStatus, String expChecksum, ITestContext context) throws Exception {
        String accessId = (String) context.getAttribute(objectId);
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/stream/" + objectId + "/" + accessId))
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
