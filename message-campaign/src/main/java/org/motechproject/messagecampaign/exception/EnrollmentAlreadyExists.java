package org.motechproject.messagecampaign.exception;

import java.util.Arrays;

/**
 * Thrown when trying to enroll a person twice to the same campaign.
 */
public class EnrollmentAlreadyExists extends MessageCampaignException {

    private static final long serialVersionUID = -5315927822391760933L;

    private static final String MESSAGE = "Person with external ID \"%s\" is already enrolled to the \"%s\" campaign.";

    public EnrollmentAlreadyExists(String externalId, String campaignName) {
        this(externalId, campaignName, null);
    }

    public EnrollmentAlreadyExists(String externalId, String campaignName, Throwable e) {
        super(String.format(MESSAGE, externalId, campaignName), "msgCampaign.error.enrollmentAlreadyExists",
                Arrays.asList(externalId, campaignName), e);
    }

}
