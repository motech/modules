package org.motechproject.dhis2.service.impl;

import org.joda.time.DateTime;
import org.joda.time.Period;
import org.motechproject.commons.api.TasksEventParser;
import org.motechproject.commons.date.util.JodaFormatter;
import org.motechproject.dhis2.domain.DataElement;
import org.motechproject.dhis2.domain.DataSet;
import org.motechproject.dhis2.event.EventParams;
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
import org.motechproject.dhis2.service.DataSetService;
import org.motechproject.dhis2.service.DhisService;
import org.motechproject.dhis2.service.TrackedEntityInstanceMappingService;
import org.motechproject.scheduler.service.MotechSchedulerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service("dhisService")
public class DhisServiceImpl implements DhisService {
    private static final Logger LOGGER = LoggerFactory.getLogger(DhisServiceImpl.class);

    @Autowired
    private DhisWebService dhisWebService;

    @Autowired
    private TrackedEntityInstanceMappingService trackedEntityInstanceMappingService;

    @Autowired
    private DataElementService dataElementService;

    @Autowired
    private DataSetService dataSetService;

    @Override
    public void createEntity(Map<String, Object> parameters) {
        Map<String, Object> params = prepareDhisAttributesMap(parameters);
        String externalUUID = (String) params.remove(EventParams.EXTERNAL_ID);
        TrackedEntityInstanceDto trackedEntityInstance = createTrackedEntityInstanceFromParams(params);

        LOGGER.debug("Sending request to create entity to the DHIS Web Service");
        DhisStatusResponse response = dhisWebService.createTrackedEntityInstance(trackedEntityInstance);

        LOGGER.trace("Received response from the DHIS server. Status: {}", response.getStatus());
        if (response.getStatus() == DhisStatus.SUCCESS || response.getStatus() == DhisStatus.OK) {
            trackedEntityInstanceMappingService.create(externalUUID, response.getReference());
        }
    }

    @Override
    public void enrollInProgram(Map<String, Object> parameters) {
        Map<String, Object> params = prepareDhisAttributesMap(parameters);
        EnrollmentDto enrollment = createEnrollmentFromParams(params);
        dhisWebService.createEnrollment(enrollment);
    }

    @Override
    public void updateProgramStage(Map<String, Object> parameters) {
        Map<String, Object> params = prepareDhisAttributesMap(parameters);
        DhisEventDto dhisEventDto = createDhisEventFromParams(params);
        dhisWebService.createEvent(dhisEventDto);
    }

    @Override
    public void createAndEnroll(Map<String, Object> parameters) {
        Map<String, Object> params = prepareDhisAttributesMap(parameters);
        Map<String, Object> enrollmentParams = new HashMap<>();

        enrollmentParams.put(EventParams.PROGRAM, params.remove(EventParams.PROGRAM));
        enrollmentParams.put(EventParams.DATE, params.remove(EventParams.DATE));

        enrollmentParams.put(EventParams.EXTERNAL_ID, params.get(EventParams.EXTERNAL_ID));
        enrollmentParams.put(EventParams.LOCATION, params.get(EventParams.LOCATION));

        createEntity(params);
        enrollInProgram(enrollmentParams);
    }

    @Override
    @Transactional
    public void sendDataValue(Map<String, Object> params) {
        DataElement dataElement = dataElementService.findByName((String) params.get(EventParams.DATA_ELEMENT));

        if (dataElement == null) {
            throw new DataElementNotFoundException("The data element " + params.get(EventParams.DATA_ELEMENT) +
                    " that was sent did not match any values imported from DHIS2. Please make sure that the " +
                    "data element field matches a data element name in the DHIS2 module");
        }

        String orgUnitId = (String) params.get(EventParams.LOCATION);
        Period period = (Period) params.get(EventParams.PERIOD);
        String value = (String) params.get(EventParams.VALUE);
        String categoryOptionCombo = (String) params.get(EventParams.CATEGORY_OPTION_COMBO);
        String comment = (String) params.get(EventParams.COMMENT);

        DataValueDto dataValueDto = new DataValueDto();
        dataValueDto.setDataElement(dataElement.getUuid());
        dataValueDto.setValue(value);
        dataValueDto.setOrgUnit(orgUnitId);
        dataValueDto.setPeriod(convertPeriodToString(period));
        dataValueDto.setCategoryOptionCombo(categoryOptionCombo);
        dataValueDto.setComment(comment);

        DataValueSetDto dataValueSetDto = new DataValueSetDto();
        List<DataValueDto> dataValueDtos = new ArrayList<>();
        dataValueDtos.add(dataValueDto);
        dataValueSetDto.setDataValues(dataValueDtos);

        dhisWebService.sendDataValueSet(dataValueSetDto);
    }

