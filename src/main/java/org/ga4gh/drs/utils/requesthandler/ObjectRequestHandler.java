package org.ga4gh.drs.utils.requesthandler;

import org.ga4gh.drs.exception.ResourceNotFoundException;
import org.ga4gh.drs.model.DrsObject;
import org.ga4gh.drs.utils.DataSourceLookup;
import org.springframework.beans.factory.annotation.Autowired;

public class ObjectRequestHandler implements RequestHandler<DrsObject> {

    private String objectId;

    @Autowired
    DataSourceLookup dataSourceLookup;

    public ObjectRequestHandler() {
        
    }

    public ObjectRequestHandler(String objectId) {
        this.objectId = objectId;
    }

    public DrsObject handleRequest() {
        System.out.println("Inside the handle request function");
        System.out.println(getObjectId());
        System.out.println(dataSourceLookup);

        // System.out.println(dataSourceLookup.dataSources);

        int matchingPattern = dataSourceLookup.findMatch(objectId);
        if (matchingPattern == -1) {
            throw new ResourceNotFoundException("Could not locate data source associated with requested object_id");
        }

        return null;
    }

    public void setObjectId(String objectId) {
        this.objectId = objectId;
    }

    public String getObjectId() {
        return objectId;
    }
    
}
