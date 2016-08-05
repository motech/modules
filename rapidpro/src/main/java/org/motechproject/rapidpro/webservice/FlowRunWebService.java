package org.motechproject.rapidpro.webservice;


import org.motechproject.rapidpro.exception.WebServiceException;
import org.motechproject.rapidpro.webservice.dto.FlowRunRequest;
import org.motechproject.rapidpro.webservice.dto.FlowRunResponse;

import java.util.List;

/**
 * Webservice interface for RapidPro REST API for flow runs.
 */
public interface FlowRunWebService {

    /**
     * Initiates a flow run request to RapidPro.
     * @param flowRunRequest
     * @return a List of {@link FlowRunResponse}
     * @throws WebServiceException If there is an error communicating with RapidPro.
     */
    List<FlowRunResponse> startFlowRuns(FlowRunRequest flowRunRequest) throws WebServiceException;
}
