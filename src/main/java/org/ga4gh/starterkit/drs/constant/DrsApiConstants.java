package org.ga4gh.starterkit.drs.constant;

import static org.ga4gh.starterkit.common.constant.StarterKitConstants.ADMIN;
import static org.ga4gh.starterkit.common.constant.StarterKitConstants.GA4GH;
import static org.ga4gh.starterkit.common.constant.StarterKitConstants.DRS;
import static org.ga4gh.starterkit.common.constant.StarterKitConstants.V1;

/**
 * DRS API URL path/routing constants
 */
public class DrsApiConstants {

    /**
     * Common REST API route to most (if not all) DRS-related controller functions
     */
    public static final String DRS_API_V1 = "/" + GA4GH + "/" + DRS + "/" + V1;

    /**
     * Common REST API route to most (if not all) off-spec, administrative controller
     * functions for modifying DRS-related entities
     */
    public static final String ADMIN_DRS_API_V1 = "/" + ADMIN + DRS_API_V1;
}
