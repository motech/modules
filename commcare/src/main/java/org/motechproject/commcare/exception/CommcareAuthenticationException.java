package org.motechproject.commcare.exception;

public class CommcareAuthenticationException extends RuntimeException {

    public CommcareAuthenticationException(String message) {
        super(message);
    }

    public CommcareAuthenticationException(Exception ex, String message) {
        super(message, ex);
    }
}
