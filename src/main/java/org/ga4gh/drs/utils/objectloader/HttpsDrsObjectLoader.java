package org.ga4gh.drs.utils.objectloader;

import org.ga4gh.drs.model.DrsObject;

public class HttpsDrsObjectLoader extends AbstractDrsObjectLoader {

    //TODO populate this class

    public HttpsDrsObjectLoader(String objectPath) {
        super(objectPath);
    }

    public boolean exists() {
        return false;
    }

    public boolean isBundle() {
        return false;
    }

    public DrsObject getImputedDrsObject() {
        return null;
    }

    public DrsObject getDefiniteDrsObject() {
        return null;
    }
    
}
