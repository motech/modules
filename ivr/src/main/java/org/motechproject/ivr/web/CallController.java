package org.motechproject.ivr.web;

import org.motechproject.ivr.service.OutboundCallService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.Map;

/**
 * Responds to HTTP queries to {motech-server}/module/ivr/call/{config} by initiating an outbound IVR call
 */
@Controller
@RequestMapping(value = "/call")
public class CallController {

    private static final Logger LOGGER = LoggerFactory.getLogger(CallController.class);
    private OutboundCallService outboundCallService;

    @Autowired
    public CallController(OutboundCallService outboundCallService) {
        this.outboundCallService = outboundCallService;
    }

    /**
     * Initiates an outbound call
     *
     * @param configName the name of the configuration to use
     * @param params the params that will be passed to {@link org.motechproject.ivr.service.OutboundCallService#initiateCall(String, java.util.Map)}
     */
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    @RequestMapping(value = "/{configName}")
    public void handle(@PathVariable String configName, @RequestParam Map<String, String> params,
                       @RequestHeader Map<String, String> headers) {

        LOGGER.debug(String.format("handle(configName = %s, params = %s, headers = %s)", configName, params, headers));

        outboundCallService.initiateCall(configName, params);
    }
}
