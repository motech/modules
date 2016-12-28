package org.motechproject.dhis2.service.impl;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.motechproject.dhis2.domain.DataElement;
import org.motechproject.dhis2.domain.DataSet;
import org.motechproject.dhis2.repository.DataSetDataService;
import org.motechproject.dhis2.rest.domain.DataElementDto;
import org.motechproject.dhis2.rest.domain.DataSetDto;
import org.motechproject.dhis2.service.DataElementService;
import org.motechproject.dhis2.service.DataSetService;

import java.util.ArrayList;
import java.util.List;

import static junit.framework.TestCase.assertNotNull;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class DataSetServiceImplTest {

    private static final String ID = "id";
    private static final String NAME = "NAME";

    @Mock
    private DataSetDataService dataSetDataService;

    @Mock
    private DataElementService dataElementService;

    @InjectMocks
    private DataSetService dataSetService;

    @Before
    public void setUp() {
        dataSetService = new DataSetServiceImpl();
        initMocks(this);
    }

    @Test
    public void shouldCreateFromDetails() {
        DataSetDto dto = prepareDataSetDto();
        DataSet expected = prepareDataSet(dto);

        createFromDetails(dto, expected);
    }

    @Test
    public void shouldCreateFromDetailsWhenDataElementListIsNull() {
        DataSetDto dto = prepareDataSetDtoWithNullDataElementList();
        assertNotNull(dto.getDataElements());

        DataSet expected = prepareDataSet(dto);

        createFromDetails(dto, expected);
    }

    @Test
    public void shouldDeleteAll() {
        dataSetService.deleteAll();

        verify(dataSetDataService).deleteAll();
    }

    private void createFromDetails(DataSetDto actual, DataSet expected) {
        for (DataElementDto dataElementDto : actual.getDataElements()) {
            when(dataElementService.findById(eq(dataElementDto.getId()))).thenReturn(prepareDataElement(dataElementDto));
        }
        when(dataSetDataService.create(eq(expected))).thenReturn(expected);

        DataSet created = dataSetService.createFromDetails(actual);

        assertThat(created, equalTo(expected));

        for (DataElementDto dataElementDto : actual.getDataElements()) {
            verify(dataElementService).findById(dataElementDto.getId());
        }
        verify(dataSetDataService).create(eq(expected));
    }

    private DataSetDto prepareDataSetDto() {
        DataSetDto dto = new DataSetDto();

        dto.setId(ID);
        dto.setName(NAME);

        List<DataElementDto> dataElementDtos = new ArrayList<>();
        dataElementDtos.add(prepareDataElementDto("One"));
        dataElementDtos.add(prepareDataElementDto("Two"));
        dto.setDataElements(dataElementDtos);

        return dto;
    }

    private DataSetDto prepareDataSetDtoWithNullDataElementList() {
        DataSetDto dto = new DataSetDto();

        dto.setId(ID);
        dto.setName(NAME);
        dto.setDataElements(null);

        return dto;
    }

    private DataElementDto prepareDataElementDto(String suffix) {
        DataElementDto dto = new DataElementDto();

        dto.setId(ID + suffix);
        dto.setName(NAME + suffix);

        return dto;
    }

    private DataSet prepareDataSet(DataSetDto dto) {
        DataSet dataSet = new DataSet();

        dataSet.setName(dto.getName());
        dataSet.setUuid(dto.getId());

        List<DataElement> dataElements = new ArrayList<>();
        for (DataElementDto dataElementDto : dto.getDataElements()) {
            dataElements.add(prepareDataElement(dataElementDto));
        }
        dataSet.setDataElementList(dataElements);

        return dataSet;
    }

    private DataElement prepareDataElement(DataElementDto dto) {
        DataElement dataElement = new DataElement();

        dataElement.setName(dto.getName());
        dataElement.setUuid(dto.getId());

        return dataElement;
    }

}