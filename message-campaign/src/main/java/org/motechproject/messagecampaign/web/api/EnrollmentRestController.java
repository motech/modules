package org.motechproject.messagecampaign.web.api;

import org.motechproject.messagecampaign.contract.CampaignRequest;
import org.motechproject.messagecampaign.domain.campaign.CampaignEnrollment;
import org.motechproject.messagecampaign.domain.campaign.CampaignEnrollmentStatus;
import org.motechproject.messagecampaign.exception.EnrollmentNotFoundException;
import org.motechproject.messagecampaign.service.CampaignEnrollmentsQuery;
import org.motechproject.messagecampaign.service.EnrollmentService;
import org.motechproject.messagecampaign.service.MessageCampaignService;
import org.motechproject.messagecampaign.web.MessageCampaignController;
import org.motechproject.messagecampaign.web.model.EnrollmentDto;
import org.motechproject.messagecampaign.web.model.EnrollmentList;
import org.motechproject.messagecampaign.web.model.EnrollmentRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.List;
import java.util.Locale;

/**
 * REST API controller for handling message campaign enrollment requests.
 */
@Controller
@RequestMapping(value = "web-api/enrollments")
public class EnrollmentRestController extends MessageCampaignController {

    private static final String HAS_MANAGE_ENROLLMENTS_ROLE = "hasRole('manageEnrollments')";

    @Autowired
    private MessageCampaignService messageCampaignService;

    @Autowired
    private EnrollmentService enrollmentService;

