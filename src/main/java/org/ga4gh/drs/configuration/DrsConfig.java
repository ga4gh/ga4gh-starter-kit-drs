package org.ga4gh.drs.configuration;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.ga4gh.drs.model.ServiceInfo;

public class DrsConfig {

    private ServerProps serverProps;
    private ServiceInfo serviceInfo;
    private DataSourceRegistry dataSourceRegistry;

    @JsonProperty("S3")
    private S3 s3;

    public DrsConfig() {
        serverProps = new ServerProps();
        serviceInfo = new ServiceInfo();
        dataSourceRegistry = new DataSourceRegistry();
        s3 = new S3();
    }

    public void setServerProps(ServerProps serverProps) {
        this.serverProps = serverProps;
    }

    public ServerProps getServerProps() {
        return serverProps;
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

    public S3 getS3() {
        return s3;
    }

    public void setS3(S3 S3) {
        this.s3 = S3;
    }
}
