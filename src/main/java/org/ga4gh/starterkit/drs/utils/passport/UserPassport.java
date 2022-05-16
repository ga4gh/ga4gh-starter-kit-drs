package org.ga4gh.starterkit.drs.utils.passport;

import java.util.HashMap;
import java.util.Map;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class UserPassport {

    private String passportJwt;
    private Map<String, String> visaJwtMap;

    public UserPassport() {
        visaJwtMap = new HashMap<>();
    }
}
