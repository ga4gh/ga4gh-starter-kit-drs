package integration;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class DemoServerMultiPortTest {

    private static final int OK = 200;
    private static final int NOT_FOUND = 404;
    private static final String EMPTY_RESPONSE = "";
    private static final String PUBLIC_RESPONSE = "Hello, World! You have reached the PUBLIC 'hello-world' endpoint";
    private static final String ADMIN_RESPONSE = "Hello, World! You have reached the ADMIN 'hello-world' endpoint";

    @DataProvider(name = "cases")
    public Object[][] getData() {
        return new Object[][] {
            // DEFAULT PUBLIC AND ADMIN PORTS

            // public port, public endpoint
            {
                "http://localhost:4500/hello-world",
                OK,
                PUBLIC_RESPONSE
            },
            // admin port, public endpoint
            {
                "http://localhost:4501/hello-world",
                NOT_FOUND,
                EMPTY_RESPONSE
            },
            // public port, admin endpoint
            {
                "http://localhost:4500/admin/hello-world",
                NOT_FOUND,
                EMPTY_RESPONSE
            },
            // admin port, admin endpoint
            {
                "http://localhost:4501/admin/hello-world",
                OK,
                ADMIN_RESPONSE
            },

            // CUSTOM PUBLIC AND ADMIN PORTS PROVIDED BY CONFIG
            // public port, public endpoint
            {
                "http://localhost:7000/hello-world",
                OK,
                PUBLIC_RESPONSE
            },
            // admin port, public endpoint
            {
                "http://localhost:7001/hello-world",
                NOT_FOUND,
                EMPTY_RESPONSE
            },
            // public port, admin endpoint
            {
                "http://localhost:7000/admin/hello-world",
                NOT_FOUND,
                EMPTY_RESPONSE
            },
            // admin port, admin endpoint
            {
                "http://localhost:7001/admin/hello-world",
                OK,
                ADMIN_RESPONSE
            }
        };
    }
    
    @Test(dataProvider = "cases")
    public void testDemoServer(String requestURL, int expStatusCode, String expResponseBody) throws Exception {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
            .GET()
            .uri(URI.create(requestURL))
            .build();
        HttpResponse<String> response = client.send(request, BodyHandlers.ofString());

        Assert.assertEquals(response.statusCode(), expStatusCode);
        Assert.assertEquals(response.body(), expResponseBody);
    }
}
