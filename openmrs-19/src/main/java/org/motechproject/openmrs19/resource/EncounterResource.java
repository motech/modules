package org.motechproject.openmrs19.resource;

import org.motechproject.openmrs19.rest.HttpException;
import org.motechproject.openmrs19.resource.model.Encounter;
import org.motechproject.openmrs19.resource.model.EncounterListResult;

public interface EncounterResource {

    Encounter createEncounter(Encounter encounter) throws HttpException;

    EncounterListResult queryForAllEncountersByPatientId(String id) throws HttpException;

    Encounter getEncounterById(String uuid) throws HttpException;

}
