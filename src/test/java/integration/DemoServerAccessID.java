package integration;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpRequest.Builder;
import java.net.http.HttpResponse.BodyHandlers;
import java.nio.file.Files;

import java.io.File;
import java.nio.file.Path;  
import java.nio.file.Paths;

import org.json.JSONObject;
import org.json.JSONArray;

import org.ga4gh.starterkit.drs.testutils.ResourceLoader;
import java.util.ArrayList;
import java.util.List;
import org.springframework.http.HttpHeaders;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class DemoServerAccessID
{
    // Define variables and constants
    private static final String LOCAL_PUBLIC_URL = "http://localhost:4500/ga4gh/drs/v1/objects/";
    
    @DataProvider(name = "cases")
    public Object[][] getData() 
    {
        return new Object[][] 
        {
            {   // Getting a DrsObject
                LOCAL_PUBLIC_URL,
                "697907bf-d5bd-433e-aac2-1747f1faf366",
                "00.json",
            },   
            {
                LOCAL_PUBLIC_URL,
                "2506f0e1-29e4-4132-9b37-f7452dc8a89b",
                "01.json",
            },
            {
                LOCAL_PUBLIC_URL,
                "456e9ee0-5b60-4f38-82b5-83ba5d338038",
                "02.json",
            },               
        };
    }

    @Test(dataProvider = "cases")
    public void testDemoServerAccessID(String requestURL, String drsObjectId, String expFileName) throws Exception 
    {
        String[] requestedRawData = new String[3];

        HttpClient client = HttpClient.newHttpClient();

        Builder requestBuilder = HttpRequest.newBuilder()
            .GET()
            .uri(URI.create(requestURL + drsObjectId)); //Ask for the object

        HttpRequest requestDRS = requestBuilder.build();

        // Request 3 access_IDs and get the raw data

        for (int i = 0; i < 3; i++)
        {
            HttpResponse<String> responseDRS = client.send(requestDRS, BodyHandlers.ofString());

            JSONObject req_JSON = new JSONObject(responseDRS.body());
            JSONArray JSONarray = req_JSON.getJSONArray("access_methods");
            JSONObject JSONobj_access_methods = JSONarray.getJSONObject(0);

            String accessID = JSONobj_access_methods.getString("access_id"); // Store the access_ids

            // Getting the URL

            Builder requestBuilderAccessURL = HttpRequest.newBuilder()
                .GET()
                .uri(URI.create(requestURL + drsObjectId + "/access/" + accessID)); //Ask for the access URL

            HttpRequest requestAccessURL = requestBuilderAccessURL.build();
            HttpResponse<String> responseAccessURL = client.send(requestAccessURL, BodyHandlers.ofString());

            JSONObject accessURLJson = new JSONObject(responseAccessURL.body());
            String accessURL = accessURLJson.getString("url");
            
            // Getting the Raw Data

            Builder requestBuilderRawData = HttpRequest.newBuilder()
                .GET()
                .uri(URI.create(accessURL)); //Ask for the raw data

            HttpRequest requestRawData = requestBuilderRawData.build();
            HttpResponse<String> responseRawData = client.send(requestRawData, BodyHandlers.ofString());

            requestedRawData[i] = responseRawData.body(); // Store the raw data
        }
        
        // The different access_ids should lead to the same raw data
        int result = verifyIDs(requestedRawData);

        Assert.assertEquals(1, result); // If all the raw data are equal, 1 will be returned
    }

    public int verifyIDs(String[] array) 
    {
        for (String s : array) 
        {
            if (!s.equals(array[0]))
            {
                return 0;
            } 
        }
        
        return 1;
    }
}
