package org.ga4gh.drs.utils;

import org.ga4gh.drs.AppConfigConstants;
import org.ga4gh.drs.configuration.DrsConfigContainer;
import org.ga4gh.drs.utils.datasource.LocalFileDataSource;
import org.ga4gh.drs.utils.datasource.S3DataSource;
import org.ga4gh.drs.utils.objectloader.DrsObjectLoader;
import org.ga4gh.drs.utils.objectloader.DrsObjectLoaderFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import javax.annotation.PostConstruct;
import java.util.List;

public class DataSourceLookup {

    @Autowired
    @Qualifier(AppConfigConstants.MERGED_DRS_CONFIG_CONTAINER)
    DrsConfigContainer drsConfigContainer;

    @Autowired
    DrsObjectLoaderFactory drsObjectLoaderFactory;

    List<LocalFileDataSource> localDataSources;
    List<S3DataSource> s3DataSources;

    public DataSourceLookup() {

    }

    @PostConstruct
    private void postConstruct() {
        localDataSources = drsConfigContainer.getDrsConfig().getDataSourceRegistry().getLocal();
        s3DataSources = drsConfigContainer.getDrsConfig().getDataSourceRegistry().getS3();
    }

    public DrsObjectLoader getDrsObjectLoaderFromId(String objectId) {
        for (LocalFileDataSource localDataSource : localDataSources) {
            if (localDataSource.objectIdMatches(objectId)) {
                return drsObjectLoaderFactory.createFileDrsObjectLoader(localDataSource, objectId);
            }
        }

        for (S3DataSource s3DataSource : s3DataSources) {
            if (s3DataSource.objectIdMatches(objectId)) {
                return drsObjectLoaderFactory.createS3DrsObjectLoader(s3DataSource, objectId);
            }
        }
        return null;
    }
}
