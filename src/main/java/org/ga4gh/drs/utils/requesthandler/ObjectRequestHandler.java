package org.ga4gh.drs.utils.requesthandler;

import org.ga4gh.drs.exception.ResourceNotFoundException;
import org.ga4gh.drs.model.DrsObject;
import org.ga4gh.drs.utils.DataSourceLookup;
import org.ga4gh.drs.utils.objectloader.DrsObjectLoader;
import org.springframework.beans.factory.annotation.Autowired;

public class ObjectRequestHandler implements RequestHandler<DrsObject> {

    private String objectId;

    @Autowired
    DataSourceLookup dataSourceLookup;

    public ObjectRequestHandler() {
        
    }

    public DrsObject handleRequest() {
        DrsObjectLoader drsObjectLoader = dataSourceLookup.getDrsObjectLoaderFromId(getObjectId());
        if (drsObjectLoader == null) {
            throw new ResourceNotFoundException("Could not locate data source associated with requested object_id");
        }
        if (!drsObjectLoader.exists()) {
            throw new ResourceNotFoundException("No object found for the provided id");
        }
        return drsObjectLoader.generateDrsObject();
    }

    public void setObjectId(String objectId) {
        this.objectId = objectId;
    }

    public String getObjectId() {
        return objectId;
    }
}
