package org.ga4gh.starterkit.drs.model;

import org.ga4gh.starterkit.common.model.ServiceInfo;
import static org.ga4gh.starterkit.drs.constant.DrsServiceInfoDefaults.ID;
import static org.ga4gh.starterkit.drs.constant.DrsServiceInfoDefaults.NAME;
import static org.ga4gh.starterkit.drs.constant.DrsServiceInfoDefaults.DESCRIPTION;
import static org.ga4gh.starterkit.drs.constant.DrsServiceInfoDefaults.CONTACT_URL;
import static org.ga4gh.starterkit.drs.constant.DrsServiceInfoDefaults.DOCUMENTATION_URL;
import static org.ga4gh.starterkit.drs.constant.DrsServiceInfoDefaults.CREATED_AT;
import static org.ga4gh.starterkit.drs.constant.DrsServiceInfoDefaults.UPDATED_AT;
import static org.ga4gh.starterkit.drs.constant.DrsServiceInfoDefaults.ENVIRONMENT;
import static org.ga4gh.starterkit.drs.constant.DrsServiceInfoDefaults.VERSION;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import static org.ga4gh.starterkit.drs.constant.DrsServiceInfoDefaults.ORGANIZATION_NAME;
import static org.ga4gh.starterkit.drs.constant.DrsServiceInfoDefaults.ORGANIZATION_URL;
import static org.ga4gh.starterkit.drs.constant.DrsServiceInfoDefaults.SERVICE_TYPE_GROUP;
import static org.ga4gh.starterkit.drs.constant.DrsServiceInfoDefaults.SERVICE_TYPE_ARTIFACT;
import static org.ga4gh.starterkit.drs.constant.DrsServiceInfoDefaults.SERVICE_TYPE_VERSION;

/**
 * Extension of the GA4GH base service info specification to include DRS-specific
 * properties
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonNaming(PropertyNamingStrategies.LowerCamelCaseStrategy.class)
public class DrsServiceInfo extends ServiceInfo {

    /**
     * Instantiates a new DrsServiceInfo object
     */
    public DrsServiceInfo() {
        super();
        setAllDefaults();
    }

    /**
     * Sets all default properties
     */
    private void setAllDefaults() {
        setId(ID);
        setName(NAME);
        setDescription(DESCRIPTION);
        setContactUrl(CONTACT_URL);
        setDocumentationUrl(DOCUMENTATION_URL);
        setCreatedAt(CREATED_AT);
        setUpdatedAt(UPDATED_AT);
        setEnvironment(ENVIRONMENT);
        setVersion(VERSION);
        getOrganization().setName(ORGANIZATION_NAME);
        getOrganization().setUrl(ORGANIZATION_URL);
        getType().setGroup(SERVICE_TYPE_GROUP);
        getType().setArtifact(SERVICE_TYPE_ARTIFACT);
        getType().setVersion(SERVICE_TYPE_VERSION);
    }
}
