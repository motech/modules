package org.motechproject.openmrs.domain;


import java.util.Objects;

/**
 * Represents a single form.
 */
public class Form {

    private String uuid;
    private String name;
    private String description;
    private String version;
    private boolean published;
    private boolean retired;

    public Form(){
    }

    public Form(String uuid, String name, String description, String version, boolean published, boolean retired) {
        this.uuid = uuid;
        this.name = name;
        this.description = description;
        this.version = version;
        this.published = published;
        this.retired = retired;
    }

    public Form(String name, String version) {
        this(null, name, null, version, false, false);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public boolean isRetired() {
        return retired;
    }

    public void setRetired(boolean retired) {
        this.retired = retired;
    }

    public boolean isPublished() {
        return published;
    }

    public void setPublished(boolean published) {
        this.published = published;
    }

    @Override
    public int hashCode() {
        return Objects.hash(uuid, name, description, version, published, retired);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Form form = (Form) o;
        return Objects.equals(uuid, form.uuid) &&
                Objects.equals(name, form.name) &&
                Objects.equals(description, form.description) &&
                Objects.equals(version, form.version) &&
                Objects.equals(published, form.published) &&
                Objects.equals(retired, form.retired);
    }
}
