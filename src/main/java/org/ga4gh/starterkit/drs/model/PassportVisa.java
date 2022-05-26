package org.ga4gh.starterkit.drs.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonView;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import org.ga4gh.starterkit.common.hibernate.HibernateEntity;
import org.ga4gh.starterkit.drs.utils.SerializeView;
import org.hibernate.Hibernate;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

@Entity
@Table(name = "passport_visa")
@Setter
@Getter
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class PassportVisa implements HibernateEntity<Integer> {

    @Id
    @Column(name = "id", updatable = false, nullable = false)
    @NonNull
    @JsonView(SerializeView.Admin.class)
    private Integer id;

    @Column(name = "name")
    @JsonView(SerializeView.Admin.class)
    private String name;

    @Column(name = "issuer")
    @JsonView(SerializeView.Admin.class)
    private String issuer;

    @Column(name = "secret")
    @JsonView(SerializeView.Admin.class)
    private String secret;

    @ManyToOne(
        fetch = FetchType.EAGER,
        cascade = {CascadeType.PERSIST, CascadeType.MERGE,
                   CascadeType.DETACH, CascadeType.REFRESH}
    )
    @JoinColumn(name = "passport_broker_url")
    @JsonView(SerializeView.Admin.class)
    private PassportBroker passportBroker;

    @ManyToMany
    @JoinTable(
        name = "drs_object_visa",
        joinColumns = {@JoinColumn(name = "visa_id")},
        inverseJoinColumns = {@JoinColumn(name = "drs_object_id")}
    )
    @JsonView(SerializeView.Never.class)
    private List<DrsObject> drsObjects;

    public PassportVisa() {
        drsObjects = new ArrayList<>();
    }

    public void loadRelations() {
        Hibernate.initialize(getDrsObjects());
    }
}
