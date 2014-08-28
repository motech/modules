package org.motechproject.commcare.service;

import org.motechproject.commcare.domain.CommcareDataForwardingEndpoint;

import java.util.List;

/**
 * A service to perform queries and updates against CommCareHQ's data forwarding endpoints.
 */
public interface CommcareDataForwardingEndpointService {

    /**
     * Queries CommCareHQ for a list of all data forwarding rules on the configured domain.
     * @return A list of CommcareDataForwardingEndpoints that represent the information about each data forwarding rule from CommCareHQ
     */
    List<CommcareDataForwardingEndpoint> getAllDataForwardingEndpoints();

    /**
     * Queries CommCareHQ for a list of data forwarding rules(located on the given page) on the configured domain.
     * @param pageSize
     * @param pageNumber
     * @return A list of CommcareDataForwardingEndpoints that represent the information about each data forwarding rule located on the given pagefrom CommCareHQ
     */
    List<CommcareDataForwardingEndpoint> getDataForwardingEndpoints(Integer pageSize, Integer pageNumber);

    /**
     * Creates a new data forwarding rule for the CommCareHQ.
     * @return true - if data was successfully sent; otherwise - false
     */
    boolean createNewDataForwardingRule(CommcareDataForwardingEndpoint newForwardingRule);

    /**
     * Updates specified CommCareHQ data forwarding rule.
     * @return true - if the data has been updated; otherwise - false
     */
    boolean updateDataForwardingRule(CommcareDataForwardingEndpoint updatedForwardingRule);
}
