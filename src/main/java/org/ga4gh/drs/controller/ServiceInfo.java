package org.ga4gh.drs.controller;

import org.ga4gh.drs.AppConfigConstants;
import org.ga4gh.drs.configuration.DrsConfigContainer;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

@RestController
@RequestMapping("/service-info")
public class ServiceInfo {

    @Autowired
    @Qualifier(AppConfigConstants.FINAL_DRS_CONFIG_CONTAINER)
    DrsConfigContainer drsConfigContainer;

    @GetMapping(produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<org.ga4gh.starterkit.common.model.ServiceInfo> getServiceInfo() {
        return new ResponseEntity<>(drsConfigContainer.getDrsConfig().getServiceInfo(), HttpStatus.OK);
    }
}
