package org.ga4gh.starterkit.drs.controller;

import com.fasterxml.jackson.annotation.JsonView;
import org.ga4gh.starterkit.common.exception.BadRequestException;
import org.ga4gh.starterkit.common.exception.ConflictException;
import org.ga4gh.starterkit.common.exception.ResourceNotFoundException;
import org.ga4gh.starterkit.common.hibernate.exception.EntityDoesntExistException;
import org.ga4gh.starterkit.common.hibernate.exception.EntityExistsException;
import org.ga4gh.starterkit.common.hibernate.exception.EntityMismatchException;
import org.ga4gh.starterkit.common.util.logging.LoggingUtil;
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

/**
 * Controller functions for the administrative API, create and modify DRS-related
 * entities
 */
@RestController
@RequestMapping(ADMIN_DRS_API_V1 + "/objects") // /admin/ga4gh/drs/v1
public class DrsAdmin {

    @Autowired
    private DrsHibernateUtil hibernateUtil;

    @Autowired
    private LoggingUtil loggingUtil;

    // Non-standard endpoints - admin views

    /**
     * Display DRSObject list
     * @return DRSObject list
     */
    @GetMapping
    @JsonView(SerializeView.Always.class)
    public List<DrsObject> indexDrsObjects() {
        loggingUtil.debug("Admin API request: DrsObject list");
        return hibernateUtil.getEntityList(DrsObject.class);
    }

    /**
     * Display all data for a single DRSObject
     * @param id identifier for DRSObject of interest
     * @return metadata for DRSObject
     */
    @GetMapping(path = "/{object_id:.+}")
    @JsonView(SerializeView.Admin.class)
    public DrsObject showDrsObject(
        @PathVariable(name = "object_id") String id
    ) {
        loggingUtil.debug("Admin API request: DrsObject with id '" + id + "'");
        DrsObject drsObject = hibernateUtil.loadDrsObject(id, false);
        if (drsObject == null) {
            throw new ResourceNotFoundException("No DrsObject found by id: " + id);
        }
        return getAdminFormattedDrsObject(id);
    }

    // Non-standard endpoints - write operations

    /**
     * Create a new DRSObject in the database
     * @param drsObject new, non persistent DRSObject
     * @return persistent DRSObject saved with the requested attributes
     */
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @JsonView(SerializeView.Admin.class)
    public DrsObject createDrsObject(
        @RequestBody DrsObject drsObject
    ) {
        loggingUtil.debug("Admin API request: create new DrsObject");
        loggingUtil.trace(drsObject.toString());
        try {
            hibernateUtil.createEntityObject(DrsObject.class, drsObject);
            return getAdminFormattedDrsObject(drsObject.getId());
        } catch (EntityExistsException ex) {
            throw new ConflictException(ex.getMessage());
        }
    }

    /**
     * Update an existing DRSObject with new properties
     * @param id identifier of DRSObject to be modified
     * @param drsObject new DRSObject properties
     * @return persistent DRSObject with overwritten attributes according to request body
     */
    @PutMapping(path = "/{object_id:.+}")
    public DrsObject updateDrsObject(
        @PathVariable(name = "object_id") String id,
        @RequestBody DrsObject drsObject
    ) {
        loggingUtil.debug("Admin API request: update DrsObject with id '" + id + "'");
        loggingUtil.trace(drsObject.toString());
        try {
            hibernateUtil.updateEntityObject(DrsObject.class, id, drsObject);
            return getAdminFormattedDrsObject(id);
        } catch (EntityMismatchException ex) {
            throw new BadRequestException(ex.getMessage());
        } catch (EntityDoesntExistException ex) {
            throw new ConflictException(ex.getMessage());
        }
    }

    /**
     * Delete an existing DRSObject
     * @param id identifier of DRSObject to be deleted
     * @return empty response body, indicating successful deletion
     */
    @DeleteMapping(path = "/{object_id:.+}")
    public DrsObject deleteDrsObject(
        @PathVariable(name = "object_id") String id
    ) {
        loggingUtil.debug("Admin API request: delete DrsObject with id '" + id + "'");
        try {
            hibernateUtil.deleteEntityObject(DrsObject.class, id);
            return hibernateUtil.readEntityObject(DrsObject.class, id, false);
        } catch (EntityDoesntExistException ex) {
            throw new ConflictException(ex.getMessage());
        } catch (EntityExistsException ex) {
            throw new ConflictException(ex.getMessage());
        }
    }

    /**
     * High-level function to load a DRSObject with all necessary properties
     * to get an administrative view, while avoiding infinite recursive 
     * deserialization problem
     * @param id identifier of DRSObject to be loaded
     * @return administrative view of DRSObject
     */
    private DrsObject getAdminFormattedDrsObject(String id) {
        DrsObject drsObject = hibernateUtil.loadDrsObject(id, false);
        breakInterminableFetchForChildrenAndParents(drsObject);
        return drsObject;
    }

    /**
     * For a given DRSObject, avoid infinite recursive serialization by setting
     * certain attributes of its parents and children to null
     * @param drsObject DRSObject to be loaded
     */
    private void breakInterminableFetchForChildrenAndParents(DrsObject drsObject) {
        for (DrsObject childDrsObject: drsObject.getDrsObjectChildren()) {
            // each of the DRSObject's children have certain attributes set to
            // null to avoid infinite serialization
            breakInterminableFetch(childDrsObject);
        }
        for (DrsObject parentDrsObject: drsObject.getDrsObjectParents()) {
            // each of the DRSObject's parents have certain attributes set to
            // null to avoid infinite serialization
            breakInterminableFetch(parentDrsObject);
        }
    }

    /**
     * Assign certain attributes to null to avoid infinite recursive serialization.
     * Only for administrative views of an object, does not affect the persistent
     * state of an object
     * @param drsObject DRSObject to be modified
     */
    private void breakInterminableFetch(DrsObject drsObject) {
        drsObject.setAliases(null);
        drsObject.setChecksums(null);
        drsObject.setFileAccessObjects(null);
        drsObject.setAwsS3AccessObjects(null);
        drsObject.setDrsObjectChildren(null);
        drsObject.setDrsObjectParents(null);
        drsObject.setPassportVisas(null);
    }
}
