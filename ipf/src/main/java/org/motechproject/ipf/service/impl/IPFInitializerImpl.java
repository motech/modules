package org.motechproject.ipf.service.impl;

import org.motechproject.ipf.service.IPFInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
public class IPFInitializerImpl implements IPFInitializer {

    private static final Logger LOGGER = LoggerFactory.getLogger(IPFInitializerImpl.class);

    @Override
    @PostConstruct
    public void init() {
        LOGGER.info("IPF Module started - Java compiled.");
    }
}
