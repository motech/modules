package org.motechproject.messagecampaign.web;

import org.motechproject.messagecampaign.exception.CampaignAlreadyEndedException;
import org.motechproject.messagecampaign.exception.CampaignJsonException;
import org.motechproject.messagecampaign.exception.CampaignNotFoundException;
import org.motechproject.messagecampaign.exception.EnrollmentAlreadyExists;
import org.motechproject.messagecampaign.exception.EnrollmentNotFoundException;
import org.motechproject.messagecampaign.exception.MessageCampaignException;
import org.motechproject.messagecampaign.exception.SchedulingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

public abstract class MessageCampaignController {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @ExceptionHandler({EnrollmentNotFoundException.class, CampaignNotFoundException.class })
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ResponseBody
    public String handleNotFoundException(Exception e) {
        return e.getMessage();
    }

    @ExceptionHandler({CampaignAlreadyEndedException.class, CampaignJsonException.class,
            EnrollmentAlreadyExists.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public MessageCampaignException.MessageKey handleBadRequestException(MessageCampaignException e) {
        return handleException(e);
    }

    @ExceptionHandler(SchedulingException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    public MessageCampaignException.MessageKey handleBadRequestException(SchedulingException e) {
        return handleException(e);
    }

    private MessageCampaignException.MessageKey handleException(MessageCampaignException e) {
        logger.error(e.getMessage(), e);
        return e.getMessageKey();
    }

}
