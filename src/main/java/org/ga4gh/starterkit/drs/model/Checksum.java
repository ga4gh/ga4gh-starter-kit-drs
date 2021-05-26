package org.ga4gh.starterkit.drs.model;

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
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonView;
import org.ga4gh.starterkit.common.hibernate.HibernateEntity;
import org.ga4gh.starterkit.drs.utils.SerializeView;
import org.springframework.lang.NonNull;

/**
 * Directly from DRS specification, indicates a checksum value for a DrsObject's
 * file bytes, as well as the algorithm used.
 */
@Entity
@Table(name = "drs_object_checksum")
@JsonView(SerializeView.Always.class)
public class Checksum implements HibernateEntity<Long> {

    public static final long serialVersionUID = 1L;

    /**
     * Unique identifier for checksum in the database
     */
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonIgnore
    private Long id;

    /**
     * Checksum hash value
     */
    @Column(name = "checksum")
    @NonNull
    private String checksum;

    /**
     * Hashing algorithm used (eg md5, sha1)
     */
    @Column(name = "type")
    private String type;

    /**
     * DrsObject that owns this checksum
     */
    @ManyToOne(fetch = FetchType.EAGER,
               cascade = {CascadeType.PERSIST, CascadeType.MERGE,
                          CascadeType.DETACH, CascadeType.REFRESH})
    @JoinColumn(name = "drs_object_id", nullable = false, insertable = true, updatable = true)
    @JsonBackReference
    private DrsObject drsObject;

    /**
     * Instantiates a new Checksum
     */
    public Checksum() {

    }

    /**
     * Instantiates a new Checksum with preconfigured id, checksum, and type
     * @param id unique identifier
     * @param checksum checksum value
     * @param type hashing algorithm
     */
    public Checksum(Long id, String checksum, String type) {
        this.id = id;
        this.checksum = checksum;
        this.type = type;
    }

    /**
     * Fetch relational data that is not loaded automatically (lazy load)
     */
    public void loadRelations() {

    }

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
     * Assign checksum
     * @param checksum checksum value
     */
    public void setChecksum(String checksum) {
        this.checksum = checksum;
    }

    /**
     * Retrieve checksum
     * @return checksum value
     */
    public String getChecksum() {
        return checksum;
    }

    /**
     * Assign type
     * @param type hashing algorithm
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * Retrieve type
     * @return hashing algorithm
     */
    public String getType() {
        return type;
    }

    /**
     * Assign drsObject
     * @param drsObject DrsObject owning the checksum
     */
    public void setDrsObject(DrsObject drsObject) {
        this.drsObject = drsObject;
    }

    /**
     * Retrieve drsObject
     * @return DrsObject owning the checksum
     */
    public DrsObject getDrsObject() {
        return drsObject;
    }

    @Override
	public boolean equals(Object that) {
		return (this == that) || ((that instanceof Checksum) && this.getId().equals(((Checksum) that).getId()));
	}

    @Override
    public int hashCode() {
        return getId().hashCode();
    }
}
