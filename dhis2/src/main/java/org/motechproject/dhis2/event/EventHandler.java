package org.motechproject.dhis2.event;

import org.motechproject.dhis2.rest.domain.AttributeDto;
import org.motechproject.dhis2.rest.domain.DataValueDto;
import org.motechproject.dhis2.rest.domain.DhisEventDto;
import org.motechproject.dhis2.rest.domain.DhisStatusResponse;
import org.motechproject.dhis2.rest.domain.DhisStatusResponse.DhisStatus;
import org.motechproject.dhis2.rest.domain.EnrollmentDto;
import org.motechproject.dhis2.rest.domain.TrackedEntityInstanceDto;
import org.motechproject.dhis2.rest.service.DhisWebService;
import org.motechproject.dhis2.service.OrgUnitService;
import org.motechproject.dhis2.service.TrackedEntityInstanceMappingService;
import org.motechproject.event.MotechEvent;
import org.motechproject.event.listener.annotations.MotechListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

/**
 * Event Handler for the DHIS2 module. The public methods listen for the event subjects listed in
 * {@link org.motechproject.dhis2.event.EventSubjects}
 *
 */
@Service
public class EventHandler {
    @Autowired
    private DhisWebService dhisWebService;

    @Autowired
    private TrackedEntityInstanceMappingService trackedEntityInstanceMappingService;

    @Autowired
    private OrgUnitService orgUnitService;

    public EventHandler(DhisWebService webService,
                        TrackedEntityInstanceMappingService trackedEntityInstanceMappingService,
                        OrgUnitService orgUnitService) {

        this.dhisWebService = webService;
        this.trackedEntityInstanceMappingService = trackedEntityInstanceMappingService;
        this.orgUnitService = orgUnitService;

    }


    public EventHandler() {}

    /**
     * Parses the MotechEvent and creates a {@link org.motechproject.dhis2.rest.domain.TrackedEntityInstanceDto}
     * which is then sent to the DHIS2 server via {@link org.motechproject.dhis2.rest.service.DhisWebService}
     *
     * @param event MotechEvent pertaining to tracked entity instance creation.
     */
    @MotechListener(subjects = EventSubjects.CREATE_ENTITY )
    public void handleCreate(MotechEvent event) {
        Map<String, Object> params = new HashMap<>(event.getParameters());
        String externalUUID = (String) params.remove(EventParams.EXTERNAL_ID);
        TrackedEntityInstanceDto trackedEntityInstance = createTrackedEntityInstanceFromParams(params);
        DhisStatusResponse response = dhisWebService.createTrackedEntityInstance(trackedEntityInstance);

        if (response.getStatus() == DhisStatus.SUCCESS) {
            trackedEntityInstanceMappingService.create(externalUUID, response.getReference());
        }
    }

    /**
     * Parses the MotechEvent and creates a {@link org.motechproject.dhis2.rest.domain.EnrollmentDto}
     * which is then sent to the DHIS2 server via {@link org.motechproject.dhis2.rest.service.DhisWebService}
     *
     * @param event MotechEvent pertaining to enrolling a tracked entity instance in a program.
     */
    @MotechListener(subjects = {EventSubjects.ENROLL_IN_PROGRAM })
    public void handleEnrollment(MotechEvent event) {
        Map<String, Object> params = new HashMap<>(event.getParameters());
        EnrollmentDto enrollment = createEnrollmentFromParams(params);
        dhisWebService.createEnrollment(enrollment);
    }


    /**
     * Parses the MotechEvent and creates a {@link org.motechproject.dhis2.rest.domain.DhisEventDto}
     * which is then sent to the DHIS2 server via {@link org.motechproject.dhis2.rest.service.DhisWebService}
     *
     * @param event MotechEvent pertaining to a DHIS2 program stage event.
     */
    @MotechListener(subjects = {EventSubjects.UPDATE_PROGRAM_STAGE })
    public void handleStageUpdate(MotechEvent event) {
        Map<String, Object> params = new HashMap<>(event.getParameters());
        DhisEventDto dhisEventDto = createDhisEventFromParams(params);
        dhisWebService.createEvent(dhisEventDto);
    }

