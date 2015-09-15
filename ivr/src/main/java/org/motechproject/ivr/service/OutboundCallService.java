package org.motechproject.ivr.service;

import java.util.Map;

/**
 * Service Interface used to initiate an outgoing (Mobile Terminated) call
 */
public interface OutboundCallService {
    /**
     * Constructs & executes an HTTP GET request, replacing [xxx] placeholders in the outgoingCallUrl string with their
     * provided values from params and adding the unmatched params to the request.
     *
     * @param configName the name of the configuration to use when initiating the call
     * @param params the parameters that will be used for building the url, the ones that don't match the placeholders will be sent as params of the request
     */
    void initiateCall(String configName, Map<String, String> params);

    /**
     * Gets the defaultConfig and calls the initiateCall method
     * @param params the parameters that will be used for building the url, the ones that don't match the placeholders will be sent as params of the request
     */
    void initiateCall(Map<String, String> params);
}
