package org.ga4gh.drs.utils.objectloader;

import java.time.LocalDateTime;
import java.util.List;

import org.ga4gh.drs.model.AccessMethod;
import org.ga4gh.drs.model.AccessType;
import org.ga4gh.drs.model.Checksum;
import org.ga4gh.drs.model.ContentsObject;
import org.ga4gh.drs.model.DrsObject;

public class S3DrsObjectLoader extends AbstractDrsObjectLoader {

    public S3DrsObjectLoader(String objectId, String objectPath) {
        super(objectId, objectPath);
    }

    @Override
    public boolean exists() {
        // TODO implement stub method
        return false;
    }

    @Override
    public boolean isBundle() {
        // TODO implement stub method
        return false;
    }

    @Override
    public List<AccessMethod> generateAccessMethods() {
        // TODO implement stub method
        return null;
    }

    @Override
    public List<ContentsObject> generateContents() {
        // TODO fill out stub method
        return null;
    }

    @Override
    public DrsObject generateCustomDrsObjectProperties() {
        // TODO fill out stub method
        return null;
    }

    @Override
    public List<Checksum> imputeChecksums() {
        // TODO implement stub method
        return null;
    }

    @Override
    public long imputeSize() {
        // TODO implement stub method
        return 0;
    }

    @Override
    public String imputeName() {
        // TODO implement stub method
        return null;
    }

    @Override
    public String imputeMimeType() {
        // TODO implement stub method
        return null;
    }

    @Override
    public LocalDateTime imputeCreatedTime() {
        // TODO implement stub method
        return null;
    }

    public AccessType getAccessType() {
        return AccessType.s3;
    }
}
