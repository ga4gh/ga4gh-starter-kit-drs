package org.ga4gh.starterkit.drs.controller;

import static org.ga4gh.starterkit.drs.constant.DrsApiConstants.DRS_API_V1;

import org.ga4gh.starterkit.common.util.logging.LoggingUtil;
import org.ga4gh.starterkit.drs.model.AccessURL;
import org.ga4gh.starterkit.drs.model.DrsObject;
import org.ga4gh.starterkit.drs.utils.SerializeView;
import org.ga4gh.starterkit.drs.utils.requesthandler.AccessRequestHandler;
import org.ga4gh.starterkit.drs.utils.requesthandler.ObjectRequestHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestParam;
import javax.annotation.Resource;
import com.fasterxml.jackson.annotation.JsonView;

/**
 * Controller functions for accessing DRSObjects according to the DRS specification
 */
@RestController
@RequestMapping(DRS_API_V1 + "/objects")
public class Objects {

    @Resource(name = "objectRequestHandler")
    private ObjectRequestHandler objectRequestHandler;

    @Resource(name = "accessRequestHandler")
    private AccessRequestHandler accessRequestHandler;

    @Autowired
    private LoggingUtil loggingUtil;

    // Standard endpoints

    /**
     * Show information about a DRS object
     * @param objectId identifier of DRSObject of interest 
     * @param expand if true, display recursive bundling under 'contents' property
     * @return DRSObject by the requested id
     */
    @GetMapping(path = "/{object_id:.+}")
    @JsonView(SerializeView.Public.class)
    public DrsObject getObjectById(
        @PathVariable(name = "object_id") String objectId,
        @RequestParam(name = "expand", required = false) boolean expand
    ) {
        loggingUtil.debug("Public API request: DrsObject with id '" + objectId + "', expand=" + expand);
        return objectRequestHandler.prepare(objectId, expand).handleRequest();
    }

    /**
     * Get an access URL for fetching the DRS Object's  file bytes
     * @param objectId DRSObject identifier
     * @param accessId access identifier
     * @return a DRS-spec AccessURL indicating file bytes location
     */
    @GetMapping(path = "/{object_id:.+}/access/{access_id:.+}")
    public AccessURL getAccessURLById(
        @PathVariable(name = "object_id") String objectId,
        @PathVariable(name = "access_id") String accessId
    ) {
        loggingUtil.debug("Public API request: AccessURL for DRS id '" + objectId + "', access id '" + accessId + "'");
        return accessRequestHandler.prepare(objectId, accessId).handleRequest();
    }
}
