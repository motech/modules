package org.motechproject.messagecampaign.web.controller;

import org.apache.commons.io.IOUtils;
import org.joda.time.LocalDate;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.motechproject.messagecampaign.domain.campaign.CampaignType;
import org.motechproject.messagecampaign.service.MessageCampaignService;
import org.motechproject.messagecampaign.domain.message.CampaignMessageRecord;
import org.motechproject.messagecampaign.domain.campaign.CampaignRecord;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.server.MockMvc;
import org.springframework.test.web.server.setup.MockMvcBuilders;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;

import static java.util.Arrays.asList;
import static junit.framework.Assert.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.motechproject.testing.utils.rest.RestTestUtil.jsonMatcher;
import static org.springframework.test.web.server.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.server.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.server.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.server.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.server.result.MockMvcResultMatchers.status;

public class CampaignControllerTest {
    private static final String ABSOLUTE_CAMPAIGN_NAME = "Absolute Dates Message Program";
    private static final String REPEAT_INTERVAL_CAMPAIGN_NAME = "Relative Parameterized Dates Message Program";
    private static final MediaType APPLICATION_JSON_UTF8 = new MediaType("application", "json", Charset.forName("UTF-8"));
    private MockMvc controller;

    @InjectMocks
    CampaignController campaignController = new CampaignController();

    @Mock
    private MessageCampaignService messageCampaignService;

    @Before
    public void setup() {
        initMocks(this);
        controller = MockMvcBuilders.standaloneSetup(campaignController).build();
    }

    @Test
    public void shouldCreateCampaign() throws Exception {
        controller.perform(
                post("/campaigns")
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(loadJson("rest/campaigns/campaignDetails.json").getBytes("UTF-8"))
        ).andExpect(
                status().is(HttpStatus.OK.value())
        );

        ArgumentCaptor<CampaignRecord> captor = ArgumentCaptor.forClass(CampaignRecord.class);
        verify(messageCampaignService).saveCampaign(captor.capture());

        assertEquals(ABSOLUTE_CAMPAIGN_NAME, captor.getValue().getName());
        assertEquals(CampaignType.ABSOLUTE, captor.getValue().getCampaignType());
        assertEquals(2, captor.getValue().getMessages().size());
    }

    @Test
    public void shouldReturnCampaign() throws Exception {
        CampaignRecord campaignRecord = createAbsoluteCampaignRecord();

        when(messageCampaignService.getCampaignRecord(ABSOLUTE_CAMPAIGN_NAME)).thenReturn(campaignRecord);

        final String expectedResponse = loadJson("rest/campaigns/campaignDetails.json");

        controller.perform(
                get("/campaigns/{campaignName}", ABSOLUTE_CAMPAIGN_NAME)
        ).andExpect(
                status().is(HttpStatus.OK.value())
        ).andExpect(
                content().type(APPLICATION_JSON_UTF8)
        ).andExpect(
                content().string(jsonMatcher(expectedResponse))
        );

        verify(messageCampaignService).getCampaignRecord(ABSOLUTE_CAMPAIGN_NAME);
    }

    @Test
    public void shouldReturn404ForNonExistentCampaign() throws Exception {
        when(messageCampaignService.getCampaignRecord(ABSOLUTE_CAMPAIGN_NAME)).thenReturn(null);

        controller.perform(
                get("/campaigns/{campaignName}", ABSOLUTE_CAMPAIGN_NAME)
        ).andExpect(
                status().is(HttpStatus.NOT_FOUND.value())
        );

        verify(messageCampaignService).getCampaignRecord(ABSOLUTE_CAMPAIGN_NAME);
    }

    @Test
    public void shouldReturnAllCampaign() throws Exception {
        CampaignRecord campaignRecord = createAbsoluteCampaignRecord();
        CampaignRecord campaignRecord2 = createRepeatIntervalCampaignRecord();

        when(messageCampaignService.getAllCampaignRecords()).thenReturn(asList(campaignRecord,
                campaignRecord2));

        final String expectedResponse = loadJson("rest/campaigns/campaignList.json");

        controller.perform(
                get("/campaigns/")
        ).andExpect(
                status().is(HttpStatus.OK.value())
        ).andExpect(
                content().type(APPLICATION_JSON_UTF8)
        ).andExpect(
                content().string(jsonMatcher(expectedResponse))
        );

        verify(messageCampaignService).getAllCampaignRecords();
    }

