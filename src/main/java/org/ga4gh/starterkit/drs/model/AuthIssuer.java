package org.ga4gh.starterkit.drs.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonView;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import org.ga4gh.starterkit.drs.utils.SerializeView;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class AuthIssuer {

    @JsonView(SerializeView.Always.class)
    private String brokerUrl;

    @JsonView(SerializeView.Always.class)
    private String visaName;
    
    @JsonView(SerializeView.Always.class)
    private String visaIssuer;
}
