package org.motechproject.openmrs19.util;

import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.StringUtils;
import org.joda.time.DateTime;
import org.motechproject.openmrs19.domain.OpenMRSAttribute;
import org.motechproject.openmrs19.domain.OpenMRSConcept;
import org.motechproject.openmrs19.domain.OpenMRSConceptName;
import org.motechproject.openmrs19.domain.OpenMRSEncounterType;
import org.motechproject.openmrs19.domain.OpenMRSFacility;
import org.motechproject.openmrs19.domain.OpenMRSObservation;
import org.motechproject.openmrs19.domain.OpenMRSPatient;
import org.motechproject.openmrs19.domain.OpenMRSPerson;
import org.motechproject.openmrs19.domain.OpenMRSProvider;
import org.motechproject.openmrs19.domain.OpenMRSUser;
import org.motechproject.openmrs19.resource.model.Attribute;
import org.motechproject.openmrs19.resource.model.Concept;
import org.motechproject.openmrs19.resource.model.ConceptListResult;
import org.motechproject.openmrs19.resource.model.Encounter;
import org.motechproject.openmrs19.resource.model.Identifier;
import org.motechproject.openmrs19.resource.model.IdentifierType;
import org.motechproject.openmrs19.resource.model.Location;
import org.motechproject.openmrs19.resource.model.Observation;
import org.motechproject.openmrs19.resource.model.Patient;
import org.motechproject.openmrs19.resource.model.Person;
import org.motechproject.openmrs19.resource.model.Person.PreferredAddress;
import org.motechproject.openmrs19.resource.model.Person.PreferredName;
import org.motechproject.openmrs19.resource.model.Provider;
import org.motechproject.openmrs19.resource.model.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Utility class for converting entities between the MOTECH and the OpenMRS models. The classes from the  MOTECH model
 * are used by this module, while the classes from the OpenMRS model are used for passing/retrieving data to/from the
 * OpenMRS server.
 */
public final class ConverterUtils {

    /**
     * Utility class, should not be instantiated.
     */
    private ConverterUtils() {
    }

    /**
     * Converts the given person, represented as the OpenMRS model, to the MOTECH model used by this module.
     *
     * @param person  the person(represented as the OpenMRS model) to be converted
     * @return the person converted to the MOTECH module used by this module
     */
    public static OpenMRSPerson toOpenMRSPerson(Person person) {
        OpenMRSPerson converted = new OpenMRSPerson();
        converted.setId(person.getUuid());
        converted.setDisplay(person.getDisplay());
        PreferredName personName = person.getPreferredName();
        if (personName != null) {
            converted.setFirstName(personName.getGivenName());
            converted.setMiddleName(personName.getMiddleName());
            converted.setLastName(personName.getFamilyName());
        }

        converted.setGender(person.getGender());

        if (person.getPreferredAddress() != null) {
            converted.setAddress(person.getPreferredAddress().getAddress1());
        }

        if (person.getBirthdate() != null) {
            converted.setDateOfBirth(new DateTime(person.getBirthdate()));
        }

        if (person.getDeathDate() != null) {
            converted.setDeathDate(new DateTime(person.getDeathDate()));
        }

        converted.setDead(person.isDead());
        converted.setAge(person.getAge());
        converted.setBirthDateEstimated(person.isBirthdateEstimated());

        if (person.getAuditInfo() != null) {
            if (person.getAuditInfo().getDateCreated() != null) {
                converted.setDateCreated(new DateTime(person.getAuditInfo().getDateCreated()));
            }

            if (person.getAuditInfo().getDateChanged() != null) {
                converted.setDateChanged(new DateTime(person.getAuditInfo().getDateChanged()));
            }
        }

        if (person.getAttributes() != null) {
            for (Attribute attr : person.getAttributes()) {
                // extract name/value from the display property
                // there is no explicit property for name attribute
                // the display attribute is formatted as: name = value
                String display = attr.getDisplay();
                int index = display.indexOf('=');
                String name = display.substring(0, index).trim();

                converted.addAttribute(new OpenMRSAttribute(name, attr.getValue()));
            }
        }

        return converted;
    }

