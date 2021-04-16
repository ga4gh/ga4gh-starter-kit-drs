package org.ga4gh.starterkit.drs.model;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonView;
import org.ga4gh.starterkit.common.hibernate.HibernateEntity;
import org.ga4gh.starterkit.drs.utils.SerializeView;
import org.springframework.lang.NonNull;

@Entity
@Table(name = "drs_object_checksum")
@IdClass(ChecksumId.class)
@JsonView(SerializeView.Always.class)
public class Checksum implements HibernateEntity<ChecksumId> {

    public static final long serialVersionUID = 1L;

    @Id
    @ManyToOne(fetch = FetchType.EAGER,
               cascade = {CascadeType.PERSIST, CascadeType.MERGE,
                          CascadeType.DETACH, CascadeType.REFRESH})
    @JoinColumn(name = "drs_object_id")
    @JsonIgnore
    private DrsObject drsObject;

    @Id
    @NonNull
    private String type;

    @NonNull
    private String checksum;

    public Checksum() {
        
    }

    public Checksum(String checksum, String type) {
        this.checksum = checksum;
        this.type = type;
    }

    public void loadRelations() {

    }

    public void setId(ChecksumId checksumId) {
        this.drsObject = checksumId.getDrsObject();
        this.type = checksumId.getType();
    }

    @JsonIgnore
    public ChecksumId getId() {
        return new ChecksumId(drsObject, type);
    }

    public DrsObject getDrsObject() {
        return drsObject;
    }

    public void setDrsObject(DrsObject drsObject) {
        this.drsObject = drsObject;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getChecksum() {
        return checksum;
    }

    public void setChecksum(String checksum) {
        this.checksum = checksum;
    }
}
