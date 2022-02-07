package org.ga4gh.starterkit.drs.controller;

import org.ga4gh.starterkit.drs.app.DrsServer;
import org.ga4gh.starterkit.drs.app.DrsServerSpringConfig;
import static org.ga4gh.starterkit.drs.constant.DrsApiConstants.ADMIN_DRS_API_V1;
import org.ga4gh.starterkit.drs.model.DrsObject;
import org.ga4gh.starterkit.drs.testutils.ResourceLoader;
import org.ga4gh.starterkit.drs.utils.hibernate.DrsHibernateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.testng.Assert;
import org.testng.annotations.AfterGroups;
import org.testng.annotations.BeforeGroups;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest
@ContextConfiguration(classes = {
    DrsServer.class,
    DrsServerSpringConfig.class,
    DrsAdmin.class
})
@WebAppConfiguration
public class DrsAdminTest extends AbstractTestNGSpringContextTests {

    private static final String API_PREFIX = ADMIN_DRS_API_V1 + "/objects";

    private static final String RESPONSE_DIR = "/responses/admin/";

    private static final String PAYLOAD_DIR = "/payloads/admin/";

    @Autowired
    private DrsHibernateUtil hibernateUtil;

    @Autowired
    private WebApplicationContext webAppContext;

    private MockMvc mockMvc;

