package org.ga4gh.starterkit.drs.app;

import org.ga4gh.starterkit.common.config.DatabaseProps;
import org.ga4gh.starterkit.common.config.ServerProps;
import org.ga4gh.starterkit.drs.model.DrsServiceInfo;

public class DrsStandaloneYamlConfig {

    private ServerProps serverProps;
    private DatabaseProps databaseProps;
    private DrsServiceInfo serviceInfo;

    public DrsStandaloneYamlConfig() {
        serverProps = new ServerProps();
        databaseProps = new DatabaseProps();
        serviceInfo = new DrsServiceInfo();
    }

    public void setServerProps(ServerProps serverProps) {
        this.serverProps = serverProps;
    }

    public ServerProps getServerProps() {
        return serverProps;
    }

    public void setServiceInfo(DrsServiceInfo serviceInfo) {
        this.serviceInfo = serviceInfo;
    }

    public DrsServiceInfo getServiceInfo() {
        return serviceInfo;
    }

    public void setDatabaseProps(DatabaseProps databaseProps) {
        this.databaseProps = databaseProps;
    }

    public DatabaseProps getDatabaseProps() {
        return databaseProps;
    }
    
}
