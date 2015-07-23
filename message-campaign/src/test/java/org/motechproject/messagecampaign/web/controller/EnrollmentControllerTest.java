package org.motechproject.messagecampaign.web.controller;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.apache.commons.io.IOUtils;
import org.joda.time.LocalDate;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.motechproject.commons.date.model.Time;
import org.motechproject.commons.date.util.DateUtil;
import org.motechproject.messagecampaign.contract.CampaignRequest;
import org.motechproject.messagecampaign.domain.campaign.CampaignEnrollment;
import org.motechproject.messagecampaign.domain.campaign.CampaignEnrollmentStatus;
import org.motechproject.messagecampaign.exception.CampaignAlreadyEndedException;
import org.motechproject.messagecampaign.exception.EnrollmentAlreadyExists;
import org.motechproject.messagecampaign.exception.MessageCampaignException;
import org.motechproject.messagecampaign.exception.SchedulingException;
import org.motechproject.messagecampaign.web.api.EnrollmentRestController;
import org.motechproject.messagecampaign.web.model.EnrollmentList;
import org.motechproject.messagecampaign.web.model.EnrollmentRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.server.MockMvc;
import org.springframework.test.web.server.setup.MockMvcBuilders;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.Arrays;

import static java.util.Arrays.asList;
import static junit.framework.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.motechproject.testing.utils.rest.RestTestUtil.jsonMatcher;
import static org.springframework.test.web.server.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.server.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.server.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.server.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.server.result.MockMvcResultMatchers.status;

public class EnrollmentControllerTest {

    private static final MediaType APPLICATION_JSON_UTF8 = new MediaType("application", "json", Charset.forName("UTF-8"));
    private static final Gson GSON = new GsonBuilder().create();

    private static final String EXTERNAL_ID = "externalId";
    private static final String CAMPAIGN_NAME = "Friday Campaign";

    private MockMvc controller;

    @InjectMocks
    private EnrollmentController enrollmentController = new EnrollmentController();

    @Mock
    EnrollmentRestController enrollmentRestController;

    private CampaignRequest enrollRequest = new CampaignRequest(
            EXTERNAL_ID, CAMPAIGN_NAME, new LocalDate(2020, 7, 10), null
    );

    @Before
    public void setUp() throws Exception {
        initMocks(this);
        controller = MockMvcBuilders.standaloneSetup(enrollmentController).build();
    }

    @Test
    public void shouldGetAllEnrollments() throws Exception {
        CampaignEnrollment enrollment1 = new CampaignEnrollment("47sf6a", "PREGNANCY");
        enrollment1.setDeliverTime(new Time(20, 1));
        enrollment1.setReferenceDate(new LocalDate(2012, 1, 2));
        enrollment1.setId(9001L);

        CampaignEnrollment enrollment2 = new CampaignEnrollment("d6gt40", "PREGNANCY");
        enrollment2.setDeliverTime(new Time(10, 0));
        enrollment2.setReferenceDate(new LocalDate(2012, 2, 15));
        enrollment2.setId(9002L);

        CampaignEnrollment enrollment3 = new CampaignEnrollment("o34j6f", "CHILD_DEVELOPMENT");
        enrollment3.setDeliverTime(new Time(10, 0));
        enrollment3.setReferenceDate(new LocalDate(2012, 3, 13));
        enrollment3.setId(9003L);

        when(enrollmentRestController.getAllEnrollments(CampaignEnrollmentStatus.ACTIVE.name(), null, null))
                .thenReturn(new EnrollmentList(asList(enrollment1, enrollment2, enrollment3))
                );

        final String expectedResponse = loadJson("rest/enrollments/enrollmentList.json");

        controller.perform(
                get("/enrollments/users")
        ).andExpect(
                status().is(HttpStatus.OK.value())
        ).andExpect(
                content().type(APPLICATION_JSON_UTF8)
        ).andExpect(
                content().string(jsonMatcher(expectedResponse))
        );

        verify(enrollmentRestController).getAllEnrollments(CampaignEnrollmentStatus.ACTIVE.name(), null, null);
    }

