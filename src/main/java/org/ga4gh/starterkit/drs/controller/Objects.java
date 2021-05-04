package org.ga4gh.starterkit.drs.controller;

import static org.ga4gh.starterkit.drs.constant.DrsApiConstants.DRS_API_V1;
import org.ga4gh.starterkit.drs.model.AccessURL;
import org.ga4gh.starterkit.drs.model.DrsObject;
import org.ga4gh.starterkit.drs.utils.SerializeView;
import org.ga4gh.starterkit.drs.utils.requesthandler.AccessRequestHandler;
import org.ga4gh.starterkit.drs.utils.requesthandler.ObjectRequestHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestParam;
import javax.annotation.Resource;
import com.fasterxml.jackson.annotation.JsonView;

@RestController
@RequestMapping(DRS_API_V1 + "/objects")
public class Objects {

    @Resource(name = "objectRequestHandler")
    ObjectRequestHandler objectRequestHandler;

    @Resource(name = "accessRequestHandler")
    AccessRequestHandler accessRequestHandler;

    // Standard endpoints

    @GetMapping(path = "/{object_id:.+}")
    @JsonView(SerializeView.Public.class)
    public DrsObject getObjectById(
        @PathVariable(name = "object_id") String id,
        @RequestParam(name = "expand", required = false) boolean expand
    ) {
        objectRequestHandler.setObjectId(id);
        objectRequestHandler.setExpand(expand);
        return objectRequestHandler.handleRequest();
    }

    @GetMapping(path = "/{object_id:.+}/access/{access_id:.+}")
    public AccessURL getAccessURLById(
        @PathVariable(name = "object_id") String objectId,
        @PathVariable(name = "access_id") String accessId
    ) {
        accessRequestHandler.setObjectId(objectId);
        accessRequestHandler.setAccessId(accessId);
        return accessRequestHandler.handleRequest();
    }
}
