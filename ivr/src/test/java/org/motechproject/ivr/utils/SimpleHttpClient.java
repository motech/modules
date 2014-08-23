package org.motechproject.ivr.utils;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.DefaultHttpClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * Simple HTTP client with retries
 */
public class SimpleHttpClient {
    private static final int NUM_TRIES = 3;
    private static final int MS_WAIT = 5000;

    private static Logger logger = LoggerFactory.getLogger(SimpleHttpClient.class);

    public static boolean execHttpRequest(HttpUriRequest request, int expectedStatus) throws InterruptedException,
            IOException {
        int tries = 0;
        do {
            tries++;
            HttpResponse response = new DefaultHttpClient().execute(request);
            if (expectedStatus == response.getStatusLine().getStatusCode()) {
                logger.debug(String.format("Successfully received HTTP %d in %d %s", expectedStatus, tries,
                        tries == 1 ? "try" : "tries"));
                return true;
            }
            logger.debug(String.format("Was expecting HTTP %d but received %d, trying again in %f", expectedStatus,
                    response.getStatusLine().getStatusCode(), MS_WAIT/1000.0));
            Thread.sleep(MS_WAIT);
        } while (tries < NUM_TRIES);

        logger.debug("Giving up trying to receive HTTP {} after {} tries", expectedStatus, NUM_TRIES);
        return false;
    }
}
