package org.motechproject.batch.service.impl;

import javax.batch.api.Batchlet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This is a batchlet class used to test trigger job
 *
 * @author naveen
 *
 */
public class TestBatchlet implements Batchlet {

    private static final Logger LOGGER = LoggerFactory
            .getLogger(TestBatchlet.class);

    @Override
    public String process() {
        LOGGER.debug("Processing Batchlet");
        return "Step executed";
    }

    @Override
    public void stop() {

    }

}
