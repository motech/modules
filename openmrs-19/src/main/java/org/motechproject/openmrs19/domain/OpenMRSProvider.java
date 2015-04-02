package org.motechproject.openmrs19.domain;

public class OpenMRSProvider {

    private String providerId;
    private OpenMRSPerson person;
    private String identifier;

    public OpenMRSProvider(OpenMRSPerson person) {
        this.person = person;
    }

    public OpenMRSProvider() {
        this(null);
    }

    public OpenMRSPerson getPerson() {
        return person;
    }

    public void setPerson(OpenMRSPerson person) {
        this.person = person;
    }

    public String getProviderId() {
        return providerId;
    }

    public void setProviderId(String providerId) {
        this.providerId = providerId;
    }

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }
}
