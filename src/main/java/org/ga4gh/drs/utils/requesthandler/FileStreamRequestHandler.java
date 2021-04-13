package org.ga4gh.drs.utils.requesthandler;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.io.IOUtils;
import org.ga4gh.starterkit.common.exception.ResourceNotFoundException;
import org.ga4gh.drs.utils.cache.AccessCache;
import org.ga4gh.drs.utils.cache.AccessCacheItem;
import org.springframework.beans.factory.annotation.Autowired;

public class FileStreamRequestHandler implements RequestHandler<Void> {

    @Autowired
    private AccessCache accessCache;

    private String objectId;
    private String accessId;
    private HttpServletResponse response;

    public Void handleRequest() {
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

    public void setResponse(HttpServletResponse response) {
        this.response = response;
    }

    public HttpServletResponse getResponse() {
        return response;
    }
}