    /**
     * Converts the given person, represented as the MOTECH model, to the model used by the OpenMRS server.
     *
     * @param person  the person(represented as the OpenMRS model) to be converted
     * @param includeNames  defines if the person names and address should be included in the converted object
     * @return the person converted to the model used by the OpenMRS server
     */
    public static Person toPerson(OpenMRSPerson person, boolean includeNames) {
        Person converted = new Person();
        converted.setUuid(person.getPersonId());
        if (person.getDateOfBirth() != null) {
            converted.setBirthdate(person.getDateOfBirth().toDate());
        }
        if (person.getDeathDate() != null) {
            converted.setDeathDate(person.getDeathDate().toDate());
        }
        converted.setBirthdateEstimated((Boolean) ObjectUtils.defaultIfNull(person.getBirthDateEstimated(), false));
        converted.setDead((Boolean) ObjectUtils.defaultIfNull(person.getDead(), false));
        if (person.getCauseOfDeath() != null) {
            converted.setCauseOfDeath(toConcept(person.getCauseOfDeath()));
        }
        converted.setGender(person.getGender());

        if (includeNames) {
            PreferredName name = new PreferredName();
            name.setGivenName(person.getFirstName());
            name.setMiddleName(person.getMiddleName());
            name.setFamilyName(person.getLastName());
            List<PreferredName> names = new ArrayList<PreferredName>();
            names.add(name);
            converted.setNames(names);

            PreferredAddress address = new PreferredAddress();
            address.setAddress1(person.getAddress());
            List<PreferredAddress> addresses = new ArrayList<PreferredAddress>();
            addresses.add(address);
            converted.setAddresses(addresses);
        }

        return converted;
    }

    /**
     * Converts the given location, represented as the OpenMRS model, to the MOTECH model of the facility used by this
     * module.
     *
     * @param location  the location(represented as OpenMRS model) to be converted
     * @return the location converted to the MOTECH model of the facility used by this module
     */
    public static OpenMRSFacility toOpenMRSFacility(Location location) {
        return new OpenMRSFacility(location.getUuid(), location.getDisplay(), location.getName(), location.getCountry(), location.getAddress6(),
                location.getCountyDistrict(), location.getStateProvince());
    }

    /**
     * Converts the given facility, represented as the MOTECH model, to the OpenMRS model of location used by the
     * OpenMRS server.
     *
     * @param facility  the facility(represented as Motech model) to be converted
     * @return the facility converted to the OpenMRS model of the location used by the OpenMRS server
     */
    public static Location toLocation(OpenMRSFacility facility) {
        Location location = new Location();
        location.setAddress6(facility.getRegion());
        location.setDescription(facility.getName());
        location.setCountry(facility.getCountry());
        location.setCountyDistrict(facility.getCountyDistrict());
        location.setName(facility.getName());
        location.setStateProvince(facility.getStateProvince());
        location.setUuid(facility.getFacilityId());
        return location;
    }

    /**
     * Converts the given observation, represented as the OpenMRS, to the MOTECH model used by this module.
     *
     * @param ob  the observation(represented as the OpenMRS model) to be converted
     * @return the observation converted to the MOTECH model used by this module
     */
    public static OpenMRSObservation toOpenMRSObservation(Observation ob) {
        OpenMRSObservation obs = new OpenMRSObservation(ob.getUuid(), ob.getObsDatetime(), ob.getConcept().getDisplay(), 
            ob.getValue().getDisplay());
        if (ob.getEncounter() != null && ob.getEncounter().getPatient() != null) {
            obs.setPatientId(ob.getEncounter().getPatient().getUuid());
        }
        return obs;
    }

    /**
     * Converts the given observation, represented as the MOTECH module, to the model used by the OpenMRS server.
     *
     * @param openMRSObservation  the observation(represented as MOTECH model) to be converted
     * @return the observation converted to the model use by the OpenMRS server
     */
    public static Observation toObservation(OpenMRSObservation openMRSObservation) {
        Observation observation = new Observation();
        observation.setUuid(openMRSObservation.getObservationId());
        observation.setObsDatetime(openMRSObservation.getDate().toDate());
        Observation.ObservationValue observationValue = new Observation.ObservationValue();
        observationValue.setDisplay(openMRSObservation.getValue().toString());
        observation.setValue(observationValue);

        Concept concept = new Concept();
        Concept.ConceptName conceptName = new Concept.ConceptName();
        conceptName.setName(openMRSObservation.getConceptName());
        concept.setName(conceptName);

        observation.setConcept(concept);

        return observation;
    }

