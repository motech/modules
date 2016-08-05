package org.motechproject.rapidpro.webservice.impl;

import org.apache.commons.io.IOUtils;
import org.codehaus.jackson.type.TypeReference;
import org.motechproject.rapidpro.exception.JsonUtilException;
import org.motechproject.rapidpro.exception.RapidProClientException;
import org.motechproject.rapidpro.exception.WebServiceException;
import org.motechproject.rapidpro.util.JsonUtils;
import org.motechproject.rapidpro.webservice.AbstractWebService;
import org.motechproject.rapidpro.webservice.FlowWebService;
import org.motechproject.rapidpro.webservice.MediaFormat;
import org.motechproject.rapidpro.webservice.RapidProHttpClient;
import org.motechproject.rapidpro.webservice.dto.Flow;
import org.motechproject.rapidpro.webservice.dto.PaginatedResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Implementation of {@link org.motechproject.rapidpro.webservice.FlowWebService}
 */

@Service("rapidproFlowWebService")
public class FlowWebServiceImpl extends AbstractWebService<Flow> implements FlowWebService {
    private static final String FINDING_BY_NAME = "Finding flow with name: ";
    private static final String FINDING_BY_UUID = "Finding flow with UUID: ";
    private static final String FLOWS_ENDPOINT = "/flows";
    private static final String MORE_THAN_ONE_FLOW = "Query returned more than one flow";
    private static final String ERROR_RETRIEVING_FLOWS = "Error Retrieving flows from RapidPro: ";

    private static final Logger LOGGER = LoggerFactory.getLogger(FlowWebServiceImpl.class);
    private static final TypeReference PAGED_RESPONSE_FLOW_TYPE_REF = new TypeReference<PaginatedResponse<Flow>>() { };

    @Autowired
    public FlowWebServiceImpl(RapidProHttpClient client) {
        super(client);
    }

    @Override
    public Flow getFlow(String name) throws WebServiceException {
        LOGGER.debug(FINDING_BY_NAME + name);
        try {
            PaginatedResponse<Flow> flows = getResponse();
            List<Flow> filtered = flows.getResults()
                    .stream()
                    .filter(flow -> flow.getName().equals(name))
                    .collect(Collectors.toList());
            return getOne(filtered);

        } catch (RapidProClientException | JsonUtilException e) {
            throw new WebServiceException(ERROR_RETRIEVING_FLOWS + e.getMessage());
        }
    }

    @Override
    public Flow getFlow(UUID uuid) throws WebServiceException {
        LOGGER.debug(FINDING_BY_UUID + uuid);
        try {
            PaginatedResponse<Flow> flows = getResponse();
            List<Flow> filtered = flows.getResults()
                    .stream()
                    .filter(flow -> flow.getUuid().equals(uuid))
                    .collect(Collectors.toList());
            return getOne(filtered);

        } catch (RapidProClientException | JsonUtilException e) {
            throw new WebServiceException(ERROR_RETRIEVING_FLOWS + e.getMessage());
        }
    }

    private PaginatedResponse<Flow> getResponse() throws RapidProClientException, JsonUtilException {
        InputStream response = null;

        try  {
            response = getClient().executeGet(FLOWS_ENDPOINT, MediaFormat.JSON, null);
            return (PaginatedResponse<Flow>) JsonUtils.toObject(response, PAGED_RESPONSE_FLOW_TYPE_REF);

        } finally {
            IOUtils.closeQuietly(response);
        }
    }

    private Flow getOne(List<Flow> filtered) throws WebServiceException {
        if (filtered.size() == 0) {
            return null;

        } else if (filtered.size() == 1) {
            return filtered.get(0);

        } else {
            throw new WebServiceException(MORE_THAN_ONE_FLOW);
        }
    }
}
