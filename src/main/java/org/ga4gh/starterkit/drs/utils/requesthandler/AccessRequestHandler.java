package org.ga4gh.starterkit.drs.utils.requesthandler;

import java.net.URI;
import org.ga4gh.starterkit.drs.configuration.ServerProps;
import org.ga4gh.starterkit.drs.constant.DrsServerConstants;
import org.ga4gh.starterkit.common.exception.ResourceNotFoundException;
import org.ga4gh.starterkit.drs.model.AccessURL;
import org.ga4gh.starterkit.drs.utils.cache.AccessCache;
import org.ga4gh.starterkit.drs.utils.cache.AccessCacheItem;
import org.springframework.beans.factory.annotation.Autowired;

public class AccessRequestHandler implements RequestHandler<AccessURL> {

    @Autowired
    private ServerProps serverProps;

    @Autowired
    private AccessCache accessCache;

    private String objectId;
    private String accessId;

    public AccessRequestHandler() {

    }

    public AccessURL handleRequest() {
        AccessCacheItem cacheItem = accessCache.get(objectId, accessId);
        if (cacheItem == null) {
            throw new ResourceNotFoundException("invalid access_id/object_id");
        }

        AccessURL accessURL = generateAccessURLForFile();
        return accessURL;
    }

    private AccessURL generateAccessURLForFile() {
        String hostname = serverProps.getHostname();
        String path = DrsServerConstants.DRS_API_PREFIX + "/stream/" + getObjectId() + "/" + getAccessId();
        return new AccessURL(URI.create("http://" + hostname + path));
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
}
