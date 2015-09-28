package org.motechproject.messagecampaign.web.controller;

import org.motechproject.messagecampaign.domain.campaign.CampaignRecord;
import org.motechproject.messagecampaign.exception.CampaignNotFoundException;
import org.motechproject.messagecampaign.service.MessageCampaignService;
import org.motechproject.messagecampaign.web.MessageCampaignController;
import org.motechproject.messagecampaign.web.model.CampaignDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.ArrayList;
import java.util.List;

/**
 * Controller responsible for handling campaign requests.
 */
@Controller
public class CampaignController extends MessageCampaignController {

    public static final String HAS_ROLE_MANAGECAMPAIGNS = "hasRole('manageCampaigns')";
    @Autowired
    private MessageCampaignService messageCampaignService;

    /**
     * Retrieves campaign of the given name.
     *
     * @param campaignName the name of the campaign
     * @return campaign record of the given name
     */
    @RequestMapping(value = "/campaigns/{campaignName}", method = RequestMethod.GET)
    @PreAuthorize(HAS_ROLE_MANAGECAMPAIGNS)
    @ResponseBody
    public CampaignDto getCampaign(@PathVariable String campaignName) {
        return new CampaignDto(getCampaignRecord(campaignName));
    }

    /**
     * Retrieves campaign of the given name.
     *
     * @param campaignName the name of the campaign
     * @return campaign record of the given name
     */
    @RequestMapping(value = "/campaign-record/{campaignName}", method = RequestMethod.GET)
    @PreAuthorize(HAS_ROLE_MANAGECAMPAIGNS)
    @ResponseBody
    public CampaignRecord getCampaignRecord(@PathVariable String campaignName) {
        CampaignRecord campaignRecord = messageCampaignService.getCampaignRecord(campaignName);

        if (campaignRecord == null) {
            throw new CampaignNotFoundException("Campaign not found: " + campaignName);
        }

        return campaignRecord;
    }

    /**
     * Creates a new message campaign.
     *
     * @param campaign the campaign to create
     */
    @RequestMapping(value = "/campaigns", method = RequestMethod.POST)
    @PreAuthorize(HAS_ROLE_MANAGECAMPAIGNS)
    @ResponseStatus(HttpStatus.OK)
    public void createCampaign(@RequestBody CampaignDto campaign) {
        CampaignRecord campaignRecord = campaign.toCampaignRecord();
        messageCampaignService.saveCampaign(campaignRecord);
    }

    /**
     * Retrieves all message campaigns.
     *
     * @return all message campaigns.
     */
    @RequestMapping(value = "/campaigns", method = RequestMethod.GET)
    @PreAuthorize(HAS_ROLE_MANAGECAMPAIGNS)
    @ResponseBody
    public List<CampaignDto> getAllCampaigns() {
        List<CampaignRecord> campaignRecords = messageCampaignService.getAllCampaignRecords();

        List<CampaignDto> campaignDtos = new ArrayList<>();
        for (CampaignRecord record : campaignRecords) {
            campaignDtos.add(new CampaignDto(record));
        }

        return campaignDtos;
    }

    /**
     * Deletes a message campaign of the given name.
     *
     * @param campaignName the name of the campaign to remove
     */
    @RequestMapping(value = "/campaigns/{campaignName}", method = RequestMethod.DELETE)
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize(HAS_ROLE_MANAGECAMPAIGNS)
    public void deleteCampaign(@PathVariable String campaignName) {
        messageCampaignService.deleteCampaign(campaignName);
    }
}
