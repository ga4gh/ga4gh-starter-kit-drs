package org.ga4gh.drs.utils.objectloader;

import org.ga4gh.drs.exception.ResourceNotFoundException;
import org.ga4gh.drs.model.AccessType;

public class DrsObjectLoaderFactory {

    public DrsObjectLoaderFactory() {

    }

    public DrsObjectLoader createDrsObjectLoader(AccessType accessType) {
        switch (accessType) {
            case FILE:
                return new FileDrsObjectLoader();
            case HTTPS:
                return new HttpsDrsObjectLoader();
            default:
                throw new ResourceNotFoundException();
        }
    }
}
