package org.ga4gh.drs.utils.objectloader;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.time.LocalDateTime;
import java.util.List;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.ga4gh.drs.model.AccessMethod;
import org.ga4gh.drs.model.Checksum;
import org.ga4gh.drs.model.ContentsObject;
import org.ga4gh.drs.model.DrsObject;

public class FileDrsObjectLoader extends AbstractDrsObjectLoader {

    private File file;

    public FileDrsObjectLoader(String objectPath) {
        super(objectPath);
        file = new File(objectPath);
    }

    public boolean exists() {
        return getFile().exists();
    }

    public boolean isBundle() {
        return getFile().isDirectory();
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
        DrsObject customDrsObject = null;
        String customDrsObjectFilePath = getObjectPath() + ".drsmeta.json";
        File customDrsObjectFile = new File(customDrsObjectFilePath);
        if (customDrsObjectFile.exists() && !customDrsObjectFile.isDirectory()) {
            try {
                ObjectMapper mapper = new ObjectMapper(new JsonFactory());
                customDrsObject = mapper.readValue(customDrsObjectFile, DrsObject.class);
            } catch (IOException e) {
                System.out.println("AN EXCEPTION WAS CAUGHT " + e.getMessage());
            }
        }
        return customDrsObject;
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

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;

    }
}
