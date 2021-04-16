package org.ga4gh.starterkit.drs.model;

import java.io.Serializable;

public class FileAccessObjectId implements Serializable {

    public static final long serialVersionUID = 1L;

    private DrsObject drsObject;

    private String path;

    public FileAccessObjectId() {
        
    }

    public FileAccessObjectId(DrsObject drsObject, String path) {
        this.drsObject = drsObject;
        this.path = path;
    }

    public void setDrsObject(DrsObject drsObject) {
        this.drsObject = drsObject;
    }

    public DrsObject getDrsObject() {
        return drsObject;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getPath() {
        return path;
    }
}
