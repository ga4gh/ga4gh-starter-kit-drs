package org.ga4gh.starterkit.drs.model;

import java.io.Serializable;

public class AwsS3AccessObjectId implements Serializable {

    public static final long serialVersionUID = 1L;

    private DrsObject drsObject;

    private String bucket;

    private String key;

    public AwsS3AccessObjectId() {
        
    }

    public AwsS3AccessObjectId(DrsObject drsObject, String bucket, String key) {
        this.drsObject = drsObject;
        this.bucket = bucket;
        this.key = key;
    }

    public void setDrsObject(DrsObject drsObject) {
        this.drsObject = drsObject;
    }

    public DrsObject getDrsObject() {
        return drsObject;
    }

    public void setBucket(String bucket) {
        this.bucket = bucket;
    }

    public String getBucket() {
        return bucket;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getKey() {
        return key;
    }
}
