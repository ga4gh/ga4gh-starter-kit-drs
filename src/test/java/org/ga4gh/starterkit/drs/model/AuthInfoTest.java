package org.ga4gh.starterkit.drs.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class AuthInfoTest {

    @Test
    public void testAuthInfoDefaultConstructor() {
        AuthInfo authInfo = new AuthInfo();
        Assert.assertNotNull(authInfo.getSupportedTypes());
        Assert.assertTrue(authInfo.getSupportedTypes().isEmpty());
        Assert.assertNotNull(authInfo.getPassportAuthIssuers());
        Assert.assertTrue(authInfo.getPassportAuthIssuers().isEmpty());
    }

    @DataProvider(name = "cases")
    public Object[][] getData() {
        AuthIssuer issuer1 = new AuthIssuer();
        issuer1.setBrokerUrl("https://broker.example.org");
        issuer1.setVisaName("ResearcherVisa");
        issuer1.setVisaIssuer("https://issuer.example.org");

        AuthIssuer issuer2 = new AuthIssuer();
        issuer2.setBrokerUrl("https://auth.sample.org");
        issuer2.setVisaName("ControlledAccessVisa");
        issuer2.setVisaIssuer("https://token-issuer.sample.org");

        return new Object[][] {
            {
                new ArrayList<>(Arrays.asList(AuthType.BearerAuth, AuthType.PassportAuth)),
                new ArrayList<>(Arrays.asList(issuer1, issuer2))
            },
            {
                new ArrayList<>(Arrays.asList(AuthType.BasicAuth)),
                new ArrayList<AuthIssuer>()
            },
            {
                new ArrayList<AuthType>(),
                new ArrayList<>(Arrays.asList(issuer1))
            }
        };
    }

    @Test(dataProvider = "cases")
    public void testAuthInfoSettersAndGetters(List<AuthType> supportedTypes, List<AuthIssuer> passportAuthIssuers) {
        AuthInfo authInfo = new AuthInfo();
        authInfo.setSupportedTypes(supportedTypes);
        authInfo.setPassportAuthIssuers(passportAuthIssuers);

        Assert.assertEquals(authInfo.getSupportedTypes(), supportedTypes);
        Assert.assertEquals(authInfo.getPassportAuthIssuers(), passportAuthIssuers);
    }

    @Test
    public void testAuthInfoAddElementsToDefaultLists() {
        AuthInfo authInfo = new AuthInfo();

        authInfo.getSupportedTypes().add(AuthType.None);
        Assert.assertEquals(authInfo.getSupportedTypes().size(), 1);
        Assert.assertEquals(authInfo.getSupportedTypes().get(0), AuthType.None);

        AuthIssuer issuer = new AuthIssuer();
        issuer.setBrokerUrl("https://passport.broker.org");
        authInfo.getPassportAuthIssuers().add(issuer);
        Assert.assertEquals(authInfo.getPassportAuthIssuers().size(), 1);
        Assert.assertEquals(authInfo.getPassportAuthIssuers().get(0).getBrokerUrl(), "https://passport.broker.org");
    }
}