    @Test
    public void shouldCreateEnrollment() throws Exception {
        controller.perform(
                post("/enrollments/{campaignName}/users", CAMPAIGN_NAME)
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("externalId", EXTERNAL_ID).param("enrollmentId", "9001")
        ).andExpect(
                status().is(HttpStatus.OK.value())
        );

        ArgumentCaptor<EnrollmentRequest> captor = ArgumentCaptor.forClass(EnrollmentRequest.class);
        verify(enrollmentRestController).enrollOrUpdateUser(eq(CAMPAIGN_NAME), eq(EXTERNAL_ID), captor.capture());

        assertEquals(DateUtil.today(), captor.getValue().getReferenceDate());
        assertEquals(new Long(9001L), captor.getValue().getEnrollmentId());
    }

    @Test
    public void shouldDeleteEnrollment() throws Exception {
        controller.perform(
                delete("/enrollments/{campaignName}/users/{externalId}", CAMPAIGN_NAME, EXTERNAL_ID)
        ).andExpect(
                status().is(HttpStatus.OK.value())
        );

        verify(enrollmentRestController).removeEnrollment(CAMPAIGN_NAME, EXTERNAL_ID);
    }

    @Test
    public void shouldReturn400WhenTryingToEnrollEnrolledEnrollee() throws Exception {

        MessageCampaignException.MessageKey message = new MessageCampaignException.MessageKey("msgCampaign.error.enrollmentAlreadyExists",
                Arrays.asList(EXTERNAL_ID, CAMPAIGN_NAME));

        doThrow(new EnrollmentAlreadyExists(EXTERNAL_ID, CAMPAIGN_NAME))
                .when(enrollmentRestController).enrollOrUpdateUser(any(String.class), any(String.class), any(EnrollmentRequest.class));

        controller.perform(
                post("/enrollments/{campaignName}/users", CAMPAIGN_NAME)
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("externalId", EXTERNAL_ID).param("enrollmentId", "9001")
        ).andExpect(
                status().is(HttpStatus.BAD_REQUEST.value())
        ).andExpect(
                content().string(jsonMatcher(GSON.toJson(message)))
        );
    }

    @Test
    public void shouldReturn400WhenTryingToEnrollEnrolleeToFinishedCampaign() throws Exception {

        MessageCampaignException.MessageKey message = new MessageCampaignException.MessageKey("msgCampaign.error.campaignAlreadyEnded",
                Arrays.asList(CAMPAIGN_NAME));

        doThrow(new CampaignAlreadyEndedException(CAMPAIGN_NAME, null))
                .when(enrollmentRestController).enrollOrUpdateUser(any(String.class), any(String.class), any(EnrollmentRequest.class));

        controller.perform(
                post("/enrollments/{campaignName}/users", CAMPAIGN_NAME)
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("externalId", EXTERNAL_ID).param("enrollmentId", "9001")
        ).andExpect(
                status().is(HttpStatus.BAD_REQUEST.value())
        ).andExpect(
                content().string(jsonMatcher(GSON.toJson(message)))
        );
    }

    @Test
    public void shouldReturn500WhenFailedToScheduleJobForEnrollment() throws Exception {

        MessageCampaignException.MessageKey message = new MessageCampaignException.MessageKey("msgCampaign.error.schedulingError",
                Arrays.asList(EXTERNAL_ID));

        doThrow(new SchedulingException(EXTERNAL_ID, null))
                .when(enrollmentRestController).enrollOrUpdateUser(any(String.class), any(String.class), any(EnrollmentRequest.class));

        controller.perform(
                post("/enrollments/{campaignName}/users", CAMPAIGN_NAME)
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("externalId", EXTERNAL_ID).param("enrollmentId", "9001")
        ).andExpect(
                status().is(HttpStatus.INTERNAL_SERVER_ERROR.value())
        ).andExpect(
                content().string(jsonMatcher(GSON.toJson(message)))
        );
    }

    private String loadJson(String filename) throws IOException {
        try (InputStream in = getClass().getClassLoader().getResourceAsStream(filename)) {
            return IOUtils.toString(in);
        }
    }
}
