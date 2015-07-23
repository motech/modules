package org.motechproject.messagecampaign.exception;

/**
 * Thrown when JSON sent to the server is not a valid campaign.
 */
public class CampaignJsonException extends MessageCampaignException {

    private static final String MESSAGE = "The JSON sent to the server was not a valid campaign.";

    public CampaignJsonException(Throwable e) {
        super(MESSAGE, "msgCampaign.error.campaignJsonMalformed", e);
    }

}
