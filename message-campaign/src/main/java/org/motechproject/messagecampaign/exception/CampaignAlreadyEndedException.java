package org.motechproject.messagecampaign.exception;

import java.util.Arrays;

/**
 * Thrown when trying to enroll a person to campaign that has already ended.
 */
public class CampaignAlreadyEndedException extends MessageCampaignException {

    private static final long serialVersionUID = 6685367232884724905L;

    private static final String MESSAGE = "Campaign \"%s\" has already ended.";

    public CampaignAlreadyEndedException(String campaignName, Throwable e) {
        super(String.format(MESSAGE, campaignName), "msgCampaign.error.campaignAlreadyEnded",
                Arrays.asList(campaignName), e);
    }

}
