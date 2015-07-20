package org.motechproject.batch.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.batch.api.Batchlet;

/**
 * This is a batchlet class that simply logs its executions.
 *
 * @author naveen
 *
 */
public class LoggingBatchlet implements Batchlet {

    private static final Logger LOGGER = LoggerFactory.getLogger(LoggingBatchlet.class);

    @Override
    public String process() {
        LOGGER.debug("Processing Batchlet");
        return "Step executed";
    }

    @Override
    public void stop() {
    }
}
