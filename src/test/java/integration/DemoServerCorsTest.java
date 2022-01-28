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

public class DemoServerCorsTest {

    private static final String DEFAULT_PUBLIC_ENDPOINT = "http://localhost:4500/hello-world";
    private static final String DEFAULT_ADMIN_ENDPOINT = "http://localhost:4501/admin/hello-world";
    private static final String CUSTOM_PUBLIC_ENDPOINT = "http://localhost:7000/hello-world";
    private static final String CUSTOM_ADMIN_ENDPOINT = "http://localhost:7001/admin/hello-world";

    private static final int OK = 200;
    private static final int FORBIDDEN = 403;

    private static final String PUBLIC_RESPONSE = "Hello, World! You have reached the PUBLIC 'hello-world' endpoint";
    private static final String ADMIN_RESPONSE = "Hello, World! You have reached the ADMIN 'hello-world' endpoint";
    private static final String INVALID_CORS_RESPONSE = "Invalid CORS request";

    private static final String DEFAULT_VALID_CORS_HEADER = "http://localhost";
    private static final String CUSTOM_PUBLIC_VALID_CORS_HEADER = "http://some-public-site.com";
    private static final String CUSTOM_PUBLIC_INVALID_CORS_HEADER = "http://another-admin-site.com";
    private static final String CUSTOM_ADMIN_VALID_CORS_HEADER = "http://some-admin-site.com";
    private static final String CUSTOM_ADMIN_INVALID_CORS_HEADER = "http://some-public-site.com";

    private static final List<String> DEFAULT_EXP_CORS_HEADER = new ArrayList<String>() {{
        add("*");
    }};
    private static final List<String> CUSTOM_PUBLIC_EXP_CORS_HEADER = new ArrayList<String>() {{
        add("http://some-public-site.com");
    }};
    private static final List<String> CUSTOM_ADMIN_EXP_CORS_HEADER = new ArrayList<String>() {{
        add("http://some-admin-site.com");
    }};

    @DataProvider(name = "cases")
    public Object[][] getData() {
        return new Object[][] {
            // DEFAULT SERVER, PUBLIC PORT, NO ORIGIN
            {
                DEFAULT_PUBLIC_ENDPOINT,
                null,
                OK,
                PUBLIC_RESPONSE,
                null
            },
            // DEFAULT SERVER, PUBLIC PORT, VALID ORIGIN
            {
                DEFAULT_PUBLIC_ENDPOINT,
                DEFAULT_VALID_CORS_HEADER,
                OK,
                PUBLIC_RESPONSE,
                DEFAULT_EXP_CORS_HEADER
            },
            // DEFAULT SERVER, ADMIN PORT, NO ORIGIN
            {
                DEFAULT_ADMIN_ENDPOINT,
                null,
                OK,
                ADMIN_RESPONSE,
                null
            },
            // DEFAULT SERVER, ADMIN PORT, VALID ORIGIN
            {
                DEFAULT_ADMIN_ENDPOINT,
                DEFAULT_VALID_CORS_HEADER,
                OK,
                ADMIN_RESPONSE,
                DEFAULT_EXP_CORS_HEADER
            },
            // CUSTOM SERVER, PUBLIC PORT, NO ORIGIN
            {
                CUSTOM_PUBLIC_ENDPOINT,
                null,
                OK,
                PUBLIC_RESPONSE,
                null
            },
            // CUSTOM SERVER, PUBLIC PORT, VALID ORIGIN
            {
                CUSTOM_PUBLIC_ENDPOINT,
                CUSTOM_PUBLIC_VALID_CORS_HEADER,
                OK,
                PUBLIC_RESPONSE,
                CUSTOM_PUBLIC_EXP_CORS_HEADER
            },
            // CUSTOM SERVER, PUBLIC PORT, INVALID ORIGIN
            {
                CUSTOM_PUBLIC_ENDPOINT,
                CUSTOM_PUBLIC_INVALID_CORS_HEADER,
                FORBIDDEN,
                INVALID_CORS_RESPONSE,
                null
            },
            // CUSTOM SERVER, ADMIN PORT, NO ORIGIN
            {
                CUSTOM_ADMIN_ENDPOINT,
                null,
                OK,
                ADMIN_RESPONSE,
                null
            },
            // CUSTOM SERVER, ADMIN PORT, VALID ORIGIN
            {
                CUSTOM_ADMIN_ENDPOINT,
                CUSTOM_ADMIN_VALID_CORS_HEADER,
                OK,
                ADMIN_RESPONSE,
                CUSTOM_ADMIN_EXP_CORS_HEADER
            },
            // CUSTOM SERVER, ADMIN PORT, INVALID ORIGIN
            {
                CUSTOM_ADMIN_ENDPOINT,
                CUSTOM_ADMIN_INVALID_CORS_HEADER,
                FORBIDDEN,
                INVALID_CORS_RESPONSE,
                null
            },
        };
    }

    @Test(dataProvider = "cases")
    public void testDemoServerCors(String requestURL, String originRequestHeader, int expStatusCode, String expResponseBody, List<String> expResponseCorsOriginHeaderValue) throws Exception {
        HttpClient client = HttpClient.newHttpClient();
        Builder requestBuilder = HttpRequest.newBuilder()
            .GET()
            .uri(URI.create(requestURL));
        if (originRequestHeader != null) {
            requestBuilder.header(HttpHeaders.ORIGIN, originRequestHeader);
        }
        HttpRequest request = requestBuilder.build();
        HttpResponse<String> response = client.send(request, BodyHandlers.ofString());

        Assert.assertEquals(response.statusCode(), expStatusCode);
        Assert.assertEquals(response.body(), expResponseBody);

        // OK responses may have CORS header
        if (expStatusCode == OK) {

            // if Origin header submitted, then Access-Control-Allow-Origin is evaluated,
            // otherwise, there must not be a value for it
            if (originRequestHeader != null) {
                List<String> corsAllowedOrigin = (List<String>) response.headers().map().get(HttpHeaders.ACCESS_CONTROL_ALLOW_ORIGIN);
                Assert.assertEquals(corsAllowedOrigin, expResponseCorsOriginHeaderValue);
            } else {
                Object corsAllowedOrigin = response.headers().map().get(HttpHeaders.ACCESS_CONTROL_ALLOW_ORIGIN);
                Assert.assertNull(corsAllowedOrigin);
            }
        
        // NON-OK responses will never have CORS header
        } else {
            Object corsAllowedOrigin = response.headers().map().get(HttpHeaders.ACCESS_CONTROL_ALLOW_ORIGIN);
            Assert.assertNull(corsAllowedOrigin);
        }
    }
}
