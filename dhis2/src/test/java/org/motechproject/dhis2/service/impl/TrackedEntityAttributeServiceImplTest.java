package org.motechproject.dhis2.service.impl;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.motechproject.dhis2.domain.TrackedEntityAttribute;
import org.motechproject.dhis2.repository.TrackedEntityAttributeDataService;
import org.motechproject.dhis2.rest.domain.TrackedEntityAttributeDto;
import org.motechproject.dhis2.service.TrackedEntityAttributeService;

import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class TrackedEntityAttributeServiceImplTest {
    private static final String ID = "id";
    private static final String NAME = "NAME";

    @Mock
    private TrackedEntityAttributeDataService trackedEntityAttributeDataService;

    @InjectMocks
    private TrackedEntityAttributeService trackedEntityAttributeService;

    @Before
    public void setUp() {
        trackedEntityAttributeService = new TrackedEntityAttributeServiceImpl();
        initMocks(this);
    }

    @Test
    public void shouldCreateFromDetails() {
        TrackedEntityAttributeDto actual = new TrackedEntityAttributeDto(ID, NAME);
        TrackedEntityAttribute expected = new TrackedEntityAttribute(actual.getName(), actual.getId());

        when(trackedEntityAttributeDataService.create(eq(expected))).thenReturn(expected);
        TrackedEntityAttribute created = trackedEntityAttributeService.createFromDetails(actual);

        assertThat(created, equalTo(expected));

        verify(trackedEntityAttributeDataService).create(eq(expected));
    }

    @Test
    public void shouldDeleteAll() {
        trackedEntityAttributeService.deleteAll();
        verify(trackedEntityAttributeDataService).deleteAll();
    }
}