    /**
     * Parses the event and creates a{@link org.motechproject.dhis2.rest.domain.TrackedEntityInstanceDto}
     * and a {@link org.motechproject.dhis2.rest.domain.EnrollmentDto} which is then sent to the DHIS2 server
     * via {@link org.motechproject.dhis2.rest.service.DhisWebService}
     *
     * @param event pertaining to combined DHIS2 tracked entity instance creation and enrollment.
     */
    @MotechListener(subjects = {EventSubjects.CREATE_AND_ENROLL })
    public void handleCreateAndEnroll(MotechEvent event) {
        Map<String, Object> params = new HashMap<>(event.getParameters());
        Map<String, Object> enrollmentParams = new HashMap<>();

        enrollmentParams.put(EventParams.PROGRAM, params.remove(EventParams.PROGRAM));
        enrollmentParams.put(EventParams.DATE, params.remove(EventParams.DATE));

        enrollmentParams.put(EventParams.EXTERNAL_ID, params.get(EventParams.EXTERNAL_ID));

        handleCreate(new MotechEvent(EventSubjects.CREATE_ENTITY, params));
        handleEnrollment(new MotechEvent(EventSubjects.ENROLL_IN_PROGRAM, enrollmentParams));
    }

    private TrackedEntityInstanceDto createTrackedEntityInstanceFromParams(Map<String, Object> params) {
        String trackedEntity = (String) params.remove(EventParams.ENTITY_TYPE);
        String orgUnitName = (String) params.remove(EventParams.LOCATION);
        String orgUnitId = orgUnitService.findByName(orgUnitName).getUuid();

        List<AttributeDto> attributes = new ArrayList<AttributeDto>();
        for (Entry<String, Object> entry : params.entrySet()) {
            if (entry.getValue() != null) {
                AttributeDto attribute = new AttributeDto();
                attribute.setAttribute(entry.getKey());
                attribute.setValue(String.valueOf(entry.getValue()));
                attributes.add(attribute);
            }
        }

        TrackedEntityInstanceDto trackedEntityInstance = new TrackedEntityInstanceDto();
        trackedEntityInstance.setTrackedEntity(trackedEntity);
        trackedEntityInstance.setOrgUnit(orgUnitId);
        trackedEntityInstance.setAttributes(attributes);

        return trackedEntityInstance;
    }

    private EnrollmentDto createEnrollmentFromParams(Map<String, Object> params) {
        String program = (String) params.remove(EventParams.PROGRAM);

        String externalId = (String) params.remove(EventParams.EXTERNAL_ID);
        String trackedEntityInstanceId = trackedEntityInstanceMappingService.mapFromExternalId(externalId);

        String date = (String) params.remove(EventParams.DATE);

        List<AttributeDto> attributes = new ArrayList<>();
        for (Entry<String, Object> entry : params.entrySet()) {
            if (entry.getValue() != null) {
                AttributeDto attribute = new AttributeDto();
                attribute.setAttribute(entry.getKey());
                attribute.setValue((String) entry.getValue());
                attributes.add(attribute);
            }
        }

        EnrollmentDto enrollment = new EnrollmentDto();
        enrollment.setProgram(program);
        enrollment.setTrackedEntityInstance(trackedEntityInstanceId);
        enrollment.setDateOfEnrollment(date);
        enrollment.setAttributes(attributes);

        return enrollment;
    }

    private DhisEventDto createDhisEventFromParams(Map<String, Object> params) {
        DhisEventDto dhisEventDto = new DhisEventDto();

        String registationString = (String) params.remove(EventParams.REGISTRATION);
        boolean registration = registationString.contains("true");

        if (registration) {
            String externalTrackedEntityInstanceId = (String) params.remove(EventParams.EXTERNAL_ID);
            String trackedEntityInstanceId = trackedEntityInstanceMappingService.mapFromExternalId(externalTrackedEntityInstanceId);
            dhisEventDto.setTrackedEntityInstance(trackedEntityInstanceId);
        }

        String orgUnitName = (String) params.remove(EventParams.LOCATION);
        String orgUnitId = orgUnitService.findByName(orgUnitName).getUuid();
        String program = (String) params.remove(EventParams.PROGRAM);
        String date = (String) params.remove(EventParams.DATE);
        String stage = (String) params.remove(EventParams.STAGE);

        List<DataValueDto> dataValues = new ArrayList<>();

        for (Entry<String, Object> entry : params.entrySet()) {
            DataValueDto dataValue = new DataValueDto();
            dataValue.setDataElement(entry.getKey());
            dataValue.setValue((String) entry.getValue());
            dataValues.add(dataValue);
        }

        dhisEventDto.setProgram(program);
        dhisEventDto.setEventDate(date);
        dhisEventDto.setProgramStage(stage);
        dhisEventDto.setOrgUnit(orgUnitId);
        dhisEventDto.setDataValues(dataValues);

        return dhisEventDto;
    }
}
