package org.motechproject.messagecampaign.web.api;

import org.motechproject.messagecampaign.domain.campaign.CampaignRecurrence;
import org.motechproject.messagecampaign.exception.CampaignNotFoundException;
import org.motechproject.messagecampaign.service.MessageCampaignService;
import org.motechproject.messagecampaign.web.model.CampaignDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping(value = "web-api")
public class CampaignRestController {

    private static final String HAS_MANAGE_CAMPAIGNS_ROLE = "hasRole('manageCampaigns')";

    @Autowired
    private MessageCampaignService messageCampaignService;

    @RequestMapping(value = "/campaigns/{campaignName}", method = RequestMethod.GET)
    @PreAuthorize(HAS_MANAGE_CAMPAIGNS_ROLE)
    @ResponseBody
    public CampaignDto getCampaign(@PathVariable String campaignName) {
        CampaignRecurrence campaignRecurrence = messageCampaignService.getCampaignRecord(campaignName);

        if (campaignRecurrence == null) {
            throw new CampaignNotFoundException("Campaign not found: " + campaignName);
        }

        return new CampaignDto(campaignRecurrence);
    }

    @RequestMapping(value = "/campaigns", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize(HAS_MANAGE_CAMPAIGNS_ROLE)
    public void createCampaign(@RequestBody CampaignDto campaign) {
        CampaignRecurrence campaignRecurrence = campaign.toCampaignRecord();
        messageCampaignService.saveCampaign(campaignRecurrence);
    }

    @RequestMapping(value = "/campaigns", method = RequestMethod.GET)
    @PreAuthorize(HAS_MANAGE_CAMPAIGNS_ROLE)
    @ResponseBody
    public List<CampaignDto> getAllCampaigns() {
        List<CampaignRecurrence> campaignRecurrences = messageCampaignService.getAllCampaignRecords();

        List<CampaignDto> campaignDtos = new ArrayList<>();
        for (CampaignRecurrence record : campaignRecurrences) {
            campaignDtos.add(new CampaignDto(record));
        }

        return campaignDtos;
    }

    @RequestMapping(value = "/campaigns/{campaignName}", method = RequestMethod.DELETE)
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize(HAS_MANAGE_CAMPAIGNS_ROLE)
    public void deleteCampaign(@PathVariable String campaignName) {
        messageCampaignService.deleteCampaign(campaignName);
    }

    @ExceptionHandler(CampaignNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ResponseBody
    public String handleException(Exception e) {
        return e.getMessage();
    }
}
