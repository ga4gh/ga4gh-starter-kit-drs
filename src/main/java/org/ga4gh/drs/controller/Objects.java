package org.ga4gh.drs.controller;

import org.ga4gh.drs.model.DrsObject;
import org.ga4gh.drs.utils.requesthandler.ObjectRequestHandler;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@RequestMapping("/objects")
public class Objects {

    @Resource(name = "objectRequestHandler")
    ObjectRequestHandler objectRequestHandler;

    @GetMapping(path = "/{object_id:.+}")
    public DrsObject getObjectById(
        @PathVariable(name = "object_id") String id,
        @RequestParam(name = "expand", required = false) boolean expand
    ) {
        objectRequestHandler.setObjectId(id);
        return objectRequestHandler.handleRequest();
    }

    @GetMapping(path = "/{object_id:.+}/access/{access_id:.+}")
    public String getAccessMethodById(
        @PathVariable(name = "object_id") String object_id,
        @PathVariable(name = "access_id") String access_id
    ) {
        return "Access endpoint. object_id: " + object_id + ", access_id: " + access_id;
    }
}