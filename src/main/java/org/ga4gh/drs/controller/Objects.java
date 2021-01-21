package org.ga4gh.drs.controller;

import java.util.Map;

import javax.annotation.Resource;

import org.ga4gh.drs.model.DrsObject;
import org.ga4gh.drs.utils.requesthandler.ObjectRequestHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;

@RestController
@RequestMapping("/objects")
public class Objects {

    @Resource(name = "objectRequestHandler")
    ObjectRequestHandler objectRequestHandler;

    @GetMapping(path = "/{object_id:.+}")
    public DrsObject getObjectById(@PathVariable Map<String, String> pathParams) {
        objectRequestHandler.setObjectId(pathParams.get("object_id"));
        return objectRequestHandler.handleRequest();
    }

    @GetMapping(path = "/{object_id:.+}/access/{access_id:.+}")
    public String getAccessMethodById(@PathVariable Map<String, String> pathParams) {
        return "Access endpoint. object_id: " + pathParams.get("object_id") + ", access_id: " + pathParams.get("access_id");
    }
}