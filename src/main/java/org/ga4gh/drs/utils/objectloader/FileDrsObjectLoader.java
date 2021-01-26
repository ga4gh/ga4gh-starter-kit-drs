package org.ga4gh.drs.utils.objectloader;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.attribute.FileTime;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.io.FilenameUtils;
import org.ga4gh.drs.model.AccessMethod;
import org.ga4gh.drs.model.AccessType;
import org.ga4gh.drs.model.Checksum;
import org.ga4gh.drs.model.ContentsObject;
import org.ga4gh.drs.model.DrsObject;
import org.ga4gh.drs.utils.MimeTypeImputer;
import org.springframework.util.DigestUtils;

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
        // TODO currently access ID has no meaning within the app, should be
        // used to populate a lookup cache
        String accessID = UUID.randomUUID().toString();
        AccessMethod accessMethod = new AccessMethod(accessID, AccessType.HTTPS);
        return new ArrayList<AccessMethod>() {{add(accessMethod);}};
    }

    public List<ContentsObject> generateContents() {
        // TODO fill out stub method
        return null;
    }

    public DrsObject generateCustomDrsObjectProperties() {
        DrsObject customDrsObject = new DrsObject();
        String customDrsObjectFilePath = getObjectPath() + ".drsmeta.json";
        File customDrsObjectFile = new File(customDrsObjectFilePath);
        if (customDrsObjectFile.exists() && !customDrsObjectFile.isDirectory()) {
            try {
                ObjectMapper mapper = new ObjectMapper(new JsonFactory());
                customDrsObject = mapper.readValue(customDrsObjectFile, DrsObject.class);
            } catch (IOException e) {
                // TODO log the exception
                return customDrsObject;
            }
        }
        return customDrsObject;
    }

    public List<Checksum> imputeChecksums() {
        List<Checksum> checksums = new ArrayList<>();
        try (InputStream is = Files.newInputStream(getFile().toPath())) {
            String digest = DigestUtils.md5DigestAsHex(is);
            Checksum md5Checksum = new Checksum(digest, "md5");
            checksums.add(md5Checksum);
        } catch (IOException e) {
            return checksums;
        }
        return checksums;
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
            return createdTime;
        }
        return createdTime;
    }

    private File getFile() {
        return file;
    }
}
