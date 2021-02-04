package org.ga4gh.drs.utils.datasource;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import org.ga4gh.drs.utils.objectloader.FileDrsObjectLoader;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class LocalFileDataSource implements DataSource<FileDrsObjectLoader> {

    private String idPrefix;
    private String rootDir;

    public LocalFileDataSource() {

    }

    @Override
    public boolean objectIdMatches(String objectId) {
        return objectId.startsWith(getIdPrefix());
    }

    public String renderObjectPath(String objectId) {
        String postPrefix = objectId.replaceFirst(getIdPrefix(), "");
        String trailingFilePath = postPrefix.replaceAll("-", "/");
        return getRootDir() + trailingFilePath;
    }

    public void setIdPrefix(String idPrefix) {
        this.idPrefix = idPrefix;
    }

    public String getIdPrefix() {
        return idPrefix;
    }

    public void setRootDir(String rootDir) {
        this.rootDir = rootDir;
    }

    public String getRootDir() {
        return rootDir;
    }
}
