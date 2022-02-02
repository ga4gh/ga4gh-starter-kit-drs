package integration;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpRequest.Builder;
import java.net.http.HttpResponse.BodyHandlers;
import java.util.ArrayList;
import java.util.List;
import org.springframework.http.HttpHeaders;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class DemoServerGetDrsObjects 
{
    // Define variables and constants
    private static final String LOCAL_HOST_URL = "http://localhost:4500/ga4gh/drs/v1/objects/";
    
    @DataProvider(name = "cases")
    public Object[][] getData() 
    {
        return new Object[][] 
        {
            // Getting a DrsObject
            {
                LOCAL_HOST_URL,
                "b8cd0667-2c33-4c9f-967b-161b905932c9",
                "phenopackets.test.dataset"
            },            
        };
    }

    @Test(dataProvider = "cases")
    public void testDemoServerGetDrsObjects(String requestURL, String drsObjectId, String expName) throws Exception 
    {
        HttpClient client = HttpClient.newHttpClient();

        Builder requestBuilder = HttpRequest.newBuilder()
            .GET()
            .uri(URI.create(requestURL + drsObjectId)); //Ask for the object

        HttpRequest request = requestBuilder.build();
        HttpResponse<String> response = client.send(request, BodyHandlers.ofString());

        System.out.print(response);

        // Assert.assertEquals(response.headers().name), expName);

        // Assert.assertEquals(response.statusCode(), expStatusCode); //Checks if the two things are the same [where testing happens]
        // Assert.assertEquals(response.body(), expResponseBodyam
    }
}
