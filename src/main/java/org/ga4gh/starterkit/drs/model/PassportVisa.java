package org.ga4gh.starterkit.drs.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonView;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import org.ga4gh.starterkit.common.hibernate.HibernateEntity;
import org.ga4gh.starterkit.drs.utils.SerializeView;
import org.hibernate.Hibernate;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "passport_visa")
@Setter
@Getter
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class PassportVisa implements HibernateEntity<String> {

    @Id
    @Column(name = "id", updatable = false, nullable = false)
    @NonNull
    @JsonView(SerializeView.Admin.class)
    private String id;

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
