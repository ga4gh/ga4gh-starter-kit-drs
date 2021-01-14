package org.ga4gh.drs.controller;

import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;

@RestController
@RequestMapping("/objects")
public class Objects {

    @GetMapping(path = "/{object_id:.+}")
    public String getObjectById(@PathVariable Map<String, String> pathParams) {
        return "Object endpoint. object_id: " + pathParams.get("object_id");
    }

    @GetMapping(path = "/{object_id:.+}/access/{access_id:.+}")
    public String getAccessMethodById(@PathVariable Map<String, String> pathParams) {
        return "Access endpoint. object_id: " + pathParams.get("object_id") + ", access_id: " + pathParams.get("access_id");
    }
}