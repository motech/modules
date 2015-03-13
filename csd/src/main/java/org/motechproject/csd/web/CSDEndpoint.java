package org.motechproject.csd.web;

import org.motechproject.csd.domain.CSD;
import org.motechproject.csd.domain.GetModificationsRequest;
import org.motechproject.csd.domain.GetModificationsResponse;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;

@Endpoint
public class CSDEndpoint {

    public CSDEndpoint() {
    }

    @PayloadRoot(namespace = "urn:ihe:iti:csd:2013", localPart = "getModificationsRequest")
    @ResponsePayload
    public GetModificationsResponse getModifications(@RequestPayload GetModificationsRequest request) {
        GetModificationsResponse response = new GetModificationsResponse();
        // TODO: fetch CSD from MDS
        response.setCSD(new CSD());

        return response;
    }
}
