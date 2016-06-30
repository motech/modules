package org.motechproject.rapidpro.webservice.impl;

import org.codehaus.jackson.type.TypeReference;
import org.motechproject.rapidpro.exception.JsonUtilException;
import org.motechproject.rapidpro.exception.RapidProClientException;
import org.motechproject.rapidpro.exception.WebServiceException;
import org.motechproject.rapidpro.webservice.AbstractWebService;
import org.motechproject.rapidpro.webservice.GroupWebService;
import org.motechproject.rapidpro.webservice.RapidProHttpClient;
import org.motechproject.rapidpro.webservice.dto.Group;
import org.motechproject.rapidpro.webservice.dto.PaginatedResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Implementation of {@link GroupWebService}
 */

@Service("rapidproGroupWebService")
public class GroupWebServiceImpl extends AbstractWebService<Group> implements GroupWebService {

    private static final String GROUPS_ENDPOINT = "/groups";
    private static final String FINDING_NAME = "Finding Group with name: ";
    private static final String ERROR_RETRIEVING = "Error retrieving group with name: ";

    private static final Logger LOGGER = LoggerFactory.getLogger(GroupWebServiceImpl.class);
    private static final TypeReference PAGED_RESPONSE_GROUP_TYPE_REF = new TypeReference<PaginatedResponse<Group>>() { };

    @Autowired
    public GroupWebServiceImpl(RapidProHttpClient httpClient) {
        super(httpClient);
    }

    @Override
    public Group getGroupByName(String groupName) throws WebServiceException {
        try {
            LOGGER.debug(FINDING_NAME + groupName);
            return getOneWithParams(NAME, groupName, GROUPS_ENDPOINT, PAGED_RESPONSE_GROUP_TYPE_REF);
        } catch (RapidProClientException | JsonUtilException e) {
            throw new WebServiceException(ERROR_RETRIEVING + groupName, e);
        }
    }
}
