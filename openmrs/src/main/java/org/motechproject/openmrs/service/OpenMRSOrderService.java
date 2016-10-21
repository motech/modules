package org.motechproject.openmrs.service;

import org.motechproject.openmrs.domain.Order;

/**
 * Interface for handling orders on the OpenMRS server.
 */
public interface OpenMRSOrderService {

    /**
     * Creates the given {@code order} on the OpenMRS server. Configuration with the given {@code configName} will
     * be used while performing this action.
     *
     * @param configName  the name of the configuration
     * @param order  the order to be created
     * @return  the created order
     */
    Order createOrder(String configName, Order order);
}
