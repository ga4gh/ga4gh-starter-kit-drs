package integration;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpRequest.BodyPublisher;
import java.net.http.HttpRequest.BodyPublishers;
import java.net.http.HttpRequest.Builder;
import java.net.http.HttpResponse.BodyHandlers;
import java.nio.file.Files;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.File;
import java.nio.file.Path;  
import java.nio.file.Paths;

import org.json.JSONObject;
import org.json.JSONArray;
import org.checkerframework.checker.units.qual.m;
import org.ga4gh.starterkit.drs.testutils.ResourceLoader;
import java.util.ArrayList;
import java.util.List;
import org.springframework.http.HttpHeaders;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class DemoServerAdminTest 
{
    /*
    TO-DO:
    1. Get a DRS Object
    2. Create it as a new object in the server
    3. Change its name
    4. Confirm it
    5. Delete object
    */

    // Define variables and constants
    private static final String LOCAL_PUBLIC_URL = "http://localhost:4500/ga4gh/drs/v1/objects/";
    private static final String LOCAL_ADMIN_URL = "http://localhost:4501/admin/ga4gh/drs/v1/objects/";

    // DRS Object directory
    private static final String OBJ_DIR = "/responses/objects/getObjectById/";
    
    @DataProvider(name = "cases")
    public Object[][] getData() 
    {
        return new Object[][] 
        {
            { 
                LOCAL_ADMIN_URL,
                "697907bf-d5bd-433e-aac2-1747f1faf366",
                "New Test Name 1",
                "10000000-test-test-test-000000000000"
            },  
            {
                LOCAL_ADMIN_URL,
                "456e9ee0-5b60-4f38-82b5-83ba5d338038",
                "New Test Name 2",
                "20000000-test-test-test-000000000000"
            },  
            {
                LOCAL_ADMIN_URL,
                "b8cd0667-2c33-4c9f-967b-161b905932c9",
                "New Test Name 3",
                "30000000-test-test-test-000000000000"
            },            
        };
    }

    @Test(dataProvider = "cases")
    public void testDemoServerAdminGetDrsObjects(String requestURL, String drsObjectId, String newName, String newID) throws Exception 
    {
        HttpClient client = HttpClient.newHttpClient();
        
        // (1.1) Get DRS Object
        Builder requestBuilder = HttpRequest.newBuilder()
            .GET()
            .uri(URI.create(requestURL + drsObjectId)); //Ask for the object

        HttpRequest request = requestBuilder.build();
        HttpResponse<String> response = client.send(request, BodyHandlers.ofString());

        String requestedObj = response.body();
        JSONObject req_JSON = new JSONObject(requestedObj);
        JSONObject starting_JSON = req_JSON;
        
        // (1.2) Change the DRS Object's name and id (edit it)
        req_JSON = changeDrsobjectValue(req_JSON, "name", newName); // Change the name of the DRS Object
        req_JSON = changeDrsobjectValue(req_JSON, "id", newID);
        String newObject = req_JSON.toString();

        // (2.1) Create the edited object as a new DRS Object
        HttpRequest createRequest = HttpRequest.newBuilder()
            .uri(URI.create(requestURL))
            .header("Content-Type", "application/json")
            .POST(BodyPublishers.ofString(newObject))
            .build();

        HttpResponse<String> createResponse = client.send(createRequest,
            HttpResponse.BodyHandlers.ofString());

        JSONObject createResponseJO = new JSONObject(createResponse.body());
        String createResponseFixed = createResponseJO.toString();
        
        Assert.assertEquals(newObject, createResponseFixed);

        // (3.1) Change the DRS Object's name and id (edit it)
        req_JSON = changeDrsobjectValue(req_JSON, "name", newName + " Edited"); // Change the name of the DRS Object
        String newObjectEdited = req_JSON.toString();

        // (3.2) Send the edit request to change newly created the DRS Object in the server
        HttpRequest editRequest = HttpRequest.newBuilder()
            .uri(URI.create(requestURL + newID)) // This is probably wrong
            .header("Content-Type", "application/json")
            .PUT(BodyPublishers.ofString(newObjectEdited)) // Sending the edited DRS Object with the request
            // Sent object should be a DRS Object but a string is sent in the test in DrsAdminTest too
            .build();

        HttpResponse<String> editResponse = client.send(editRequest,
            HttpResponse.BodyHandlers.ofString());

        // (3.2) Confirm object edit
        JSONObject editResponseJO = new JSONObject(editResponse.body());
        String editResponseFixed = editResponseJO.toString();

        Assert.assertEquals(newObjectEdited, editResponseFixed);

        // (4.1) Delete the new object
        HttpRequest deleteRequest = HttpRequest.newBuilder()
            .uri(URI.create(requestURL + newID)) // This is probably wrong
            .header("Content-Type", "application/json")
            .DELETE()
            .build();

        HttpResponse<String> deleteResponse = client.send(deleteRequest,
            HttpResponse.BodyHandlers.ofString());

        // (4.2) Confirm object deletion (succesful deletion means and empty body)
        Assert.assertEquals("", deleteResponse.body());
    }

    public JSONObject changeDrsobjectValue(JSONObject obj, String key, String value) throws Exception
    {
        obj.put(key, value);

        return obj;
    }
}
