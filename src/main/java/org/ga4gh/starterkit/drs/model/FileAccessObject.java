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
@Table(name = "file_access_object")
@JsonView(SerializeView.Admin.class)
public class FileAccessObject implements Serializable, HibernateEntity<FileAccessObjectId> {

    public static final long serialVersionUID = 1L;

    @Id
    @ManyToOne(fetch = FetchType.EAGER,
               cascade = {CascadeType.PERSIST, CascadeType.MERGE,
                          CascadeType.DETACH, CascadeType.REFRESH})
    @JoinColumn(name = "drs_object_id")
    @JsonIgnore
    private DrsObject drsObject;

    @Id
    private String path;

    /* Constructors */

    public FileAccessObject() {

    }

    public void loadRelations() {
        
    }

    public void setId(FileAccessObjectId fileAccessObjectId) {
        this.drsObject = fileAccessObjectId.getDrsObject();
        this.path = fileAccessObjectId.getPath();
    }

    @JsonIgnore
    public FileAccessObjectId getId() {
        return new FileAccessObjectId(drsObject, path);
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
