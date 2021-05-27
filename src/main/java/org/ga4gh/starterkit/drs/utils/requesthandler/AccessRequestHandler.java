package org.ga4gh.starterkit.drs.utils.requesthandler;

import java.net.URI;
import org.ga4gh.starterkit.common.config.ServerProps;
import static org.ga4gh.starterkit.drs.constant.DrsApiConstants.DRS_API_V1;
import org.ga4gh.starterkit.common.exception.ResourceNotFoundException;
import org.ga4gh.starterkit.common.requesthandler.RequestHandler;
import org.ga4gh.starterkit.drs.model.AccessURL;
import org.ga4gh.starterkit.drs.utils.cache.AccessCache;
import org.ga4gh.starterkit.drs.utils.cache.AccessCacheItem;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Request handling logic for providing an AccessURL from a provided DrsObject id
 * and access ID
 */
public class AccessRequestHandler implements RequestHandler<AccessURL> {

    @Autowired
    private ServerProps serverProps;

    @Autowired
    private AccessCache accessCache;

    private String objectId;
    private String accessId;

    /**
     * Instantiates a new AccessRequestHandler
     */
    public AccessRequestHandler() {

    }

    /**
     * Prepares the request handler with input params from the controller function
     * @param objectId DrsObject identifier
     * @param accessId access identifier
     * @return the prepared request handler
     */
    public AccessRequestHandler prepare(String objectId, String accessId) {
        this.objectId = objectId;
        this.accessId = accessId;
        return this;
    }

    /**
     * Provides an AccessURL for the given DrsObject id and access ID
     */
    public AccessURL handleRequest() {
        AccessCacheItem cacheItem = accessCache.get(objectId, accessId);
        if (cacheItem == null) {
            throw new ResourceNotFoundException("invalid access_id/object_id");
        }

        AccessURL accessURL = generateAccessURLForFile();
        return accessURL;
    }

    /**
     * Constructs the streaming endpoint URL for the given ids
     * @return AccessURL pointing to this service's streaming endpoint
     */
    private AccessURL generateAccessURLForFile() {
        String scheme = serverProps.getScheme();
        String hostname = serverProps.getHostname();
        String path = DRS_API_V1 + "/stream/" + objectId + "/" + accessId;
        return new AccessURL(URI.create(scheme + "://" + hostname + path));
    }
}
