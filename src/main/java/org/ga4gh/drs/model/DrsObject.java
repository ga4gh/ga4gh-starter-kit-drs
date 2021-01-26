package org.ga4gh.drs.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import org.springframework.lang.NonNull;

import java.net.URI;
import java.time.LocalDateTime;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class DrsObject {
    // Required
    @NonNull
    private String id;

    @NonNull
    private URI selfURI;

    @NonNull
    private List<Checksum> checksums;

    @NonNull
    private LocalDateTime createdTime;

    private long size;

    // Optional
    private List<AccessMethod> accessMethods;

    private List<String> aliases;

    private List<ContentsObject> contents;

    private LocalDateTime updatedTime;

    private String description;

    private String mimeType;

    private String name;

    private String version;

    public DrsObject() {
        
    }

    public DrsObject(String id, URI selfURI, List<Checksum> checksums, LocalDateTime createdTime, long size) {
        this.setId(id);
        this.setSelfURI(selfURI);
        this.setChecksums(checksums);
        this.setCreatedTime(createdTime);
        this.setSize(size);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public URI getSelfURI() {
        return selfURI;
    }

    public void setSelfURI(URI selfURI) {
        this.selfURI = selfURI;
    }

    public List<Checksum> getChecksums() {
        return checksums;
    }

    public void setChecksums(List<Checksum> checksums) {
        this.checksums = checksums;
    }

    public LocalDateTime getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(LocalDateTime createdTime) {
        this.createdTime = createdTime;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public List<AccessMethod> getAccessMethods() {
        return accessMethods;
    }

    public void setAccessMethods(List<AccessMethod> accessMethods) {
        this.accessMethods = accessMethods;
    }

    public List<String> getAliases() {
        return aliases;
    }

    public void setAliases(List<String> aliases) {
        this.aliases = aliases;
    }

    public List<ContentsObject> getContents() {
        return contents;
    }

    public void setContents(List<ContentsObject> contents) {
        this.contents = contents;
    }

    public LocalDateTime getUpdatedTime() {
        return updatedTime;
    }

    public void setUpdatedTime(LocalDateTime updatedTime) {
        this.updatedTime = updatedTime;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getMimeType() {
        return mimeType;
    }

    public void setMimeType(String mimeType) {
        this.mimeType = mimeType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }
}
