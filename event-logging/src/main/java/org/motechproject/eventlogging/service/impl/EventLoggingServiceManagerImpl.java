package org.motechproject.eventlogging.service.impl;

import org.motechproject.event.MotechEvent;
import org.motechproject.event.listener.EventListener;
import org.motechproject.event.listener.EventListenerRegistryService;
import org.motechproject.event.listener.annotations.MotechListenerEventProxy;
import org.motechproject.eventlogging.service.EventLoggingService;
import org.motechproject.eventlogging.service.EventLoggingServiceManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Implementation of {@link org.motechproject.eventlogging.service.EventLoggingServiceManager}.
 * Allows registering logging services.
 */
@Component
public class EventLoggingServiceManagerImpl implements EventLoggingServiceManager {

    private static final Logger LOGGER = LoggerFactory.getLogger(EventLoggingServiceManagerImpl.class);

    private List<EventLoggingService> eventLoggingServices = new ArrayList<>();

    @Autowired
    private EventListenerRegistryService eventListenerRegistryService;

    @Autowired
    private DbEventLoggingService dbEventLoggingService;

    /**
     * Registers default service for event logging, which is DbEventLoggingService
     */
    @PostConstruct
    public void registerDefaultService() {
        registerEventLoggingService(dbEventLoggingService);
    }

    @Override
    public void registerEventLoggingService(EventLoggingService eventLoggingService) {
        registerListeners(eventLoggingService);
        eventLoggingServices.add(eventLoggingService);
    }

    private void registerListeners(EventLoggingService eventLoggingService) {

        Set<String> subjectsToListenOn = eventLoggingService.getLoggedEventSubjects();

        EventListener eventListener = null;

        try {
            Method method = eventLoggingService.getClass().getMethod("logEvent", MotechEvent.class);
            String classFullName = eventLoggingService.getClass().getName();
            eventListener = new MotechListenerEventProxy(classFullName, eventLoggingService, method);
        } catch (NoSuchMethodException e) {
            LOGGER.warn("Unable to write to: add listener for " + eventLoggingService.getClass().getName());
        } catch (SecurityException e) {
            LOGGER.warn("Unable to write to: add listener for " + eventLoggingService.getClass().getName());
        }

        List<String> subjectList = new ArrayList<>(subjectsToListenOn);

        if (eventListener != null) {
            LOGGER.debug("Event Listener not null - attempting to register");
            eventListenerRegistryService.registerListener(eventListener, subjectList);
        }

        LOGGER.info("Event Listener registered");
    }

    @Override
    public void updateEventLoggingService(EventLoggingService eventLoggingService) {
        // TODO Auto-generated method stub
    }
}
