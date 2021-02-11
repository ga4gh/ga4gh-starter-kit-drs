package org.ga4gh.drs.utils.datasource;

import org.ga4gh.drs.utils.objectloader.S3DrsObjectLoader;

public class S3DataSource implements DataSource<S3DrsObjectLoader> {

    private String idPrefix;
    private String region;
    private String bucket;
    private String rootDir;

    public S3DataSource() {

    }

    public S3DataSource(String idPrefix, String region, String bucket, String rootDir) {
        this.idPrefix = idPrefix;
        this.region = region;
        this.bucket = bucket;
        this.rootDir = rootDir;
    }

    public String renderObjectKey(String objectId) {
        String postPrefix = objectId.replaceFirst(getIdPrefix(), "");
        return getRootDir() + postPrefix;
    }


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
