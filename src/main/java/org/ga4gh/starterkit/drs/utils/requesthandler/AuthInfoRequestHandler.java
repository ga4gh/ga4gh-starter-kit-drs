package org.ga4gh.starterkit.drs.utils.requesthandler;

import org.ga4gh.starterkit.common.exception.ResourceNotFoundException;
import org.ga4gh.starterkit.common.requesthandler.RequestHandler;
import org.ga4gh.starterkit.drs.model.AuthInfo;
import org.ga4gh.starterkit.drs.model.AuthIssuer;
import org.ga4gh.starterkit.drs.model.AuthType;
import org.ga4gh.starterkit.drs.model.DrsObject;
import org.ga4gh.starterkit.drs.model.PassportVisa;
import org.ga4gh.starterkit.drs.utils.hibernate.DrsHibernateUtil;
import org.springframework.beans.factory.annotation.Autowired;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class AuthInfoRequestHandler implements RequestHandler<AuthInfo> {
    
    @Autowired
    private DrsHibernateUtil hibernateUtil;

    private String objectId;

    public AuthInfoRequestHandler prepare(String objectId) {
        this.objectId = objectId;
        return this;
    }

    public AuthInfo handleRequest() {
        AuthInfo authInfo = new AuthInfo();

        DrsObject drsObject = hibernateUtil.loadDrsObject(objectId, false);
        if (drsObject == null) {
            throw new ResourceNotFoundException("No DrsObject found by id: " + objectId);
        }

        if (drsObject.getPassportVisas().size() == 0) {
            authInfo.getSupportedTypes().add(AuthType.None);
        } else {
            authInfo.getSupportedTypes().add(AuthType.PassportAuth);

            for (PassportVisa visa : drsObject.getPassportVisas()) {
                AuthIssuer issuer = new AuthIssuer();
                issuer.setBrokerUrl(visa.getPassportBroker().getId());
                issuer.setVisaName(visa.getName());
                issuer.setVisaIssuer(visa.getIssuer());
                authInfo.getPassportAuthIssuers().add(issuer);
            }
        }
        System.out.println("what is auth info??");
        System.out.println(authInfo.getSupportedTypes().size());
        System.out.println(authInfo.getSupportedTypes().get(0));
        return authInfo;
    }
}
