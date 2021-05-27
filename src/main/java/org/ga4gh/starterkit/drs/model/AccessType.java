package org.ga4gh.starterkit.drs.model;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

/**
 * Directly from DRS specification, enumeration of different Access Types for
 * AccessMethods under a DRSObject. Access Types correspond closely with URL
 * schemes for fetching file bytes (e.g. file://, https://, s3://, etc.)
 */
@JsonNaming(PropertyNamingStrategy.LowerCaseStrategy.class)
public enum AccessType {
    // TODO: incorporate other data source schemes, for now only file, s3, https
    s3,
    // GS,
    // FTP,
    // GSIFTP,
    // GLOBUS,
    // HTSGET,
    https,
    file,
}
