package org.motechproject.dhis2.event;

import org.motechproject.dhis2.domain.DataElement;
import org.motechproject.dhis2.exception.DataElementNotFoundException;
import org.motechproject.dhis2.rest.domain.AttributeDto;
import org.motechproject.dhis2.rest.domain.DataValueDto;
import org.motechproject.dhis2.rest.domain.DataValueSetDto;
import org.motechproject.dhis2.rest.domain.DhisEventDto;
import org.motechproject.dhis2.rest.domain.DhisStatus;
import org.motechproject.dhis2.rest.domain.DhisStatusResponse;
import org.motechproject.dhis2.rest.domain.EnrollmentDto;
import org.motechproject.dhis2.rest.domain.TrackedEntityInstanceDto;
import org.motechproject.dhis2.rest.service.DhisWebService;
import org.motechproject.dhis2.service.DataElementService;
import org.motechproject.dhis2.service.TrackedEntityInstanceMappingService;
import org.motechproject.event.MotechEvent;
import org.motechproject.event.listener.annotations.MotechListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
 */
@Service
public class EventHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(EventHandler.class);

    @Autowired
    private DhisWebService dhisWebService;

    @Autowired
    private TrackedEntityInstanceMappingService trackedEntityInstanceMappingService;

    @Autowired
    private DataElementService dataElementService;

    public EventHandler() {

    }

    /**
     * Parses the MotechEvent and creates a {@link org.motechproject.dhis2.rest.domain.TrackedEntityInstanceDto}
     * which is then sent to the DHIS2 server via {@link org.motechproject.dhis2.rest.service.DhisWebService}
     *
     * @param event MotechEvent pertaining to tracked entity instance creation.
     */
    @MotechListener(subjects = EventSubjects.CREATE_ENTITY)
    public void handleCreate (MotechEvent event) {
        LOGGER.debug("Handling CREATE_ENTITY MotechEvent");
        Map<String, Object> params = prepareDhisAttributesMap(event.getParameters());
        String externalUUID = (String) params.remove(EventParams.EXTERNAL_ID);
        TrackedEntityInstanceDto trackedEntityInstance = createTrackedEntityInstanceFromParams(params);

        LOGGER.debug("Sending request to create entity to the DHIS Web Service");
        DhisStatusResponse response = dhisWebService.createTrackedEntityInstance(trackedEntityInstance);

        LOGGER.trace("Received response from the DHIS server. Status: {}", response.getStatus());
        if (response.getStatus() == DhisStatus.SUCCESS || response.getStatus() == DhisStatus.OK) {
            trackedEntityInstanceMappingService.create(externalUUID, response.getReference());
        }
    }

    /**
     * Parses the MotechEvent and creates a {@link org.motechproject.dhis2.rest.domain.EnrollmentDto}
     * which is then sent to the DHIS2 server via {@link org.motechproject.dhis2.rest.service.DhisWebService}
     *
     * @param event MotechEvent pertaining to enrolling a tracked entity instance in a program.
     */
    @MotechListener(subjects = {EventSubjects.ENROLL_IN_PROGRAM})
    public void handleEnrollment (MotechEvent event) {
        Map<String, Object> params = prepareDhisAttributesMap(event.getParameters());
        EnrollmentDto enrollment = createEnrollmentFromParams(params);
        dhisWebService.createEnrollment(enrollment);
    }


    /**
     * Parses the MotechEvent and creates a {@link org.motechproject.dhis2.rest.domain.DhisEventDto}
     * which is then sent to the DHIS2 server via {@link org.motechproject.dhis2.rest.service.DhisWebService}
     *
     * @param event MotechEvent pertaining to a DHIS2 program stage event.
     */
    @MotechListener(subjects = {EventSubjects.UPDATE_PROGRAM_STAGE})
    public void handleStageUpdate (MotechEvent event) {
        Map<String, Object> params = prepareDhisAttributesMap(event.getParameters());
        DhisEventDto dhisEventDto = createDhisEventFromParams(params);
        dhisWebService.createEvent(dhisEventDto);
    }

    /**
     * Parses the event and creates a {@link org.motechproject.dhis2.rest.domain.TrackedEntityInstanceDto}
     * and a {@link org.motechproject.dhis2.rest.domain.EnrollmentDto} which is then sent to the DHIS2 server
     * via {@link org.motechproject.dhis2.rest.service.DhisWebService}
     *
     * @param event pertaining to combined DHIS2 tracked entity instance creation and enrollment.
     */
    @MotechListener(subjects = {EventSubjects.CREATE_AND_ENROLL})
    public void handleCreateAndEnroll (MotechEvent event) {
        Map<String, Object> params = prepareDhisAttributesMap(event.getParameters());
        Map<String, Object> enrollmentParams = new HashMap<>();

        enrollmentParams.put(EventParams.PROGRAM, params.remove(EventParams.PROGRAM));
        enrollmentParams.put(EventParams.DATE, params.remove(EventParams.DATE));

        enrollmentParams.put(EventParams.EXTERNAL_ID, params.get(EventParams.EXTERNAL_ID));

        handleCreate(new MotechEvent(EventSubjects.CREATE_ENTITY, params));
        handleEnrollment(new MotechEvent(EventSubjects.ENROLL_IN_PROGRAM, enrollmentParams));
    }

    /**
     * Parses the event and creates a {@link org.motechproject.dhis2.rest.domain.DataValueDto} which
     * is then sent to the DHIS2 server via {@link org.motechproject.dhis2.rest.service.DhisWebService}
     *
     * @param event
     */
    @MotechListener(subjects = EventSubjects.SEND_DATA_VALUE)
    public void handleDataValue(MotechEvent event) {

        Map<String, Object> params = event.getParameters();

        DataElement dataElement = dataElementService.findByName((String) params.get(EventParams.DATA_ELEMENT));

        if (dataElement == null) {
            throw new DataElementNotFoundException("The data element " + params.get(EventParams.DATA_ELEMENT) +
                    " that was sent did not match any values imported from DHIS2. Please make sure that the " +
                    "data element field matches a data element name in the DHIS2 module");
        }

        String orgUnitId = (String) params.get(EventParams.LOCATION);
        String period = (String) params.get(EventParams.PERIOD);
        String value = (String) params.get(EventParams.VALUE);
        String categoryOptionCombo = (String) params.get(EventParams.CATEGORY_OPTION_COMBO);
        String comment = (String) params.get(EventParams.COMMENT);

        DataValueDto dataValueDto = new DataValueDto();
        dataValueDto.setDataElement(dataElement.getUuid());
        dataValueDto.setValue(value);
        dataValueDto.setOrgUnit(orgUnitId);
        dataValueDto.setPeriod(period);
        dataValueDto.setCategoryOptionCombo(categoryOptionCombo);
        dataValueDto.setComment(comment);

        DataValueSetDto dataValueSetDto = new DataValueSetDto();
        List<DataValueDto> dataValueDtos = new ArrayList<>();
        dataValueDtos.add(dataValueDto);
        dataValueSetDto.setDataValues(dataValueDtos);

        dhisWebService.sendDataValueSet(dataValueSetDto);
    }

    /**
     * Parses the event and creates a{@link org.motechproject.dhis2.rest.domain.DataValueSetDto}which
     * is then sent to the DHIS2 server via {@link org.motechproject.dhis2.rest.service.DhisWebService}
     *
     * @param event
     */
    @MotechListener(subjects = EventSubjects.SEND_DATA_VALUE_SET)
    public void handleDataValueSet(MotechEvent event) {
        Map<String, Object> params = prepareDhisAttributesMap(event.getParameters());
        String dataSet = (String) params.get(EventParams.DATA_SET);
        String completeDate = (String) params.get(EventParams.COMPLETE_DATE);
        String period = (String) params.get(EventParams.PERIOD);
        String orgUnitId = (String) params.get(EventParams.LOCATION);
        String categoryOptionCombo = (String) params.get(EventParams.CATEGORY_OPTION_COMBO);
        String comment = (String) params.get(EventParams.COMMENT);
        String attributeOptionCombo = (String) params.get(EventParams.ATTRIBUTE_OPTION_COMBO);
        Map<String, Object> dataValues = (Map<String, Object>) params.get(EventParams.DATA_VALUES);


        List<DataValueDto> dataValueDtos = new ArrayList<>();

        for (Object o : dataValues.entrySet()) {
            Entry pair = (Entry) o;
            String dataElement = (String) pair.getKey();
            String dataElementId = dataElementService.findByName(dataElement).getUuid();
            String value = (String) pair.getValue();
            DataValueDto dataValueDto = new DataValueDto();
            dataValueDto.setDataElement(dataElementId);
            dataValueDto.setValue(value);

            dataValueDtos.add(dataValueDto);
        }

        DataValueSetDto dataValueSetDto = new DataValueSetDto();
        dataValueSetDto.setDataSet(dataSet);
        dataValueSetDto.setPeriod(period);
        dataValueSetDto.setCompleteDate(completeDate);
        dataValueSetDto.setOrgUnit(orgUnitId);
        dataValueSetDto.setDataValues(dataValueDtos);
        dataValueSetDto.setAttributeOptionCombo(attributeOptionCombo);
        dataValueSetDto.setCategoryOptionCombo(categoryOptionCombo);
        dataValueSetDto.setComment(comment);
        dhisWebService.sendDataValueSet(dataValueSetDto);

    }

    private TrackedEntityInstanceDto createTrackedEntityInstanceFromParams (Map<String, Object> params) {
        String trackedEntity = (String) params.remove(EventParams.ENTITY_TYPE);
        String orgUnitId = (String) params.remove(EventParams.LOCATION);

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

    private EnrollmentDto createEnrollmentFromParams (Map<String, Object> params) {
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

    private DhisEventDto createDhisEventFromParams (Map<String, Object> params) {
        DhisEventDto dhisEventDto = new DhisEventDto();

        String registationString = (String) params.remove(EventParams.REGISTRATION);
        boolean registration = registationString.contains("true");

        if (registration) {
            String externalTrackedEntityInstanceId = (String) params.remove(EventParams.EXTERNAL_ID);
            String trackedEntityInstanceId = trackedEntityInstanceMappingService.mapFromExternalId(externalTrackedEntityInstanceId);
            dhisEventDto.setTrackedEntityInstance(trackedEntityInstanceId);
        }

        String orgUnitId = (String) params.remove(EventParams.LOCATION);
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

    private Map<String, Object> prepareDhisAttributesMap(Map<String, Object> eventParams) {
        Map<String, Object> dhisAttributes = new HashMap<>(eventParams);

        dhisAttributes.remove(MotechEvent.PARAM_INVALID_MOTECH_EVENT);
        dhisAttributes.remove(MotechEvent.PARAM_REDELIVERY_COUNT);
        dhisAttributes.remove(EventParams.MESSAGE_DESTINATION);

        return dhisAttributes;
    }

}
