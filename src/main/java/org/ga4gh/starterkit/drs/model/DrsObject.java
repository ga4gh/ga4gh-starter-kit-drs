package org.ga4gh.starterkit.drs.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonView;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import org.ga4gh.starterkit.common.hibernate.HibernateEntity;
import org.ga4gh.starterkit.drs.utils.SerializeView;
import org.hibernate.Hibernate;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
import org.springframework.lang.NonNull;
import java.net.URI;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name = "drs_object")
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class DrsObject implements HibernateEntity<String> {
    
    /*
        Simple attributes lifted directly from the DRS spec: id, description,
        createdTime, mimeType, name, size, updatedTime, version, aliases,
        checksums
    */

    @Id
    @Column(name = "id", updatable = false, nullable = false)
    @NonNull
    @JsonView(SerializeView.Always.class)
    private String id;

    @Column(name = "description")
    @JsonView(SerializeView.Always.class)
    private String description;

    @Column(name = "created_time")
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'")
    @NonNull
    @JsonView(SerializeView.Always.class)
    private LocalDateTime createdTime;

    @Column(name = "mime_type")
    @JsonView(SerializeView.Always.class)
    private String mimeType;

    @Column(name = "name")
    @JsonView(SerializeView.Always.class)
    private String name;

    @Column(name = "size")
    @JsonView(SerializeView.Always.class)
    private Long size;

    @Column(name = "updated_time")
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'")
    @JsonView(SerializeView.Always.class)
    private LocalDateTime updatedTime;

    @Column(name = "version")
    @JsonView(SerializeView.Always.class)
    private String version;

    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(name = "drs_object_alias", joinColumns = @JoinColumn(name = "drs_object_id"))
    @Column(name = "alias")
    @Cascade(value = {CascadeType.ALL})
    @JsonView(SerializeView.Always.class)
    private List<String> aliases;

    @OneToMany(mappedBy = "drsObject",
               fetch = FetchType.LAZY,
               cascade = {javax.persistence.CascadeType.ALL},
               orphanRemoval = true)
    @JsonView(SerializeView.Always.class)
    private List<Checksum> checksums;

    /* 
        Attributes capturing the parent-child relationship of DRS bundles to
        nested/sub bundles, to single blob DRS Objects
    */

    @ManyToMany
    @JoinTable(
        name = "drs_object_bundle",
        joinColumns = {@JoinColumn(name = "parent_id")},
        inverseJoinColumns = {@JoinColumn(name = "child_id")}
    )
    @JsonView(SerializeView.Admin.class)
    private List<DrsObject> drsObjectChildren;

    @ManyToMany
    @JoinTable(
        name = "drs_object_bundle",
        joinColumns = {@JoinColumn(name = "child_id")},
        inverseJoinColumns = {@JoinColumn(name = "parent_id")}
    )
    @JsonView(SerializeView.Admin.class)
    private List<DrsObject> drsObjectParents;

    /*
        Attributes capturing multiple byte storage/access locations associated
        with a single DRSObject
    */

    @OneToMany(mappedBy = "drsObject",
               fetch = FetchType.LAZY,
               cascade = javax.persistence.CascadeType.ALL,
               orphanRemoval = true)
    @JsonView(SerializeView.Admin.class)
    private List<FileAccessObject> fileAccessObjects;

    @OneToMany(mappedBy = "drsObject",
               fetch = FetchType.LAZY,
               cascade = javax.persistence.CascadeType.ALL,
               orphanRemoval = true)
    @JsonView(SerializeView.Admin.class)
    private List<AwsS3AccessObject> awsS3AccessObjects;

    /*
        Transient attributes produced from transforming database records. They
        are needed to conform to the DRS spec: selfURI, accessMethods, contents
    */

    @Transient
    @NonNull
    @JsonView(SerializeView.Public.class)
    private URI selfURI;

    @Transient
    @JsonView(SerializeView.Public.class)
    private List<AccessMethod> accessMethods;

    @Transient
    @JsonView(SerializeView.Public.class)
    private List<ContentsObject> contents;

    public DrsObject() {
        checksums = new ArrayList<>();
        fileAccessObjects = new ArrayList<>();
        awsS3AccessObjects = new ArrayList<>();
    }

    /* Constructors */

    public DrsObject(String id, URI selfURI, List<Checksum> checksums, LocalDateTime createdTime, Long size) {
        super();
        this.setId(id);
        this.setSelfURI(selfURI);
        this.setChecksums(checksums);
        this.setCreatedTime(createdTime);
        this.setSize(size);
    }

    /* Custom API methods */

    public void loadRelations() {
        Hibernate.initialize(getAliases());
        Hibernate.initialize(getChecksums());
        Hibernate.initialize(getDrsObjectChildren());
        Hibernate.initialize(getDrsObjectParents());
        Hibernate.initialize(getFileAccessObjects());
        Hibernate.initialize(getAwsS3AccessObjects());
    }

    /* Setters and Getters */

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public void setCreatedTime(LocalDateTime createdTime) {
        this.createdTime = createdTime;
    }

    public LocalDateTime getCreatedTime() {
        return createdTime;
    }

    public void setMimeType(String mimeType) {
        this.mimeType = mimeType;
    }

    public String getMimeType() {
        return mimeType;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setSize(Long size) {
        this.size = size;
    }

    public Long getSize() {
        return size;
    }

    public void setUpdatedTime(LocalDateTime updatedTime) {
        this.updatedTime = updatedTime;
    }

    public LocalDateTime getUpdatedTime() {
        return updatedTime;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getVersion() {
        return version;
    }

    public void setAliases(List<String> aliases) {
        this.aliases = aliases;
    }

    public List<String> getAliases() {
        return aliases;
    }

    public void setChecksums(List<Checksum> checksums) {
        this.checksums = checksums;
    }

    public List<Checksum> getChecksums() {
        return checksums;
    }

    public void setDrsObjectChildren(List<DrsObject> drsObjectChildren) {
        this.drsObjectChildren = drsObjectChildren;
    }

    public List<DrsObject> getDrsObjectChildren() {
        return drsObjectChildren;
    }

    public void setDrsObjectParents(List<DrsObject> drsObjectParents) {
        this.drsObjectParents = drsObjectParents;
    }

    public List<DrsObject> getDrsObjectParents() {
        return drsObjectParents;
    }

    public void setFileAccessObjects(List<FileAccessObject> fileAccessObjects) {
        this.fileAccessObjects = fileAccessObjects;
    }

    public List<FileAccessObject> getFileAccessObjects() {
        return fileAccessObjects;
    }

    public void setAwsS3AccessObjects(List<AwsS3AccessObject> awsS3AccessObjects) {
        this.awsS3AccessObjects = awsS3AccessObjects;
    }

    public List<AwsS3AccessObject> getAwsS3AccessObjects() {
        return awsS3AccessObjects;
    }

    public void setSelfURI(URI selfURI) {
        this.selfURI = selfURI;
    }

    public URI getSelfURI() {
        return selfURI;
    }

    public void setAccessMethods(List<AccessMethod> accessMethods) {
        this.accessMethods = accessMethods;
    }

    public List<AccessMethod> getAccessMethods() {
        return accessMethods;
    }

    public void setContents(List<ContentsObject> contents) {
        this.contents = contents;
    }

    public List<ContentsObject> getContents() {
        return contents;
    }
}
