package org.motechproject.csd.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ws.context.MessageContext;
import org.springframework.ws.soap.SoapHeaderElement;
import org.springframework.ws.soap.server.SoapEndpointInterceptor;

public class CSDEndpointInterceptor implements SoapEndpointInterceptor {
    private static final Logger LOGGER = LoggerFactory.getLogger(CSDEndpointInterceptor.class);

    public CSDEndpointInterceptor() {
        LOGGER.debug("CSDEndpointInterceptor()");
    }

    @Override
    public boolean understands(SoapHeaderElement header) {
        LOGGER.debug("understands(header={})", header);

        //todo: here we could inspect the <wsa:??? mustUnderstand="1"> SOAP headers and decide what we'd like to do with
        //todo: them, but for now, just pretend we know what we're doing...

        return true;
    }

    public boolean handleRequest(MessageContext messageContext, Object endpoint) {
        LOGGER.debug("handleRequest(messageContext={}, endpoint={})", messageContext, endpoint);

        return true;
    }

    public boolean handleResponse(MessageContext messageContext, Object endpoint) {
        LOGGER.debug("handleResponse(messageContext={}, endpoint={})", messageContext, endpoint);

        return true;
    }

    public boolean handleFault(MessageContext messageContext, Object endpoint) {
        LOGGER.debug("handleFault(messageContext={}, endpoint={})", messageContext, endpoint);

        return true;
    }

    public void afterCompletion(MessageContext messageContext, Object endpoint, Exception ex) {
        LOGGER.debug(String.format("afterCompletion(messageContext=%s, endpoint=%s, ex=%s)", messageContext, endpoint,
                ex));
    }

}
