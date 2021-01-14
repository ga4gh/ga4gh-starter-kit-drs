package org.ga4gh.drs.model;

import java.util.Date;

public class ServiceInfo {

    private String id;
    private String name;
    private String description;
    private String contactUrl;
    private String documentationUrl;
    private Date createdAt;
    private Date updatedAt;
    private String environment;
    private String version;
    private ServiceType type;
    private Organization organization;

    public ServiceInfo() {

    }

    public ServiceInfo(String id, String name, String description, 
        String contactUrl, String documentationUrl, Date createdAt,
        Date updatedAt, String environment, String version, ServiceType type,
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
        this.type = type;
        this.organization = organization;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public void setContactUrl(String contactUrl) {
        this.contactUrl = contactUrl;
    }

    public String getContactUrl() {
        return contactUrl;
    }

    public void setDocumentationUrl(String documentationUrl) {
        this.documentationUrl = documentationUrl;
    }

    public String getDocumentationUrl() {
        return documentationUrl;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setEnvironment(String environment) {
        this.environment = environment;
    }

    public String getEnvironment() {
        return environment;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getVersion() {
        return version;
    }

    public void setType(ServiceType type) {
        this.type = type;
    }

    public ServiceType getType() {
        return type;
    }

    public void setOrganization(Organization organization) {
        this.organization = organization;
    }

    public Organization getOrganization() {
        return organization;
    }

    @Override
    public String toString() {
        return "ServiceInfo ["
            + "id=" + id + ", "
            + "name=" + name + ", "
            + "description=" + description + ", "
            + "contactUrl=" + contactUrl + ", "
            + "documentationUrl=" + documentationUrl + ", "
            + "environment=" + environment + ", "
            + "version=" + version + "]";
    }
}