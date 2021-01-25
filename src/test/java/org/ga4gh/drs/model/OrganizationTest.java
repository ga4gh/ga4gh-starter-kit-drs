package org.ga4gh.drs.model;

import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class OrganizationTest {

    @DataProvider(name = "cases")
    public Object[][] getData() {
        return new Object[][] {
            {"ga4gh", "https://genomicsandhealth.org"},
            {"ebi", "https://ebi.ac.uk"},
            {"broadinstitute", "https://broadinstitute.org"}
        };
    }

    private void assertions(Organization organization, String name, String url) {
        Assert.assertEquals(organization.getName(), name);
        Assert.assertEquals(organization.getUrl(), url);
    }

    @Test(dataProvider = "cases")
    public void testOrganizationNoArgsConstructor(String name, String url) {
        Organization organization = new Organization();
        organization.setName(name);
        organization.setUrl(url);
        assertions(organization, name, url);
    }

    @Test(dataProvider = "cases")
    public void testOrganizationAllArgsConstructor(String name, String url) {
        Organization organization = new Organization(name, url);
        assertions(organization, name, url);

    }
    
}
