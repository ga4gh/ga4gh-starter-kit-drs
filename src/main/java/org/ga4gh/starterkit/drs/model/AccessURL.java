package org.ga4gh.starterkit.drs.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonView;
import org.ga4gh.starterkit.drs.utils.SerializeView;
import org.springframework.lang.NonNull;
import java.net.URI;
import java.util.Map;

/**
 * Directly from DRS specification, contains URL and headers necessary for 
 * fetching file bytes of a requested DRSObject
 */
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@JsonView(SerializeView.Public.class)
public class AccessURL {

    @NonNull
    private URI url;

    private Map<String, String> headers;

    /**
     * Instantiates a new AccessURL object with default properties
     */
    public AccessURL() {
        
    }

    /**
     * Instantiates a new AccessURL object with preconfigured URL
     * @param url URL to file bytes
     */
    public AccessURL(URI url) {
        this.url = url;
    }

    /**
     * Instantiates a new AccessURL object with preconfigured URL and headers
     * @param url URL to file bytes
     * @param headers headers to be provided in request to facilitate access to data (eg Auth)
     */
    public AccessURL(URI url, Map<String, String> headers) {
        this.url = url;
        this.headers = headers;
    }

    /**
     * Retrieve URL
     * @return URL
     */
    public URI getUrl() {
        return url;
    }

    /**
     * Assign URL
     * @param url URL
     */
    public void setUrl(URI url) {
        this.url = url;
    }

    /**
     * Retrieve headers
     * @return headers
     */
    public Map<String, String> getHeaders() {
        return headers;
    }

    /**
     * Assign headers
     * @param headers headers
     */
    public void setHeaders(Map<String, String> headers) {
        this.headers = headers;
    }
}
