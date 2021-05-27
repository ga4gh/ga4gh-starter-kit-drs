package org.ga4gh.starterkit.drs.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonView;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import org.ga4gh.starterkit.drs.utils.SerializeView;
import org.springframework.lang.NonNull;

/**
 * Directly from DRS specification, indicates how to obtain file bytes for a 
 * DRSObject
 */
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

    /**
     * Instantiates a new AccessMethod with empty properties
     */
    public AccessMethod() {
        
    }

    /**
     * Instantiates a new AccessMethod with preconfigured accessID and access type
     * @param accessID access ID
     * @param type access type
     */
    public AccessMethod(String accessID, AccessType type) {
        this.accessId = accessID;
        this.type = type;
    }

    /**
     * Instantiates a new AccessMethod with preconfigured accessURL and access type
     * @param accessURL access URL
     * @param type access type
     */
    public AccessMethod(AccessURL accessURL, AccessType type) {
        this.accessUrl = accessURL;
        this.type = type;
    }

    /**
     * Instantiates a new AccessMethod with preconfigured accessID, access type, and region
     * @param accessID access ID
     * @param type access type
     * @param region region
     */
    public AccessMethod(String accessID, AccessType type, String region) {
        this(accessID, type);
        this.region = region;
    }

    /**
     * Instantiates a new AccessMethod with preconfigured access URL, access type, and region
     * @param accessURL access URL
     * @param type access type
     * @param region region
     */
    public AccessMethod(AccessURL accessURL, AccessType type, String region) {
        this(accessURL, type);
        this.region = region;
    }

    /**
     * Retrieve accessID
     * @return accessID
     */
    public String getAccessId() {
        return accessId;
    }

    /**
     * Assign accessID
     * @param accessId accessID
     */
    public void setAccessId(String accessId) {
        this.accessId = accessId;
    }

    /**
     * Retrieve access URL
     * @return access URL
     */
    public AccessURL getAccessUrl() {
        return accessUrl;
    }

    /**
     * Assign access URL
     * @param accessUrl access URL
     */
    public void setAccessUrl(AccessURL accessUrl) {
        this.accessUrl = accessUrl;
    }

    /**
     * Retrieve access type
     * @return access type
     */
    public AccessType getType() {
        return type;
    }

    /**
     * Assign access type
     * @param type access type
     */
    public void setType(AccessType type) {
        this.type = type;
    }

    /**
     * Retrieve region
     * @return region
     */
    public String getRegion() {
        return region;
    }

    /**
     * Assign region
     * @param region region
     */
    public void setRegion(String region) {
        this.region = region;
    }
}
