package org.ga4gh.drs.utils.objectloader;

import org.ga4gh.drs.model.DrsObject;
import org.ga4gh.drs.utils.DeepObjectMerger;

public abstract class AbstractDrsObjectLoader implements DrsObjectLoader {

    private String objectId;
    private String objectPath;

    public AbstractDrsObjectLoader(String objectId, String objectPath) {
        this.objectId = objectId;
        this.objectPath = objectPath;
    }

    public String generateId() {
        return getObjectId();
    }

    public DrsObject generateDrsObject() {
        System.out.println("Generating DRS Object");
        
        // set mandatory properties that cannot be 
        // overriden by custom property files 
        DrsObject mandatoryProps = new DrsObject();
        mandatoryProps.setId(generateId());
        mandatoryProps.setSelfURI(generateSelfURI());
        if (isBundle()) {
            mandatoryProps.setContents(generateContents());
        } else {
            mandatoryProps.setAccessMethods(generateAccessMethods());
        }

        // parse custom DrsObject property values from
        // associated metadata files
        DrsObject customProps = generateCustomDrsObjectProperties();

        // merge mandatory properties onto custom properties
        DeepObjectMerger.merge(mandatoryProps, customProps);
        DrsObject finalProps = customProps;

        // impute properties that were not indicated in the
        // custom properties file
        if (finalProps.getChecksums() == null) {
            finalProps.setChecksums(imputeChecksums());
        }
        if (finalProps.getSize() == 0) {
            finalProps.setSize(imputeSize());
        }
        if (finalProps.getName() == null) {
            finalProps.setName(imputeName());
        }
        if (finalProps.getMimeType() == null) {
            finalProps.setMimeType(imputeMimeType());
        }
        if (finalProps.getCreatedTime() == null) {
            finalProps.setCreatedTime(imputeCreatedTime());
        }
        
        return finalProps;
    }

    public void setObjectId(String objectId) {
        this.objectId = objectId;
    }

    public String getObjectId() {
        return objectId;
    }

    public void setObjectPath(String objectPath) {
        this.objectPath = objectPath;
    }

    public String getObjectPath() {
        return objectPath;
    }
}
