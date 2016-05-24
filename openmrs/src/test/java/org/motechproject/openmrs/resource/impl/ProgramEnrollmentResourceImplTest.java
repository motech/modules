package org.motechproject.openmrs.resource.impl;

import com.google.gson.JsonObject;
import org.hamcrest.CoreMatchers;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.motechproject.openmrs.config.Config;
import org.motechproject.openmrs.config.ConfigDummyData;
import org.motechproject.openmrs.domain.Location;
import org.motechproject.openmrs.domain.Patient;
import org.motechproject.openmrs.domain.Program;
import org.motechproject.openmrs.domain.ProgramEnrollment;
import org.motechproject.openmrs.util.JsonUtils;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.web.client.RestOperations;

import java.net.URI;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class ProgramEnrollmentResourceImplTest extends AbstractResourceImplTest {

    private static final String PROGRAM_ENROLLMENT_CREATE = "json/programEnrollment/program-enrollment-create.json";
    private static final String PROGRAM_ENROLLMENT_RESPONSE = "json/programEnrollment/program-enrollment-response.json";
    private static final String PROGRAM_ENROLLMENT_TABLE = "json/programEnrollment/program-enrollment-table.json";

    @Mock
    private RestOperations restOperations;

    @Captor
    private ArgumentCaptor<HttpEntity<String>> requestCaptor;

    private ProgramEnrollmentResourceImpl programEnrollmentResource;

    private Config config;

    @Before
    public void setUp() {
        initMocks(this);
        programEnrollmentResource = new ProgramEnrollmentResourceImpl(restOperations);
        config = ConfigDummyData.prepareConfig("one");
    }

    @Test
    public void shouldCreateProgramEnrollment() throws Exception {
        ProgramEnrollment programEnrollment = prepareProgramEnrollment();

        URI url = config.toInstancePath("/programenrollment");

        when(restOperations.exchange(eq(url), eq(HttpMethod.POST), any(HttpEntity.class), eq(String.class)))
                .thenReturn(getResponse(PROGRAM_ENROLLMENT_RESPONSE));

        ProgramEnrollment created = programEnrollmentResource.createProgramEnrollment(config, programEnrollment);

        verify(restOperations).exchange(eq(url), eq(HttpMethod.POST), requestCaptor.capture(), eq(String.class));

        assertThat(created, equalTo(programEnrollment));
        assertThat(requestCaptor.getValue().getHeaders(), equalTo(getHeadersForPost(config)));
        assertThat(JsonUtils.readJson(requestCaptor.getValue().getBody(), JsonObject.class),
                equalTo(readFromFile(PROGRAM_ENROLLMENT_CREATE, JsonObject.class)));
    }

    @Test
    public void shouldUpdateProgramEnrollment() throws Exception {
        ProgramEnrollment programEnrollment = prepareProgramEnrollment();

        URI url = config.toInstancePathWithParams("/programenrollment/{uuid}", programEnrollment.getUuid());

        when(restOperations.exchange(eq(url), eq(HttpMethod.POST), any(HttpEntity.class), eq(String.class)))
                .thenReturn(getResponse(PROGRAM_ENROLLMENT_RESPONSE));

        ProgramEnrollment updated = programEnrollmentResource.updateProgramEnrollment(config, programEnrollment);

        verify(restOperations).exchange(eq(url), eq(HttpMethod.POST), requestCaptor.capture(), eq(String.class));

        assertThat(updated, equalTo(programEnrollment));
        assertThat(requestCaptor.getValue().getHeaders(), equalTo(getHeadersForPost(config)));
        assertThat(JsonUtils.readJson(requestCaptor.getValue().getBody(), JsonObject.class),
                equalTo(readFromFile(PROGRAM_ENROLLMENT_CREATE, JsonObject.class)));
    }

    @Test
    public void shouldGetProgramEnrollmentByPatientUuid() throws Exception {
        ProgramEnrollment programEnrollment = prepareProgramEnrollment();

        URI url = config.toInstancePathWithParams("/programenrollment?patient={uuid}&v=full", programEnrollment.getPatient().getUuid());

        when(restOperations.exchange(eq(url), eq(HttpMethod.GET), any(HttpEntity.class), eq(String.class)))
                .thenReturn(getResponse(PROGRAM_ENROLLMENT_TABLE));

        List<ProgramEnrollment> fetched = programEnrollmentResource.getProgramEnrollmentByPatientUuid(config, programEnrollment.getPatient().getUuid());

        verify(restOperations).exchange(eq(url), eq(HttpMethod.GET), requestCaptor.capture(), eq(String.class));

        assertThat(fetched, hasItem(programEnrollment));
        assertThat(fetched.size(), equalTo(1));
        assertThat(requestCaptor.getValue().getHeaders(), equalTo(getHeadersForGet(config)));
        assertThat(JsonUtils.readJson(requestCaptor.getValue().getBody(), JsonObject.class), nullValue());
    }

    private ProgramEnrollment prepareProgramEnrollment() throws Exception {
        return (ProgramEnrollment) readFromFile(PROGRAM_ENROLLMENT_RESPONSE, ProgramEnrollment.class);
    }
}
