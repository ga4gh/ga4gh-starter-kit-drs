package org.ga4gh.starterkit.drs.controller;

import static org.ga4gh.starterkit.drs.constant.DrsApiConstants.DRS_API_V1;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Service info controller, displays generic and DRS-specific service info
 */
@RestController
@RequestMapping(DRS_API_V1 + "/service-info")
public class DrsServiceInfo {

    @Autowired
    org.ga4gh.starterkit.drs.model.DrsServiceInfo drsServiceInfo;

    /**
     * Display service info
     * @return DRS service info
     */
    @GetMapping(produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public org.ga4gh.starterkit.drs.model.DrsServiceInfo getServiceInfo() {
        return drsServiceInfo;
    }
}
