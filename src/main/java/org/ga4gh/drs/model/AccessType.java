package org.ga4gh.drs.model;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

@JsonNaming(PropertyNamingStrategy.LowerCaseStrategy.class)
public enum AccessType {
    // TODO: incorporate other data source schemes, for now only https, and file
    // S3,
    // GS,
    // FTP,
    // GSIFTP,
    // GLOBUS,
    // HTSGET,
    HTTPS,
    FILE,
}
