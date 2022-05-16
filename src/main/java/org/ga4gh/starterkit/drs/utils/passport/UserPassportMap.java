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
        System.out.println("***");
        System.out.println("DECODING THE USER'S JWT");

        for (String rawPassportJwt : passports) {
            UserPassport userPassport = new UserPassport();
            userPassport.setPassportJwt(rawPassportJwt);

            DecodedJWT decodedPassportJwt = JWT.decode(rawPassportJwt);
            String passportIss = decodedPassportJwt.getClaim("iss").asString();
            
            System.out.println("what is the passport-level iss?");
            System.out.println(passportIss);

            String[] rawVisaJwts = decodedPassportJwt.getClaim("ga4gh_passport_v1").asArray(String.class);
            for (String rawVisaJwt : rawVisaJwts) {
                DecodedJWT decodedVisaJwt = JWT.decode(rawVisaJwt);
                String visaIss = decodedPassportJwt.getClaim("iss").asString();

                System.out.println("-");
                System.out.println(rawVisaJwt);

            }


            map.put(passportIss, userPassport);
        }
        System.out.println("***");
    }
}
