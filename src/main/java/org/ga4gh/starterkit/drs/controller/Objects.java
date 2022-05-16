package org.ga4gh.starterkit.drs.controller;

import static org.ga4gh.starterkit.drs.constant.DrsApiConstants.DRS_API_V1;

import java.util.ArrayList;
import java.util.List;

import org.ga4gh.starterkit.common.exception.CustomException;
import org.ga4gh.starterkit.common.util.logging.LoggingUtil;
import org.ga4gh.starterkit.drs.model.AccessURL;
import org.ga4gh.starterkit.drs.model.BulkAccessRequest;
import org.ga4gh.starterkit.drs.model.BulkObjectAccessId;
import org.ga4gh.starterkit.drs.model.BulkRequest;
import org.ga4gh.starterkit.drs.model.BulkResponse;
import org.ga4gh.starterkit.drs.model.DrsObject;
import org.ga4gh.starterkit.drs.utils.SerializeView;
import org.ga4gh.starterkit.drs.utils.passport.UserPassportMap;
import org.ga4gh.starterkit.drs.utils.requesthandler.AccessRequestHandler;
import org.ga4gh.starterkit.drs.utils.requesthandler.ObjectRequestHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.annotation.Resource;
import com.fasterxml.jackson.annotation.JsonView;

/**
 * Controller functions for accessing DRSObjects according to the DRS specification
 */
@RestController
@RequestMapping(DRS_API_V1 + "/objects")
public class Objects implements ApplicationContextAware {

    @Resource(name = "objectRequestHandler")
    private ObjectRequestHandler objectRequestHandler;

    @Resource(name = "accessRequestHandler")
    private AccessRequestHandler accessRequestHandler;

    @Autowired
    private LoggingUtil loggingUtil;

    private ApplicationContext context;

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
        return objectRequestHandler.prepare(objectId, expand, null).handleRequest();
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

    @PostMapping
    @JsonView(SerializeView.Public.class)
    public BulkResponse getBulkObjects(
        @RequestBody BulkRequest bulkRequest
    ) {
        // parse user passport
        UserPassportMap userPassportMap = null;
        if (bulkRequest.getPassports() != null) {
            userPassportMap = new UserPassportMap(bulkRequest.getPassports());
        }

        BulkResponse bulkResponse = new BulkResponse();
        int requested = 0;
        int resolved = 0;
        int unresolved = 0;

        for (String drsObjectId : bulkRequest.getSelection()) {
            requested++;
            try {
                ObjectRequestHandler handler = context.getBean(ObjectRequestHandler.class);
                DrsObject drsObject = handler.prepare(drsObjectId, false, userPassportMap).handleRequest();
                bulkResponse.getResolvedDrsObject().add(drsObject);
                resolved++;
            } catch (CustomException ex) {
                int httpStatus = ex.getClass().getAnnotation(ResponseStatus.class).value().value();
                bulkResponse.getUnresolvedDrsObject().put(drsObjectId, httpStatus);
                unresolved++;
            }
        }
        bulkResponse.getSummary().setRequested(requested);
        bulkResponse.getSummary().setResolved(resolved);
        bulkResponse.getSummary().setUnresolved(unresolved);

        return bulkResponse;
    }

    @PostMapping(path = "/access")
    @JsonView(SerializeView.Public.class)
    public List<AccessURL> getBulkAccessURLs(
        @RequestBody BulkAccessRequest bulkAccessRequest
    ) {
        List<AccessURL> accessURLs = new ArrayList<>();
        for (BulkObjectAccessId idPair : bulkAccessRequest.getSelection()) {
            try {
                AccessRequestHandler handler = context.getBean(AccessRequestHandler.class);
                AccessURL accessURL = handler.prepare(idPair.getObjectId(), idPair.getAccessId()).handleRequest();
                accessURLs.add(accessURL);
            } catch (CustomException ex) {

            }
        }

        return accessURLs;
    }

    public void setApplicationContext(ApplicationContext context) {
        this.context = context;
    }
}
