package org.ga4gh.starterkit.drs.model;

import java.io.Serializable;

public class ChecksumId implements Serializable {

    public static final long serialVersionUID = 1L;

    private DrsObject drsObject;

    private String type;

    public ChecksumId() {

    }

    public ChecksumId(DrsObject drsObject, String type) {
        this.drsObject = drsObject;
        this.type = type;
    }

    public void setDrsObject(DrsObject drsObject) {
        this.drsObject = drsObject;
    }

    public DrsObject getDrsObject() {
        return drsObject;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }
}
