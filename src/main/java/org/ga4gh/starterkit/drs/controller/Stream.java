package org.ga4gh.starterkit.drs.controller;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

import org.ga4gh.starterkit.drs.utils.requesthandler.FileStreamRequestHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/stream")
public class Stream {

    @Resource(name = "fileStreamRequestHandler")
    FileStreamRequestHandler fileStreamRequestHandler;

    @GetMapping(path = "/{object_id:.+}/{access_id:.+}")
    public void streamFile(
        @PathVariable(name = "object_id") String objectId,
        @PathVariable(name = "access_id") String accessId,
        HttpServletResponse response
    ) {
        fileStreamRequestHandler.setObjectId(objectId);
        fileStreamRequestHandler.setAccessId(accessId);
        fileStreamRequestHandler.setResponse(response);
        fileStreamRequestHandler.handleRequest();
    }
}
