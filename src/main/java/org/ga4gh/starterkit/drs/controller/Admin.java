package org.ga4gh.starterkit.drs.controller;

import com.fasterxml.jackson.annotation.JsonView;
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

    // Non-standard endpoints - write operations

    @GetMapping(path = "/{object_id:.+}")
    @JsonView(SerializeView.Admin.class)
    public DrsObject showDrsObject(
        @PathVariable(name = "object_id") String id
    ) {
        return getAdminFormattedDrsObject(id);
    }

    @PostMapping
    public DrsObject createDrsObject(
        @RequestBody DrsObject drsObject
    ) {
        hibernateUtil.createEntityObject(DrsObject.class, drsObject);
        return getAdminFormattedDrsObject(drsObject.getId());
    }

    @PutMapping(path = "/{object_id:.+}")
    public DrsObject updateDrsObject(
        @PathVariable(name = "object_id") String oldId,
        @RequestBody DrsObject drsObject
    ) {
        String newId = drsObject.getId();
        hibernateUtil.updateEntityObject(DrsObject.class, oldId, newId, drsObject);
        return getAdminFormattedDrsObject(newId);
    }

    @DeleteMapping(path = "/{object_id:.+}")
    public DrsObject deleteDrsObject(
        @PathVariable(name = "object_id") String id
    ) {
        hibernateUtil.deleteEntityObject(DrsObject.class, id);
        return hibernateUtil.readEntityObject(DrsObject.class, id, false);
    }

    private DrsObject getAdminFormattedDrsObject(String id) {
        DrsObject drsObject = hibernateUtil.loadDrsObject(id, false);
        breakInterminableFetchForChildrenAndParents(drsObject);
        return drsObject;
    }

    private void breakInterminableFetchForChildrenAndParents(DrsObject drsObject) {
        for (DrsObject childDrsObject: drsObject.getDrsObjectChildren()) {
            breakInterminableFetch(childDrsObject);
        }
        for (DrsObject parentDrsObject: drsObject.getDrsObjectParents()) {
            breakInterminableFetch(parentDrsObject);
        }
    }

    private void breakInterminableFetch(DrsObject drsObject) {
        drsObject.setAliases(null);
        drsObject.setChecksums(null);
        drsObject.setFileAccessObjects(null);
        drsObject.setAwsS3AccessObjects(null);
        drsObject.setDrsObjectChildren(null);
        drsObject.setDrsObjectParents(null);
    }
}