    /**
     * Enrolls client into the given message campaign or updates an existing enrollment.
     *
     * @param campaignName the name of the campaign
     * @param userId external ID of the client
     * @param enrollmentRequest representation of the enrollment
     */
    @RequestMapping(value = "/{campaignName}/users/{userId}", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize(HAS_MANAGE_ENROLLMENTS_ROLE)
    public void enrollOrUpdateUser(@PathVariable String campaignName, @PathVariable String userId,
                                   @RequestBody EnrollmentRequest enrollmentRequest) {

        CampaignRequest campaignRequest = new CampaignRequest(userId, campaignName,
                enrollmentRequest.getReferenceDate(), enrollmentRequest.getStartTime());

        Long enrollmentId = enrollmentRequest.getEnrollmentId();

        if (enrollmentId != null && enrollmentId > 0) {
            messageCampaignService.updateEnrollment(campaignRequest, enrollmentId);
        } else {
            messageCampaignService.enroll(campaignRequest);
        }
    }

    /**
     * Retrieves an enrollment, based on the provided campaign name and external client ID.
     *
     * @param campaignName the name of the campaign
     * @param userId external client ID
     * @return enrollment record
     */
    @RequestMapping(value = "/{campaignName}/users/{userId}", method = RequestMethod.GET)
    @PreAuthorize(HAS_MANAGE_ENROLLMENTS_ROLE)
    @ResponseBody
    public EnrollmentDto getEnrollment(@PathVariable String campaignName, @PathVariable String userId) {
        CampaignEnrollmentsQuery query = new CampaignEnrollmentsQuery()
                .withCampaignName(campaignName).withExternalId(userId);

        List<CampaignEnrollment> enrollments = enrollmentService.search(query);

        if (enrollments.isEmpty()) {
            throw enrollmentsNotFoundException(userId);
        } else {
            return new EnrollmentDto(enrollments.get(0));
        }
    }

    /**
     * Unenrolls and enrolls a client again, into the specified message campaign.
     *
     * @param campaignName the name of the campaign
     * @param userId external client ID
     * @param enrollmentRequest representation of the enrollment
     */
    @RequestMapping(value = "/{campaignName}/users/{userId}", method = RequestMethod.PUT)
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize(HAS_MANAGE_ENROLLMENTS_ROLE)
    public void updateEnrollment(@PathVariable String campaignName, @PathVariable String userId,
                                 @RequestBody EnrollmentRequest enrollmentRequest) {
        CampaignRequest campaignRequest = new CampaignRequest(userId, campaignName,
                enrollmentRequest.getReferenceDate(), enrollmentRequest.getStartTime());

        messageCampaignService.unenroll(userId, campaignName);
        messageCampaignService.enroll(campaignRequest);
    }

    /**
     * Removes the existing enrollment.
     *
     * @param campaignName the name of the campaign
     * @param externalId external ID of the client
     */
    @RequestMapping(value = "/{campaignName}/users/{externalId}", method = RequestMethod.DELETE)
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize(HAS_MANAGE_ENROLLMENTS_ROLE)
    public void removeEnrollment(@PathVariable String campaignName, @PathVariable String externalId) {
        CampaignEnrollmentsQuery query = new CampaignEnrollmentsQuery()
                .withCampaignName(campaignName).withExternalId(externalId);

        List<CampaignEnrollment> enrollments = enrollmentService.search(query);

        if (enrollments.isEmpty()) {
            throw enrollmentsNotFoundException(externalId);
        } else {
            CampaignRequest campaignRequest = new CampaignRequest();
            campaignRequest.setCampaignName(campaignName);
            campaignRequest.setExternalId(externalId);

            messageCampaignService.unenroll(externalId, campaignName);
        }
    }

    /**
     * Retrieves all enrollments for the given campaign name.
     *
     * @param campaignName the name of the campaign
     * @return a list of enrollments
     */
    @RequestMapping(value = "/{campaignName}/users", method = RequestMethod.GET)
    @PreAuthorize(HAS_MANAGE_ENROLLMENTS_ROLE)
    @ResponseBody
    public EnrollmentList getEnrollmentsForCampaign(@PathVariable String campaignName) {
        CampaignEnrollmentsQuery query = new CampaignEnrollmentsQuery().withCampaignName(campaignName);

        List<CampaignEnrollment> enrollments = enrollmentService.search(query);

        EnrollmentList enrollmentList = new EnrollmentList(enrollments);
        enrollmentList.setCommonCampaignName(campaignName);

        return enrollmentList;
    }

    /**
     * Retrieves all enrollments assigned to the given client.
     *
     * @param userId external client ID
     * @return a list of enrollments
     */
    @RequestMapping(value = "/users/{userId}", method = RequestMethod.GET)
    @PreAuthorize(HAS_MANAGE_ENROLLMENTS_ROLE)
    @ResponseBody
    public EnrollmentList getEnrollmentsForUser(@PathVariable String userId) {
        CampaignEnrollmentsQuery query = new CampaignEnrollmentsQuery().withExternalId(userId);

        List<CampaignEnrollment> enrollments = enrollmentService.search(query);

        if (enrollments.isEmpty()) {
            throw enrollmentsNotFoundException(userId);
        } else {
            EnrollmentList enrollmentList = new EnrollmentList(enrollments);
            enrollmentList.setCommonExternalId(userId);

            return enrollmentList;
        }
    }

    /**
     * Returns a key of the latest message sent to the client.
     *
     * @param campaignName the name of the campaign
     * @param externalId external ID of the client
     * @return a key of the latest message
     */
    @RequestMapping(value = "/{campaignName}/users/{externalId}/latest", method = RequestMethod.GET)
    @PreAuthorize(HAS_MANAGE_ENROLLMENTS_ROLE)
    @ResponseBody
    public String getLatestCampaingMessageKey(@PathVariable String campaignName, @PathVariable String externalId) {
        return messageCampaignService.getLatestCampaignMessage(campaignName, externalId);
    }

    /**
     * Returns a key of the next message that will be sent to the client.
     *
     * @param campaignName the name of the campaign
     * @param externalId external ID of the client
     * @return a key of the next message
     */
    @RequestMapping(value = "/{campaignName}/users/{externalId}/next", method = RequestMethod.GET)
    @PreAuthorize(HAS_MANAGE_ENROLLMENTS_ROLE)
    @ResponseBody
    public String getNextCampaingMessageKey(@PathVariable String campaignName, @PathVariable String externalId) {
        return messageCampaignService.getNextCampaignMessage(campaignName, externalId);
    }

    /**
     * Retrieves all enrollments, optionally filtered by an enrollment status, campaign name or external client ID.
     *
     * @param enrollmentStatus status of the enrollment
     * @param externalId external ID of the client
     * @param campaignName the name of the campaign
     * @return a list of matching enrollments
     */
    @RequestMapping(value = "/users", method = RequestMethod.GET)
    @PreAuthorize(HAS_MANAGE_ENROLLMENTS_ROLE)
    @ResponseBody
    public EnrollmentList getAllEnrollments(@RequestParam(required = false) String enrollmentStatus,
                                            @RequestParam(required = false) String externalId,
                                            @RequestParam(required = false) String campaignName) {
        CampaignEnrollmentsQuery query = new CampaignEnrollmentsQuery();

        if (enrollmentStatus != null) {
            query.havingState(CampaignEnrollmentStatus.valueOf(enrollmentStatus.toUpperCase(Locale.ENGLISH)));
        }
        if (campaignName != null) {
            query.withCampaignName(campaignName);
        }
        if (externalId != null) {
            query.withExternalId(externalId);
        }

        List<CampaignEnrollment> enrollments = enrollmentService.search(query);

        return new EnrollmentList(enrollments);
    }

    private EnrollmentNotFoundException enrollmentsNotFoundException(String userId) {
        return new EnrollmentNotFoundException("No enrollments found for user " + userId);
    }
}
