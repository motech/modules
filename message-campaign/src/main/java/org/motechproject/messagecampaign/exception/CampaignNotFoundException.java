package org.motechproject.messagecampaign.exception;

/**
 * Thrown when a campaign cannot be found.
 */
public class CampaignNotFoundException extends RuntimeException {

    private static final long serialVersionUID = 613778581776845400L;

    public CampaignNotFoundException(String message) {
        super(message);
    }
}