    @Override
    @Transactional
    public void sendDataValueSet(Map<String, Object> parameters) {
        Map<String, Object> params = prepareDhisAttributesMap(parameters);
        DataSet dataSet = dataSetService.findByUuid((String) params.get(EventParams.DATA_SET));
        DateTime completeDate = (DateTime) params.get(EventParams.COMPLETE_DATE);
        Period period = (Period) params.get(EventParams.PERIOD);
        String orgUnitId = (String) params.get(EventParams.LOCATION);
        String categoryOptionCombo = (String) params.get(EventParams.CATEGORY_OPTION_COMBO);
        String comment = (String) params.get(EventParams.COMMENT);
        String attributeOptionCombo = (String) params.get(EventParams.ATTRIBUTE_OPTION_COMBO);


        List<DataValueDto> dataValueDtos = new ArrayList<>();

        for (DataElement element : dataSet.getDataElementList()) {
            DataValueDto dataValueDto = new DataValueDto();
            dataValueDto.setDataElement(element.getUuid());
            dataValueDto.setValue((String) params.get(element.getUuid()));

            dataValueDtos.add(dataValueDto);
        }

        DataValueSetDto dataValueSetDto = new DataValueSetDto();
        dataValueSetDto.setDataSet(dataSet.getUuid());
        dataValueSetDto.setPeriod(convertPeriodToString(period));
        dataValueSetDto.setCompleteDate(convertDateTimeToString(completeDate));
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

        List<AttributeDto> attributes = new ArrayList<>();
        for (Map.Entry<String, Object> entry : params.entrySet()) {
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
        String orgUnit = (String) params.remove(EventParams.LOCATION);

        DateTime date = (DateTime) params.remove(EventParams.DATE);

        List<AttributeDto> attributes = new ArrayList<>();
        for (Map.Entry<String, Object> entry : params.entrySet()) {
            if (entry.getValue() != null) {
                AttributeDto attribute = new AttributeDto();
                attribute.setAttribute(entry.getKey());
                attribute.setValue((String) entry.getValue());
                attributes.add(attribute);
            }
        }

        EnrollmentDto enrollment = new EnrollmentDto();
        enrollment.setProgram(program);
        enrollment.setOrgUnit(orgUnit);
        enrollment.setTrackedEntityInstance(trackedEntityInstanceId);
        enrollment.setDateOfEnrollment(convertDateTimeToString(date));
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
        DateTime date = (DateTime) params.remove(EventParams.DATE);
        String stage = (String) params.remove(EventParams.STAGE);
        String status = (String) params.remove(EventParams.STATUS);

        List<DataValueDto> dataValues = new ArrayList<>();

        for (Map.Entry<String, Object> entry : params.entrySet()) {
            DataValueDto dataValue = new DataValueDto();
            dataValue.setDataElement(entry.getKey());
            dataValue.setValue((String) entry.getValue());
            dataValues.add(dataValue);
        }

        dhisEventDto.setProgram(program);
        dhisEventDto.setEventDate(convertDateTimeToString(date));
        dhisEventDto.setProgramStage(stage);
        dhisEventDto.setOrgUnit(orgUnitId);
        dhisEventDto.setStatus(status);
        dhisEventDto.setDataValues(dataValues);

        return dhisEventDto;
    }

    private Map<String, Object> prepareDhisAttributesMap(Map<String, Object> eventParams) {
        Map<String, Object> dhisAttributes = new HashMap<>(eventParams);

        dhisAttributes.remove(MotechSchedulerService.JOB_ID_KEY);
        dhisAttributes.remove(TasksEventParser.CUSTOM_PARSER_EVENT_KEY);

        return dhisAttributes;
    }

    private String convertDateTimeToString(DateTime dateTime) {
        return new JodaFormatter().formatDateTime(dateTime);
    }

    private String convertPeriodToString(Period period) {
        return new JodaFormatter().formatPeriod(period);
    }
}
