package org.ga4gh.drs.utils.objectloader;

import java.net.URI;
import java.time.LocalDateTime;
import java.util.List;

import org.ga4gh.drs.model.AccessMethod;
import org.ga4gh.drs.model.Checksum;
import org.ga4gh.drs.model.ContentsObject;
import org.ga4gh.drs.model.DrsObject;

public class HttpsDrsObjectLoader extends AbstractDrsObjectLoader {

    public HttpsDrsObjectLoader(String objectId, String objectPath) {
        super(objectId, objectPath);
    }

    public boolean exists() {
        // TODO fill out stub method
        return false;
    }

    public boolean isBundle() {
        // TODO fill out stub method
        return false;
    }

    public List<AccessMethod> generateAccessMethods() {
        // TODO fill out stub method
        return null;
    }

    public List<ContentsObject> generateContents() {
        // TODO fill out stub method
        return null;
    }

    public String generateId() {
        // TODO fill out stub method
        return null;
    }

    public URI generateSelfURI() {
        // TODO fill out stub method
        return null;
    }

    public DrsObject generateCustomDrsObjectProperties() {
        // TODO fill out stub method
        return null;
    }

    public List<Checksum> imputeChecksums() {
        // TODO fill out stub method
        return null;
    }

    public int imputeSize() {
        // TODO fill out stub method
        return 0;
    }

    public String imputeName() {
        // TODO fill out stub method
        return null;
    }

    public String imputeMimeType() {
        // TODO fill out stub method
        return null;
    }

    public LocalDateTime imputeCreatedTime() {
        // TODO fill out stub method
        return null;
    }
}
