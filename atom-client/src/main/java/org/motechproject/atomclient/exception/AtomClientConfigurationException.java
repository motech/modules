package org.motechproject.atomclient.exception;

public class AtomClientConfigurationException extends RuntimeException {

    public AtomClientConfigurationException(String message) {
        super(message);
    }

    public AtomClientConfigurationException(String message, Throwable t) {
        super(message, t);
    }
}
