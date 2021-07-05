package org.ga4gh.starterkit.drs.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonView;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import org.ga4gh.starterkit.common.constant.DateTimeConstants;
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

/**
 * Directly from DRS specification, with modifications, contains all metadata for
 * a DRSObject as described in the spec. Database (entity) attributes do not 
 * completely align with spec attributes where more relational sophistication
 * is warranted. Entity attributes can be converted to transient attributes that
 * align with and fulfill the DRS spec
 */
@Entity
@Table(name = "drs_object")
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
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
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = DateTimeConstants.DATE_FORMAT)
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
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = DateTimeConstants.DATE_FORMAT)
    @JsonView(SerializeView.Always.class)
    private LocalDateTime updatedTime;

    @Column(name = "version")
    @JsonView(SerializeView.Always.class)
    private String version;

    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(name = "drs_object_alias", joinColumns = @JoinColumn(name = "drs_object_id"))
    @Column(name = "alias")
    @Cascade(value = {CascadeType.ALL})
    @JsonView({
        SerializeView.Public.class,
        SerializeView.Admin.class
    })
    private List<String> aliases;

    @OneToMany(mappedBy = "drsObject",
               fetch = FetchType.LAZY,
               cascade = {javax.persistence.CascadeType.ALL},
               orphanRemoval = true)
    @JsonView({
        SerializeView.Public.class,
        SerializeView.Admin.class
    })
    @JsonManagedReference
    private List<Checksum> checksums;

    /* 
        Attributes capturing the parent-child relationship of DRS bundles to
        nested/sub bundles, to single blob DRS Objects
    */

    @Column(name = "is_bundle")
    @NonNull
    @JsonView(SerializeView.Admin.class)
    private Boolean isBundle;

    /**
     * List of bundles to which this DRSObject belongs, ie its 'parents'
     */
    @ManyToMany
    @JoinTable(
        name = "drs_object_bundle",
        joinColumns = {@JoinColumn(name = "parent_id")},
        inverseJoinColumns = {@JoinColumn(name = "child_id")}
    )
    @JsonView(SerializeView.Admin.class)
    private List<DrsObject> drsObjectChildren;

    /**
     * List of sub-bundles and/or objects that this bundle has, ie its 'children'
     */
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

    /**
     * List of file-based byte sources, ie files local to server
     */
    @OneToMany(mappedBy = "drsObject",
               fetch = FetchType.LAZY,
               cascade = javax.persistence.CascadeType.ALL,
               orphanRemoval = true)
    @JsonView(SerializeView.Admin.class)
    @JsonManagedReference
    private List<FileAccessObject> fileAccessObjects;

    /**
     * List of s3-based byte sources, ie objects on an AWS S3 bucket
     */
    @OneToMany(mappedBy = "drsObject",
               fetch = FetchType.LAZY,
               cascade = javax.persistence.CascadeType.ALL,
               orphanRemoval = true)
    @JsonView(SerializeView.Admin.class)
    @JsonManagedReference
    private List<AwsS3AccessObject> awsS3AccessObjects;

    /*
        Transient attributes produced from transforming database records. They
        are needed to conform to the DRS spec: selfURI, accessMethods, contents
    */

    /**
     * self DRS URI derived from service hostname and DRS object id
     */
    @Transient
    @NonNull
    @JsonView(SerializeView.Public.class)
    private URI selfURI;

    /**
     * access methods derived from all 'AccessObject' subtypes (e.g. FileAccessObjects,
     * AwsS3AccessObjects)
     */
    @Transient
    @JsonView(SerializeView.Public.class)
    private List<AccessMethod> accessMethods;

    /**
     * contents objects derived from 'children' DrsObjects
     */
    @Transient
    @JsonView(SerializeView.Public.class)
    private List<ContentsObject> contents;

    /**
     * Instantiates a new DrsObject
     */
    public DrsObject() {
        checksums = new ArrayList<>();
        fileAccessObjects = new ArrayList<>();
        awsS3AccessObjects = new ArrayList<>();
    }

    /* Constructors */

    /**
     * Instantiates a new DrsObject with preconfigured attributes
     * @param id DRSObject identifier
     * @param selfURI self URI
     * @param checksums checksum object list
     * @param createdTime timestamp of DRSObject creation
     * @param size size of file in bytes
     */
    public DrsObject(String id, URI selfURI, List<Checksum> checksums, LocalDateTime createdTime, Long size) {
        super();
        this.setId(id);
        this.setSelfURI(selfURI);
        this.setChecksums(checksums);
        this.setCreatedTime(createdTime);
        this.setSize(size);
    }

    /* Custom API methods */

    /**
     * Fetch relational data that is not loaded automatically (lazy load)
     */
    public void loadRelations() {
        Hibernate.initialize(getAliases());
        Hibernate.initialize(getChecksums());
        Hibernate.initialize(getDrsObjectChildren());
        Hibernate.initialize(getDrsObjectParents());
        Hibernate.initialize(getFileAccessObjects());
        Hibernate.initialize(getAwsS3AccessObjects());
    }

    /* Setters and Getters */

    /**
     * Assign id
     * @param id DRSObject identifier
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * Retrieve id
     * @return DRSObject identifier
     */
    public String getId() {
        return id;
    }

    /**
     * Assign description
     * @param description DRSObject description
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Retrieve description
     * @return DRSObject description
     */
    public String getDescription() {
        return description;
    }

    /**
     * Assign created time
     * @param createdTime timestamp of object creation time
     */
    public void setCreatedTime(LocalDateTime createdTime) {
        this.createdTime = createdTime;
    }

    /**
     * Retrieve created time
     * @return timestamp of object creation time
     */
    public LocalDateTime getCreatedTime() {
        return createdTime;
    }

    /**
     * Assign mimeType
     * @param mimeType valid media type
     */
    public void setMimeType(String mimeType) {
        this.mimeType = mimeType;
    }

    /**
     * Retrieve mimeType
     * @return valid media type
     */
    public String getMimeType() {
        return mimeType;
    }

    /**
     * Assign name
     * @param name DRSObject name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Retrieve name
     * @return DRSObject name
     */
    public String getName() {
        return name;
    }

    /**
     * Assign size
     * @param size DRSObject size
     */
    public void setSize(Long size) {
        this.size = size;
    }

    /**
     * Retrieve size
     * @return DRSObject size
     */
    public Long getSize() {
        return size;
    }

    /**
     * Assign updatedTime
     * @param updatedTime timestamp of object last updated time
     */
    public void setUpdatedTime(LocalDateTime updatedTime) {
        this.updatedTime = updatedTime;
    }

    /**
     * Retrieve updatedTime
     * @return timestamp of object last updated time
     */
    public LocalDateTime getUpdatedTime() {
        return updatedTime;
    }

    /**
     * Assign version
     * @param version DRSObject version
     */
    public void setVersion(String version) {
        this.version = version;
    }

    /**
     * Retrieve version
     * @return DRSObject version
     */
    public String getVersion() {
        return version;
    }

    /**
     * Assign aliases
     * @param aliases DRSObject aliases
     */
    public void setAliases(List<String> aliases) {
        this.aliases = aliases;
    }

    /**
     * Retrieve aliases
     * @return DRSObject aliases
     */
    public List<String> getAliases() {
        return aliases;
    }

    /**
     * Assign checksums
     * @param checksums checksum list
     */
    public void setChecksums(List<Checksum> checksums) {
        this.checksums = checksums;
    }

    /**
     * Retrieve checksums
     * @return checksum list
     */
    public List<Checksum> getChecksums() {
        return checksums;
    }

    public void setIsBundle(Boolean isBundle) {
        this.isBundle = isBundle;
    }

    public Boolean getIsBundle() {
        return isBundle;
    }

    /**
     * Assign drsObjectChildren
     * @param drsObjectChildren list of child DRS Objects 
     */
    public void setDrsObjectChildren(List<DrsObject> drsObjectChildren) {
        this.drsObjectChildren = drsObjectChildren;
    }

    /**
     * Retrieve drsObjectChildren
     * @return list of child DRS Objects
     */
    public List<DrsObject> getDrsObjectChildren() {
        return drsObjectChildren;
    }

    /**
     * Assign drsObjectParents
     * @param drsObjectParents list of parent DRS Objects
     */
    public void setDrsObjectParents(List<DrsObject> drsObjectParents) {
        this.drsObjectParents = drsObjectParents;
    }

    /**
     * Retrieve drsObjectParents
     * @return list of parent DRS Objects
     */
    public List<DrsObject> getDrsObjectParents() {
        return drsObjectParents;
    }

    /**
     * Assign fileAccessObjects
     * @param fileAccessObjects list of file-based access objects
     */
    public void setFileAccessObjects(List<FileAccessObject> fileAccessObjects) {
        this.fileAccessObjects = fileAccessObjects;
    }

    /**
     * Retrieve fileAccessObjects
     * @return list of file-based access objects
     */
    public List<FileAccessObject> getFileAccessObjects() {
        return fileAccessObjects;
    }

    /**
     * Assign awsS3AccessObjects
     * @param awsS3AccessObjects list of s3-based access objects
     */
    public void setAwsS3AccessObjects(List<AwsS3AccessObject> awsS3AccessObjects) {
        this.awsS3AccessObjects = awsS3AccessObjects;
    }

    /**
     * Retrieve awsS3AccessObjects
     * @return list of s3-based access objects
     */
    public List<AwsS3AccessObject> getAwsS3AccessObjects() {
        return awsS3AccessObjects;
    }

    /**
     * Assign selfURI
     * @param selfURI self-referencing DRS URI
     */
    public void setSelfURI(URI selfURI) {
        this.selfURI = selfURI;
    }

    /**
     * Retrieve selfURI
     * @return self-referencing DRS URI
     */
    public URI getSelfURI() {
        return selfURI;
    }

    /**
     * Assign access methods
     * @param accessMethods list of all valid access methods
     */
    public void setAccessMethods(List<AccessMethod> accessMethods) {
        this.accessMethods = accessMethods;
    }

    /**
     * Retrieve access methods
     * @return list of all valid access methods
     */
    public List<AccessMethod> getAccessMethods() {
        return accessMethods;
    }

    /**
     * Assign contents
     * @param contents list of contents objects representing sub/child DRS Objects
     */
    public void setContents(List<ContentsObject> contents) {
        this.contents = contents;
    }

    /**
     * Retrieve contents
     * @return list of contents objects representing child DRS Objects
     */
    public List<ContentsObject> getContents() {
        return contents;
    }
}
