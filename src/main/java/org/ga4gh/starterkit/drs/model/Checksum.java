package org.ga4gh.starterkit.drs.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonView;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import org.ga4gh.starterkit.common.hibernate.HibernateEntity;
import org.ga4gh.starterkit.drs.utils.SerializeView;
import org.springframework.lang.NonNull;

import javax.persistence.*;

/**
 * Directly from DRS specification, indicates a checksum value for a DrsObject's
 * file bytes, as well as the algorithm used.
 */
@Entity
@Table(name = "drs_object_checksum")
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
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
     * Instantiates a new Checksum with preconfigured checksum, type, and drsObject
     * @param checksum checksum value
     * @param type hashing algorithm
     * @param drsObject drsObject to which checksum belongs
     */
    public Checksum(String checksum, String type, DrsObject drsObject) {
        this.checksum = checksum;
        this.type = type;
        this.drsObject = drsObject;
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
}
