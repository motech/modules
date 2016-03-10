package org.motechproject.atomclient.exception;

/**
 * Exception thrown when an invalid setting is found
 *
 * Note: currently only an invalid fetch job cron exception will trigger this
 */
public class AtomClientConfigurationException extends RuntimeException {

    private static final long serialVersionUID = -8988750253213562089L;

    public AtomClientConfigurationException(String message) {
        super(message);
    }

    public AtomClientConfigurationException(String message, Throwable t) {
        super(message, t);
    }
}