    @BeforeMethod
    public void setup() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webAppContext).build();
    }

    @DataProvider(name = "showDrsObjectCases")
    public Object[][] showDrsObjectCases() {
        return new Object[][] {
            {
                "b8cd0667-2c33-4c9f-967b-161b905932c9",
                status().isOk(),
                true,
                "success-00.json",
                null
            },
            {
                "355a74bd-6571-4d4a-8602-a9989936717f",
                status().isOk(),
                true,
                "success-01.json",
                null
            },
            {
                "697907bf-d5bd-433e-aac2-1747f1faf366",
                status().isOk(),
                true,
                "success-02.json",
                null
            },
            {
                "00000000-0000-0000-0000-000000000000",
                status().isNotFound(),
                false,
                null,
                "No DrsObject found by id: 00000000-0000-0000-0000-000000000000"
            }
        };
    }

    @DataProvider(name = "createDrsObjectCases")
    public Object[][] createDrsObjectCases() {
        return new Object[][] {
            {
                "success-00.json",
                status().isOk(),
                true,
                "success-00.json",
                null
            },
            {
                "success-01.json",
                status().isOk(),
                true,
                "success-01.json",
                null
            },
            {
                "success-00.json",
                status().isConflict(),
                false,
                null,
                "A(n) DrsObject already exists at id 22100588-a60d-4d8f-9a58-a369443f4a58"
            }
        };
    }

    @DataProvider
    public Object[][] updateDrsObjectCases() {
        return new Object[][] {
            {
                "22100588-a60d-4d8f-9a58-a369443f4a58",
                "success-00.json",
                status().isOk(),
                true,
                "success-00.json",
                null
            },
            {
                "e7b5f232-a0a7-4113-a072-2b6a6233e267",
                "success-01.json",
                status().isOk(),
                true,
                "success-01.json",
                null
            },
            {
                "00000000-0000-0000-0000-000000000000",
                "failure-00.json",
                status().isConflict(),
                false,
                null,
                "No DrsObject at id 00000000-0000-0000-0000-000000000000"
            },
            {
                "e7b5f232-a0a7-4113-a072-2b6a6233e267",
                "failure-01.json",
                status().isBadRequest(),
                false,
                null,
                "Update requested at id e7b5f232-a0a7-4113-a072-2b6a6233e267, but new DrsObject has an id of 4ac53482-c146-412b-9dfb-1b9cc8e34460"
            }
        };
    }

    @DataProvider
    public Object[][] deleteDrsObjectCases() {
        return new Object[][] {
            {
                "22100588-a60d-4d8f-9a58-a369443f4a58",
                status().isOk(),
                true,
                null
            },
            {
                "e7b5f232-a0a7-4113-a072-2b6a6233e267",
                status().isOk(),
                true,
                null
            },
            {
                "00000000-0000-0000-0000-000000000000",
                status().isConflict(),
                false,
                "No DrsObject at id 00000000-0000-0000-0000-000000000000"
            }
        };
    }

    private void createTestEntities() throws Exception {
        String[] payloadFiles = {
            "/payloads/admin/create/success-00.json",
            "/payloads/admin/create/success-01.json"
        };
        for (String payloadFile: payloadFiles) {
            String payloadBody = ResourceLoader.load(payloadFile);
            ObjectMapper objectMapper = new ObjectMapper();
            DrsObject drsObject = objectMapper.readValue(payloadBody, DrsObject.class);
            hibernateUtil.createEntityObject(DrsObject.class, drsObject);
        }
    }

    private void deleteTestEntities() throws Exception {
        hibernateUtil.deleteEntityObject(DrsObject.class, "22100588-a60d-4d8f-9a58-a369443f4a58");
        hibernateUtil.deleteEntityObject(DrsObject.class, "e7b5f232-a0a7-4113-a072-2b6a6233e267");
    }

    @AfterGroups("create")
    public void cleanupCreate() throws Exception {
        deleteTestEntities();
    }

    @BeforeGroups("update")
    public void setupUpdate() throws Exception {
        createTestEntities();
    }

    @AfterGroups("update")
    public void cleanupUpdate() throws Exception {
        deleteTestEntities();
    }

    @BeforeGroups("delete")
    public void setupDelete() throws Exception {
        createTestEntities();
    }

    private void genericAdminApiRequestTest(MvcResult result, boolean expSuccess, String expSubdir, String expFilename, String expMessage) throws Exception {
        if (expSuccess) {
            String responseBody = result.getResponse().getContentAsString();
            String drsObjectExpFile = RESPONSE_DIR + expSubdir + "/" + expFilename;
            String expResponseBody = ResourceLoader.load(drsObjectExpFile);
            Assert.assertEquals(responseBody, expResponseBody);
        } else {
            String message = result.getResolvedException().getMessage();
            Assert.assertEquals(message, expMessage);
        }
    }

    @Test(groups = "index")
    public void testIndexDrsObject() throws Exception {
        MvcResult result = mockMvc.perform(get(API_PREFIX))
            .andExpect(status().isOk())
            .andReturn();
        genericAdminApiRequestTest(result, true, "index", "success-00.json", null);
    }

    @Test(dataProvider = "showDrsObjectCases", groups = "show")
    public void testShowDrsObject(String objectId, ResultMatcher expStatus, boolean expSuccess, String expFilename, String expMessage) throws Exception {
        MvcResult result = mockMvc.perform(get(API_PREFIX + "/" + objectId))
            .andExpect(expStatus)
            .andReturn();
        genericAdminApiRequestTest(result, expSuccess, "show", expFilename, expMessage);
    }

    @Test(dataProvider = "createDrsObjectCases", groups = "create")
    public void testCreateDrsObject(String payloadFilename, ResultMatcher expStatus, boolean expSuccess, String expFilename, String expMessage) throws Exception {
        String expSubdir = "create";
        String payloadFile = PAYLOAD_DIR + expSubdir + "/" + payloadFilename;
        String payloadBody = ResourceLoader.load(payloadFile);

        MvcResult result = mockMvc.perform(
            post(API_PREFIX)
            .content(payloadBody)
            .header("Content-Type", "application/json")
        )
            .andExpect(expStatus)
            .andReturn();
        genericAdminApiRequestTest(result, expSuccess, expSubdir, expFilename, expMessage);
    }

    @Test(dataProvider = "updateDrsObjectCases", groups = "update")
    public void testUpdateDrsObject(String id, String payloadFilename, ResultMatcher expStatus, boolean expSuccess, String expFilename, String expMessage) throws Exception {
        String expSubdir = "update";
        String payloadFile = PAYLOAD_DIR + expSubdir + "/" + payloadFilename;
        String payloadBody = ResourceLoader.load(payloadFile);

        MvcResult result = mockMvc.perform(
            put(API_PREFIX + "/" + id)
            .content(payloadBody)
            .header("Content-Type", "application/json")
        )
            .andExpect(expStatus)
            .andReturn();
        genericAdminApiRequestTest(result, expSuccess, expSubdir, expFilename, expMessage);
    }

    @Test(dataProvider = "deleteDrsObjectCases", groups = "delete")
    public void testDeleteDrsObject(String id, ResultMatcher expStatus, boolean expSuccess, String expMessage) throws Exception {
        MvcResult result = mockMvc.perform(
            delete(API_PREFIX + "/" + id)
        )
            .andExpect(expStatus)
            .andReturn();
        if (!expSuccess) {
            String message = result.getResolvedException().getMessage();
            Assert.assertEquals(message, expMessage);
        }
    }
}
