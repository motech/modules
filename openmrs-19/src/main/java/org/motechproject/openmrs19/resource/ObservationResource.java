package org.motechproject.openmrs19.resource;

import org.motechproject.openmrs19.rest.HttpException;
import org.motechproject.openmrs19.resource.model.Observation;
import org.motechproject.openmrs19.resource.model.ObservationListResult;

public interface ObservationResource {

    ObservationListResult queryForObservationsByPatientId(String id) throws HttpException;

    void deleteObservation(String id, String reason) throws HttpException;

    Observation getObservationById(String id) throws HttpException;

}
