package org.ga4gh.drs.controller;

import org.ga4gh.drs.AppConfigConstants;
import org.ga4gh.drs.configuration.DrsConfigContainer;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

@RestController
@RequestMapping("/service-info")
public class ServiceInfo {

    @Autowired
    @Qualifier(AppConfigConstants.DEFAULT_DRS_CONFIG_CONTAINER)
    DrsConfigContainer drsConfigContainer;

    @GetMapping()
    public String getServiceInfo() {
        // TODO: method is currently a stub to test merging of Service Info
        // properties from config file. Update to real method
        org.ga4gh.drs.model.ServiceInfo serviceInfo = drsConfigContainer.getDrsConfig().getServiceInfo();
        String result = serviceInfo.toString()
            + " createdAt: " + serviceInfo.getCreatedAt().toString()
            + " updatedAt: " + serviceInfo.getUpdatedAt().toString()
            + " organizationName: " + serviceInfo.getOrganization().getName()
            + " organizationUrl: " + serviceInfo.getOrganization().getUrl();
        return result;
    }
}
