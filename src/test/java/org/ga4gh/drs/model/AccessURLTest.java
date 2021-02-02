package org.ga4gh.drs.model;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class AccessURLTest {

    @DataProvider(name = "cases")
    public Object[][] getData() throws MalformedURLException {
        return new Object[][] {
            {
                new URL("https", "ga4gh.org", "/genomic-data-toolkit"),
                new HashMap<String, String>() {{
                    put("Authorization", "bearer mytoken");
                }}
            },
            {
                new URL("https", "ebi.ac.uk", "/cram-reference-registry"),
                new HashMap<String, String>() {{
                    put("Range", "bytes=0-5000");
                }}
            }
        };
    }

    private void assertions(AccessURL accessURL, URL url, Map<String, String> headers) {
        Assert.assertEquals(accessURL.getUrl(), url);
        Assert.assertEquals(accessURL.getHeaders(), headers);
    }

    @Test(dataProvider = "cases")
    public void testAccessURLSingleArgConstructor(URL url, Map<String, String> headers) {
        AccessURL accessURL = new AccessURL(url);
        accessURL.setUrl(url);
        accessURL.setHeaders(headers);
        assertions(accessURL, url, headers);
    }

    @Test(dataProvider = "cases")
    public void testAccessURLAllArgsConstructor(URL url, Map<String, String> headers) {
        AccessURL accessURL = new AccessURL(url, headers);
        assertions(accessURL, url, headers);
    }
}
