package org.motechproject.http.agent.components;

import org.motechproject.event.MotechEvent;
import org.springframework.http.ResponseEntity;

/**
 * CommunicationType component is used for sending http requests.
 *
 * @see org.motechproject.http.agent.components.SynchronousCall
 * @see org.motechproject.http.agent.components.AsynchronousCall
 * @see org.motechproject.http.agent.domain.EventDataKeys
 */
public interface CommunicationType {

    /**
     * Sends an http request. Request sections such as headers, url or method are built from event parameters.
     * @param motechEvent the event which contains data for request
     */
    void send(MotechEvent motechEvent);

    /**
     * Sends an http request. Request sections such as headers, url or method are built from event parameters.
     * Returns the response from the performed request. If the request fails, then it will be posted again, retry count
     * is specified by event parameter with org.motechproject.http.agent.domain.EventDataKeys.RETRY_COUNT key(default value is 1).
     * Subsequent attempts are invoked at certain periods of time which is specified by event parameter with
     * org.motechproject.http.agent.domain.EventDataKeys.RETRY_INTERVAL key(default value is 0, expressed in milliseconds).
     * @param motechEvent the event which contains data for the request
     * @return response from the posted request
     */
    ResponseEntity<?> sendWithReturnType(MotechEvent motechEvent);
}
