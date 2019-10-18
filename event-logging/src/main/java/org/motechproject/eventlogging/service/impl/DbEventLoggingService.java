package org.motechproject.eventlogging.service.impl;

import org.motechproject.event.MotechEvent;
import org.motechproject.eventlogging.converter.impl.DefaultDbToLogConverter;
import org.motechproject.eventlogging.loggers.impl.DbEventLogger;
import org.motechproject.eventlogging.matchers.LoggableEvent;
import org.motechproject.eventlogging.repository.AllEventMappings;
import org.motechproject.eventlogging.service.EventLogService;
import org.motechproject.eventlogging.service.EventLoggingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * The implementation of {@link org.motechproject.eventlogging.service.EventLoggingService}.
 * It is used to persist logs in the database.
 */
@Service
public class DbEventLoggingService implements EventLoggingService {

    private List<DbEventLogger> dbEventLoggers;

    private DbEventLogger defaultDbEventLogger;

    @Autowired
    private AllEventMappings allEventMappings;

    @Autowired
    private EventLogService eventLogService;

    @Autowired
    private DefaultDbToLogConverter defaultDbToLogConverter;

    /**
     * Creates an instance of DbEventLoggingService without any loggers.
     */
    public DbEventLoggingService() {
        this.dbEventLoggers = Collections.<DbEventLogger> emptyList();
    }

    /**
     * Creates an instance of DbEventLoggingService using {@link org.motechproject.eventlogging.repository.AllEventMappings}
     * configuration, empty loggers list and a default event logger.
     *
     * @param allEventMappings AllEventMappings object representing all event mappings read from json configuration file
     */
    public DbEventLoggingService(AllEventMappings allEventMappings) {
        this.allEventMappings = allEventMappings;
        this.dbEventLoggers = Collections.<DbEventLogger> emptyList();
        this.defaultDbEventLogger = createDefaultEventLogger();
    }

    @PostConstruct
    private DbEventLogger createDefaultEventLogger() {
        defaultDbEventLogger = new DbEventLogger(eventLogService, defaultDbToLogConverter);
        List<LoggableEvent> loggableEvents = allEventMappings.converToLoggableEvents();
        defaultDbEventLogger.addLoggableEvents(loggableEvents);

        return defaultDbEventLogger;
    }

    /**
     * Creates an instance of DbEventLoggingService and adds to it all loggers
     * passed as a parameter.
     *
     * @param dbEventLoggers list of loggers to add to this service
     */
    public DbEventLoggingService(List<DbEventLogger> dbEventLoggers) {
        this.dbEventLoggers = new ArrayList<>();
        this.dbEventLoggers.addAll(dbEventLoggers);
    }

    @Override
    public void logEvent(MotechEvent event) {
        for (DbEventLogger dbEventLogger : dbEventLoggers) {
            dbEventLogger.log(event);
        }

        defaultDbEventLogger.log(event);
    }

    @Override
    public Set<String> getLoggedEventSubjects() {
        Set<String> eventSubjectsSet = new HashSet<String>();
        for (DbEventLogger eventLogger : dbEventLoggers) {
            List<LoggableEvent> events = eventLogger.getLoggableEvents();
            for (LoggableEvent event : events) {
                List<String> eventSubjects = event.getEventSubjects();
                eventSubjectsSet.addAll(eventSubjects);
            }
        }

        List<LoggableEvent> events = defaultDbEventLogger.getLoggableEvents();
        for (LoggableEvent event : events) {
            List<String> eventSubjects = event.getEventSubjects();
            eventSubjectsSet.addAll(eventSubjects);
        }

        return eventSubjectsSet;
    }

    public List<DbEventLogger> getDbEventLoggers() {
        return dbEventLoggers;
    }

    public void setDbEventLoggers(List<DbEventLogger> dbEventLoggers) {
        this.dbEventLoggers = dbEventLoggers;
    }

    public DbEventLogger getDefaultDbEventLogger() {
        return defaultDbEventLogger;
    }

    public void setDefaultDbEventLogger(DbEventLogger defaultDbEventLogger) {
        this.defaultDbEventLogger = defaultDbEventLogger;
    }

    public AllEventMappings getAllEventMappings() {
        return allEventMappings;
    }

    public void setAllEventMappings(AllEventMappings allEventMappings) {
        this.allEventMappings = allEventMappings;
    }

    public DefaultDbToLogConverter getDefaultDbToLogConverter() {
        return defaultDbToLogConverter;
    }

    public void setDefaultDbToLogConverter(DefaultDbToLogConverter defaultDbToLogConverter) {
        this.defaultDbToLogConverter = defaultDbToLogConverter;
    }

}
