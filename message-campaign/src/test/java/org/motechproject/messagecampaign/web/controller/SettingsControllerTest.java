package org.motechproject.messagecampaign.web.controller;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonParseException;
import org.apache.commons.io.IOUtils;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.motechproject.messagecampaign.exception.MessageCampaignException;
import org.motechproject.messagecampaign.service.MessageCampaignService;
import org.motechproject.config.SettingsFacade;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.server.MockMvc;
import org.springframework.test.web.server.setup.MockMvcBuilders;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.motechproject.testing.utils.rest.RestTestUtil.jsonMatcher;
import static org.springframework.test.web.server.request.MockMvcRequestBuilders.fileUpload;
import static org.springframework.test.web.server.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.server.result.MockMvcResultMatchers.status;

public class SettingsControllerTest {

    private final static Gson GSON = new GsonBuilder().create();

    private final static String MALFORMED_CAMPAIGN_JSON = "malformed-campaign.json";
    private final static String CORRECT_CAMPAIGN_JSON = "message-campaigns.json";

    private MockMvc controller;

    @InjectMocks
    private SettingsController settingsController = new SettingsController();

    @Mock
    SettingsFacade settingsFacade;

    @Mock
    MessageCampaignService messageCampaignService;

    InputStream inputStream;

    @Before
    public void setUp() throws Exception {
        initMocks(this);
        controller = MockMvcBuilders.standaloneSetup(settingsController).build();
        inputStream = getClass().getClassLoader().getResourceAsStream("message-campaigns.json");
    }

    @Test
    public void shouldReturn200WhenSubmittingCorrectCampaign() throws Exception {

        MockMultipartFile jsonCampaign = new MockMultipartFile("file", "orig.json", null,
                loadJson(CORRECT_CAMPAIGN_JSON).getBytes());

        doNothing().when(settingsFacade).saveRawConfig(any(String.class), any(Resource.class));
        doNothing().when(messageCampaignService).loadCampaigns();

        when(settingsFacade.getRawConfig(MessageCampaignService.MESSAGE_CAMPAIGNS_JSON_FILENAME))
                .thenReturn(inputStream);

        controller.perform(
                fileUpload("/settings")
                        .file(jsonCampaign)
        ).andExpect(
                status().is(HttpStatus.OK.value())
        );
    }

    @Test
    public void shouldReturn400WhenSubmittingMalformedCampaign() throws Exception {

        MessageCampaignException.MessageKey messageKey = new MessageCampaignException.MessageKey("msgCampaign.error.campaignJsonMalformed",
                new ArrayList<String>());

        MockMultipartFile jsonCampaign = new MockMultipartFile("messageCampaigns", "orig", null, loadJson(MALFORMED_CAMPAIGN_JSON).getBytes());

        doThrow(new JsonParseException("ParseException"))
                .doNothing()
                .when(settingsFacade).saveRawConfig(any(String.class), any(Resource.class));

        when(settingsFacade.getRawConfig(MessageCampaignService.MESSAGE_CAMPAIGNS_JSON_FILENAME))
                .thenReturn(inputStream);

        controller.perform(
                fileUpload("/settings")
                        .file(jsonCampaign)
        ).andExpect(
                status().is(HttpStatus.BAD_REQUEST.value())
        ).andExpect(
                content().string(jsonMatcher(GSON.toJson(messageKey)))
        );
    }

    @Test
    public void shouldReturn400WhenSubmittingEmptyFile() throws Exception {

        MessageCampaignException.MessageKey messageKey = new MessageCampaignException.MessageKey("msgCampaign.error.campaignJsonMalformed",
                new ArrayList<String>());

        MockMultipartFile jsonCampaign = new MockMultipartFile("messageCampaigns", "orig", null, new byte[0]);

        doThrow(new JsonParseException("ParseException"))
                .doNothing()
                .when(settingsFacade).saveRawConfig(any(String.class), any(Resource.class));

        when(settingsFacade.getRawConfig(MessageCampaignService.MESSAGE_CAMPAIGNS_JSON_FILENAME))
                .thenReturn(inputStream);

        controller.perform(
                fileUpload("/settings")
                        .file(jsonCampaign)
        ).andExpect(
                status().is(HttpStatus.BAD_REQUEST.value())
        ).andExpect(
                content().string(jsonMatcher(GSON.toJson(messageKey)))
        );
    }

    private String loadJson(String filename) throws IOException {
        try (InputStream in = getClass().getClassLoader().getResourceAsStream(filename)) {
            return IOUtils.toString(in);
        }
    }

}
