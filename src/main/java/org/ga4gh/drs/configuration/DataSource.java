package org.ga4gh.drs.configuration;

import org.ga4gh.drs.model.AccessType;

public class DataSource {

    private String drsIdPattern;

    private AccessType protocol;
    
    private String objectPathTemplate;

    public DataSource() {

    }

    public DataSource(String drsIdPattern, AccessType protocol, String objectPathTemplate) {
        this.drsIdPattern = drsIdPattern;
        this.protocol = protocol;
        this.objectPathTemplate = objectPathTemplate;
    }

    public void setDrsIdPattern(String drsIdPattern) {
        this.drsIdPattern = drsIdPattern;
    }

    public String getDrsIdPattern() {
        return drsIdPattern;
    }

    public void setProtocol(AccessType protocol) {
        this.protocol = protocol;
    }

    public AccessType getProtocol() {
        return protocol;
    }

    public void setObjectPathTemplate(String objectPathTemplate) {
        this.objectPathTemplate = objectPathTemplate;
    }

    public String getObjectPathTemplate() {
        return objectPathTemplate;
    }
}
