package org.motechproject.commcare.events;

import org.motechproject.commcare.service.impl.CommcareCaseEventParser;
import org.motechproject.commons.api.TasksEventParser;
import org.motechproject.event.MotechEvent;

import java.util.HashMap;
import java.util.Map;

import static org.motechproject.commcare.events.constants.EventDataKeys.CONFIG_NAME;
import static org.motechproject.commcare.events.constants.EventDataKeys.FAILED_CASE_MESSAGE;
import static org.motechproject.commcare.events.constants.EventSubjects.CASES_FAIL_EVENT;

/**
 * Represents a failure event for a full case received.
 */
public class FullCaseFailureEvent {
    /**
     * Name of the Motech Commcare configuration this failure affected.
     */
    private final String configName;

    /**
     * The error message.
     */
    private final String errorMessage;

    /**
     * @param configName name of the Motech Commcare configuration this failure affected
     * @param errorMessage the error message
     */
    public FullCaseFailureEvent(String configName, String errorMessage) {
        this.configName = configName;
        this.errorMessage = errorMessage;
    }

    /**
     * Builds a {@link MotechEvent} instance to send.
     * @return the event to send
     */
    public MotechEvent toMotechEvent() {
        Map<String, Object> params = new HashMap<>();

        params.put(TasksEventParser.CUSTOM_PARSER_EVENT_KEY, CommcareCaseEventParser.PARSER_NAME);
        params.put(CONFIG_NAME, configName);
        params.put(FAILED_CASE_MESSAGE, errorMessage);

        return new MotechEvent(CASES_FAIL_EVENT, params);
    }
}
