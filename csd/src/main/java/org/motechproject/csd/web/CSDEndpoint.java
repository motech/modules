package org.motechproject.csd.web;

import org.joda.time.DateTime;
import org.motechproject.csd.domain.GetModificationsRequest;
import org.motechproject.csd.domain.GetModificationsResponse;
import org.motechproject.csd.service.CSDService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;
import org.springframework.ws.soap.addressing.server.annotation.Action;

@Endpoint
public class CSDEndpoint {

    private static final Logger LOGGER = LoggerFactory.getLogger(CSDEndpoint.class);

    @Autowired
    private CSDService csdService;

    @Action(value = "urn:ihe:iti:csd:2013:GetDirectoryModificationsRequest")
    @PayloadRoot(namespace = "urn:ihe:iti:csd:2013", localPart = "getModificationsRequest")
    @ResponsePayload
    public GetModificationsResponse getModifications(@RequestPayload GetModificationsRequest request) {
        LOGGER.debug("getModifications lastModified: " + request.getLastModified().toString());
        GetModificationsResponse response = new GetModificationsResponse();
        DateTime lastModified = new DateTime(request.getLastModified().toGregorianCalendar().getTime());
        response.setCSD(csdService.getByLastModification(lastModified));

        return response;
    }
}
