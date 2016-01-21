package org.motechproject.ipf.groovy.impl

import org.motechproject.ipf.groovy.IPFGroovyInitializer
import org.slf4j.Logger
import org.slf4j.LoggerFactory

public class IPFGroovyInitializerImpl implements IPFGroovyInitializer {

    private static final Logger LOGGER = LoggerFactory.getLogger(IPFGroovyInitializerImpl.class);

    @Override
    String init() {
        LOGGER.info("IPF Module started - Groovy compiled.")
        return "Groovy compiled";
    }
}