    @Test
    public void shouldDeleteCampaign() throws Exception {
        controller.perform(
                delete("/campaigns/{campaignName}", ABSOLUTE_CAMPAIGN_NAME)
        ).andExpect(
                status().is(HttpStatus.OK.value())
        );

        verify(messageCampaignService).deleteCampaign(ABSOLUTE_CAMPAIGN_NAME);
    }

    private CampaignRecord createAbsoluteCampaignRecord() {
        CampaignRecord campaignRecord = new CampaignRecord();
        campaignRecord.setName(ABSOLUTE_CAMPAIGN_NAME);
        campaignRecord.setCampaignType(CampaignType.ABSOLUTE);

        CampaignMessageRecord campaignMessageRecord = new CampaignMessageRecord();
        campaignMessageRecord.setName("First");
        campaignMessageRecord.setFormats(asList("IVR", "SMS"));
        campaignMessageRecord.setLanguages(asList("en"));
        campaignMessageRecord.setMessageKey("random-1");
        campaignMessageRecord.setDate(new LocalDate(2013, 6, 15));
        campaignMessageRecord.setStartTime("10:30:00");

        CampaignMessageRecord campaignMessageRecord2 = new CampaignMessageRecord();
        campaignMessageRecord2.setName("Second");
        campaignMessageRecord2.setFormats(asList("IVR"));
        campaignMessageRecord2.setLanguages(asList("en"));
        campaignMessageRecord2.setMessageKey("random-2");
        campaignMessageRecord2.setDate(new LocalDate(2013, 6, 22));
        campaignMessageRecord2.setStartTime("10:30:00");

        campaignRecord.setMessages(asList(campaignMessageRecord, campaignMessageRecord2));
        return campaignRecord;
    }

    private CampaignRecord createRepeatIntervalCampaignRecord() {
        CampaignRecord campaignRecord = new CampaignRecord();
        campaignRecord.setName(REPEAT_INTERVAL_CAMPAIGN_NAME);
        campaignRecord.setCampaignType(CampaignType.REPEAT_INTERVAL);
        campaignRecord.setMaxDuration("5 weeks");

        CampaignMessageRecord campaignMessageRecord = new CampaignMessageRecord();
        campaignMessageRecord.setName("Weekly Message #1");
        campaignMessageRecord.setFormats(asList("IVR", "SMS"));
        campaignMessageRecord.setLanguages(asList("en"));
        campaignMessageRecord.setMessageKey("child-info-week-{Offset}-1");
        campaignMessageRecord.setRepeatEvery("1 Week");
        campaignMessageRecord.setStartTime("10:30");

        CampaignMessageRecord campaignMessageRecord2 = new CampaignMessageRecord();
        campaignMessageRecord2.setName("Weekly Message #2");
        campaignMessageRecord2.setFormats(asList("SMS"));
        campaignMessageRecord2.setLanguages(asList("en"));
        campaignMessageRecord2.setMessageKey("child-info-week-{Offset}-2");
        campaignMessageRecord2.setRepeatEvery("9 Days");
        campaignMessageRecord2.setStartTime("10:30");

        CampaignMessageRecord campaignMessageRecord3 = new CampaignMessageRecord();
        campaignMessageRecord3.setName("Weekly Message #3");
        campaignMessageRecord3.setFormats(asList("SMS"));
        campaignMessageRecord3.setLanguages(asList("en"));
        campaignMessageRecord3.setMessageKey("child-info-week-{Offset}-3");
        campaignMessageRecord3.setRepeatEvery("12 Days");
        campaignMessageRecord3.setStartTime("10:30");

        campaignRecord.setMessages(asList(campaignMessageRecord, campaignMessageRecord2, campaignMessageRecord3));
        return campaignRecord;
    }

    private String loadJson(String filename) throws IOException {
        try (InputStream in = getClass().getClassLoader().getResourceAsStream(filename)) {
            return IOUtils.toString(in);
        }
    }

}
