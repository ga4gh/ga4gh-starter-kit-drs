package org.ga4gh.drs.configuration;

import org.ga4gh.drs.model.ServiceInfo;

public class DrsConfig {

    private ServiceInfo serviceInfo;
    private DataSourceRegistry dataSourceRegistry;

    public DrsConfig() {

    }

    public DrsConfig(ServiceInfo serviceInfo, DataSourceRegistry dataSourceRegistry) {
        this.serviceInfo = serviceInfo;
        this.dataSourceRegistry = dataSourceRegistry;
    }

    public void setServiceInfo(ServiceInfo serviceInfo) {
        this.serviceInfo = serviceInfo;
    }

    public ServiceInfo getServiceInfo() {
        return serviceInfo;
    }

    public void setDataSourceRegistry(DataSourceRegistry dataSourceRegistry) {
        this.dataSourceRegistry = dataSourceRegistry;
    }

    public DataSourceRegistry getDataSourceRegistry() {
        return dataSourceRegistry;
    }
}