package org.ga4gh.starterkit.drs.app;

import org.ga4gh.starterkit.common.config.DatabaseProps;
import org.ga4gh.starterkit.common.config.ServerProps;
import org.ga4gh.starterkit.drs.config.DrsServiceProps;
import org.ga4gh.starterkit.drs.model.DrsServiceInfo;

/**
 * Contains multiple configuration objects affecting application behavior.
 * To be deserialized/loaded as part of a YAML config file specified on the 
 * command line
 */
public class DrsStandaloneYamlConfig {

    private ServerProps serverProps;
    private DatabaseProps databaseProps;
    private DrsServiceInfo serviceInfo;
    private DrsServiceProps drsServiceProps;

    /**
     * Instantiates a new DrsStandaloneYamlConfig object with default properties
     */
    public DrsStandaloneYamlConfig() {
        serverProps = new ServerProps();
        databaseProps = new DatabaseProps();
        serviceInfo = new DrsServiceInfo();
        drsServiceProps = new DrsServiceProps();
    }

    /**
     * Assign serverProps
     * @param serverProps ServerProps object
     */
    public void setServerProps(ServerProps serverProps) {
        this.serverProps = serverProps;
    }

    /**
     * Retrieve server props
     * @return ServerProps object
     */
    public ServerProps getServerProps() {
        return serverProps;
    }

    /**
     * Assign databaseProps
     * @param databaseProps DatabaseProps object
     */
    public void setDatabaseProps(DatabaseProps databaseProps) {
        this.databaseProps = databaseProps;
    }

    /**
     * Retrieve databaseProps
     * @return DatabaseProps object
     */
    public DatabaseProps getDatabaseProps() {
        return databaseProps;
    }

    /**
     * Assign serviceInfo
     * @param serviceInfo DrsServiceInfo object
     */
    public void setServiceInfo(DrsServiceInfo serviceInfo) {
        this.serviceInfo = serviceInfo;
    }

    /**
     * Retrieve serviceInfo
     * @return DrsServiceInfo object
     */
    public DrsServiceInfo getServiceInfo() {
        return serviceInfo;
    }

    /**
     * Assign drsServiceProps
     * @param drsServiceProps DrsServiceProps object
     */
    public void setDrsServiceProps(DrsServiceProps drsServiceProps) {
        this.drsServiceProps = drsServiceProps;
    }

    /**
     * Retrieve drsServiceProps
     * @return DrsServiceProps object
     */
    public DrsServiceProps getDrsServiceProps() {
        return drsServiceProps;
    }
}
