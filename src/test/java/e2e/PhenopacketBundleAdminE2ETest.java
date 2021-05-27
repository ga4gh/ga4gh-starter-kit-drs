package e2e;

import org.ga4gh.starterkit.drs.app.DrsStandaloneServer;
import org.ga4gh.starterkit.drs.app.DrsStandaloneSpringConfig;
import org.ga4gh.starterkit.drs.beanconfig.StarterKitDrsSpringConfig;
import static org.ga4gh.starterkit.drs.constant.DrsApiConstants.DRS_API_V1;
import static org.ga4gh.starterkit.drs.constant.DrsApiConstants.ADMIN_DRS_API_V1;
import org.ga4gh.starterkit.drs.controller.DrsAdmin;
import org.ga4gh.starterkit.drs.controller.Objects;
import org.ga4gh.starterkit.drs.controller.Stream;
import org.ga4gh.starterkit.drs.model.AccessURL;
import org.ga4gh.starterkit.drs.model.ContentsObject;
import org.ga4gh.starterkit.drs.model.DrsObject;
import org.ga4gh.starterkit.drs.testutils.ResourceLoader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.util.DigestUtils;
import org.springframework.web.context.WebApplicationContext;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Simulates an end-to-end use case in which an administrative user inspects,
 * creates, updates, and deletes various DRSObjects (both bundles and non-bundles).
 * Asserts that the correct output is returned for each request in a chain of
 * requests
 */
@SpringBootTest
@ContextConfiguration(classes = {
    DrsStandaloneServer.class,
    DrsStandaloneSpringConfig.class,
    StarterKitDrsSpringConfig.class,
    DrsAdmin.class,
    Objects.class,
    Stream.class,
})
@WebAppConfiguration
public class PhenopacketBundleAdminE2ETest extends AbstractTestNGSpringContextTests {

    private static final String DATASET_BUNDLE_ID = "b8cd0667-2c33-4c9f-967b-161b905932c9";
    private static final String FAMILY_BUNDLE_ID = "22100588-a60d-4d8f-9a58-a369443f4a58";
    private static final String SUBJECT_2_ID = "e7b5f232-a0a7-4113-a072-2b6a6233e267";
    private static final String SUBJECT_3_ID = "a6816b60-dca4-4944-878d-906fb7c7f975";
    private static final String SUBJECT_4_ID = "13525b16-a841-4cf3-ac91-092a75722b6e";
    private static final String SUBJECT_5_ID = "f0454e32-437e-41c4-935e-684b35b2c4bd";
    private static final String SUBJECT_6_ID = "70f66ae7-09fd-44e0-9b7c-057591100d52";
    private static final String SUBJECT_7_ID = "a5a8d9c1-9dca-4a24-b4a0-51ba9e80a465";
    private static final String SUBJECT_8_ID = "3e130b8a-b181-445b-9b26-9984ed40ad7d";
    private static final String SUBJECT_9_ID = "881ea9af-e23b-44db-bc3f-f81ea5a7c27a";

    private static final String PAYLOAD_DIR = "/payloads/e2e/phenopackets-bundle-admin";
    private static final String RESPONSE_DIR = "/responses/e2e/phenopackets-bundle-admin";

    private static final String PUBLIC_API_PREFIX = DRS_API_V1+ "/objects";
    private static final String ADMIN_API_PREFIX = ADMIN_DRS_API_V1 + "/objects";

    private static final Map<String, String> expMd5HexStrings = new HashMap<>() {{
        put("phenopackets.jordan.2", "26d1b78394962b3a0a07010f5c52fde7");
        put("phenopackets.jordan.3", "fba06909da6f6dbbca4bca87e2ff3d07");
        put("phenopackets.jordan.4", "b31c453b237f058915d12e99b473dd41");
        put("phenopackets.jordan.5", "4c3b4185db35d4e04711cfe24ac0d1db");
        put("phenopackets.jordan.6", "5d6b8707891369f4c2906c49d3438037");
        put("phenopackets.jordan.7", "f91b547f5c20215e0e9adbd95302aab4");
        put("phenopackets.jordan.8", "21c51a8974f2f06e6a354d607812d9da");
        put("phenopackets.jordan.9", "f518d5ec16157db051d2469530825c23");
    }};

