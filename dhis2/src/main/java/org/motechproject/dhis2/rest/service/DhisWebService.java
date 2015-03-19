package org.motechproject.dhis2.rest.service;

import org.motechproject.dhis2.rest.domain.DataElementDto;
import org.motechproject.dhis2.rest.domain.DhisEventDto;
import org.motechproject.dhis2.rest.domain.DhisStatusResponse;
import org.motechproject.dhis2.rest.domain.EnrollmentDto;
import org.motechproject.dhis2.rest.domain.OrganisationUnitDto;
import org.motechproject.dhis2.rest.domain.ProgramDto;
import org.motechproject.dhis2.rest.domain.ProgramStageDto;
import org.motechproject.dhis2.rest.domain.TrackedEntityAttributeDto;
import org.motechproject.dhis2.rest.domain.TrackedEntityDto;
import org.motechproject.dhis2.rest.domain.TrackedEntityInstanceDto;

import java.util.List;

/**
 * Handles HTTP requests and responses to and from DHIS2.
 */
public interface DhisWebService {

    /**
     * Gets a list of all the Data Elements from DHIS2.
     * @return a list of {@link org.motechproject.dhis2.rest.domain.DataElementDto}
     */
    List<DataElementDto> getDataElements();

    /**
     * Gets the Data Element specified in the URL
     * @param href
     * @return the {@link org.motechproject.dhis2.rest.domain.DataElementDto}
     */
    DataElementDto getDataElementByHref(String href);

    /**
     * Gets a list of all the Organisation Units from DHIS2
     * @return a list of {@link org.motechproject.dhis2.rest.domain.OrganisationUnitDto}
     */
    List<OrganisationUnitDto> getOrganisationUnits();

    /**
     * Gets the Organisation Unit specified in the URL
     * @param href
     * @return the {@link org.motechproject.dhis2.rest.domain.OrganisationUnitDto}
     */
    OrganisationUnitDto getOrganisationUnitByHref(String href);

    /**
     * Gets a list of all the programs from DHIS2.
     * @return a list of {@link org.motechproject.dhis2.rest.domain.ProgramDto}
     */
    List<ProgramDto> getPrograms();

    /**
     * Gets the program specified in the URL
     * @param href
     * @return the {@link org.motechproject.dhis2.rest.domain.ProgramDto}
     */
    ProgramDto getProgramByHref(String href);

    /**
     * Gets a list of all the program Stages from DHIS2
     * @return a list of {@link org.motechproject.dhis2.rest.domain.ProgramStageDto\}
     */
    List<ProgramStageDto> getProgramStages();

    /**
     * Gets the program stage specified in the URL
     * @param href
     * @return the {@link org.motechproject.dhis2.rest.domain.ProgramStageDto}
     */
    ProgramStageDto getProgramStageByHref(String href);

    /**
     * Gets a list of all the tracked entity attributes from DHIS2
     * @return a list of {@link org.motechproject.dhis2.rest.domain.TrackedEntityInstanceDto}
     */
    List<TrackedEntityAttributeDto> getTrackedEntityAttributes();

    /**
     * Gets the tracked entity attribute specified in the URL
     * @param href
     * @return the {@link org.motechproject.dhis2.rest.domain.TrackedEntityAttributeDto}
     */
    TrackedEntityAttributeDto getTrackedEntityAttributeByHref(String href);

    /**
     * Gets a list of all the tracked entity types in DHIS2
     * @return a list of {@link org.motechproject.dhis2.rest.domain.TrackedEntityDto}
     */
    List<TrackedEntityDto> getTrackedEntities();

    /**
     * Gets the tracked entity specified in the URL.
     * @param href
     * @return the {@link org.motechproject.dhis2.rest.domain.TrackedEntityDto}
     */
    TrackedEntityDto getTrackedEntityByHref(String href);

    /**
     * Attempts to create an enrollment in DHIS2 via an HTTP post request
     * @param enrollmentDto
     * @return a {@link org.motechproject.dhis2.rest.domain.DhisStatusResponse} indicating success or failure
     */
    DhisStatusResponse createEnrollment(EnrollmentDto enrollmentDto);

    /**
     * Attempts to create a DHIS2 event in DHIS2 via an HTTP post request
     * @param event
     * @return a {@link org.motechproject.dhis2.rest.domain.DhisStatusResponse} indicating success or failure
     */
    DhisStatusResponse createEvent(DhisEventDto event);

    /**
     * Attempts to create a tracked entity instance in DHIS2 via an HTTP post request
     * @param trackedEntity
     * @returna {@link org.motechproject.dhis2.rest.domain.DhisStatusResponse} indicating success or failure
     */
    DhisStatusResponse createTrackedEntityInstance(TrackedEntityInstanceDto trackedEntity);
}


