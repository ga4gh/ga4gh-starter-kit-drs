package org.ga4gh.starterkit.drs.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonView;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import org.ga4gh.starterkit.common.hibernate.HibernateEntity;
import org.ga4gh.starterkit.drs.utils.SerializeView;
import org.hibernate.Hibernate;
import org.springframework.lang.NonNull;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "passport_broker")
@Setter
@Getter
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class PassportBroker implements HibernateEntity<String> {

    @Id
    @Column(name = "url", updatable = false, nullable = false)
    @NonNull
    @JsonView(SerializeView.Admin.class)
    private String url;

    @Column(name = "secret")
    @JsonView(SerializeView.Admin.class)
    private String secret;

    @OneToMany(
        mappedBy = "passportBroker",
        fetch = FetchType.LAZY,
        cascade = {CascadeType.ALL},
        orphanRemoval = true
    )
    @JsonView(SerializeView.Never.class)
    private List<PassportVisa> passportVisas;

    public PassportBroker() {
        passportVisas = new ArrayList<>();
    }

    public void setId(String url) {
        this.url = url;
    }

    public String getId() {
        return url;
    }

    public void loadRelations() {
        Hibernate.initialize(getPassportVisas());
    }
}
