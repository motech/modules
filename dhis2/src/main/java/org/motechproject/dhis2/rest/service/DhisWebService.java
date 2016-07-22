package org.motechproject.dhis2.rest.service;

import org.motechproject.dhis2.rest.domain.DataElementDto;
import org.motechproject.dhis2.rest.domain.DataSetDto;
import org.motechproject.dhis2.rest.domain.DataValueSetDto;
import org.motechproject.dhis2.rest.domain.DhisDataValueStatusResponse;
import org.motechproject.dhis2.rest.domain.DhisEventDto;
import org.motechproject.dhis2.rest.domain.DhisServerInfo;
import org.motechproject.dhis2.rest.domain.DhisStatusResponse;
import org.motechproject.dhis2.rest.domain.EnrollmentDto;
import org.motechproject.dhis2.rest.domain.OrganisationUnitDto;
import org.motechproject.dhis2.rest.domain.ProgramDto;
import org.motechproject.dhis2.rest.domain.ProgramStageDataElementDto;
import org.motechproject.dhis2.rest.domain.ProgramStageDto;
import org.motechproject.dhis2.rest.domain.ProgramTrackedEntityAttributeDto;
import org.motechproject.dhis2.rest.domain.ServerVersion;
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
     * Gets a list of all the Data Sets from DHIS2.
     * @return a list of {@link DataSetDto}s
     */
    List<DataSetDto> getDataSets();

    /**
     * Gets the Data Element specified in the URL.
     * @param href
     * @return the {@link org.motechproject.dhis2.rest.domain.DataElementDto}
     */
    DataElementDto getDataElementByHref(String href);

    /**
     * Gets the Data Element specified by ID.
     * @param id
     * @return the {@link org.motechproject.dhis2.rest.domain.DataElementDto}
     */
    DataElementDto getDataElementById(String id);

    /**
     * Gets a list of all the Organisation Units from DHIS2.
     * @return a list of {@link org.motechproject.dhis2.rest.domain.OrganisationUnitDto}
     */
    List<OrganisationUnitDto> getOrganisationUnits();

    /**
     * Gets the Organisation Unit specified in the URL.
     * @param href
     * @return the {@link org.motechproject.dhis2.rest.domain.OrganisationUnitDto}
     */
    OrganisationUnitDto getOrganisationUnitByHref(String href);

    /**
     * Gets the Organisation Unit specified by ID.
     * @param id
     * @return the {@link org.motechproject.dhis2.rest.domain.OrganisationUnitDto}
     */
    OrganisationUnitDto getOrganisationUnitById(String id);

    /**
     * Gets a list of all the programs from DHIS2.
     * @return a list of {@link org.motechproject.dhis2.rest.domain.ProgramDto}
     */
    List<ProgramDto> getPrograms();

    /**
     * Gets the program specified in the URL.
     * @param href
     * @return the {@link org.motechproject.dhis2.rest.domain.ProgramDto}
     */
    ProgramDto getProgramByHref(String href);

    /**
     * Gets the program specified by ID.
     * @param id
     * @return the {@link org.motechproject.dhis2.rest.domain.ProgramDto}
     */
    ProgramDto getProgramById(String id);

    /**
     * Gets a list of all the program Stages from DHIS2.
     * @return a list of {@link org.motechproject.dhis2.rest.domain.ProgramStageDto\}
     */
    List<ProgramStageDto> getProgramStages();

    /**
     * Gets the program Stage specified in the URL.
     * @param href
     * @return the {@link org.motechproject.dhis2.rest.domain.ProgramStageDto}
     */
    ProgramStageDto getProgramStageByHref(String href);

    /**
     * Gets the program Stage Data Element specified by ID.
     * @param id
     * @return the {@link org.motechproject.dhis2.rest.domain.ProgramStageDataElementDto}
     */
    ProgramStageDataElementDto getProgramStageDataElementById(String id);

    /**
     * Gets the program Stage Data Element specified in the URL.
     * @param href
     * @return the {@link org.motechproject.dhis2.rest.domain.ProgramStageDataElementDto}
     */
    ProgramStageDataElementDto getProgramStageDataElementByHref(String href);

    /**
     * Gets the program Stage specified by ID.
     * @param id
     * @return the {@link org.motechproject.dhis2.rest.domain.ProgramStageDto}
     */
    ProgramStageDto getProgramStageById(String id);

    /**
     * Gets a list of all the tracked entity attributes from DHIS2.
     * @return a list of {@link org.motechproject.dhis2.rest.domain.TrackedEntityInstanceDto}
     */
    List<TrackedEntityAttributeDto> getTrackedEntityAttributes();

    /**
     * Gets the tracked entity attribute specified in the URL.
     * @param href
     * @return the {@link org.motechproject.dhis2.rest.domain.TrackedEntityAttributeDto}
     */
    TrackedEntityAttributeDto getTrackedEntityAttributeByHref(String href);

    /**
     * Gets the tracked entity attribute specified by ID.
     * @param id
     * @return the {@link org.motechproject.dhis2.rest.domain.TrackedEntityAttributeDto}
     */
    TrackedEntityAttributeDto getTrackedEntityAttributeById(String id);

    /**
     * Gets the tracked entity attribute specified in the URL.
     * @param href
     * @return the {@link org.motechproject.dhis2.rest.domain.TrackedEntityAttributeDto}
     */
    ProgramTrackedEntityAttributeDto getProgramTrackedEntityAttributeByHref(String href);

    /**
     * Gets the tracked entity attribute specified by ID.
     * @param id
     * @return the {@link org.motechproject.dhis2.rest.domain.TrackedEntityAttributeDto}
     */
    ProgramTrackedEntityAttributeDto getProgramTrackedEntityAttributeById(String id);

    /**
     * Gets a list of all the tracked entity types in DHIS2.
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
     * Gets the tracked entity specified by ID.
     * @param id
     * @return the {@link org.motechproject.dhis2.rest.domain.TrackedEntityDto}
     */
    TrackedEntityDto getTrackedEntityById(String id);

    /**
     * Attempts to create an enrollment in DHIS2 via an HTTP post request.
     * @param enrollmentDto
     * @return a {@link org.motechproject.dhis2.rest.domain.DhisStatusResponse} indicating success or failure
     */
    DhisStatusResponse createEnrollment(EnrollmentDto enrollmentDto);

    /**
     * Attempts to create a DHIS2 event in DHIS2 via an HTTP post request.
     * @param event
     * @return a {@link org.motechproject.dhis2.rest.domain.DhisStatusResponse} indicating success or failure
     */
    DhisStatusResponse createEvent(DhisEventDto event);

    /**
     * Attempts to create a tracked entity instance in DHIS2 via an HTTP post request.
     * @param trackedEntity
     * @returna {@link org.motechproject.dhis2.rest.domain.DhisStatusResponse} indicating success or failure
     */
    DhisStatusResponse createTrackedEntityInstance(TrackedEntityInstanceDto trackedEntity);

    /**
     * Attempts to send a data value set to DHIS2 via an HTTP post request.
     *
     * @param dataValueSetDto
     * @return
     */
    DhisDataValueStatusResponse sendDataValueSet(DataValueSetDto dataValueSetDto);

    /**
     * Retrieves DHIS2 server specific information, such as its version via HTTP GET request.
     *
     * @return DHIS2 server specific information
     */
    DhisServerInfo getDhisServerInfo();

    /**
     * Returns DHIS server version, as returned by the DHIS instance. The version is cached and refreshed when the
     * DHIS settings are changed.
     *
     * @return DHIS server version
     */
    ServerVersion getServerVersion();
}


