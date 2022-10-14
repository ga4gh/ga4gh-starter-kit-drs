package org.ga4gh.starterkit.drs.utils.requesthandler;

import java.net.URI;
import org.ga4gh.starterkit.common.config.ServerProps;
import static org.ga4gh.starterkit.drs.constant.DrsApiConstants.DRS_API_V1;
import org.ga4gh.starterkit.common.exception.ResourceNotFoundException;
import org.ga4gh.starterkit.common.requesthandler.RequestHandler;
import org.ga4gh.starterkit.common.util.logging.LoggingUtil;
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

    @Autowired
    private LoggingUtil loggingUtil;

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
            String exceptionMessage = "invalid access_id/object_id " + accessId + '/' + objectId;
            loggingUtil.error("Exception occurred: " + exceptionMessage);
            throw new ResourceNotFoundException(exceptionMessage);
        }

        AccessURL accessURL = generateAccessURLForFile();
        return accessURL;
    }

    /**
     * Constructs the streaming endpoint URL for the given ids
     * @return AccessURL pointing to this service's streaming endpoint
     */
    private AccessURL generateAccessURLForFile() {
        String path = DRS_API_V1 + "/stream/" + objectId + "/" + accessId;

        StringBuffer uriBuffer = new StringBuffer(serverProps.getScheme() + "://");
        uriBuffer.append(serverProps.getHostname());
        if (!serverProps.getPublicApiPort().equals("80")) {
            uriBuffer.append(":" + serverProps.getPublicApiPort());
        }
        uriBuffer.append(path);
        return new AccessURL(URI.create(uriBuffer.toString()));
    }
}
