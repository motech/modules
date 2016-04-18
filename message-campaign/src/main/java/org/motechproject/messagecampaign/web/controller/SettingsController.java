package org.motechproject.messagecampaign.web.controller;

import com.google.gson.JsonParseException;
import org.apache.commons.io.IOUtils;
import org.motechproject.messagecampaign.Constants;
import org.motechproject.messagecampaign.exception.CampaignJsonException;
import org.motechproject.messagecampaign.exception.CampaignValidationException;
import org.motechproject.messagecampaign.service.MessageCampaignService;
import org.motechproject.messagecampaign.web.MessageCampaignController;
import org.motechproject.config.SettingsFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;

/**
 * A controller responsible for handling settings requests in the Message Campaign module.
 */
@Controller
public class SettingsController extends MessageCampaignController {

    @Autowired
    private MessageCampaignService messageCampaignService;

    @Autowired
    @Qualifier("messageCampaignSettings")
    private SettingsFacade settingsFacade;

    /**
     * Updates Message Campaign by adding new campaign definitions from the
     * settings resource.
     *
     * @param file representation of the message campaign
     * @throws IOException in case of input/output problems with operations on the JSON file
     */
    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(value = "/settings", method = RequestMethod.POST)
    @PreAuthorize("hasRole('manageCampaigns')")
    public void saveSettings(@ModelAttribute("messageCampaigns") MultipartFile file) throws IOException {
        if (file == null || file.isEmpty()) {
            throw new CampaignJsonException(null);
        }
        String oldConfig = null;
        try {
            oldConfig = IOUtils.toString(settingsFacade.getRawConfig(MessageCampaignService.MESSAGE_CAMPAIGNS_JSON_FILENAME));

            Resource resource = new InputStreamResource(file.getInputStream());

            settingsFacade.saveRawConfig(MessageCampaignService.MESSAGE_CAMPAIGNS_JSON_FILENAME,
                    resource);

            messageCampaignService.loadCampaigns();
        } catch (JsonParseException | CampaignValidationException e) {
            //revert to previous config
            if (oldConfig != null) {
                settingsFacade.saveRawConfig(MessageCampaignService.MESSAGE_CAMPAIGNS_JSON_FILENAME,
                        new InputStreamResource(new ByteArrayInputStream(oldConfig.getBytes())));
            }
            throw new CampaignJsonException(e);
        }
    }

    /**
     * Retrieves configuration for the MDS Data Browser custom UI view.
     *
     * @return raw module configuration
     * @throws IOException in case of input/output problems with operations on the JSON file
     */
    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(value = "/mds-databrowser-config", method = RequestMethod.GET)
    @ResponseBody
    public String getCustomUISettings() throws IOException {
        return IOUtils.toString(settingsFacade.getRawConfig(Constants.UI_CONFIG));
    }
}

