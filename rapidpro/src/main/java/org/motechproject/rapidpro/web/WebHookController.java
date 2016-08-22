package org.motechproject.rapidpro.web;


import org.motechproject.rapidpro.event.publisher.EventPublisher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@Controller
@RequestMapping("/web-hook")
public class WebHookController {

    @Autowired
    EventPublisher eventPublisher;

    @RequestMapping(method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    public void receiveWebHook(HttpServletRequest request) {
        Map<String, String[]> requestParams = request.getParameterMap();
        eventPublisher.publishWebHookEvent(requestParams);
    }
}
