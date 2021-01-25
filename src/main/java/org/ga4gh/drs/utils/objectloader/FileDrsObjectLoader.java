package org.ga4gh.drs.utils.objectloader;

import java.io.File;
import java.io.IOException;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
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

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;

    }
}
