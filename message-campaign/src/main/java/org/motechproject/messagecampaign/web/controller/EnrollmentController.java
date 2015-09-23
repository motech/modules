package org.motechproject.messagecampaign.web.controller;

import org.motechproject.commons.date.util.DateUtil;
import org.motechproject.messagecampaign.domain.campaign.CampaignEnrollmentStatus;
import org.motechproject.messagecampaign.web.MessageCampaignController;
import org.motechproject.messagecampaign.web.api.EnrollmentRestController;
import org.motechproject.messagecampaign.web.model.EnrollmentList;
import org.motechproject.messagecampaign.web.model.EnrollmentRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Controller responsible for handling message campaign enrollment requests.
 */
@Controller
@RequestMapping(value = "/enrollments")
public class EnrollmentController extends MessageCampaignController {

    @Autowired
    private EnrollmentRestController enrollmentController;

    /**
     * Enrolls client into the given message campaign or updates an existing enrollment.
     *
     * @param campaignName the name of the campaign
     * @param externalId external ID of the client
     * @param enrollmentId enrollment ID
     */
    @RequestMapping(value = "/{campaignName}/users", method = RequestMethod.POST)
    @PreAuthorize("permitAll")
    @ResponseStatus(HttpStatus.OK)
    public void enrollOrUpdateUser(@PathVariable String campaignName,
                                   @RequestParam String externalId,
                                   @RequestParam Long enrollmentId) {
        EnrollmentRequest enrollmentRequest = new EnrollmentRequest();
        enrollmentRequest.setEnrollmentId(enrollmentId);
        enrollmentRequest.setReferenceDate(DateUtil.today());
        enrollmentController.enrollOrUpdateUser(campaignName, externalId, enrollmentRequest);
    }

    /**
     * Removes the existing enrollment.
     *
     * @param campaignName the name of the campaign
     * @param externalId external ID of the client
     */
    @RequestMapping(value = "/{campaignName}/users/{externalId}", method = RequestMethod.DELETE)
    @PreAuthorize("permitAll")
    @ResponseStatus(HttpStatus.OK)
    public void removeEnrollment(@PathVariable String campaignName, @PathVariable String externalId) {
        enrollmentController.removeEnrollment(campaignName, externalId);
    }

    /**
     * Retrieves all active enrollments, optionally filtered by a campaign name or external client ID.
     *
     * @param externalId external ID of the client
     * @param campaignName the name of the campaign
     * @return a list of matching enrollments
     */
    @RequestMapping(value = "/users", method = RequestMethod.GET)
    @PreAuthorize("permitAll")
    @ResponseBody
    public EnrollmentList getAllEnrollments(
            @RequestParam(required = false) String externalId, @RequestParam(required = false) String campaignName) {
        return enrollmentController.getAllEnrollments(CampaignEnrollmentStatus.ACTIVE.name(), externalId, campaignName);
    }
}

