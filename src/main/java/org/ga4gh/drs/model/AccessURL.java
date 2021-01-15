package org.ga4gh.drs.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import org.springframework.lang.NonNull;

import java.net.URL;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class AccessURL {
    @NonNull
    private URL url;

    private Map<String, String> headers;

    public AccessURL(URL url) {
        this.url = url;
    }

    public AccessURL(URL url, Map<String, String> headers) {
        this.url = url;
        this.headers = headers;
    }

    public URL getUrl() {
        return url;
    }

    public void setUrl(URL url) {
        this.url = url;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public void setHeaders(Map<String, String> headers) {
        this.headers = headers;
    }
}
