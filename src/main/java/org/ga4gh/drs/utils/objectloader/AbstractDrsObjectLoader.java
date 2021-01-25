package org.ga4gh.drs.utils.objectloader;

public abstract class AbstractDrsObjectLoader implements DrsObjectLoader {

    private String objectPath;

    public void setObjectPath(String objectPath) {
        this.objectPath = objectPath;
    }

    public String getObjectPath() {
        return objectPath;
    }
}
