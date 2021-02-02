package org.ga4gh.drs.utils.objectloader;

import org.ga4gh.drs.exception.ResourceNotFoundException;
import org.ga4gh.drs.model.AccessType;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

public class DrsObjectLoaderFactory implements ApplicationContextAware {

    private ApplicationContext context;

    public DrsObjectLoaderFactory() {

    }

    public AbstractDrsObjectLoader createDrsObjectLoader(AccessType accessType, String objectId, String objectPath) {
        if (accessType == null) return null;
        switch (accessType) {
            case FILE:
                return context.getBean(FileDrsObjectLoader.class, objectId, objectPath);
            case HTTPS:
                return context.getBean(HttpsDrsObjectLoader.class, objectId, objectPath);
            default:
                throw new ResourceNotFoundException();
        }
    }

    public void setApplicationContext(ApplicationContext context) {
        this.context = context;
    }
}
