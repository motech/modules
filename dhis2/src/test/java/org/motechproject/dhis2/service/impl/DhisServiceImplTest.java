package org.motechproject.dhis2.service.impl;

import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.motechproject.commons.date.util.JodaFormatter;
import org.motechproject.dhis2.domain.DataElement;
import org.motechproject.dhis2.event.EventParams;
import org.motechproject.dhis2.exception.DataElementNotFoundException;
import org.motechproject.dhis2.rest.domain.AttributeDto;
import org.motechproject.dhis2.rest.domain.DataValueDto;
import org.motechproject.dhis2.rest.domain.DataValueSetDto;
import org.motechproject.dhis2.rest.domain.DhisEventDto;
import org.motechproject.dhis2.rest.domain.DhisStatus;
import org.motechproject.dhis2.rest.domain.DhisStatusResponse;
import org.motechproject.dhis2.rest.domain.EnrollmentDto;
import org.motechproject.dhis2.rest.domain.ImportCountDto;
import org.motechproject.dhis2.rest.domain.TrackedEntityInstanceDto;
import org.motechproject.dhis2.rest.service.DhisWebService;
import org.motechproject.dhis2.service.DataElementService;
import org.motechproject.dhis2.service.DhisService;
import org.motechproject.dhis2.service.SettingsService;
import org.motechproject.dhis2.service.TrackedEntityInstanceMappingService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class DhisServiceImplTest {
    private static final String ORGUNIT_ID = "orgUnitID";
    private static final String ENTITY_TYPE_PERSON = "person";
    private static final String ENTITY_INSTANCE_ID = "externalID";
    private static final String ATTRIBUTE_VALUE = "attributeValue";
    private static final String ATTRIBUTE_ID = "attributeID";
    private static final String INSTANCE_DHIS_ID = "dhis2uuid";
    private static final String PROGRAM_ID = "programID";
    private static final String DATE = "2014-01-01";
    private static final String REGISTRATION = "true";
    private static final String STAGE_ID = "stageId";
    private static final String DATA_ELEMENT_ID = "dataElementID";
    private static final String DATA_ELEMENT_VALUE = "value";
    private static final String PERIOD_VALUE = "5 months";
    private static final String CATEGORY_COMBO = "categoryOption";
    private static final String COMMENT = "comment";
    private static final String STATUS = "ACTIVE";

    @Mock
    SettingsService settingsService;
    @Mock
    private TrackedEntityInstanceMappingService trackedEntityInstanceMappingService;
    @Mock
    private DataElementService dataElementService;
    @Mock
    private DhisWebService dhisWebservice;

    private DhisStatusResponse response;

    @InjectMocks
    private DhisService dhisService = new DhisServiceImpl();

    @Before
    public void setup() throws Exception{
        ImportCountDto importCount = new ImportCountDto();
        importCount.setImported(1);
        importCount.setUpdated(0);
        importCount.setIgnored(0);
        importCount.setDeleted(0);

        response = new DhisStatusResponse();
        response.setReference(INSTANCE_DHIS_ID);
        response.setStatus(DhisStatus.SUCCESS);
        response.setImportCountDto(importCount);
    }

    @Test
    public void testCreate() throws Exception {
        List<AttributeDto> attributeDtos = new ArrayList<>();
        AttributeDto dto = new AttributeDto();
        dto.setAttribute(ATTRIBUTE_ID);
        dto.setValue(ATTRIBUTE_VALUE);
        attributeDtos.add(dto);

        TrackedEntityInstanceDto instance = new TrackedEntityInstanceDto();
        instance.setTrackedEntity(ENTITY_TYPE_PERSON);
        instance.setAttributes(attributeDtos);
        instance.setOrgUnit(ORGUNIT_ID);

        when(dhisWebservice.createTrackedEntityInstance(instance)).thenReturn(response);

        Map<String,Object> params = new HashMap<>();
        params.put(EventParams.EXTERNAL_ID,ENTITY_INSTANCE_ID);
        params.put(EventParams.ENTITY_TYPE, ENTITY_TYPE_PERSON );
        params.put(EventParams.LOCATION, ORGUNIT_ID);
        params.put(ATTRIBUTE_ID, ATTRIBUTE_VALUE);

        dhisService.createEntity(params);

        verify(trackedEntityInstanceMappingService).create(ENTITY_INSTANCE_ID, INSTANCE_DHIS_ID);
        verify(dhisWebservice).createTrackedEntityInstance(instance);
    }


    @Test
    public void testEnrollment() throws Exception {
        List<AttributeDto> attributeDtos = new ArrayList<>();
        AttributeDto dto = new AttributeDto();
        dto.setAttribute(ATTRIBUTE_ID);
        dto.setValue(ATTRIBUTE_VALUE);
        attributeDtos.add(dto);

        EnrollmentDto enrollment = new EnrollmentDto();
        enrollment.setAttributes(attributeDtos);
        enrollment.setDateOfEnrollment(new JodaFormatter().formatDateTime(new DateTime(DATE)));
        enrollment.setProgram(PROGRAM_ID);
        enrollment.setTrackedEntityInstance(INSTANCE_DHIS_ID);

        when(trackedEntityInstanceMappingService.mapFromExternalId(ENTITY_INSTANCE_ID))
                .thenReturn(INSTANCE_DHIS_ID);

        when(dhisWebservice.createEnrollment(enrollment)).thenReturn(response);

        Map<String,Object> params = new HashMap<>();
        params.put(EventParams.PROGRAM,PROGRAM_ID );
        params.put(EventParams.EXTERNAL_ID, ENTITY_INSTANCE_ID);
        params.put(EventParams.DATE, new DateTime(DATE));
        params.put(ATTRIBUTE_ID, ATTRIBUTE_VALUE);

        dhisService.enrollInProgram(params);

        verify(trackedEntityInstanceMappingService).mapFromExternalId(ENTITY_INSTANCE_ID);
        verify(dhisWebservice).createEnrollment(enrollment);

    }

    @Test
    public void testEventWithRegistration() throws Exception {
        List<DataValueDto> dataValues = new ArrayList<>();
        DataValueDto datavalue = new DataValueDto();
        datavalue.setDataElement(DATA_ELEMENT_ID);
        datavalue.setValue(DATA_ELEMENT_VALUE);
        dataValues.add(datavalue);

        DhisEventDto programStageDto = new DhisEventDto();
        programStageDto.setDataValues(dataValues);
        programStageDto.setTrackedEntityInstance(INSTANCE_DHIS_ID);
        programStageDto.setProgram(PROGRAM_ID);
        programStageDto.setEventDate(new JodaFormatter().formatDateTime(new DateTime(DATE)));
        programStageDto.setOrgUnit(ORGUNIT_ID);
        programStageDto.setProgramStage(STAGE_ID);
        programStageDto.setStatus(STATUS);

        when(dhisWebservice.createEvent(programStageDto)).thenReturn(response);
        when(trackedEntityInstanceMappingService.mapFromExternalId(ENTITY_INSTANCE_ID))
                .thenReturn(INSTANCE_DHIS_ID);

        Map<String, Object> params = new HashMap<>();
        params.put(EventParams.REGISTRATION, REGISTRATION);
        params.put(EventParams.EXTERNAL_ID, ENTITY_INSTANCE_ID);
        params.put(EventParams.LOCATION, ORGUNIT_ID);
        params.put(EventParams.PROGRAM, PROGRAM_ID);
        params.put(EventParams.DATE, new DateTime(DATE));
        params.put(EventParams.STAGE,STAGE_ID);
        params.put(EventParams.STATUS, STATUS);
        params.put(DATA_ELEMENT_ID, DATA_ELEMENT_VALUE);

        dhisService.updateProgramStage(params);

        verify(trackedEntityInstanceMappingService).mapFromExternalId(ENTITY_INSTANCE_ID);
        verify(dhisWebservice).createEvent(programStageDto);
    }

    @Test
    public void testCreateAndEnroll() throws Exception {
        List<AttributeDto> attributeDtos = new ArrayList<>();
        AttributeDto dto = new AttributeDto();
        dto.setAttribute(ATTRIBUTE_ID);
        dto.setValue(ATTRIBUTE_VALUE);
        attributeDtos.add(dto);

        EnrollmentDto enrollment = new EnrollmentDto();
        enrollment.setAttributes(new ArrayList<>());
        enrollment.setDateOfEnrollment(new JodaFormatter().formatDateTime(new DateTime(DATE)));
        enrollment.setProgram(PROGRAM_ID);
        enrollment.setOrgUnit(ORGUNIT_ID);
        enrollment.setTrackedEntityInstance(INSTANCE_DHIS_ID);

        TrackedEntityInstanceDto instance = new TrackedEntityInstanceDto();
        instance.setTrackedEntity(ENTITY_TYPE_PERSON);
        instance.setAttributes(attributeDtos);
        instance.setOrgUnit(ORGUNIT_ID);

        when(dhisWebservice.createTrackedEntityInstance(instance)).thenReturn(response);
        when(dhisWebservice.createEnrollment(enrollment)).thenReturn(response);
        when(trackedEntityInstanceMappingService.mapFromExternalId(ENTITY_INSTANCE_ID))
                .thenReturn(INSTANCE_DHIS_ID);

        Map<String,Object> params = new HashMap<>();
        params.put(EventParams.EXTERNAL_ID,ENTITY_INSTANCE_ID);
        params.put(EventParams.ENTITY_TYPE, ENTITY_TYPE_PERSON );
        params.put(EventParams.LOCATION,ORGUNIT_ID);
        params.put(EventParams.PROGRAM,PROGRAM_ID );
        params.put(EventParams.DATE, new DateTime(DATE));
        params.put(ATTRIBUTE_ID, ATTRIBUTE_VALUE);

        dhisService.createAndEnroll(params);

        verify(dhisWebservice).createTrackedEntityInstance(instance);
        verify(dhisWebservice).createEnrollment(enrollment);

    }

    @Test
    public void testHandleDataValue() {

        DataValueDto dataValueDto = new DataValueDto();
        dataValueDto.setDataElement(DATA_ELEMENT_ID);
        dataValueDto.setValue(DATA_ELEMENT_VALUE);
        dataValueDto.setOrgUnit(ORGUNIT_ID);
        dataValueDto.setPeriod(PERIOD_VALUE);
        dataValueDto.setCategoryOptionCombo(CATEGORY_COMBO);
        dataValueDto.setComment(COMMENT);

        DataValueSetDto dataValueSetDto = new DataValueSetDto();
        List<DataValueDto> dataValueDtos = new ArrayList<>();
        dataValueDtos.add(dataValueDto);
        dataValueSetDto.setDataValues(dataValueDtos);

        Map<String, Object> params = new HashMap<>();
        params.put(EventParams.DATA_ELEMENT, DATA_ELEMENT_ID);
        params.put(EventParams.LOCATION, ORGUNIT_ID);
        params.put(EventParams.PERIOD, new JodaFormatter().parsePeriod(PERIOD_VALUE));
        params.put(EventParams.VALUE, DATA_ELEMENT_VALUE);
        params.put(EventParams.CATEGORY_OPTION_COMBO, CATEGORY_COMBO);
        params.put(EventParams.COMMENT, COMMENT);

        DataElement dataElement = new DataElement(DATA_ELEMENT_VALUE, DATA_ELEMENT_ID);

        when(dataElementService.findByName(DATA_ELEMENT_ID)).thenReturn(dataElement);

        dhisService.sendDataValue(params);

        verify(dhisWebservice).sendDataValueSet(Matchers.refEq(dataValueSetDto));
    }

    @Test(expected = DataElementNotFoundException.class)
    public void shouldThrowExceptionForNonExistingDataValue() {

        Map<String, Object> params = new HashMap<>();
        params.put(EventParams.DATA_ELEMENT, DATA_ELEMENT_ID);
        params.put(EventParams.LOCATION, ORGUNIT_ID);
        params.put(EventParams.PERIOD, PERIOD_VALUE);
        params.put(EventParams.VALUE, DATA_ELEMENT_VALUE);
        params.put(EventParams.CATEGORY_OPTION_COMBO, CATEGORY_COMBO);
        params.put(EventParams.COMMENT, COMMENT);

        when(dataElementService.findByName(DATA_ELEMENT_ID)).thenReturn(null);

        dhisService.sendDataValue(params);
    }
}
