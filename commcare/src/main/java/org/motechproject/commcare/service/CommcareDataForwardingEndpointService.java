package org.motechproject.commcare.service;

import org.motechproject.commcare.config.Config;
import org.motechproject.commcare.domain.CommcareDataForwardingEndpoint;

import java.util.List;

/**
 * A service to perform queries and updates against CommCareHQ's data forwarding endpoints.
 */
public interface CommcareDataForwardingEndpointService {

    /**
     * Queries CommCareHQ for a list of all data forwarding rules on the configured domain.
     *
     * @param config  the configuration to be used when connecting to the CommcareHQ server
     * @return  the list of CommcareDataForwardingEndpoints that represent the information about each data forwarding
     * rule from CommCareHQ
     */
    List<CommcareDataForwardingEndpoint> getAllDataForwardingEndpoints(Config config);

    /**
     * Queries CommCareHQ for a list of data forwarding rules(located on the given page) on the configured domain.
     *
     * @param pageSize  the size of the page
     * @param pageNumber  the number of the page
     * @param config  the configuration to be used when connecting to the CommcareHQ server
     * @return  the list of CommcareDataForwardingEndpoints that represent the information about each data forwarding
     *          rule located on the given page from CommCareHQ
     */
    List<CommcareDataForwardingEndpoint> getDataForwardingEndpoints(Integer pageSize, Integer pageNumber, Config config);

    /**
     * Creates a new data forwarding rule for the CommCareHQ.
     *
     * @return true - if data was successfully sent; otherwise - false
     */
    boolean createNewDataForwardingRule(CommcareDataForwardingEndpoint newForwardingRule, Config config);

    /**
     * Updates specified CommCareHQ data forwarding rule.
     *
     * @param updatedForwardingRule  the rule to be updated
     * @param config  the configuration to be used when connecting to the CommcareHQ server
     * @return  true - if the data has been updated, false otherwise
     */
    boolean updateDataForwardingRule(CommcareDataForwardingEndpoint updatedForwardingRule, Config config);

}
