package org.motechproject.dhis2.service.impl;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.motechproject.dhis2.domain.Program;
import org.motechproject.dhis2.domain.TrackedEntity;
import org.motechproject.dhis2.repository.ProgramDataService;
import org.motechproject.dhis2.rest.domain.ProgramDto;
import org.motechproject.dhis2.service.ProgramService;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.core.IsEqual.equalTo;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.springframework.test.util.MatcherAssertionErrors.assertThat;

public class ProgramServiceImplTest {
    private static final String ID = "id";
    private static final String NAME = "NAME";

    @Mock
    private ProgramDataService programDataService;

    @InjectMocks
    private ProgramService programService;

    @Before
    public void setUp() {
        programService = new ProgramServiceImpl();
        initMocks(this);
    }

    @Test
    public void shouldCreateFromDetails() {
        ProgramDto actual = new ProgramDto(ID, NAME);

        Program expected = new Program(actual.getId(), actual.getName(), new TrackedEntity(), new ArrayList<>(),
                new ArrayList<>(), false, true, null);

        when(programDataService.create(eq(expected))).thenReturn(expected);
        Program created = programService.createFromDetails(actual, new TrackedEntity(), new ArrayList<>(), new ArrayList<>());

        assertThat(created, equalTo(expected));

        verify(programDataService).create(eq(expected));
    }

    @Test
    public void shouldFindByRegistration() {
        List<Program> expectedPrograms = new ArrayList<>();

        expectedPrograms.add(new Program(ID, NAME, new TrackedEntity(), new ArrayList<>(), new ArrayList<>(), false, true, null));
        when(programDataService.findByRegistration(true)).thenReturn(expectedPrograms);

        List<Program> createdPrograms = programService.findByRegistration(true);
        assertThat(createdPrograms, equalTo(expectedPrograms));

        verify(programDataService).findByRegistration(true);
    }

    @Test
    public void shouldDeleteAll() {
        programService.deleteAll();
        verify(programDataService).deleteAll();
    }

}
