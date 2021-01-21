package org.ga4gh.drs.utils.objectloader;

import org.ga4gh.drs.model.DrsObject;

public interface DrsObjectLoader {

    public boolean exists();
    public DrsObject getImputedProperties();
    public DrsObject getExplicitProperties();
}
