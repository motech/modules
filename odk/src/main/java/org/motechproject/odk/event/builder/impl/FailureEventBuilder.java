package org.motechproject.odk.event.builder.impl;

import org.motechproject.event.MotechEvent;
import org.motechproject.odk.constant.EventParameters;
import org.motechproject.odk.constant.EventSubjects;

import java.util.HashMap;
import java.util.Map;

/**
 * Builder class for form failure events.
 */
public class FailureEventBuilder {

    private String message;
    private String error;
    private String configName;
    private String formTitle;
    private String body;

    public FailureEventBuilder setMessage(String message) {
       this.message = message;
       return this;
    }

    public FailureEventBuilder setError(String error) {
        this.error = error;
        return this;
    }

    public FailureEventBuilder setConfigName(String configName) {
        this.configName = configName;
        return this;
    }

    public FailureEventBuilder setFormTitle(String formTitle) {
        this.formTitle = formTitle;
        return this;
    }

    public FailureEventBuilder setBody(String body) {
        this.body = body;
        return this;
    }

    /**
     * Creates a {@link MotechEvent} for form receipt failures.
     * @return {@link MotechEvent}
     */
    public MotechEvent createFailureEvent() {
        Map<String, Object> params = new HashMap<>();
        params.put(EventParameters.CONFIGURATION_NAME, configName);
        params.put(EventParameters.FORM_TITLE, formTitle);
        params.put(EventParameters.MESSAGE, message);
        params.put(EventParameters.EXCEPTION, error);
        params.put(EventParameters.JSON_CONTENT, body);
        return new MotechEvent(EventSubjects.FORM_FAIL, params);
    }
}
