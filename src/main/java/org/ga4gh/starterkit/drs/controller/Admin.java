package org.ga4gh.starterkit.drs.controller;

import javax.annotation.Resource;

import com.fasterxml.jackson.annotation.JsonView;

import org.ga4gh.starterkit.common.hibernate.HibernateUtil;
import org.ga4gh.starterkit.common.requesthandler.BasicCreateRequestHandler;
import org.ga4gh.starterkit.common.requesthandler.BasicDeleteRequestHandler;
import org.ga4gh.starterkit.common.requesthandler.BasicShowRequestHandler;
import org.ga4gh.starterkit.common.requesthandler.BasicUpdateRequestHandler;
import org.ga4gh.starterkit.drs.constant.DrsServerConstants;
import org.ga4gh.starterkit.drs.model.DrsObject;
import org.ga4gh.starterkit.drs.utils.SerializeView;
import org.ga4gh.starterkit.drs.utils.hibernate.DrsHibernateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin" + DrsServerConstants.DRS_API_PREFIX + "/objects")
public class Admin {

    @Autowired
    DrsHibernateUtil hibernateUtil;

    @Resource(name = "showObjectRequestHandler")
    BasicShowRequestHandler<String, DrsObject> showObjectRequestHandler;

    @Resource(name = "createObjectRequestHandler")
    BasicCreateRequestHandler<String, DrsObject> createObjectRequestHandler;

    @Resource(name = "updateObjectRequestHandler")
    BasicUpdateRequestHandler<String, DrsObject> updateObjectRequestHandler;

    @Resource(name = "deleteObjectRequestHandler")
    BasicDeleteRequestHandler<String, DrsObject> deleteObjectRequestHandler;

    // Non-standard endpoints - write operations

    @GetMapping(path = "/{object_id:.+}")
    @JsonView(SerializeView.Admin.class)
    public DrsObject showDrsObject(
        @PathVariable(name = "object_id") String id
    ) {
        return hibernateUtil.loadDrsObject(id, true, true);
    }

    @PostMapping
    public DrsObject createDrsObject(
        @RequestBody DrsObject drsObject
    ) {
        return createObjectRequestHandler.prepare(drsObject).handleRequest();
    }

    @PutMapping(path = "/{object_id:.+}")
    public DrsObject updateDrsObject(
        @PathVariable(name = "object_id") String id,
        @RequestBody DrsObject drsObject
    ) {
        return updateObjectRequestHandler.prepare(id, drsObject).handleRequest();
    }

    @DeleteMapping(path = "/{object_id:.+}")
    public DrsObject deleteDrsObject(
        @PathVariable(name = "object_id") String id
    ) {
        return deleteObjectRequestHandler.prepare(id).handleRequest();
    }
}
