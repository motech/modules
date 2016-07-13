package org.motechproject.http.agent.utility;

import org.springframework.http.HttpStatus;

/**
 * Utility class to check if given HTTP status has an error code.
 */

public final class RestUtility {

    private RestUtility(){
    }

    public static boolean isError(HttpStatus status) {
        HttpStatus.Series series = status.series();
        return (HttpStatus.Series.CLIENT_ERROR.equals(series)
                || HttpStatus.Series.SERVER_ERROR.equals(series));
    }
}