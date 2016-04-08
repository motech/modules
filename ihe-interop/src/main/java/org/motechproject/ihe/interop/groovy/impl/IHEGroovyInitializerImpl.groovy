package org.motechproject.ihe.interop.groovy.impl

import org.motechproject.ihe.interop.groovy.IHEGroovyInitializer
import org.slf4j.Logger
import org.slf4j.LoggerFactory

public class IHEGroovyInitializerImpl implements IHEGroovyInitializer {

    private static final Logger LOGGER = LoggerFactory.getLogger(IHEGroovyInitializerImpl.class);

    @Override
    String init() {
        LOGGER.info("IHE Interop Module started - Groovy compiled.")
        return "Groovy compiled";
    }
}
