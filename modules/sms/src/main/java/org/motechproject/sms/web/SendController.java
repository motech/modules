package org.motechproject.sms.web;

import org.motechproject.sms.service.SmsService;
import org.motechproject.sms.service.OutgoingSms;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.io.IOException;

/**
 * handles requests to {server}/motech-platform-server/module/sms/send: how the Send SMS dialog sends a message
 */
@Controller
public class SendController {
    private SmsService smsService;

    @Autowired
    public SendController(SmsService smsService) {
        this.smsService = smsService;
    }

    @RequestMapping(value = "/send", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public String sendSms(@RequestBody OutgoingSms outgoingSms) {
        smsService.send(outgoingSms);
        return String.format("SMS to %s using the %s config was added to the message queue.",
                outgoingSms.getRecipients().toString(), outgoingSms.getConfig());
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ResponseBody
    public String handleException(Exception e) throws IOException {
        return e.getMessage();
    }
}
