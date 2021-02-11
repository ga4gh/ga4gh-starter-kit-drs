package org.ga4gh.drs.utils.datasource;

import org.ga4gh.drs.utils.objectloader.S3DrsObjectLoader;

public class S3DataSource implements DataSource<S3DrsObjectLoader> {

    private String idPrefix;
    private String region;
    private String bucket;
    private String rootDir;

    public void setIdPrefix(String idPrefix) {
        this.idPrefix = idPrefix;
    }

    public String getIdPrefix() {
        return idPrefix;
    }

    public boolean objectIdMatches(String objectId) {
        return objectId.startsWith(getIdPrefix());
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getRegion() {
        return region;
    }

    public void setBucket(String bucket) {
        this.bucket = bucket;
    }

    public String getBucket() {
        return bucket;
    }

    public void setRootDir(String rootDir) {
        this.rootDir = rootDir;
    }

    public String getRootDir() {
        return rootDir;
    }
}
