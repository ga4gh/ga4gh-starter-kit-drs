package org.ga4gh.starterkit.drs.controller;

import com.fasterxml.jackson.annotation.JsonView;
import org.ga4gh.starterkit.common.exception.BadRequestException;
import org.ga4gh.starterkit.common.exception.ConflictException;
import org.ga4gh.starterkit.common.exception.ResourceNotFoundException;
import org.ga4gh.starterkit.common.hibernate.exception.EntityDoesntExistException;
import org.ga4gh.starterkit.common.hibernate.exception.EntityExistsException;
import org.ga4gh.starterkit.common.hibernate.exception.EntityMismatchException;
import static org.ga4gh.starterkit.drs.constant.DrsApiConstants.ADMIN_DRS_API_V1;
import java.util.List;
import org.ga4gh.starterkit.drs.model.DrsObject;
import org.ga4gh.starterkit.drs.utils.SerializeView;
import org.ga4gh.starterkit.drs.utils.hibernate.DrsHibernateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(ADMIN_DRS_API_V1 + "/objects")
public class DrsAdmin {

    @Autowired
    DrsHibernateUtil hibernateUtil;

    // Non-standard endpoints - admin views

    @GetMapping
    @JsonView(SerializeView.Always.class)
    public List<DrsObject> indexDrsObjects() {
        return hibernateUtil.getEntityList(DrsObject.class);
    }

    @GetMapping(path = "/{object_id:.+}")
    @JsonView(SerializeView.Admin.class)
    public DrsObject showDrsObject(
        @PathVariable(name = "object_id") String id
    ) {
        DrsObject drsObject = hibernateUtil.loadDrsObject(id, false);
        if (drsObject == null) {
            throw new ResourceNotFoundException("No DrsObject found by id: " + id);
        }
        return getAdminFormattedDrsObject(id);
    }

    // Non-standard endpoints - write operations

    @PostMapping(consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public DrsObject createDrsObject(
        @RequestBody DrsObject drsObject
    ) {
        try {
            setBidirectionalRelationships(drsObject);
            hibernateUtil.createEntityObject(DrsObject.class, drsObject);
            return getAdminFormattedDrsObject(drsObject.getId());
        } catch (EntityExistsException ex) {
            throw new ConflictException(ex.getMessage());
        }
    }

    @PutMapping(path = "/{object_id:.+}")
    public DrsObject updateDrsObject(
        @PathVariable(name = "object_id") String id,
        @RequestBody DrsObject drsObject
    ) {
        try {
            setBidirectionalRelationships(drsObject);
            hibernateUtil.updateEntityObject(DrsObject.class, id, drsObject);
            return getAdminFormattedDrsObject(id);
        } catch (EntityMismatchException ex) {
            throw new BadRequestException(ex.getMessage());
        } catch (EntityDoesntExistException ex) {
            throw new ConflictException(ex.getMessage());
        }
    }

    @DeleteMapping(path = "/{object_id:.+}")
    public DrsObject deleteDrsObject(
        @PathVariable(name = "object_id") String id
    ) {
        try {
            hibernateUtil.deleteEntityObject(DrsObject.class, id);
            return hibernateUtil.readEntityObject(DrsObject.class, id, false);
        } catch (EntityDoesntExistException ex) {
            throw new ConflictException(ex.getMessage());
        } catch (EntityExistsException ex) {
            throw new ConflictException(ex.getMessage());
        }
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

    private void setBidirectionalRelationships(DrsObject drsObject) {
        if (drsObject.getChecksums() != null) {
            drsObject.getChecksums().forEach(checksum -> checksum.setDrsObject(drsObject));
        }
        if (drsObject.getFileAccessObjects() != null) {
            drsObject.getFileAccessObjects().forEach(fileAccessObject -> fileAccessObject.setDrsObject(drsObject));
        }
        if (drsObject.getAwsS3AccessObjects() != null) {
            drsObject.getAwsS3AccessObjects().forEach(awsS3AccessObject -> awsS3AccessObject.setDrsObject(drsObject));
        }
    }
}
