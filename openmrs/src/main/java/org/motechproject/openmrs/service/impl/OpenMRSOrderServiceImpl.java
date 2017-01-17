package org.motechproject.openmrs.service.impl;

import org.apache.commons.lang.Validate;
import org.motechproject.openmrs.config.Config;
import org.motechproject.openmrs.domain.Order;
import org.motechproject.openmrs.exception.OpenMRSException;
import org.motechproject.openmrs.resource.OrderResource;
import org.motechproject.openmrs.service.OpenMRSConfigService;
import org.motechproject.openmrs.service.OpenMRSOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;

@Service("orderService")
public class OpenMRSOrderServiceImpl implements OpenMRSOrderService {

    private OrderResource orderResource;
    private OpenMRSConfigService configService;

    @Autowired
    public OpenMRSOrderServiceImpl(OrderResource orderResource, OpenMRSConfigService configService) {
        this.orderResource = orderResource;
        this.configService = configService;
    }

    @Override
    public Order createOrder(String configName, Order order) {
        validateOrder(order);

        try {
            Config config = configService.getConfigByName(configName);
            return orderResource.createOrder(config, order);
        } catch (HttpClientErrorException e) {
            throw new OpenMRSException("Error while creating order. Response body: " + e.getResponseBodyAsString(), e);
        }
    }

    private void validateOrder(Order order) {
        Validate.notNull(order, "Order cannot be null");
        Validate.notNull(order.getConcept(), "Concept cannot be null");
        Validate.notNull(order.getCareSetting(), "Care setting cannot be null");
        Validate.notNull(order.getEncounter(), "Encounter cannot be null");
        Validate.notNull(order.getOrderer(), "Orderer type cannot be null");
        Validate.notNull(order.getType(), "Type cannot be null");
        Validate.notNull(order.getPatient(), "Patient cannot be null");
    }
}
