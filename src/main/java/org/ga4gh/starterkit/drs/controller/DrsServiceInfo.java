package org.ga4gh.starterkit.drs.controller;

import static org.ga4gh.starterkit.drs.constant.DrsApiConstants.DRS_API_V1;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.beans.factory.annotation.Autowired;

@RestController
@RequestMapping(DRS_API_V1 + "/service-info")
public class DrsServiceInfo {

    @Autowired
    org.ga4gh.starterkit.drs.model.DrsServiceInfo drsServiceInfo;

    @GetMapping(produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public org.ga4gh.starterkit.drs.model.DrsServiceInfo getServiceInfo() {
        return drsServiceInfo;
    }
}