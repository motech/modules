package org.motechproject.messagecampaign.web.controller;

import com.google.gson.JsonParseException;
import org.apache.commons.io.IOUtils;
import org.motechproject.messagecampaign.Constants;
import org.motechproject.messagecampaign.exception.CampaignValidationException;
import org.motechproject.messagecampaign.service.MessageCampaignService;
import org.motechproject.server.config.SettingsFacade;
import org.osgi.framework.BundleException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;

@Controller
public class SettingsController {
    private static final Logger LOGGER = LoggerFactory.getLogger(SettingsController.class);

    @Autowired
    private MessageCampaignService messageCampaignService;

    @Autowired
    @Qualifier("messageCampaignSettings")
    private SettingsFacade settingsFacade;

    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(value = "/settings", method = RequestMethod.POST)
    @PreAuthorize("hasRole('manageCampaigns')")
    public void saveSettings(@ModelAttribute("messageCampaigns") MultipartFile messageCampaigns) throws BundleException, IOException {
        if (messageCampaigns == null || messageCampaigns.isEmpty()) {
            throw new IllegalArgumentException("No file specified");
        }
        String oldConfig = null;
        try {
            oldConfig = IOUtils.toString(settingsFacade.getRawConfig(MessageCampaignService.MESSAGE_CAMPAIGNS_JSON_FILENAME));

            Resource resource = new InputStreamResource(messageCampaigns.getInputStream());

            settingsFacade.saveRawConfig(MessageCampaignService.MESSAGE_CAMPAIGNS_JSON_FILENAME,
                    resource);

            messageCampaignService.loadCampaigns();
        } catch (JsonParseException | CampaignValidationException e) {
            //revert to previous config
            if (oldConfig != null) {
                settingsFacade.saveRawConfig(MessageCampaignService.MESSAGE_CAMPAIGNS_JSON_FILENAME,
                        new InputStreamResource(new ByteArrayInputStream(oldConfig.getBytes())));
            }

            throw new IllegalArgumentException(e);
        }
    }

    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(value = "/mds-databrowser-config", method = RequestMethod.GET)
    @ResponseBody
    public String getCustomUISettings() throws IOException {
        return IOUtils.toString(settingsFacade.getRawConfig(Constants.UI_CONFIG));
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseBody
    public String handleIllegalArgumentException(Exception e) {
        LOGGER.error(e.getMessage(), e);
        return e.getMessage();
    }

}

