package org.ga4gh.drs.utils.objectloader;

import org.ga4gh.drs.model.DrsObject;
import java.net.URI;
import java.time.LocalDateTime;
import java.util.List;
import org.ga4gh.drs.model.AccessMethod;
import org.ga4gh.drs.model.Checksum;
import org.ga4gh.drs.model.ContentsObject;

public interface DrsObjectLoader {

    public boolean exists();
    public boolean isBundle();
    public DrsObject generateDrsObject();
    public List<AccessMethod> generateAccessMethods();
    public List<ContentsObject> generateContents();
    public String generateId();
    public URI generateSelfURI();
    public DrsObject generateCustomDrsObjectProperties();
    public List<Checksum> imputeChecksums();
    public int imputeSize();
    public String imputeName();
    public String imputeMimeType();
    public LocalDateTime imputeCreatedTime();
    public void setObjectPath(String objectPath);
    public String getObjectPath();
}
