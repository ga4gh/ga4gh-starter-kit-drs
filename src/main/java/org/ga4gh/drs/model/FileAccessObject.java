package org.ga4gh.drs.model;

import java.io.Serializable;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

import org.ga4gh.starterkit.common.hibernate.HibernateEntity;

@Entity
@Table(name = "file_access_object")
public class FileAccessObject implements Serializable, HibernateEntity {

    public static final long serialVersionUID = 1L;

    @Id
    @ManyToOne(fetch = FetchType.EAGER,
               cascade = {CascadeType.PERSIST, CascadeType.MERGE,
                          CascadeType.DETACH, CascadeType.REFRESH})
    @JoinColumn(name = "drs_object_id")
    @JsonIgnore
    private DrsObject drsObject;

    @Id
    @JsonIgnore
    private String path;

    /* Constructors */

    public FileAccessObject() {

    }

    public void loadRelations() {
        
    }

    /* Setters and Getters */

    public void setDrsObject(DrsObject drsObject) {
        this.drsObject = drsObject;
    }

    public DrsObject getDrsObject() {
        return drsObject;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getPath() {
        return path;
    }
}
