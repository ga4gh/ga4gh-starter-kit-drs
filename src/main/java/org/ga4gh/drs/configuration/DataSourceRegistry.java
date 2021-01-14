package org.ga4gh.drs.configuration;

import java.util.List;

public class DataSourceRegistry {

    private List<DataSource> dataSources;

    public DataSourceRegistry() {

    }

    public DataSourceRegistry(List<DataSource> dataSources) {
        this.dataSources = dataSources;
    }

    public void setDataSources(List<DataSource> dataSources) {
        this.dataSources = dataSources;
    }

    public List<DataSource> getDataSources() {
        return dataSources;
    }
}