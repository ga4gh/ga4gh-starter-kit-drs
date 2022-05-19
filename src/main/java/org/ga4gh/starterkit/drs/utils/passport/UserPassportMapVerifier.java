package org.ga4gh.starterkit.drs.utils.passport;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;

import org.ga4gh.starterkit.common.exception.BadRequestException;
import org.ga4gh.starterkit.drs.exception.UnauthorizedException;
import org.ga4gh.starterkit.drs.model.PassportBroker;
import org.ga4gh.starterkit.drs.model.PassportVisa;
import org.ga4gh.starterkit.drs.utils.hibernate.DrsHibernateUtil;
import org.springframework.beans.factory.annotation.Autowired;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class UserPassportMapVerifier {

    @Autowired
    private DrsHibernateUtil hibernateUtil;

    public void verifyAll(UserPassportMap userPassportMap) {
        try {
            for (String passportIssKey : userPassportMap.getMap().keySet()) {
                // verify the passport-level JWT
                // first find the secret from the DB
                // then use it to verify
                UserPassport userPassport = userPassportMap.getMap().get(passportIssKey);
                String rawPassportJwt = userPassport.getPassportJwt();
                PassportBroker passportBroker = hibernateUtil.readEntityObject(PassportBroker.class, passportIssKey, false);
                if (passportBroker == null) {
                    throw new Exception("Passports from issuer: '" + passportIssKey + "' are not accepted here.");
                }
                String passportBrokerSecret = passportBroker.getSecret();
                JWTVerifier passportVerifier = JWT.require(Algorithm.HMAC256(passportBrokerSecret)).build();
                passportVerifier.verify(rawPassportJwt);

                for (String visaKey : userPassport.getVisaJwtMap().keySet()) {
                    String rawVisaJwt = userPassport.getVisaJwtMap().get(visaKey);
                    String visaName = visaKey.split("@")[0];
                    String visaIssuer = visaKey.split("@")[1];
                    PassportVisa registeredVisa = hibernateUtil.findPassportVisa(visaName, visaIssuer);
                    if (registeredVisa == null) {
                        throw new Exception("The Visa you provided: '" + visaKey + "' is not accepted here.");
                    }
                    String visaSecret = registeredVisa.getSecret();
                    JWTVerifier visaVerifier = JWT.require(Algorithm.HMAC256(visaSecret)).build();
                    visaVerifier.verify(rawVisaJwt);
                }
            }
        } catch (Exception ex) {
            String message = "Invalid Passport(s), message: " + ex.getMessage();
            throw new UnauthorizedException(message);
        }
    }
}
