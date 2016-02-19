package org.motechproject.openmrs19.domain;

import java.util.Objects;

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

    @Override
    public int hashCode() {
        return Objects.hash(providerId, person, identifier);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }

        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }

        OpenMRSProvider other = (OpenMRSProvider) obj;

        return Objects.equals(this.providerId, other.providerId) &&
                Objects.equals(this.person, other.person) &&
                Objects.equals(this.identifier, other.identifier);
    }
}
