package org.ga4gh.starterkit.drs.model;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonView;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import org.ga4gh.starterkit.drs.utils.SerializeView;

import lombok.Getter;
import lombok.Setter;

// Authorization information for a single DRS object
@Setter
@Getter
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class AuthInfo {

    @JsonView(SerializeView.Always.class)
    private List<AuthType> supportedTypes;

    @JsonView(SerializeView.Always.class)
    private List<AuthIssuer> passportAuthIssuers;

    public AuthInfo() {
        supportedTypes = new ArrayList<>();
        passportAuthIssuers = new ArrayList<>();
    }
}
