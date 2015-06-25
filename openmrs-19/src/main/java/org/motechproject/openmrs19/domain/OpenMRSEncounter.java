package org.motechproject.openmrs19.domain;

import org.joda.time.DateTime;

import java.util.Date;
import java.util.Set;

/**
 * Represents a single OpenMRS encounter. An encounter is a single, specific interaction between the patient and a
 * provider. It can be any interaction and includes doctor visits, laboratory tests, food distribution, home visits,
 * counselor appointments, etc. It's a part of the MOTECH model.
 */
public class OpenMRSEncounter {

    private String id;
    private OpenMRSProvider provider;
    private OpenMRSUser creator;
    private OpenMRSFacility facility;
    private Date date;
    private Set<? extends OpenMRSObservation> observations;
    private OpenMRSPatient patient;
    private String encounterType;

    /**
     * Default constructor.
     */
    public OpenMRSEncounter() {
    }

    /**
     * Creates an encounter of type {@code encounterType} that took place in the {@code facility} on the {@code date}.
     * The encounter was held by the {@code provider} and its subject was the given {@code patient}. During the
     * encounter the information about the given {@code observations} were recorder and all the details were entered by
     * the user {@code creator}.
     *
     * @param provider  the staff who provides information
     * @param creator  the staff who enters the details into the OpenMRS system
     * @param facility  the location of the encounter
     * @param date  the date of the encounter
     * @param patient  the patient involved in the encounter
     * @param observations  the observations collected during the encounter
     * @param encounterType the type of the encounter.
     */
    public OpenMRSEncounter(OpenMRSProvider provider, OpenMRSUser creator, OpenMRSFacility facility, Date date,
        OpenMRSPatient patient, Set<? extends OpenMRSObservation> observations, String encounterType) {
        this.creator = creator;
        this.provider = provider;
        this.facility = facility;
        this.date = date;
        this.patient = patient;
        this.observations = observations;
        this.encounterType = encounterType;
    }

    public OpenMRSUser getCreator() {
        return creator;
    }

    public OpenMRSProvider getProvider() {
        return provider;
    }

    public OpenMRSFacility getFacility() {
        return facility;
    }

    public DateTime getDate() {
        return (date != null) ? new DateTime(date) : null;
    }

    public OpenMRSPatient getPatient() {
        return patient;
    }

    public Set<? extends OpenMRSObservation> getObservations() {
        return observations;
    }

    public String getEncounterType() {
        return encounterType;
    }

    @Deprecated
    public String getId() {
        return id;
    }

    public OpenMRSEncounter updateWithoutObs(OpenMRSEncounter fromEncounter) {
        this.patient = fromEncounter.getPatient();
        this.creator = fromEncounter.getCreator();
        this.provider = fromEncounter.getProvider();
        this.facility = fromEncounter.getFacility();
        if (fromEncounter.getDate() != null) {
            this.date = fromEncounter.getDate().toDate();
        }
        this.encounterType = fromEncounter.getEncounterType();
        return this;
    }

    public static class OpenMRSEncounterBuilder {
        private OpenMRSProvider provider;
        private OpenMRSUser creator;
        private OpenMRSFacility facility;
        private Date date;
        private OpenMRSPatient patient;
        private Set<? extends OpenMRSObservation> observations;
        private String encounterType;
        private String id;

        public OpenMRSEncounterBuilder withProvider(OpenMRSProvider provider) {
            this.provider = provider;
            return this;
        }

        public OpenMRSEncounterBuilder withCreator(OpenMRSUser creator) {
            this.creator = creator;
            return this;
        }

        public OpenMRSEncounterBuilder withFacility(OpenMRSFacility facility) {
            this.facility = facility;
            return this;
        }

        public OpenMRSEncounterBuilder withDate(Date date) {
            this.date = date;
            return this;
        }

        public OpenMRSEncounterBuilder withPatient(OpenMRSPatient patient) {
            this.patient = patient;
            return this;
        }

        public OpenMRSEncounterBuilder withObservations(Set<? extends OpenMRSObservation> observations) {
            this.observations = observations;
            return this;
        }

        public OpenMRSEncounterBuilder withEncounterType(String encounterType) {
            this.encounterType = encounterType;
            return this;
        }

        public OpenMRSEncounterBuilder withId(String id) {
            this.id = id;
            return this;
        }

        public OpenMRSEncounterBuilder withProviderId(String providerId) {
            this.provider = new OpenMRSProvider();
            this.provider.setProviderId(providerId);
            return this;
        }

        public OpenMRSEncounterBuilder withCreatorId(String creatorId) {
            this.creator = new OpenMRSUser().id(creatorId);
            return this;
        }

        public OpenMRSEncounterBuilder withFacilityId(String facilityId) {
            this.facility = new OpenMRSFacility(facilityId);
            return this;
        }

        public OpenMRSEncounterBuilder withPatientId(String patientId) {
            this.patient = new OpenMRSPatient(patientId);
            return this;
        }

        public OpenMRSEncounter build() {
            OpenMRSEncounter mrsEncounter = new OpenMRSEncounter(provider, creator, facility, date, patient, observations, encounterType);
            mrsEncounter.id = this.id;
            return mrsEncounter;
        }
    }

    public String getEncounterId() {
        return id;
    }

    public void setEncounterId(String encounterId) {
        this.id = encounterId;
    }

    public void setProvider(OpenMRSProvider provider) {
        this.provider = provider;
    }

    public void setCreator(OpenMRSUser creator) {
        this.creator = creator;
    }

    public void setFacility(OpenMRSFacility facility) {
        this.facility = facility;
    }

    public void setDate(DateTime date) {
        this.date = date.toDate();
    }

    public void setPatient(OpenMRSPatient patient) {
        this.patient = patient;
    }

    public void setEncounterType(String encounterType) {
        this.encounterType = encounterType;
    }

    public void setObservations(Set<? extends OpenMRSObservation> observations) {
        this.observations = observations;
    }
}
