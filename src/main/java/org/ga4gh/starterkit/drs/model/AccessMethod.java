package org.ga4gh.starterkit.drs.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonView;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import org.ga4gh.starterkit.drs.utils.SerializeView;
import org.springframework.lang.NonNull;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
@JsonView(SerializeView.Public.class)
public class AccessMethod {
    /**
     * At least one of accessId or accessUrl is required
     */
    private String accessId;

    private AccessURL accessUrl;

    // Required
    @NonNull
    private AccessType type;

    // Optional
    private String region;

    public AccessMethod() {
        
    }

    public AccessMethod(String accessID, AccessType type) {
        this.accessId = accessID;
        this.type = type;
    }

    public AccessMethod(AccessURL accessURL, AccessType type) {
        this.accessUrl = accessURL;
        this.type = type;
    }

    public AccessMethod(String accessID, AccessType type, String region) {
        this(accessID, type);
        this.region = region;
    }

    public AccessMethod(AccessURL accessURL, AccessType type, String region) {
        this(accessURL, type);
        this.region = region;
    }

    public String getAccessId() {
        return accessId;
    }

    public void setAccessId(String accessId) {
        this.accessId = accessId;
    }

    public AccessURL getAccessUrl() {
        return accessUrl;
    }

    public void setAccessUrl(AccessURL accessUrl) {
        this.accessUrl = accessUrl;
    }

    public AccessType getType() {
        return type;
    }

    public void setType(AccessType type) {
        this.type = type;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }
}
