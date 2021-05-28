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

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonView;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import org.ga4gh.starterkit.common.hibernate.HibernateEntity;
import org.ga4gh.starterkit.drs.utils.SerializeView;

/**
 * Inferred from DRS specification, indicates a byte source for a DRSObject with
 * a 'file' access type. References a file that is locally available wherever 
 * the server is deployed. Contains required info to facilitate access to a
 * local file (generally just file path)
 */
@Entity
@Table(name = "file_access_object")
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
@JsonView(SerializeView.Admin.class)
public class FileAccessObject implements Serializable, HibernateEntity<Long> {

    public static final long serialVersionUID = 1L;

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonIgnore
    private Long id;

    /**
     * Local file path
     */
    @Column(name = "path")
    private String path;

    @ManyToOne(fetch = FetchType.EAGER,
               cascade = {CascadeType.PERSIST, CascadeType.MERGE,
                          CascadeType.DETACH, CascadeType.REFRESH})
    @JoinColumn(name = "drs_object_id")
    @JsonBackReference
    private DrsObject drsObject;

    /* Constructors */

    /**
     * Instantiates a new FileAccessObject
     */
    public FileAccessObject() {

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
     * Assign path
     * @param path local file path
     */
    public void setPath(String path) {
        this.path = path;
    }

    /**
     * Retrive path
     * @return local file path
     */
    public String getPath() {
        return path;
    }

    /**
     * Assign drsObject
     * @param drsObject DrsObject owning this access object
     */
    public void setDrsObject(DrsObject drsObject) {
        this.drsObject = drsObject;
    }

    /**
     * Retrieve drsObject
     * @return DrsObject owning this access object
     */
    public DrsObject getDrsObject() {
        return drsObject;
    }
}
