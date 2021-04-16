package org.ga4gh.starterkit.drs.model;

import java.io.Serializable;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonView;

import org.ga4gh.starterkit.common.hibernate.HibernateEntity;
import org.ga4gh.starterkit.drs.utils.SerializeView;

@Entity
@Table(name = "aws_s3_access_object")
@JsonView(SerializeView.Admin.class)
public class AwsS3AccessObject implements Serializable, HibernateEntity<AwsS3AccessObjectId> {

    public static final long serialVersionUID = 1L;

    @Id
    @ManyToOne(fetch = FetchType.EAGER,
               cascade = {CascadeType.PERSIST, CascadeType.MERGE,
                          CascadeType.DETACH, CascadeType.REFRESH})
    @JoinColumn(name = "drs_object_id")
    @JsonIgnore
    private DrsObject drsObject;

    private String region;

    @Id
    private String bucket;

    @Id
    private String key;

    /* Constructors */

    public AwsS3AccessObject() {

    }

    public void loadRelations() {

    }

    public void setId(AwsS3AccessObjectId awsS3AccessObjectId) {
        this.drsObject = awsS3AccessObjectId.getDrsObject();
        this.bucket = awsS3AccessObjectId.getBucket();
        this.key = awsS3AccessObjectId.getKey();
    }

    @JsonIgnore
    public AwsS3AccessObjectId getId() {
        return new AwsS3AccessObjectId(drsObject, bucket, key);
    }

    /* Setters and Getters */

    public void setDrsObject(DrsObject drsObject)  {
        this.drsObject = drsObject;
    }

    public DrsObject getDrsObject() {
        return drsObject;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getRegion() {
        return region;
    }

    public void setBucket(String bucket) {
        this.bucket = bucket;
    }

    public String getBucket() {
        return bucket;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getKey() {
        return key;
    }
}
