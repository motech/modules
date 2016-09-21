package org.motechproject.rapidpro.webservice.impl;


import org.apache.commons.io.IOUtils;
import org.codehaus.jackson.type.TypeReference;
import org.motechproject.rapidpro.exception.JsonUtilException;
import org.motechproject.rapidpro.exception.RapidProClientException;
import org.motechproject.rapidpro.exception.WebServiceException;
import org.motechproject.rapidpro.util.JsonUtils;
import org.motechproject.rapidpro.webservice.AbstractWebService;
import org.motechproject.rapidpro.webservice.FlowRunWebService;
import org.motechproject.rapidpro.webservice.MediaFormat;
import org.motechproject.rapidpro.webservice.RapidProHttpClient;
import org.motechproject.rapidpro.webservice.dto.FlowRunRequest;
import org.motechproject.rapidpro.webservice.dto.FlowRunResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.util.List;

/**
 * Implementation of {@link FlowRunWebService}
 */
@Service("rapidproFlowRunWebService")
public class FlowRunWebServiceImpl extends AbstractWebService<FlowRunResponse> implements FlowRunWebService {
    private static final String RUNS_ENDPOINT = "/runs";
    private static final String ERROR_EXECUTING_FLOW_RUN = "Error executing flow run: ";
    private static final String EXECUTING_FLOW_RUN_REQUEST = "Executing flow run request: ";

    private static final TypeReference LIST_FLOW_RUN_RESPONSE = new TypeReference<List<FlowRunResponse>>() {
    };
    private static final Logger LOGGER = LoggerFactory.getLogger(FlowRunWebServiceImpl.class);

    @Autowired
    public FlowRunWebServiceImpl(RapidProHttpClient client) {
        super(client);
    }

    @Override
    public List<FlowRunResponse> startFlowRuns(FlowRunRequest flowRunRequest) throws WebServiceException {
        LOGGER.debug(EXECUTING_FLOW_RUN_REQUEST + flowRunRequest.toString());
        InputStream response = null;
        try {
            byte[] body = JsonUtils.toByteArray(flowRunRequest);
            response = getClient().executePost(RUNS_ENDPOINT, body, MediaFormat.JSON, MediaFormat.JSON);
            return (List<FlowRunResponse>) JsonUtils.toObject(response, LIST_FLOW_RUN_RESPONSE);
        } catch (JsonUtilException | RapidProClientException e) {
            throw new WebServiceException(ERROR_EXECUTING_FLOW_RUN + e.getMessage(), e);
        } finally {
            IOUtils.closeQuietly(response);
        }
    }
}