    /**
     * Performs a deep copy of the given patient represented as the MOTECH model used by this module.
     *
     * @param patient  the patient to be copied
     * @return the copy of the given patient
     */
    public static OpenMRSPatient copyPatient(OpenMRSPatient patient) {
        OpenMRSFacility facility = patient.getFacility();
        OpenMRSFacility openMRSFacility = null;
        if (facility != null) {
            openMRSFacility = new OpenMRSFacility(facility.getFacilityId());
            openMRSFacility.setCountry(facility.getCountry());
            openMRSFacility.setCountyDistrict(facility.getCountyDistrict());
            openMRSFacility.setName(facility.getName());
            openMRSFacility.setRegion(facility.getRegion());
            openMRSFacility.setStateProvince(facility.getStateProvince());
        }

        OpenMRSPerson openMRSPerson = copyPerson(patient.getPerson());

        OpenMRSPatient openMRSPatient = new OpenMRSPatient(patient.getMotechId());
        openMRSPatient.setPatientId(patient.getPatientId());
        openMRSPatient.setFacility(openMRSFacility);
        openMRSPatient.setPerson(openMRSPerson);
        openMRSPatient.setMotechId(patient.getMotechId());
        openMRSPatient.setIdentifiers(patient.getIdentifiers());

        return openMRSPatient;
    }

    /**
     * Performs a deep copy of the given person represented as the MOTECH model used by this module.
     *
     * @param personMrs  the person to be copied
     * @return the copy of the given person
     */
    public static OpenMRSPerson copyPerson(OpenMRSPerson personMrs) {
        List<OpenMRSAttribute> attributeList = copyAttributeList(personMrs.getAttributes());

        OpenMRSPerson person = new OpenMRSPerson();
        person.setId(personMrs.getId());
        person.setAddress(personMrs.getAddress());
        person.setFirstName(personMrs.getFirstName());
        person.setLastName(personMrs.getLastName());
        person.setAge(personMrs.getAge());
        person.setBirthDateEstimated(personMrs.getBirthDateEstimated());
        person.setDateOfBirth(personMrs.getDateOfBirth());
        if (personMrs.getDead() != null) {
            person.setDead(personMrs.getDead());
        }
        person.setDeathDate(personMrs.getDeathDate());
        person.setGender(personMrs.getGender());
        person.setMiddleName(personMrs.getMiddleName());
        person.setPreferredName(personMrs.getPreferredName());
        person.setAttributes(attributeList);

        return person;
    }

    /**
     * Performs a deep copy of the given list of attributes represented as the MOTECH model used by this module.
     *
     * @param attributesMrs  the attribute list to copy
     * @return the copy of the given list
     */
    public static List<OpenMRSAttribute> copyAttributeList(List<OpenMRSAttribute> attributesMrs) {
        List<OpenMRSAttribute> attributeList = new ArrayList<>();

        if (attributesMrs != null) {
            for (OpenMRSAttribute attribute : attributesMrs) {
                attributeList.add(new OpenMRSAttribute(attribute.getName(), attribute.getValue()));
            }
        }
        return  attributeList;
    }

    /**
     * Converts the given concept, represented as the MOTECH model, to the model used by the OpenMRS server.
     *
     * @param openMRSConcept  the concept(represented as the MOTECH model) to be converted
     * @return the concept converted to the model use by the OpenMRS server
     */
    public static Concept toConcept(OpenMRSConcept openMRSConcept) {
        Concept concept = new Concept();
        concept.setUuid(openMRSConcept.getUuid());
        concept.setDisplay(openMRSConcept.getDisplay());
        concept.setName(openMRSConcept.getName() != null ? copyConceptName(openMRSConcept.getName()) : null);

        if (openMRSConcept.getNames() != null) {
            List<Concept.ConceptName> names = new ArrayList<>();
            for (OpenMRSConceptName conceptName : openMRSConcept.getNames()) {
                names.add(copyConceptName(conceptName));
            }
            concept.setNames(names);
        }

        if (openMRSConcept.getConceptClass() != null) {
            Concept.ConceptClass conceptClass = new Concept.ConceptClass();
            conceptClass.setDisplay(openMRSConcept.getConceptClass());
            concept.setConceptClass(conceptClass);
        }

        if (openMRSConcept.getDataType() != null) {
            Concept.DataType dataType = new Concept.DataType();
            dataType.setDisplay(openMRSConcept.getDataType());
            concept.setDatatype(dataType);
        }

        return concept;
    }

