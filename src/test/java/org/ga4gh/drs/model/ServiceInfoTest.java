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

        public TestCase(String id, String name, String description, String contactUrl,
            String documentationUrl, LocalDateTime createdAt, LocalDateTime updatedAt,
            String environment, String version, ServiceType serviceType, 
            Organization organization) {

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
        }
    }

    @DataProvider(name = "cases")
    public Object[][] getData() {
        return new Object[][] {
            {new TestCase(
                "foobar",
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null
            )}
        };
    }

    private void assertions(ServiceInfo serviceInfo, TestCase testCase) {
        Assert.assertEquals(serviceInfo.getId(), testCase.id);
        Assert.assertEquals(serviceInfo.getName(), testCase.name);
    }

    @Test(dataProvider = "cases")
    public void testServiceInfoNoArgsConstructor(TestCase testCase) {
        ServiceInfo serviceInfo = new ServiceInfo();
        serviceInfo.setId(testCase.id);
        assertions(serviceInfo, testCase);

        

    }

    @Test(dataProvider = "cases")
    public void testServiceInfoAllArgsConstructor(TestCase testCase) {

    }
    
}
