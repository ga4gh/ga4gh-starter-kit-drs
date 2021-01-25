package org.ga4gh.drs.configuration;

import org.ga4gh.drs.model.AccessType;

import java.util.function.Supplier;
import java.util.regex.Pattern;

public class DataSource {

    // Lazily compile the regex
    private Supplier<Pattern> drsIdPattern;

    private AccessType protocol;
    
    private String objectPathTemplate;

    public DataSource() {

    }

    public DataSource(String drsIdPattern, AccessType protocol, String objectPathTemplate) {
        this.drsIdPattern = () -> Pattern.compile(drsIdPattern);
        this.protocol = protocol;
        this.objectPathTemplate = objectPathTemplate;
    }

    public void setDrsIdPattern(String drsIdPattern) {
        this.drsIdPattern = () -> Pattern.compile(drsIdPattern);
    }

    public Pattern getDrsIdPattern() {
        return drsIdPattern.get();
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
