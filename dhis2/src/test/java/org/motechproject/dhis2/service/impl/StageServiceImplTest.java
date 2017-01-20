package org.motechproject.dhis2.service.impl;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.motechproject.dhis2.domain.Stage;
import org.motechproject.dhis2.repository.StageDataService;
import org.motechproject.dhis2.rest.domain.ProgramDto;
import org.motechproject.dhis2.rest.domain.ProgramStageDto;

import java.util.ArrayList;

import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class StageServiceImplTest {

    private static final String ID = "id";
    private static final String NAME = "NAME";
    private static final String PROGRAM_ID = "ProgramId";

    @Mock
    private StageDataService stageDataService;

    @InjectMocks
    private StageServiceImpl stageService;

    @Before
    public void setUp() {
        stageService = new StageServiceImpl();
        initMocks(this);
    }

    @Test
    public void shouldCreateFromDetails() {
        ProgramStageDto actual = new ProgramStageDto(ID, NAME, new ProgramDto(PROGRAM_ID, NAME), new ArrayList<>());
        Stage expected = new Stage(ID, NAME, new ArrayList<>(), PROGRAM_ID, true);

        when(stageDataService.create(eq(expected))).thenReturn(expected);
        Stage created = stageService.createFromDetails(actual, actual.getProgram().getId(), true, new ArrayList<>());

        assertThat(created, equalTo(expected));

        verify(stageDataService).create(eq(expected));
    }

    @Test
    public void shouldDeleteAll() {
        stageService.deleteAll();
        verify(stageDataService).deleteAll();
    }
}
