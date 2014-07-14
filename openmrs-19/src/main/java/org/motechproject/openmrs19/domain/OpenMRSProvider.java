package org.motechproject.openmrs19.domain;

public class OpenMRSProvider {

    private String providerId;
    private OpenMRSPerson person;

    public OpenMRSProvider(OpenMRSPerson person) {
        this.person = person;
    }

    public OpenMRSProvider() {
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

}
