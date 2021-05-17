package org.ga4gh.starterkit.drs.app;

import org.ga4gh.starterkit.common.config.DatabaseProps;
import org.ga4gh.starterkit.common.config.ServerProps;
import org.ga4gh.starterkit.drs.config.DrsServiceProps;
import org.ga4gh.starterkit.drs.model.DrsServiceInfo;

public class DrsStandaloneYamlConfig {

    private ServerProps serverProps;
    private DatabaseProps databaseProps;
    private DrsServiceInfo serviceInfo;
    private DrsServiceProps drsServiceProps;

    public DrsStandaloneYamlConfig() {
        serverProps = new ServerProps();
        databaseProps = new DatabaseProps();
        serviceInfo = new DrsServiceInfo();
        drsServiceProps = new DrsServiceProps();
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

    public void setDrsServiceProps(DrsServiceProps drsServiceProps) {
        this.drsServiceProps = drsServiceProps;
    }

    public DrsServiceProps getDrsServiceProps() {
        return drsServiceProps;
    }
    
}
