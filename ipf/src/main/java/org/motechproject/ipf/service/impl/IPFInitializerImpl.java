package org.motechproject.ipf.service.impl;

import org.motechproject.ipf.groovy.IPFGroovyInitializer;
import org.motechproject.ipf.groovy.impl.IPFGroovyInitializerImpl;
import org.motechproject.ipf.service.IPFInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

@Service("ipfInitializer")
public class IPFInitializerImpl implements IPFInitializer {

    private static final Logger LOGGER = LoggerFactory.getLogger(IPFInitializerImpl.class);

    private IPFGroovyInitializer ipfGroovyInitializer = new IPFGroovyInitializerImpl();

    @Override
    @PostConstruct
    public String init() {
        LOGGER.info("IPF Module started - Java compiled.");
        return ipfGroovyInitializer.init();
    }

    public IPFGroovyInitializer getIpfGroovyInitializer() {
        return ipfGroovyInitializer;
    }

    public void setIpfGroovyInitializer(IPFGroovyInitializer ipfGroovyInitializer) {
        this.ipfGroovyInitializer = ipfGroovyInitializer;
    }
}
