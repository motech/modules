package org.motechproject.openmrs.resource.impl;

import com.google.gson.JsonObject;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.motechproject.openmrs.config.Config;
import org.motechproject.openmrs.config.ConfigDummyData;
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

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class ProgramEnrollmentResourceImplTest extends AbstractResourceImplTest {

    private static final String PROGRAM_ENROLLMENT_CREATE = "json/programEnrollment/program-enrollment-create.json";
    private static final String PROGRAM_ENROLLMENT_RESPONSE = "json/programEnrollment/program-enrollment-response.json";

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

    private ProgramEnrollment prepareProgramEnrollment() throws Exception {
        Program program = new Program();
        program.setUuid("37677597-27e3-4613-9514-e4d2b7b89cfd");

        Patient patient = new Patient();
        patient.setUuid("159ff70f-cc96-4d18-a252-e0aac0481a39");

        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
        Date dateEnrolled = dateFormat.parse("2016-01-01T00:00:00.000+0200");
        Date dateCompleted = dateFormat.parse("2016-12-12T00:00:00.000+0200");

        ProgramEnrollment programEnrollment = new ProgramEnrollment();
        programEnrollment.setProgram(program);
        programEnrollment.setPatient(patient);
        programEnrollment.setDateEnrolled(dateEnrolled);
        programEnrollment.setDateCompleted(dateCompleted);

        return programEnrollment;
    }
}
