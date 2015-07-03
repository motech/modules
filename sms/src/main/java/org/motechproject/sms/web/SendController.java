package org.motechproject.sms.web;

import org.motechproject.sms.service.OutgoingSms;
import org.motechproject.sms.service.SmsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.io.IOException;

import static org.motechproject.sms.util.Constants.HAS_MANAGE_SMS_ROLE;

/**
 * handles requests to {server}/motech-platform-server/module/sms/send: how the Send SMS dialog sends a message
 */
@Controller
@PreAuthorize(HAS_MANAGE_SMS_ROLE)
public class SendController {

    private static final Logger LOGGER = LoggerFactory.getLogger(SendController.class);

    @Autowired
    private SmsService smsService;

    /**
     * Sends an outgoing sms.
     * @param outgoingSms the definition of the SMS to send
     * @return a message describing that the SMS was sent
     * @see OutgoingSms
     */
    @RequestMapping(value = "/send", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public String sendSms(@RequestBody OutgoingSms outgoingSms) {
        smsService.send(outgoingSms);
        return String.format("SMS to %s using the %s config was added to the message queue.",
                outgoingSms.getRecipients().toString(), outgoingSms.getConfig());
    }

    /**
     * Handles an exception in the controller. The message of the exception will be returned as the HTTP body.
     * @param e the exception to handle
     * @return the message from the exception, to be treated as the response body
     */
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ResponseBody
    public String handleException(Exception e) throws IOException {
        LOGGER.error("Error in Send SMS Controller", e);
        return e.getMessage();
    }
}
