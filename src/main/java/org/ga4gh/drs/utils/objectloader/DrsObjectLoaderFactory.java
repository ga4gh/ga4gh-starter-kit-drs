package org.ga4gh.drs.utils.objectloader;

import org.ga4gh.drs.utils.datasource.LocalFileDataSource;
import org.ga4gh.drs.utils.datasource.S3DataSource;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

public class DrsObjectLoaderFactory implements ApplicationContextAware {

    private ApplicationContext context;

    public DrsObjectLoaderFactory() {

    }

    public FileDrsObjectLoader createFileDrsObjectLoader(LocalFileDataSource dataSource, String objectId) {
        return context.getBean(FileDrsObjectLoader.class, objectId, dataSource.renderObjectPath(objectId));
    }

    public S3DrsObjectLoader createS3DrsObjectLoader(S3DataSource dataSource, String objectId) {
        return null;
    }

    public void setApplicationContext(ApplicationContext context) {
        this.context = context;
    }
}
