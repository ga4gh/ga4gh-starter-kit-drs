package org.ga4gh.drs.model;

import java.time.LocalDateTime;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class ServiceInfoTest {

    private class TestCase {

        protected String id;
        protected String name;
        protected String description;
        protected String contactUrl;
        protected String documentationUrl;
        protected LocalDateTime createdAt;
        protected LocalDateTime updatedAt;
        protected String environment;
        protected String version;
        protected ServiceType serviceType;
        protected Organization organization;
        protected String toString;

        public TestCase(String id, String name, String description, String contactUrl,
            String documentationUrl, LocalDateTime createdAt, LocalDateTime updatedAt,
            String environment, String version, ServiceType serviceType, 
            Organization organization, String toString) {

            this.id = id;
            this.name = name;
            this.description = description;
            this.contactUrl = contactUrl;
            this.documentationUrl = documentationUrl;
            this.createdAt = createdAt;
            this.updatedAt = updatedAt;
            this.environment = environment;
            this.version = version;
            this.serviceType = serviceType;
            this.organization = organization;
            this.toString = toString;
        }
    }

    @DataProvider(name = "cases")
    public Object[][] getData() {
        return new Object[][] {
            {new TestCase(
                "org.ga4gh.service",
                "GA4GH API",
                "This is a GA4GH API...",
                "mailto:nobody@ga4gh.org",
                "https://ga4gh.org",
                LocalDateTime.now(),
                LocalDateTime.now(),
                "production",
                "12.1.1",
                new ServiceType(),
                new Organization("GA4GH", "https://ga4gh.org"),
                "ServiceInfo [id=org.ga4gh.service, name=GA4GH API, description=This is a GA4GH API..., contactUrl=mailto:nobody@ga4gh.org, documentationUrl=https://ga4gh.org, environment=production, version=12.1.1]"
            )},
            {new TestCase(
                "org.broadinstitute",
                "GA4GH API, Broad implementation",
                "This is a GA4GH API implemented by Broad...",
                "mailto:nobody@broadinstitute.org",
                "https://broadinstitute.org",
                LocalDateTime.now(),
                LocalDateTime.now(),
                "staging",
                "13.12.11",
                new ServiceType(),
                new Organization("Broad", "https://broadinstitute.org"),
                "ServiceInfo [id=org.broadinstitute, name=GA4GH API, Broad implementation, description=This is a GA4GH API implemented by Broad..., contactUrl=mailto:nobody@broadinstitute.org, documentationUrl=https://broadinstitute.org, environment=staging, version=13.12.11]"
            )}
        };
    }

    private void assertions(ServiceInfo serviceInfo, TestCase testCase) {
        Assert.assertEquals(serviceInfo.getId(), testCase.id);
        Assert.assertEquals(serviceInfo.getName(), testCase.name);
        Assert.assertEquals(serviceInfo.getDescription(), testCase.description);
        Assert.assertEquals(serviceInfo.getContactUrl(), testCase.contactUrl);
        Assert.assertEquals(serviceInfo.getDocumentationUrl(), testCase.documentationUrl);
        Assert.assertEquals(serviceInfo.getCreatedAt(), testCase.createdAt);
        Assert.assertEquals(serviceInfo.getUpdatedAt(), testCase.updatedAt);
        Assert.assertEquals(serviceInfo.getEnvironment(), testCase.environment);
        Assert.assertEquals(serviceInfo.getVersion(), testCase.version);
        Assert.assertEquals(serviceInfo.getType(), testCase.serviceType);
        Assert.assertEquals(serviceInfo.getOrganization(), testCase.organization);
        Assert.assertEquals(serviceInfo.toString(), testCase.toString);
    }

    @Test(dataProvider = "cases")
    public void testServiceInfoNoArgsConstructor(TestCase testCase) {
        ServiceInfo serviceInfo = new ServiceInfo();
        serviceInfo.setId(testCase.id);
        serviceInfo.setName(testCase.name);
        serviceInfo.setDescription(testCase.description);
        serviceInfo.setContactUrl(testCase.contactUrl);
        serviceInfo.setDocumentationUrl(testCase.documentationUrl);
        serviceInfo.setCreatedAt(testCase.createdAt);
        serviceInfo.setUpdatedAt(testCase.updatedAt);
        serviceInfo.setEnvironment(testCase.environment);
        serviceInfo.setVersion(testCase.version);
        serviceInfo.setType(testCase.serviceType);
        serviceInfo.setOrganization(testCase.organization);
        assertions(serviceInfo, testCase);
    }

    @Test(dataProvider = "cases")
    public void testServiceInfoAllArgsConstructor(TestCase testCase) {
        ServiceInfo serviceInfo = new ServiceInfo(
            testCase.id,
            testCase.name,
            testCase.description,
            testCase.contactUrl,
            testCase.documentationUrl,
            testCase.createdAt,
            testCase.updatedAt,
            testCase.environment,
            testCase.version,
            testCase.serviceType,
            testCase.organization
        );
        assertions(serviceInfo, testCase);
    }
}
