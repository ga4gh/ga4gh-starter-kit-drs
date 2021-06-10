package org.ga4gh.starterkit.drs.controller;

import static org.ga4gh.starterkit.drs.constant.DrsApiConstants.DRS_API_V1;

import org.ga4gh.starterkit.common.util.logging.LoggingUtil;
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
    private org.ga4gh.starterkit.drs.model.DrsServiceInfo drsServiceInfo;

    @Autowired
    private LoggingUtil loggingUtil;

    /**
     * Display service info
     * @return DRS service info
     */
    @GetMapping //(produces = MediaType.APPLICATION_JSON_VALUE)
    public org.ga4gh.starterkit.drs.model.DrsServiceInfo getServiceInfo() {
        loggingUtil.debug("Public API request: service info");
        loggingUtil.trace(drsServiceInfo.toString());
        return drsServiceInfo;
    }
}
