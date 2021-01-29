package org.ga4gh.drs.utils.requesthandler;

import org.ga4gh.drs.exception.ResourceNotFoundException;
import org.ga4gh.drs.model.AccessMethod;
import org.ga4gh.drs.model.AccessType;
import org.ga4gh.drs.model.DrsObject;
import org.ga4gh.drs.utils.DataSourceLookup;
import org.ga4gh.drs.utils.cache.AccessCache;
import org.ga4gh.drs.utils.cache.AccessCacheItem;
import org.ga4gh.drs.utils.objectloader.DrsObjectLoader;
import org.springframework.beans.factory.annotation.Autowired;

public class ObjectRequestHandler implements RequestHandler<DrsObject> {

    @Autowired
    DataSourceLookup dataSourceLookup;

    @Autowired
    AccessCache accessCache;

    private String objectId;

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
        DrsObject drsObject = drsObjectLoader.generateDrsObject();

        // register an access id in the cache for each returned AccessMethod with
        // an access id string
        for (AccessMethod accessMethod : drsObject.getAccessMethods()) {
            if (accessMethod.getAccessId() != null) {
                AccessCacheItem item = generateAccessCacheItem(
                    drsObject.getId(),
                    accessMethod.getAccessId(),
                    drsObjectLoader.getObjectPath(),
                    accessMethod.getType(),
                    drsObject.getMimeType()
                );
                accessCache.put(drsObject.getId() + ":" + accessMethod.getAccessId(), item);
            }
        }
        
        return drsObject;
    }

    private AccessCacheItem generateAccessCacheItem(String objectId, String accessId, String objectPath, AccessType accessType, String mimeType) {
        AccessCacheItem item = new AccessCacheItem();
        item.setObjectId(objectId);
        item.setAccessId(accessId);
        item.setObjectPath(objectPath);
        item.setAccessType(accessType);
        item.setMimeType(mimeType);
        return item;
    }

    public void setObjectId(String objectId) {
        this.objectId = objectId;
    }

    public String getObjectId() {
        return objectId;
    }
}
