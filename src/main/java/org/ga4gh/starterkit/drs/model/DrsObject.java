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
import lombok.Getter;
import lombok.Setter;
import org.ga4gh.starterkit.common.constant.DateTimeConstants;
import org.ga4gh.starterkit.common.hibernate.HibernateEntity;
import org.ga4gh.starterkit.drs.utils.SerializeView;
import org.hibernate.Hibernate;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
import org.springframework.lang.NonNull;

import javax.persistence.*;
import java.net.URI;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Directly from DRS specification, with modifications, contains all metadata for
 * a DRSObject as described in the spec. Database (entity) attributes do not 
 * completely align with spec attributes where more relational sophistication
 * is warranted. Entity attributes can be converted to transient attributes that
 * align with and fulfill the DRS spec
 */
@Entity
@Table(name = "drs_object")
@Setter
@Getter
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

    @ManyToMany
    @JoinTable(
        name = "drs_object_visa",
        joinColumns = {@JoinColumn(name = "drs_object_id")},
        inverseJoinColumns = {@JoinColumn(name = "visa_id")}
    )
    @JsonView(SerializeView.Admin.class)
    private List<PassportVisa> passportVisas;

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
        passportVisas = new ArrayList<>();
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
        Hibernate.initialize(getPassportVisas());
    }

    @Override
    public String toString() {
        return "DrsObject{" +
                "id='" + id + '\'' +
                ", description='" + description + '\'' +
                ", createdTime=" + createdTime +
                ", mimeType='" + mimeType + '\'' +
                ", name='" + name + '\'' +
                ", size=" + size +
                ", updatedTime=" + updatedTime +
                ", version='" + version + '\'' +
                ", aliases=" + aliases +
                ", checksums=" + checksums +
                ", isBundle=" + isBundle +
                ", drsObjectChildren=" + drsObjectChildren +
                ", drsObjectParents=" + drsObjectParents +
                ", fileAccessObjects=" + fileAccessObjects +
                ", awsS3AccessObjects=" + awsS3AccessObjects +
                ", passportVisas=" + passportVisas +
                ", selfURI=" + selfURI +
                ", accessMethods=" + accessMethods +
                ", contents=" + contents +
                '}';
    }
}