    private enum Route {
        PUBLIC_SHOW,
        ADMIN_SHOW,
        ADMIN_CREATE,
        ADMIN_UPDATE,
        ADMIN_DELETE
    }

    @Autowired
    private WebApplicationContext webAppContext;

    private MockMvc mockMvc;

    @BeforeMethod
    public void setup() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webAppContext).build();
    }

    private String determineApiPath(Route route, String id) {
        String path = "";
        switch(route) {
            case PUBLIC_SHOW:
                path = PUBLIC_API_PREFIX + "/" + id;
                break;
            case ADMIN_SHOW:
                path = ADMIN_API_PREFIX + "/" + id;
                break;
            case ADMIN_CREATE:
                path = ADMIN_API_PREFIX;
                break;
            case ADMIN_UPDATE:
                path = ADMIN_API_PREFIX + "/" + id;
                break;
            case ADMIN_DELETE:
                path = ADMIN_API_PREFIX + "/" + id;
                break;
            default:
                path = PUBLIC_API_PREFIX + "/" + id;
                break;
        }
        return path;
    }

    private String getPayloadBody(String payloadFilename) throws Exception {
        String payloadBody = ResourceLoader.load(PAYLOAD_DIR + "/" + payloadFilename);
        return payloadBody;
    }

    private String getExpResponseBody(String expResponseFilename) throws Exception {
        return ResourceLoader.load(RESPONSE_DIR+ "/" + expResponseFilename);
    }

    private ResultActions buildResultActions(Route route, String path, String payloadFilename) throws Exception {
        MockHttpServletRequestBuilder requestBuilder = null;
        String payloadBody = null;
        switch (route) {
            case PUBLIC_SHOW:
                requestBuilder = get(path);
                break;
            case ADMIN_SHOW:
                requestBuilder = get(path);
                break;
            case ADMIN_CREATE:
                payloadBody = getPayloadBody(payloadFilename);
                requestBuilder = post(path).content(payloadBody).header("Content-Type", "application/json");
                break;
            case ADMIN_UPDATE:
                payloadBody = getPayloadBody(payloadFilename);
                requestBuilder = put(path).content(payloadBody).header("Content-Type", "application/json");
                break;
            case ADMIN_DELETE:
                requestBuilder = delete(path);
                break;
            default:
                requestBuilder = get(path);
                break;
        }
        return mockMvc.perform(requestBuilder);
    }

    private void executeApiRequestAndAssert(Route route, String id, String payloadFilename, ResultMatcher expStatus, boolean expSuccess, String expResponseFilename) throws Exception {
        String path = determineApiPath(route, id);
        ResultActions resultActions = buildResultActions(route, path, payloadFilename);
        MvcResult result = resultActions.andExpect(expStatus).andReturn();
        if (expSuccess) {
            String responseBody = result.getResponse().getContentAsString();
            String expResponseBody = getExpResponseBody(expResponseFilename);
            Assert.assertEquals(responseBody, expResponseBody);
        }
    }

    private DrsObject executeApiRequestAndMapToDrsObject(String id) throws Exception {
        String path = determineApiPath(Route.PUBLIC_SHOW, id);
        ResultActions resultActions = buildResultActions(Route.PUBLIC_SHOW, path, null);
        MvcResult result = resultActions.andExpect(status().isOk()).andReturn();
        String responseBody = result.getResponse().getContentAsString();
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(responseBody, DrsObject.class);
    }

    private AccessURL executeApiRequestAndMapToAccessUrl(String objectId, String accessId) throws Exception {
        String path = PUBLIC_API_PREFIX + "/" + objectId + "/access/" + accessId;
        MvcResult result = mockMvc.perform(get(path)).andExpect(status().isOk()).andReturn();
        String responseBody = result.getResponse().getContentAsString();
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(responseBody, AccessURL.class);
    }

    private String getStringFromStreamEndpoint(URI streamURL) throws Exception {
        String mockMvcURL = streamURL.toString().replaceFirst("http://localhost:8080", "");
        MvcResult result = mockMvc.perform(get(mockMvcURL)).andExpect(status().isOk()).andReturn();
        return  result.getResponse().getContentAsString();
    }

    @Test
    public void test() throws Exception {
        // this method simulates an end-to-end use case in which an admin
        // creates a new bundle representing a family of phenopackets,
        // updates it with checksums and access objects, and assigns it 
        // to the overall Phenopacket bundle. Admin and public API requests
        // are made throughout the process to ensure that the returned
        // JSON is expected at each step in the process

        // STEP 0: initial state, prior to object creation
        executeApiRequestAndAssert(Route.PUBLIC_SHOW, DATASET_BUNDLE_ID, null, status().isOk(), true, "00-initial-dataset-bundle-public.json");
        executeApiRequestAndAssert(Route.ADMIN_SHOW, DATASET_BUNDLE_ID, null, status().isOk(), true, "01-initial-dataset-bundle-admin.json");
        executeApiRequestAndAssert(Route.ADMIN_SHOW, FAMILY_BUNDLE_ID, null, status().isNotFound(), false, null);

        // STEP 1: create a bundle representing Phenopackets for the "Jordan" family, assign to top-level phenopacket bundle
        executeApiRequestAndAssert(Route.ADMIN_CREATE, null, "02-create-jordan-family-bundle.json", status().isOk(), true, "02-jordan-family-bundle-created.json");
        executeApiRequestAndAssert(Route.ADMIN_CREATE, null, "02-create-jordan-family-bundle.json", status().isConflict(), false, null);
        executeApiRequestAndAssert(Route.PUBLIC_SHOW, DATASET_BUNDLE_ID, null, status().isOk(), true, "03-dataset-post-family-creation-public.json");
        executeApiRequestAndAssert(Route.ADMIN_SHOW, DATASET_BUNDLE_ID, null, status().isOk(), true, "04-dataset-post-family-creation-admin.json");

        // STEP 2: create single blob DrsObjects for each Phenopacket file in the "Jordan" family
        executeApiRequestAndAssert(Route.ADMIN_CREATE, null, "05-create-subject-2.json", status().isOk(), true, "05-create-subject-2.json");
        executeApiRequestAndAssert(Route.ADMIN_CREATE, null, "06-create-subject-3.json", status().isOk(), true, "06-create-subject-3.json");
        executeApiRequestAndAssert(Route.ADMIN_CREATE, null, "07-create-subject-4.json", status().isOk(), true, "07-create-subject-4.json");
        executeApiRequestAndAssert(Route.ADMIN_CREATE, null, "08-create-subject-5.json", status().isOk(), true, "08-create-subject-5.json");
        executeApiRequestAndAssert(Route.ADMIN_CREATE, null, "09-create-subject-6.json", status().isOk(), true, "09-create-subject-6.json");
        executeApiRequestAndAssert(Route.ADMIN_CREATE, null, "10-create-subject-7.json", status().isOk(), true, "10-create-subject-7.json");
        executeApiRequestAndAssert(Route.ADMIN_CREATE, null, "11-create-subject-8.json", status().isOk(), true, "11-create-subject-8.json");
        executeApiRequestAndAssert(Route.ADMIN_CREATE, null, "12-create-subject-9.json", status().isOk(), true, "12-create-subject-9.json");
        executeApiRequestAndAssert(Route.ADMIN_CREATE, null, "05-create-subject-2.json", status().isConflict(), false, null);
        executeApiRequestAndAssert(Route.PUBLIC_SHOW, FAMILY_BUNDLE_ID, null, status().isOk(), true, "13-family-with-all-subjects-public.json");
        executeApiRequestAndAssert(Route.ADMIN_SHOW, FAMILY_BUNDLE_ID, null, status().isOk(), true, "14-family-with-all-subjects-admin.json");

        // STEP 3: update each blob with checksum and access information
        executeApiRequestAndAssert(Route.ADMIN_UPDATE, SUBJECT_2_ID, "15-update-subject-2.json", status().isOk(), true, "15-update-subject-2.json");
        executeApiRequestAndAssert(Route.ADMIN_UPDATE, SUBJECT_3_ID, "16-update-subject-3.json", status().isOk(), true, "16-update-subject-3.json");
        executeApiRequestAndAssert(Route.ADMIN_UPDATE, SUBJECT_4_ID, "17-update-subject-4.json", status().isOk(), true, "17-update-subject-4.json");
        executeApiRequestAndAssert(Route.ADMIN_UPDATE, SUBJECT_5_ID, "18-update-subject-5.json", status().isOk(), true, "18-update-subject-5.json");
        executeApiRequestAndAssert(Route.ADMIN_UPDATE, SUBJECT_6_ID, "19-update-subject-6.json", status().isOk(), true, "19-update-subject-6.json");
        executeApiRequestAndAssert(Route.ADMIN_UPDATE, SUBJECT_7_ID, "20-update-subject-7.json", status().isOk(), true, "20-update-subject-7.json");
        executeApiRequestAndAssert(Route.ADMIN_UPDATE, SUBJECT_8_ID, "21-update-subject-8.json", status().isOk(), true, "21-update-subject-8.json");
        executeApiRequestAndAssert(Route.ADMIN_UPDATE, SUBJECT_9_ID, "22-update-subject-9.json", status().isOk(), true, "22-update-subject-9.json");
        executeApiRequestAndAssert(Route.ADMIN_UPDATE, SUBJECT_3_ID, "15-update-subject-2.json", status().isBadRequest(), false, null);

        // STEP 4: inspect bundles
        executeApiRequestAndAssert(Route.PUBLIC_SHOW, FAMILY_BUNDLE_ID, null, status().isOk(), true, "23-final-family-bundle-public.json");
        executeApiRequestAndAssert(Route.PUBLIC_SHOW, DATASET_BUNDLE_ID, null, status().isOk(), true, "24-final-dataset-bundle-public.json");

        // STEP 5: crawl through the family bundle, download all files via DRS client, and assert checksums
        DrsObject rootBundle = executeApiRequestAndMapToDrsObject(DATASET_BUNDLE_ID);
        String familyId = null;
        for (ContentsObject contentsObject : rootBundle.getContents()) {
            if (contentsObject.getName().equals("phenopackets.jordan.family")) {
                familyId = contentsObject.getId();
            }
        }
        DrsObject familyBundle = executeApiRequestAndMapToDrsObject(familyId);
        Assert.assertEquals(familyBundle.getContents().size(), 8);
        for (ContentsObject contentsObject : familyBundle.getContents()) {
            DrsObject phenopacketDrsObject = executeApiRequestAndMapToDrsObject(contentsObject.getId());
            String accessId = phenopacketDrsObject.getAccessMethods().get(0).getAccessId();
            AccessURL accessURL = executeApiRequestAndMapToAccessUrl(contentsObject.getId(), accessId);
            String streamResults = getStringFromStreamEndpoint(accessURL.getUrl());

            String name = contentsObject.getName();
            String md5 = DigestUtils.md5DigestAsHex(streamResults.getBytes());
            String expMd5 = expMd5HexStrings.get(name);
            Assert.assertEquals(md5, expMd5);
        }
        

        // STEP 6: delete individual DRSObject's and the family bundle
        executeApiRequestAndAssert(Route.ADMIN_DELETE, SUBJECT_2_ID, null, status().isOk(), true, "empty-response.json");
        executeApiRequestAndAssert(Route.ADMIN_DELETE, SUBJECT_3_ID, null, status().isOk(), true, "empty-response.json");
        executeApiRequestAndAssert(Route.ADMIN_DELETE, SUBJECT_4_ID, null, status().isOk(), true, "empty-response.json");
        executeApiRequestAndAssert(Route.ADMIN_DELETE, SUBJECT_5_ID, null, status().isOk(), true, "empty-response.json");
        executeApiRequestAndAssert(Route.ADMIN_DELETE, SUBJECT_6_ID, null, status().isOk(), true, "empty-response.json");
        executeApiRequestAndAssert(Route.ADMIN_DELETE, SUBJECT_7_ID, null, status().isOk(), true, "empty-response.json");
        executeApiRequestAndAssert(Route.ADMIN_DELETE, SUBJECT_8_ID, null, status().isOk(), true, "empty-response.json");
        executeApiRequestAndAssert(Route.ADMIN_DELETE, SUBJECT_9_ID, null, status().isOk(), true, "empty-response.json");
        executeApiRequestAndAssert(Route.ADMIN_DELETE, FAMILY_BUNDLE_ID, null, status().isOk(), true, "empty-response.json");
        executeApiRequestAndAssert(Route.ADMIN_DELETE, SUBJECT_2_ID, null, status().isConflict(), false, null);
        executeApiRequestAndAssert(Route.ADMIN_DELETE, FAMILY_BUNDLE_ID, null, status().isConflict(), false, null);
    }
}
