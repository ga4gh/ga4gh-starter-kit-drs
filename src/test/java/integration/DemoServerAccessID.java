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

    // Raw phenopacket data directory
    private static final String OBJ_DIR = "/data/phenopackets/";
    
    @DataProvider(name = "cases")
    public Object[][] getData() 
    {
        return new Object[][] 
        {
            { 
                LOCAL_PUBLIC_URL,
                "697907bf-d5bd-433e-aac2-1747f1faf366",
                "Zhang-2009-EDA-proband.json",
            },   
            {
                LOCAL_PUBLIC_URL,
                "2506f0e1-29e4-4132-9b37-f7452dc8a89b",
                "Mundhofir-2013-FGFR2-Patient_1.json",
            },
            {
                LOCAL_PUBLIC_URL,
                "456e9ee0-5b60-4f38-82b5-83ba5d338038",
                "Lalani-2016-TANGO2-Subject_1.json",
            },               
        };
    }

    @Test(dataProvider = "cases")
    public void testDemoServerAccessID(String requestURL, String drsObjectId, String expFileName) throws Exception 
    {
        // (0) Will get raw data through each access type
        List<String> requestedRawData = new ArrayList<String>(); 

        // (1) Load local file containing expected phenopacket data
        String drsObjectExpFile = OBJ_DIR + expFileName;
        String expResponseBody = ResourceLoader.load(drsObjectExpFile);
        JSONObject exp_JSON = new JSONObject(expResponseBody);
    
        requestedRawData.add(exp_JSON.toString());
        
        // (2) Request to get the access methods
        HttpClient client = HttpClient.newHttpClient();
        Builder requestBuilder = HttpRequest.newBuilder()
            .GET()
            .uri(URI.create(requestURL + drsObjectId)); //Ask for the object

        HttpRequest requestDRS = requestBuilder.build();
        HttpResponse<String> responseDRS = client.send(requestDRS, BodyHandlers.ofString());
        JSONObject responseDRS_JO = new JSONObject(responseDRS.body());

        // (3) Looping through access methods
        JSONArray accessMethods = responseDRS_JO.getJSONArray("access_methods");

        for (int i = 0; i < accessMethods.length(); i++)
        {
            JSONObject method = accessMethods.getJSONObject(i);
            String accessType = method.get("type").toString();

            if(accessType.equals("https"))
            {
                String accessID = method.get("access_id").toString();

                // Getting the access URL
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

                requestedRawData.add(responseRawData.body()); // Store the raw data
            }
            else if(accessType.equals("s3"))
            {
                String accessURL = method.getJSONObject("access_url").get("url").toString(); // "s3://<path>"
                String rawDataPath = accessURL.substring(accessURL.lastIndexOf("//") + 1); // <path> // String objectURL = "ga4gh-demo-data/phenopackets/Zhang-2009-EDA-proband.json";
                String region = method.get("region").toString(); // "us-east-2"

                // Resolve the s3 access URL into a http URL
                String baseURL = "https://s3." + region + ".amazonaws.com/";
                String finalURL = baseURL + rawDataPath;

                // Send the request to get the phenopacket
                Builder requestBuilderRawData = HttpRequest.newBuilder()
                .GET()
                .uri(URI.create(finalURL)); //Ask for the raw data

                HttpRequest requestRawData = requestBuilderRawData.build();
                HttpResponse<String> responseRawData = client.send(requestRawData, BodyHandlers.ofString());

                requestedRawData.add(responseRawData.body()); // Store the raw data
            }
        }
        
        // (4) Turn all data into JSON Objects and then back into strings so the keys are all in the same order
        for (int i = 0; i < requestedRawData.size(); i++)
        {
            JSONObject obj = new JSONObject(requestedRawData.get(i));
            requestedRawData.set(i, obj.toString());
        }
        
        // (5) All raw data should be the same
        int result = verifyIDs(requestedRawData);

        Assert.assertEquals(1, result); // If all the raw data are equal, 1 will be returned
    }

    public int verifyIDs(List<String> lst) 
    {
        for (String s : lst) 
        {
            if (!s.equals(lst.get(0)))
            {
                return 0;
            } 
        }
        
        return 1;
    }
}
