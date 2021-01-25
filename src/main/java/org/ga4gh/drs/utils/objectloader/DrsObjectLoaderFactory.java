package org.ga4gh.drs.utils.objectloader;

import org.ga4gh.drs.exception.ResourceNotFoundException;
import org.ga4gh.drs.model.AccessType;

public class DrsObjectLoaderFactory {

    public DrsObjectLoaderFactory() {

    }

    public AbstractDrsObjectLoader createDrsObjectLoader(AccessType accessType, String objectId, String objectPath) {
        if (accessType == null) return null;
        switch (accessType) {
            case FILE:
                return new FileDrsObjectLoader(objectId, objectPath);
            case HTTPS:
                return new HttpsDrsObjectLoader(objectId, objectPath);
            default:
                throw new ResourceNotFoundException();
        }
    }
}
