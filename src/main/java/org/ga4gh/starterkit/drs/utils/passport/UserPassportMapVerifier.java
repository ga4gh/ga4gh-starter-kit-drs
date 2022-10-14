package org.ga4gh.starterkit.drs.utils.passport;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;

import org.ga4gh.starterkit.common.exception.BadRequestException;
import org.ga4gh.starterkit.drs.exception.UnauthorizedException;
import org.ga4gh.starterkit.drs.model.PassportBroker;
import org.ga4gh.starterkit.drs.model.PassportVisa;
import org.ga4gh.starterkit.drs.utils.hibernate.DrsHibernateUtil;
import org.ga4gh.starterkit.common.util.logging.LoggingUtil;
import org.springframework.beans.factory.annotation.Autowired;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class UserPassportMapVerifier {

    @Autowired
    private DrsHibernateUtil hibernateUtil;

    @Autowired
    private LoggingUtil loggingUtil;

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
                    String exceptionMessage = "Passports from issuer: '" + passportIssKey + "' are not accepted here.";
                    loggingUtil.error("Exception occurred: passport broker is null " + exceptionMessage);
                    throw new Exception(exceptionMessage);
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
                        String exceptionMessage = "The Visa you provided: '" + visaKey + "' is not accepted here.";
                        loggingUtil.error("Exception occurred: registered visa is null " + exceptionMessage);
                        throw new Exception(exceptionMessage);
                    }
                    String visaSecret = registeredVisa.getSecret();
                    JWTVerifier visaVerifier = JWT.require(Algorithm.HMAC256(visaSecret)).build();
                    visaVerifier.verify(rawVisaJwt);
                }
            }
        } catch (Exception ex) {
            String message = "Invalid Passport(s), message: " + ex.getMessage();
            loggingUtil.error("Exception occurred: " + message);
            throw new UnauthorizedException(message);
        }
    }
}
