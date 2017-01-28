package org.motechproject.dhis2.service.impl;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.motechproject.dhis2.domain.OrgUnit;
import org.motechproject.dhis2.repository.OrgUnitDataService;
import org.motechproject.dhis2.rest.domain.OrganisationUnitDto;
import org.motechproject.dhis2.service.OrgUnitService;

import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class OrgUnitServiceImplTest {

    private static final String ID = "id";
    private static final String NAME = "NAME";

    @Mock
    private OrgUnitDataService orgUnitDataService;

    @InjectMocks
    private OrgUnitService orgUnitService;

    @Before
    public void setUp() {
        orgUnitService = new OrgUnitServiceImpl();
        initMocks(this);
    }

    @Test
    public void shouldCreateFromDetails() {
        OrganisationUnitDto actual = new OrganisationUnitDto(ID, NAME);
        OrgUnit expected = new OrgUnit(actual.getName(), actual.getId());

        when(orgUnitDataService.create(eq(expected))).thenReturn(expected);
        OrgUnit created = orgUnitService.createFromDetails(actual);

        assertThat(created, equalTo(expected));

        verify(orgUnitDataService).create(eq(expected));
    }

    @Test
    public void shouldDeleteAll() {
        orgUnitService.deleteAll();
        verify(orgUnitDataService).deleteAll();
    }
}
