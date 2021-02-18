package org.ga4gh.drs.utils.objectloader;

import org.ga4gh.drs.model.DrsObject;
import java.net.URI;
import java.time.LocalDateTime;
import java.util.List;
import org.ga4gh.drs.model.AccessMethod;
import org.ga4gh.drs.model.AccessType;
import org.ga4gh.drs.model.Checksum;
import org.ga4gh.drs.model.ContentsObject;

public interface DrsObjectLoader {

    boolean exists();
    boolean isBundle();
    void setExpand(boolean expand);
    DrsObject generateDrsObject();
    List<AccessMethod> generateAccessMethods();
    List<ContentsObject> generateContents();
    String generateId();
    URI generateSelfURI();
    DrsObject generateCustomDrsObjectProperties();
    List<Checksum> imputeChecksums();
    long imputeSize();
    String imputeName();
    String imputeMimeType();
    LocalDateTime imputeCreatedTime();
    void setObjectPath(String objectPath);
    String getObjectPath();
    AccessType getAccessType();
}
