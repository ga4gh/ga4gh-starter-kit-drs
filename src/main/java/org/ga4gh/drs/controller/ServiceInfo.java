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
    DrsConfigContainer defaultDrsConfigContainer;

    @GetMapping()
    public String getServiceInfo() {
        return defaultDrsConfigContainer.getDrsConfig().getServiceInfo().toString();
    }
}