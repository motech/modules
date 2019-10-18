package org.motechproject.eventlogging.converter.impl;

import org.joda.time.DateTime;
import org.motechproject.event.MotechEvent;
import org.motechproject.eventlogging.converter.EventToLogConverter;
import org.motechproject.eventlogging.matchers.MappedLoggableEvent;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * Implementaton of the {@link EventToLogConverter} interface.
 * This class is responsible for converting {@link MotechEvent}s to the
 * state that can be injected in the file.
 */
@Component
public class DefaultFileToLogConverter implements EventToLogConverter<String> {

    @Override
    public String convertToLog(MotechEvent event) {
        return convertToLogString(event.getSubject(), event.getParameters());
    }

    /**
     * Converts an event into a {@link String} with parameters configured by {@link MappedLoggableEvent}.
     * @param event Motech event to be converted into a file event log
     * @param loggableEvent Used to configure parameter conversion
     * @return String used to log into file.
     */
    public String convertToLogMapped(MotechEvent event, MappedLoggableEvent loggableEvent) {
        Map<String, Object> finalParameters = loggableEvent.filterParams(event);

        return convertToLogString(event.getSubject(), finalParameters);
    }

    private String convertToLogString(String subject, Map<String, Object> parameters) {
        StringBuilder log = new StringBuilder("EVENT: ");

        log.append(subject + " at TIME: " + DateTime.now());

        if (parameters.size() > 0) {
            log.append(" with PARAMETERS: ");

            for (Map.Entry<String, Object> entry : parameters.entrySet()) {
                log.append(entry.getKey() + "/" + entry.getValue().toString() + " ");
            }
        }

        return log.toString();
    }

}
