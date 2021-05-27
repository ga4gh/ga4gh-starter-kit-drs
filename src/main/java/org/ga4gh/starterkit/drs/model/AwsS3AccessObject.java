package org.ga4gh.starterkit.drs.model;

import java.io.Serializable;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonView;
import org.ga4gh.starterkit.common.hibernate.HibernateEntity;
import org.ga4gh.starterkit.drs.utils.SerializeView;

/**
 * Inferred from DRS specification, indicates byte source for a DRSObject with
 * an 's3' access type. References a file stored on AWS S3. Contains required
 * info to facilitate access to an s3 file/object.
 */
@Entity
@Table(name = "aws_s3_access_object")
@JsonView(SerializeView.Admin.class)
public class AwsS3AccessObject implements Serializable, HibernateEntity<Long> {

    public static final long serialVersionUID = 1L;

    /**
     * unique identifier
     */
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonIgnore
    private Long id;

    /**
     * AWS region where bucket is hosted 
     */
    @Column(name = "region")
    private String region;

    /**
     * AWS S3 bucket name hosting the object
     */
    @Column(name = "bucket")
    private String bucket;

    /**
     * The key/file path to the object bytes
     */
    @Column(name = "key")
    private String key;

    /**
     * The DRSObject associated with this access object
     */
    @ManyToOne(fetch = FetchType.EAGER,
               cascade = {CascadeType.PERSIST, CascadeType.MERGE,
                          CascadeType.DETACH, CascadeType.REFRESH})
    @JoinColumn(name = "drs_object_id")
    @JsonIgnore
    private DrsObject drsObject;

    /* Constructors */

    /**
     * Instantiates a new AwsS3AccessObject
     */
    public AwsS3AccessObject() {

    }

    /**
     * Fetch relational data that is not loaded automatically (lazy load)
     */
    public void loadRelations() {

    }

    /* Setters and Getters */

    /**
     * Assign id
     * @param id identifier
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Retrieve id
     * @return identifier
     */
    public Long getId() {
        return id;
    }

    /**
     * Assign region
     * @param region AWS region
     */
    public void setRegion(String region) {
        this.region = region;
    }

    /**
     * Retrieve region
     * @return AWS region
     */
    public String getRegion() {
        return region;
    }

    /**
     * Assign bucket
     * @param bucket AWS bucket
     */
    public void setBucket(String bucket) {
        this.bucket = bucket;
    }

    /**
     * Retrieve bucket
     * @return AWS bucket
     */
    public String getBucket() {
        return bucket;
    }

    /**
     * Assign key
     * @param key key/file path to object bytes
     */
    public void setKey(String key) {
        this.key = key;
    }

    /**
     * Retrieve key
     * @return key/file path to object bytes
     */
    public String getKey() {
        return key;
    }

    /**
     * Assign drsObject
     * @param drsObject DrsObject associated with this access object
     */
    public void setDrsObject(DrsObject drsObject)  {
        this.drsObject = drsObject;
    }

    /**
     * Retrieve drsObject
     * @return DrsObject associated with this access object
     */
    public DrsObject getDrsObject() {
        return drsObject;
    }
}
