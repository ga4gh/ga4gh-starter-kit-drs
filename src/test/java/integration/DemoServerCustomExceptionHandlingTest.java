package integration;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpRequest.Builder;
import java.net.http.HttpResponse.BodyHandlers;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.ga4gh.starterkit.common.exception.CustomExceptionResponse;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class DemoServerCustomExceptionHandlingTest {

    private static final String BASE_URL = "http://localhost:4500";

    private static final String ENDPOINT_BAD_REQUEST = "/errors/bad-request";
    private static final String ENDPOINT_NOT_FOUND = "/errors/not-found";
    private static final String ENDPOINT_CONFLICT = "/errors/conflict";

    private static final int OK = 200;
    private static final int BAD_REQUEST = 400;
    private static final int NOT_FOUND = 404;
    private static final int CONFLICT = 409;

    @DataProvider(name = "cases")
    public Object[][] getData() {
        return new Object[][] {
            {
                BASE_URL + ENDPOINT_BAD_REQUEST,
                false,
                false,
                BAD_REQUEST,
                "Bad Request",
                "Required request parameter 'error' for method parameter type boolean is not present"
            },
            {
                BASE_URL + ENDPOINT_BAD_REQUEST,
                true,
                true,
                BAD_REQUEST,
                "Bad Request",
                "Bad request parameter(s) encountered"
            },
            {
                BASE_URL + ENDPOINT_BAD_REQUEST,
                true,
                false,
                OK,
                null,
                null
            },
            {
                BASE_URL + ENDPOINT_NOT_FOUND,
                false,
                false,
                BAD_REQUEST,
                "Bad Request",
                "Required request parameter 'error' for method parameter type boolean is not present"
            },
            {
                BASE_URL + ENDPOINT_NOT_FOUND,
                true,
                true,
                NOT_FOUND,
                "Not Found",
                "The requested resource was not found"
            },
            {
                BASE_URL + ENDPOINT_NOT_FOUND,
                true,
                false,
                OK,
                null,
                null
            },
            {
                BASE_URL + ENDPOINT_CONFLICT,
                false,
                false,
                BAD_REQUEST,
                "Bad Request",
                "Required request parameter 'error' for method parameter type boolean is not present"
            },
            {
                BASE_URL + ENDPOINT_CONFLICT,
                true,
                true,
                CONFLICT,
                "Conflict",
                "Conflict with server state"
            },
            {
                BASE_URL + ENDPOINT_CONFLICT,
                true,
                false,
                OK,
                null,
                null
            }
        };
    }

    @Test(dataProvider = "cases")
    public void testDemoServerCustomExceptionHandling(String requestURL, boolean useErrorParam, boolean errorParam, int expStatusCode, String expError, String expMessage) throws Exception {
        HttpClient client = HttpClient.newHttpClient();
        Builder requestBuilder = HttpRequest.newBuilder();

        String finalURL = requestURL;
        if (useErrorParam) {
            finalURL += "?error=" + String.valueOf(errorParam);
        }
        requestBuilder.uri(URI.create(finalURL));
        HttpRequest request = requestBuilder.build();
        HttpResponse<String> response = client.send(request, BodyHandlers.ofString());

        Assert.assertEquals(response.statusCode(), expStatusCode);
        if (expStatusCode != OK) {
            ObjectMapper mapper = new ObjectMapper();
            CustomExceptionResponse customExceptionResponse = mapper.readValue(response.body(), CustomExceptionResponse.class);
            Assert.assertEquals(customExceptionResponse.getStatusCode(), expStatusCode);
            Assert.assertEquals(customExceptionResponse.getError(), expError);
            Assert.assertEquals(customExceptionResponse.getMessage(), expMessage);
        }
    }
}
