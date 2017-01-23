package org.motechproject.dhis2.service.impl;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.motechproject.dhis2.domain.DataElement;
import org.motechproject.dhis2.repository.DataElementDataService;
import org.motechproject.dhis2.rest.domain.DataElementDto;
import org.motechproject.dhis2.service.DataElementService;

import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class DataElementServiceImplTest {

    private static final String ID = "id";
    private static final String NAME = "NAME";

    @Mock
    private DataElementDataService dataElementDataService;

    @InjectMocks
    private DataElementService dataElementService;

    @Before
    public void setUp() {
        dataElementService = new DataElementServiceImpl();
        initMocks(this);
    }

    @Test
    public void shouldCreateFromDetails() {
        DataElementDto actual = new DataElementDto(ID, NAME);
        DataElement expected = new DataElement(actual.getName(), actual.getId());

        when(dataElementDataService.create(eq(expected))).thenReturn(expected);
        DataElement created = dataElementService.createFromDetails(actual);

        assertThat(created, equalTo(expected));

        verify(dataElementDataService).create(eq(expected));
    }

    @Test
    public void shouldDeleteAll() {
        dataElementService.deleteAll();
        verify(dataElementDataService).deleteAll();
    }
}
