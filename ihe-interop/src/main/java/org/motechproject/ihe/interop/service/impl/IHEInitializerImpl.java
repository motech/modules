package org.motechproject.ihe.interop.service.impl;

import org.motechproject.ihe.interop.service.IHEInitializer;
import org.motechproject.ihe.interop.groovy.IHEGroovyInitializer;
import org.motechproject.ihe.interop.groovy.impl.IHEGroovyInitializerImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

@Service("iheInitializer")
public class IHEInitializerImpl implements IHEInitializer {

    private static final Logger LOGGER = LoggerFactory.getLogger(IHEInitializerImpl.class);

    private IHEGroovyInitializer iheGroovyInitializer = new IHEGroovyInitializerImpl();

    @Override
    @PostConstruct
    public String init() {
        LOGGER.info("IHE Interop Module started - Java initializer");
        return iheGroovyInitializer.init();
    }

    public IHEGroovyInitializer getIheGroovyInitializer() {
        return iheGroovyInitializer;
    }

    public void setIheGroovyInitializer(IHEGroovyInitializer iheGroovyInitializer) {
        this.iheGroovyInitializer = iheGroovyInitializer;
    }
}