    /**
     * Converts the given concept, represented as the OpenMRS model, to the MOTECH model used by this module.
     *
     * @param concept  the concept(represented as the OpenMRS mode) to be converted
     * @return the concept converted to the MOTECH model use by this module
     */
    public static OpenMRSConcept toOpenMRSConcept(Concept concept) {
        OpenMRSConcept openMRSConcept = new OpenMRSConcept();

        openMRSConcept.setName(concept.getName() == null ? null : new OpenMRSConceptName(concept.getName()));
        openMRSConcept.setUuid(concept.getUuid());
        openMRSConcept.setDataType(concept.getDatatype() == null ? null : concept.getDatatype().getDisplay());
        openMRSConcept.setConceptClass(concept.getConceptClass() == null ? null : concept.getConceptClass().getDisplay());
        openMRSConcept.setDisplay(concept.getDisplay());

        if (concept.getNames() != null) {
            List<OpenMRSConceptName> names = new ArrayList<>();

            for (Concept.ConceptName conceptName : concept.getNames()) {
                names.add(new OpenMRSConceptName(conceptName));
            }

            openMRSConcept.setNames(names);
        }

        return openMRSConcept;
    }

    /**
     * Converts the given user, represented as the OpenMRS model, to the MOTECH model used by this module.
     *
     * @param user  the user(represented as the OpenMRS model) to be converted
     * @return  the user converted to the MOTECH model used by this module
     */
    public static OpenMRSUser toOpenMRSUser(User user) {

        OpenMRSUser openMRSUser = new OpenMRSUser();

        openMRSUser.setUserName(user.getUsername());
        openMRSUser.setUserId(user.getUuid());
        openMRSUser.setSecurityRole(user.getFirstRole());
        openMRSUser.setSystemId(user.getSystemId());
        openMRSUser.setPerson(user.getPerson() != null ? toOpenMRSPerson(user.getPerson()) : null);

        return openMRSUser;
    }

    public static OpenMRSPatient toOpenMRSPatient(Patient patient) {
        return toOpenMRSPatient(patient, null, null, null);
    }

    /**
     * Creates an object of the MOTECH model patient based on the given OpenMRS model patient, facility, motechID and
     * supportedIdentifierTypeList.
     *
     * @param patient  the patient(represented as OpenMRS model)
     * @param facility  the facility(represented as MOTECH model)
     * @param motechId  the MOTECH ID of the patient
     * @param supportedIdentifierTypeList  the supported patient identifier type list in MOTECH
     * @return a Patient object converted to the MOTECH model representation of Patient
     */
    public static OpenMRSPatient toOpenMRSPatient(Patient patient, OpenMRSFacility facility, String motechId,
                                                  List<Identifier> supportedIdentifierTypeList) {
        OpenMRSPatient openMRSPatient = new OpenMRSPatient(patient.getUuid());

        Map<String, String> identifiers = new HashMap<>();

        if (supportedIdentifierTypeList != null) {
            for (Identifier identifier : supportedIdentifierTypeList) {
                String identifierName = identifier.getIdentifierType().getName();
                identifiers.put(identifierName, identifier.getIdentifier());
            }
        }

        openMRSPatient.setPerson(toOpenMRSPerson(patient.getPerson()));
        openMRSPatient.setFacility(facility);
        openMRSPatient.setMotechId(motechId);
        openMRSPatient.setIdentifiers(identifiers);

        return openMRSPatient;
    }


