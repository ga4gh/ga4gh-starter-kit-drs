package org.ga4gh.starterkit.drs.utils.passport;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.auth0.jwt.JWT;
import com.auth0.jwt.interfaces.DecodedJWT;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class UserPassportMap {

    private Map<String, UserPassport> map;

    public UserPassportMap(List<String> passports) {
        map = new HashMap<>();

        for (String rawPassportJwt : passports) {
            UserPassport userPassport = new UserPassport();
            userPassport.setPassportJwt(rawPassportJwt);

            DecodedJWT decodedPassportJwt = JWT.decode(rawPassportJwt);
            String passportIss = decodedPassportJwt.getClaim("iss").asString();
            
            String[] containedVisas = decodedPassportJwt.getClaim("contained_visas").asArray(String.class);
            String[] rawVisaJwts = decodedPassportJwt.getClaim("ga4gh_passport_v1").asArray(String.class);
            for (int i = 0; i < containedVisas.length; i++) {
                String containedVisa = containedVisas[i];
                String rawVisaJwt = rawVisaJwts[i];
                userPassport.getVisaJwtMap().put(containedVisa, rawVisaJwt);
            }
            map.put(passportIss, userPassport);
        }
    }

    public void verifyAllJwts() {

    }
}
