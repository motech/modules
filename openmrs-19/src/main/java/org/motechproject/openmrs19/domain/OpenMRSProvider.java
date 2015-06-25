package org.motechproject.openmrs19.domain;

/**
 * Represents a single provider. Provider is a clinician responsible for providing care to a patient. It's part of the
 * MOTECH model.
 */
public class OpenMRSProvider {

    private String providerId;
    private OpenMRSPerson person;
    private String identifier;

    /**
     * Default constructor.
     */
    public OpenMRSProvider() {
        this(null);
    }

    /**
     * Creates a provider based on the given {@code person} details.
     *
     * @param person  the personal information about the provider
     */
    public OpenMRSProvider(OpenMRSPerson person) {
        this.person = person;
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
