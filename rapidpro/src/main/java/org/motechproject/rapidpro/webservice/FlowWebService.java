package org.motechproject.rapidpro.webservice;

import org.motechproject.rapidpro.exception.WebServiceException;
import org.motechproject.rapidpro.webservice.dto.Flow;

import java.util.UUID;

/**
 * Webservice interface for RapidPro REST API for Flows.
 */
public interface FlowWebService {

    /**
     * Finds a flow by its name, if it exists.
     *
     * @param name The name of the flow
     * @return {@link Flow}
     * @throws WebServiceException If there is an error communicating with RapidPro,
     */
    Flow getFlow(String name) throws WebServiceException;

    /**
     * Finds a flow by its UUID, if it exists.
     *
     * @param uuid The UUID of the flow
     * @return {@link Flow}
     * @throws WebServiceException If there is an error communicating with RapidPro,
     */
    Flow getFlow(UUID uuid) throws WebServiceException;
}
