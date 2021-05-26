package org.ga4gh.starterkit.drs.app;

/**
 * String constants for deployment config, generally Spring bean name/qualifier
 * constants
 */
public class DrsStandaloneConstants {

    /* Spring bean names - DRS config container */

    /**
     * Spring bean qualifier for an empty drs config container
     */
    public static final String EMPTY_DRS_CONFIG_CONTAINER = "emptyDrsConfigContainer";

    /**
     * Spring bean qualifier for the drs config container containing all defaults
     */
    public static final String DEFAULT_DRS_CONFIG_CONTAINER = "defaultDrsConfigContainer";

    /**
     * Spring bean qualifier for the drs config container containing user-loaded properties
     */
    public static final String USER_DRS_CONFIG_CONTAINER = "userDrsConfigContainer";

    /**
     * Spring bean qualifier for the final drs config container containing merged
     * properties from default and user-loaded (user-loaded properties override
     * defaults) 
     */
    public static final String FINAL_DRS_CONFIG_CONTAINER = "finalDrsConfigContainer";

    /* Spring bean scope */

    /**
     * Indicates Spring bean has 'prototype' lifecycle
     */
    public static final String PROTOTYPE = "prototype";
}
