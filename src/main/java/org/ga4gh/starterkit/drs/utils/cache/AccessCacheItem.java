package org.ga4gh.starterkit.drs.utils.cache;

import org.ga4gh.starterkit.drs.model.AccessType;

/**
 * A single item within the access cache, stores information on how to access
 * the file bytes for a composite DRSObject id + access id
 */
public class AccessCacheItem {

    private String objectId;
    private String accessId;
    private String objectPath;
    private AccessType accessType;
    private String mimeType;

    /**
     * Instantiates a new AccessCacheItem
     */
    public AccessCacheItem() {

    }

    /**
     * Assign objectId
     * @param objectId DRSObject id
     */
    public void setObjectId(String objectId) {
        this.objectId = objectId;
    }

    /**
     * Retrieve objectId
     * @return DRSObject id
     */
    public String getObjectId() {
        return objectId;
    }

    /**
     * Assign accessId
     * @param accessId access id
     */
    public void setAccessId(String accessId) {
        this.accessId = accessId;
    }

    /**
     * Retrieve accessId
     * @return access id
     */
    public String getAccessId() {
        return accessId;
    }

    /**
     * Assign objectPath
     * @param objectPath path to the file bytes
     */
    public void setObjectPath(String objectPath) {
        this.objectPath = objectPath;
    }

    /**
     * Retrieve objectPath
     * @return path to the file bytes
     */
    public String getObjectPath() {
        return objectPath;
    }

    /**
     * Assign accessType
     * @param accessType access type for file byte source (ie URL scheme)
     */
    public void setAccessType(AccessType accessType) {
        this.accessType = accessType;
    }

    /**
     * Retrieve accessType
     * @return access type for file byte source (ie URL scheme)
     */
    public AccessType getAccessType() {
        return accessType;
    }

    /**
     * Assign mimeType
     * @param mimeType valid media type
     */
    public void setMimeType(String mimeType) {
        this.mimeType = mimeType;
    }

    /**
     * Retrieve mimeType
     * @return valid media type
     */
    public String getMimeType() {
        return mimeType;
    }
}
