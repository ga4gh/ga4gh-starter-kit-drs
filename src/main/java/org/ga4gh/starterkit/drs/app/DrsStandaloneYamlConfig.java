package org.ga4gh.starterkit.drs.app;

import org.ga4gh.starterkit.common.hibernate.HibernateProps;
import org.ga4gh.starterkit.drs.configuration.ServerProps;
import org.ga4gh.starterkit.drs.model.DrsServiceInfo;

public class DrsStandaloneYamlConfig {

    private ServerProps serverProps;
    private DrsServiceInfo serviceInfo;
    private HibernateProps hibernateProps;

    public DrsStandaloneYamlConfig() {
        serverProps = new ServerProps();
        serviceInfo = new DrsServiceInfo();
        hibernateProps = new HibernateProps();
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

    public void setHibernateProps(HibernateProps hibernateProps) {
        this.hibernateProps = hibernateProps;
    }

    public HibernateProps getHibernateProps() {
        return hibernateProps;
    }
    
}
