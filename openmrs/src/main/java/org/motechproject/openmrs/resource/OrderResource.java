package org.motechproject.openmrs.resource;

import org.motechproject.openmrs.config.Config;
import org.motechproject.openmrs.domain.Order;

/**
 * Interface for orders management.
 */
public interface OrderResource {

    /**
     * Creates the given order on the OpenMRS server. The given {@code config} will be used while performing this
     * action.
     *
     * @param config  the configuration to be used while performing this action
     * @param order  the order to be created
     * @return  the saved order
     */
    Order createOrder(Config config, Order order);
}
