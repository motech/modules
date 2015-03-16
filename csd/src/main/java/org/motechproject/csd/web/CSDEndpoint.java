package org.motechproject.csd.web;

import org.joda.time.DateTime;
import org.motechproject.csd.domain.GetModificationsRequest;
import org.motechproject.csd.domain.GetModificationsResponse;
import org.motechproject.csd.service.CSDService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;

@Endpoint
public class CSDEndpoint {

    @Autowired
    private CSDService csdService;

    @PayloadRoot(namespace = "urn:ihe:iti:csd:2013", localPart = "getModificationsRequest")
    @ResponsePayload
    public GetModificationsResponse getModifications(@RequestPayload GetModificationsRequest request) {
        GetModificationsResponse response = new GetModificationsResponse();
        DateTime lastModified = new DateTime(request.getLastModified().toGregorianCalendar().getTime());
        response.setCSD(csdService.getByLastModified(lastModified));

        return response;
    }
}
