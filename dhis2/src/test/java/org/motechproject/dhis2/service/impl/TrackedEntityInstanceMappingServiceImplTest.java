package org.motechproject.dhis2.service.impl;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.motechproject.dhis2.domain.TrackedEntityInstanceMapping;
import org.motechproject.dhis2.repository.TrackedEntityInstanceMappingDataService;
import org.motechproject.dhis2.service.TrackedEntityInstanceMappingService;

import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class TrackedEntityInstanceMappingServiceImplTest {
    private static final String EXTERNAL_NAME = "externalName";
    private static final String DHIS2_ID = "dhis2Id";

    @Mock
    private TrackedEntityInstanceMappingDataService trackedEntityInstanceMappingDataService;

    @InjectMocks
    private TrackedEntityInstanceMappingService trackedEntityInstanceMappingService;

    @Before
    public void setUp() {
        trackedEntityInstanceMappingService = new TrackedEntityInstanceMappingServiceImpl();
        initMocks(this);
    }

    @Test
    public void shouldCreate() {
        TrackedEntityInstanceMapping expected = new TrackedEntityInstanceMapping(EXTERNAL_NAME, DHIS2_ID);

        when(trackedEntityInstanceMappingDataService.create(eq(expected))).thenReturn(expected);
        TrackedEntityInstanceMapping created = trackedEntityInstanceMappingService.create(EXTERNAL_NAME, DHIS2_ID);

        assertThat(created, equalTo(expected));

        verify(trackedEntityInstanceMappingDataService).create(eq(expected));
    }

    @Test
    public void shouldMapFromExternalId() {
        TrackedEntityInstanceMapping expected = new TrackedEntityInstanceMapping(EXTERNAL_NAME, DHIS2_ID);

        when(trackedEntityInstanceMappingDataService.findByExternalName(EXTERNAL_NAME)).thenReturn(expected);

        String fetchedDhis2Uuid = trackedEntityInstanceMappingService.mapFromExternalId(EXTERNAL_NAME);

        assertEquals(expected.getDhis2Uuid(), fetchedDhis2Uuid);
        verify(trackedEntityInstanceMappingDataService).findByExternalName(EXTERNAL_NAME);
    }

    @Test
    public void shouldDeleteAll() {
        trackedEntityInstanceMappingService.deleteAll();
        verify(trackedEntityInstanceMappingDataService).deleteAll();
    }
}
