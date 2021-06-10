package org.ga4gh.starterkit.drs.controller;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import static org.ga4gh.starterkit.drs.constant.DrsApiConstants.DRS_API_V1;
import org.ga4gh.starterkit.common.util.logging.LoggingUtil;
import org.ga4gh.starterkit.drs.utils.requesthandler.FileStreamRequestHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Stream handler, enables client to access bytes for files on the server machine
 * that the client doesn't have direct access to 
 */
@RestController
@RequestMapping(DRS_API_V1 + "/stream")
public class Stream {

    @Resource(name = "fileStreamRequestHandler")
    private FileStreamRequestHandler fileStreamRequestHandler;

    @Autowired
    private LoggingUtil loggingUtil;

    /**
     * Stream file bytes for the requested DRSObject and access id
     * @param objectId DRSObject identifier
     * @param accessId access identifier
     * @param response Spring HttpServletResponse
     */
    @GetMapping(path = "/{object_id:.+}/{access_id:.+}")
    public void streamFile(
        @PathVariable(name = "object_id") String objectId,
        @PathVariable(name = "access_id") String accessId,
        HttpServletResponse response
    ) {
        loggingUtil.debug("Public API request: local file streaming. drs id='" + objectId + "', access id='" + accessId + "'");
        fileStreamRequestHandler.prepare(objectId, accessId, response).handleRequest();
    }
}
