package org.ga4gh.drs.utils.objectloader;

import java.net.URI;

import org.ga4gh.drs.AppConfigConstants;
import org.ga4gh.drs.configuration.DrsConfigContainer;
import org.ga4gh.drs.model.DrsObject;
import org.ga4gh.drs.utils.DeepObjectMerger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

public abstract class AbstractDrsObjectLoader implements DrsObjectLoader {

    private String objectId;
    private String objectPath;
    private DrsConfigContainer drsConfigContainer;

    public AbstractDrsObjectLoader(String objectId, String objectPath) {
        this.objectId = objectId;
        this.objectPath = objectPath;
    }

    public String generateId() {
        return getObjectId();
    }

    public URI generateSelfURI() {
        String hostname = getDrsConfigContainer().getDrsConfig().getServerProps().getHostname();
        return URI.create("drs://" + hostname + "/" + getObjectId());
    }

    public DrsObject generateDrsObject() {
        
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

    @Autowired
    @Qualifier(AppConfigConstants.MERGED_DRS_CONFIG_CONTAINER)
    public final void setDrsConfigContainer(DrsConfigContainer drsConfigContainer) {
        this.drsConfigContainer = drsConfigContainer;
    }

    public final DrsConfigContainer getDrsConfigContainer() {
        return drsConfigContainer;
    }
}
