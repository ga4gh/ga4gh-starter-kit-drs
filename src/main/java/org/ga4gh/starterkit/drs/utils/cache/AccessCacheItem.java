package org.ga4gh.starterkit.drs.utils.cache;

import org.ga4gh.starterkit.drs.model.AccessType;

public class AccessCacheItem {

    private String objectId;
    private String accessId;
    private String objectPath;
    private AccessType accessType;
    private String mimeType;

    public AccessCacheItem() {

    }

    public void setObjectId(String objectId) {
        this.objectId = objectId;
    }

    public String getObjectId() {
        return objectId;
    }

    public void setAccessId(String accessId) {
        this.accessId = accessId;
    }

    public String getAccessId() {
        return accessId;
    }

    public void setObjectPath(String objectPath) {
        this.objectPath = objectPath;
    }

    public String getObjectPath() {
        return objectPath;
    }

    public void setAccessType(AccessType accessType) {
        this.accessType = accessType;
    }

    public AccessType getAccessType() {
        return accessType;
    }

    public void setMimeType(String mimeType) {
        this.mimeType = mimeType;
    }

    public String getMimeType() {
        return mimeType;
    }
}
