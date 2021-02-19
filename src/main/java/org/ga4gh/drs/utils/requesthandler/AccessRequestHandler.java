package org.ga4gh.drs.utils.requesthandler;

import java.net.URI;
import java.net.URISyntaxException;
import org.ga4gh.drs.AppConfigConstants;
import org.ga4gh.drs.configuration.DrsConfigContainer;
import org.ga4gh.drs.exception.ResourceNotFoundException;
import org.ga4gh.drs.model.AccessURL;
import org.ga4gh.drs.utils.cache.AccessCache;
import org.ga4gh.drs.utils.cache.AccessCacheItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

public class AccessRequestHandler implements RequestHandler<AccessURL> {

    @Autowired
    @Qualifier(AppConfigConstants.MERGED_DRS_CONFIG_CONTAINER)
    private DrsConfigContainer drsConfigContainer;

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

        AccessURL accessURL;

        try {
            switch (cacheItem.getAccessType()) {
                case FILE:
                    accessURL = generateAccessURLForFile();
                    break;
                case HTTPS:
                    accessURL = null;
                    break;
                default:
                    // TODO THROW ERROR
                    accessURL = null;
                    break;
            }
        } catch (URISyntaxException e) {
            // TODO THROW REST CONTROLLER EXCEPTION
            return null;
        }
        
        return accessURL;
    }

    private AccessURL generateAccessURLForFile() throws URISyntaxException {
        String hostname = drsConfigContainer.getDrsConfig().getServerProps().getHostname();
        String path = "/stream/" + getObjectId() + "/" + getAccessId();
        return new AccessURL(new URI("http://" + hostname + path));
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
