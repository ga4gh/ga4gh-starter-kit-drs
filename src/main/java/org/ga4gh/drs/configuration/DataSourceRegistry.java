package org.ga4gh.drs.configuration;

import java.util.List;

import org.ga4gh.drs.utils.datasource.LocalFileDataSource;
import org.ga4gh.drs.utils.datasource.S3DataSource;

public class DataSourceRegistry {

    private List<LocalFileDataSource> local;
    private List<S3DataSource> s3;

    public DataSourceRegistry() {

    }

    public void setLocal(List<LocalFileDataSource> local) {
        this.local = local;
    }

    public List<LocalFileDataSource> getLocal() {
        return local;
    }

    public void setS3(List<S3DataSource> s3) {
        this.s3 = s3;
    }

    public List<S3DataSource> getS3() {
        return s3;
    }
}
