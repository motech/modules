package org.motechproject.eventlogging.converter.impl;

import org.joda.time.DateTime;
import org.motechproject.event.MotechEvent;
import org.motechproject.eventlogging.converter.EventToLogConverter;
import org.motechproject.eventlogging.domain.EventLog;
import org.motechproject.eventlogging.matchers.MappedLoggableEvent;
import org.springframework.stereotype.Component;

/**
 * Implementaton of the {@link EventToLogConverter} interface.
 * This class is responsible for converting {@link MotechEvent}s to the
 * class persistable in the database.
 */
@Component
public class DefaultDbToLogConverter implements EventToLogConverter<EventLog> {

    @Override
    public EventLog convertToLog(MotechEvent eventToLog) {

        EventLog eventLog = new EventLog(eventToLog.getSubject(), eventToLog.getParameters(),
                DateTime.now());

        return eventLog;
    }

    /**
     * Converts an event to a {@link EventLog} object, which represents a single
     * logged event in database. Uses {@link MappedLoggableEvent} implementation of LoggableEvent
     * to configure the returned object.
     *
     * @param eventToLog the incoming motech event to be converted to a database event log
     * @param loggableEvent contains data about an event to log, used to configure returned log instance
     * @return EventLog object that is used to log the event to the database
     * @throws org.motechproject.commons.api.MotechException if the passed LoggableEvent is not an instance of MappedLoggableEvent
     */
    public EventLog configuredConvertEventToDbLog(MotechEvent eventToLog, MappedLoggableEvent loggableEvent) {
        EventLog eventLog = new EventLog(eventToLog.getSubject(), loggableEvent.filterParams(eventToLog), DateTime.now());

        return eventLog;
    }
}
