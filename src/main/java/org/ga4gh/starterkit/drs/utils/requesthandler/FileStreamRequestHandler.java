package org.ga4gh.starterkit.drs.utils.requesthandler;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.io.IOUtils;
import org.ga4gh.starterkit.common.exception.ResourceNotFoundException;
import org.ga4gh.starterkit.common.requesthandler.RequestHandler;
import org.ga4gh.starterkit.drs.utils.cache.AccessCache;
import org.ga4gh.starterkit.drs.utils.cache.AccessCacheItem;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Enables handling logic for the streaming endpoint, allows files stored on the 
 * service's file system to be streamed over the API
 */
public class FileStreamRequestHandler implements RequestHandler<Void> {

    @Autowired
    private AccessCache accessCache;

    private String objectId;
    private String accessId;
    private HttpServletResponse response;

    /**
     * Streams the file contents referenced by the provided object id and access id
     * to client
     */
    public Void handleRequest() {
        // look up the access cache to see if a valid set of object id and 
        // access id was provided
        AccessCacheItem cacheItem = accessCache.get(getObjectId(), getAccessId());
        if (cacheItem == null) {
            throw new ResourceNotFoundException("invalid access_id/object_id");
        }

        try {
            // Open file input stream
            InputStream inputStream = new FileInputStream(new File(cacheItem.getObjectPath()));

            // Set Response headers
            response.addHeader("Content-Disposition", "attachment");
            if (cacheItem.getMimeType() != null) {
                response.setContentType(cacheItem.getMimeType());
            }

            // copy file input stream to response's output stream
            IOUtils.copy(inputStream, response.getOutputStream());
            response.flushBuffer();
        } catch (IOException e) {
            // TODO THROW REST CONTROLLER EXCEPTION
            return null;
        }

        return null;
    }

    /* Setters and getters */

    /**
     * Assign objectId
     * @param objectId DrsObject id
     */
    public void setObjectId(String objectId) {
        this.objectId = objectId;
    }

    /**
     * Retrieve objectId
     * @return DrsObject id
     */
    public String getObjectId() {
        return objectId;
    }

    /**
     * Assign accessId
     * @param accessId access identifier
     */
    public void setAccessId(String accessId) {
        this.accessId = accessId;
    }

    /**
     * Retrieve accessId
     * @return access identifier
     */
    public String getAccessId() {
        return accessId;
    }

    /**
     * Assign response
     * @param response low-level Spring response object handling file streaming
     */
    public void setResponse(HttpServletResponse response) {
        this.response = response;
    }

    /**
     * Retrieve response
     * @return low-level Spring response object handling file streaming
     */
    public HttpServletResponse getResponse() {
        return response;
    }
}
