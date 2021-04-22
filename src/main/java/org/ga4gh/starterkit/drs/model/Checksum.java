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

@Entity
@Table(name = "drs_object_checksum")
@JsonView(SerializeView.Always.class)
public class Checksum implements HibernateEntity<Long> {

    public static final long serialVersionUID = 1L;

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonIgnore
    private Long id;

    @Column(name = "checksum")
    @NonNull
    private String checksum;

    @Column(name = "type")
    private String type;

    @ManyToOne(fetch = FetchType.EAGER,
               cascade = {CascadeType.PERSIST, CascadeType.MERGE,
                          CascadeType.DETACH, CascadeType.REFRESH})
    @JoinColumn(name = "drs_object_id", nullable = false, insertable = true, updatable = true)
    @JsonBackReference
    private DrsObject drsObject;

    public Checksum() {

    }

    public Checksum(Long id, String checksum, String type) {
        this.id = id;
        this.checksum = checksum;
        this.type = type;
    }

    public void loadRelations() {

    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setChecksum(String checksum) {
        this.checksum = checksum;
    }

    public String getChecksum() {
        return checksum;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public void setDrsObject(DrsObject drsObject) {
        this.drsObject = drsObject;
    }

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
