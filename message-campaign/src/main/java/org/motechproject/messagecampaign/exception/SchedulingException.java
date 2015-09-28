package org.motechproject.messagecampaign.exception;

import java.util.Arrays;

/**
 * Thrown when there was a problem while scheduling job for enrollee.
 */
public class SchedulingException extends MessageCampaignException {

    private static final long serialVersionUID = -3270835551950852463L;

    private static final String MESSAGE = "Couldn't schedule job(s) for enrollee with external ID \"%s\".";

    public SchedulingException(String externalId, Throwable e) {
        super(String.format(MESSAGE, externalId), "msgCampaign.error.schedulingError", Arrays.asList(externalId), e);
    }

}
