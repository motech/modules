package org.motechproject.http.agent.domain;

/**
 * Event data keys.
 */
public final class EventDataKeys {

    private EventDataKeys() {
        // static utility class
    }

    public static final String DATA = "data";
    public static final String URL = "url";
    public static final String METHOD = "method";

    public static final String HEADERS = "headers";
    public static final String USERNAME = "username";
    public static final String PASSWORD = "password";

    // Constants required for executeWithReturnType used by hub module
    public static final String RETRY_COUNT = "retry_count";
    public static final String RETRY_INTERVAL = "retry_interval";

}
