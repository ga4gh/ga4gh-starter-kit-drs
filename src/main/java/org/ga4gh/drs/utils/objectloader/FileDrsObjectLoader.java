package org.ga4gh.drs.utils.objectloader;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.attribute.FileTime;
import java.security.MessageDigest;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.io.FilenameUtils;
import org.ga4gh.drs.model.AccessMethod;
import org.ga4gh.drs.model.Checksum;
import org.ga4gh.drs.model.ContentsObject;
import org.ga4gh.drs.model.DrsObject;
import org.ga4gh.drs.utils.MimeTypeImputer;

public class FileDrsObjectLoader extends AbstractDrsObjectLoader {

    private File file;

    public FileDrsObjectLoader(String objectId, String objectPath) {
        super(objectId, objectPath);
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
        /*
        try {
            InputStream is = Files.newInputStream(getFile().toPath());
            MessageDigest foo = MessageDigest.getInstance("MD5");
            foo.digest(is);
        } catch () {

        }
        */
        
        return null;
    }

    public int imputeSize() {
        return (int)getFile().length();
    }

    public String imputeName() {
        return getFile().getName();
    }

    public String imputeMimeType() {
        String ext = FilenameUtils.getExtension(getObjectPath());
        return MimeTypeImputer.imputeMimeType(ext);
    }

    public LocalDateTime imputeCreatedTime() {
        LocalDateTime createdTime = null;
        try {
            FileTime creationTime = (FileTime) Files.getAttribute(getFile().toPath(), "creationTime");
            createdTime = LocalDateTime.ofInstant(creationTime.toInstant(), ZoneId.of("UTC"));
        } catch (IOException e) {
        }
        return createdTime;
    }

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;

    }
}