    /**
     * Converts the given patient, represented as the MOTECH model, to the model used by the OpenMRS server.
     *
     * @param patient  the MOTECH model of patient
     * @param savedPerson  the savedPerson connected with the given patient
     * @param motechPatientIdentifierTypeUuid  the MOTECH identifier type uuid
     * @param patientIdentifiers the patient identifiers to be stored, key - identifier type uuid, value - identifier number
     * @return an OpenMRS representation of patient object
     */
    public static Patient toPatient(OpenMRSPatient patient, OpenMRSPerson savedPerson, String motechPatientIdentifierTypeUuid,
                                    Map<String, String> patientIdentifiers) {

        Patient converted = new Patient();
        Person person = new Person();
        person.setUuid(savedPerson.getPersonId());
        converted.setPerson(person);

        Location location = null;
        if (patient.getFacility() != null && StringUtils.isNotBlank(patient.getFacility().getFacilityId())) {
            location = new Location();
            location.setUuid(patient.getFacility().getFacilityId());
        }

        List<Identifier> identifiers = new ArrayList<>();
        identifiers.add(createMotechPatientIdentifier(patient.getMotechId(), motechPatientIdentifierTypeUuid, location));

        for (String identifierTypeUuid : patientIdentifiers.keySet()) {
            IdentifierType type = new IdentifierType();
            type.setUuid(identifierTypeUuid);

            Identifier identifier = new Identifier();
            identifier.setIdentifier(patientIdentifiers.get(identifierTypeUuid));
            identifier.setLocation(location);
            identifier.setIdentifierType(type);

            identifiers.add(identifier);
        }

        converted.setIdentifiers(identifiers);

        return converted;
    }

    public static OpenMRSProvider toOpenMRSProvider(Provider provider) {
        OpenMRSProvider openMRSProvider = new OpenMRSProvider();

        openMRSProvider.setProviderId(provider.getUuid());
        openMRSProvider.setPerson(provider.getPerson() != null ? toOpenMRSPerson(provider.getPerson()) : null);
        openMRSProvider.setIdentifier(provider.getIdentifier());

        return openMRSProvider;
    }

    public static Provider toProvider(OpenMRSProvider openMRSProvider) {

        Provider provider = new Provider();

        provider.setUuid(openMRSProvider.getProviderId());
        provider.setPerson(openMRSProvider.getPerson() != null ? toPerson(openMRSProvider.getPerson(), false) : null);
        provider.setIdentifier(openMRSProvider.getIdentifier());

        return provider;
    }

    public static OpenMRSEncounterType toOpenMRSEncounterType(Encounter.EncounterType encounterType) {

        OpenMRSEncounterType openMRSEncounterType = new OpenMRSEncounterType();

        openMRSEncounterType.setUuid(encounterType.getUuid());
        openMRSEncounterType.setName(encounterType.getName());
        openMRSEncounterType.setDescription(encounterType.getDescription());

        return openMRSEncounterType;
    }

    public static Encounter.EncounterType toEncounterType(OpenMRSEncounterType openMRSEncounterType) {

        Encounter.EncounterType encounterType = new Encounter.EncounterType();

        encounterType.setUuid(openMRSEncounterType.getUuid());
        encounterType.setName(openMRSEncounterType.getName());
        encounterType.setDescription(openMRSEncounterType.getDescription());

        return encounterType;
    }

    public static List<OpenMRSConcept> toOpenMRSConcepts(ConceptListResult conceptListResult) {

        List<OpenMRSConcept> concepts = new ArrayList<>();

        for (Concept concept : conceptListResult.getResults()) {
            concepts.add(toOpenMRSConcept(concept));
        }

        return concepts;
    }

    private static Concept.ConceptName copyConceptName(OpenMRSConceptName openMRSConceptName) {
        Concept.ConceptName conceptName = new Concept.ConceptName();
        conceptName.setName(openMRSConceptName.getName());
        conceptName.setLocale(openMRSConceptName.getLocale());
        conceptName.setConceptNameType(openMRSConceptName.getConceptNameType());

        return conceptName;
    }

    private static Identifier createMotechPatientIdentifier(String motechId, String motechPatientIdentifierTypeUuid, Location location) {
        IdentifierType type = new IdentifierType();
        type.setUuid(motechPatientIdentifierTypeUuid);

        Identifier identifier = new Identifier();
        identifier.setIdentifier(motechId);
        identifier.setLocation(location);
        identifier.setIdentifierType(type);

        return identifier;
    }
}
