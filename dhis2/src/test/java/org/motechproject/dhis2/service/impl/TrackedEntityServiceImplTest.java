package org.motechproject.dhis2.service.impl;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.motechproject.dhis2.domain.TrackedEntity;
import org.motechproject.dhis2.repository.TrackedEntityDataService;
import org.motechproject.dhis2.rest.domain.TrackedEntityDto;
import org.motechproject.dhis2.service.TrackedEntityService;

import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class TrackedEntityServiceImplTest {
    private static final String ID = "id";
    private static final String NAME = "NAME";

    @Mock
    private TrackedEntityDataService trackedEntityDataService;

    @InjectMocks
    private TrackedEntityService trackedEntityService;

    @Before
    public void setUp() {
        trackedEntityService = new TrackedEntityServiceImpl();
        initMocks(this);
    }

    @Test
    public void shouldCreateFromDetails() {
        TrackedEntityDto actual = new TrackedEntityDto(ID, NAME);
        TrackedEntity expected = new TrackedEntity(actual.getName(), actual.getId());

        when(trackedEntityDataService.create(eq(expected))).thenReturn(expected);
        TrackedEntity created = trackedEntityService.createFromDetails(actual);

        assertThat(created, equalTo(expected));

        verify(trackedEntityDataService).create(eq(expected));
    }

    @Test
    public void shouldDeleteAll() {
        trackedEntityService.deleteAll();
        verify(trackedEntityDataService).deleteAll();
    }
}
