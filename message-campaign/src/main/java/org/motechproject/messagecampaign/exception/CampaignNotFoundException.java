package org.motechproject.messagecampaign.exception;

public class CampaignNotFoundException extends RuntimeException {
    public CampaignNotFoundException(String message) {
        super(message);
    }
}
