package org.motechproject.batch.service.impl;

import javax.batch.api.Batchlet;

import org.apache.log4j.Logger;

public class TestBatchlet implements Batchlet {

    private static final Logger LOGGER = Logger
            .getLogger(JobTriggerServiceImpl.class);
    
    @Override
    public String process() {
        LOGGER.debug("Processing Batchlet");
        return "Step executed";
    }

    @Override
    public void stop() {
        // TODO Auto-generated method stub

    }

}
