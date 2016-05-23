package org.motechproject.openmrs19.tasks;

import org.motechproject.event.MotechEvent;
import org.motechproject.event.listener.annotations.MotechListener;
import org.motechproject.openmrs19.exception.OpenMRSException;
import org.motechproject.openmrs19.service.OpenMRSConfigService;
import org.motechproject.openmrs19.tasks.builder.ChannelRequestBuilder;
import org.motechproject.openmrs19.tasks.constants.EventSubjects;
import org.osgi.framework.BundleContext;
import org.osgi.framework.InvalidSyntaxException;
import org.osgi.framework.ServiceEvent;
import org.osgi.framework.ServiceListener;
import org.osgi.framework.ServiceReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

/**
 * Responsible for communication with the Tasks module. It sends
 * {@link org.motechproject.tasks.contract.ChannelRequest}s in order to create or update tasks channel for the OpenMRS
 * module. If the tasks module is not present, no updates will be sent.
 */
@Component("openMrsTasksNotifier")
public class OpenMRSTasksNotifier implements ServiceListener {

    private static final Logger LOGGER = LoggerFactory.getLogger(OpenMRSTasksNotifier.class);

    private static final String FILTER = "(&(objectClass=org.motechproject.tasks.service.ChannelService))";

    private BundleContext bundleContext;
    private OpenMRSConfigService configService;
    private TasksChannelServiceInstance tasksChannelServiceInstance;

    @Autowired
    public OpenMRSTasksNotifier(BundleContext bundleContext, OpenMRSConfigService configService) {
        this.bundleContext = bundleContext;
        this.configService = configService;
    }

    @PostConstruct
    public void postConstruct() {
        try {
            Object service = getChannelService();
            if (service != null && tasksChannelServiceInstance == null) {
                LOGGER.info("Tasks module available, registering channel");
                createTasksChannelServiceInstance(service);
                updateTasksChannel();
            }
            bundleContext.addServiceListener(this, FILTER);
        } catch (InvalidSyntaxException e) {
            throw new OpenMRSException("Invalid filter has been passed as an parameter", e);
        }
    }

    @MotechListener(subjects = EventSubjects.CONFIG_CHANGE_EVENT)
    public void updateTasksChannel(MotechEvent motechEvent) {
        LOGGER.info("Channel update request received, updating Tasks channel");
        updateTasksChannel();
    }

    @Override
    public void serviceChanged(ServiceEvent event) {
        if (event.getType() == ServiceEvent.REGISTERED) {
            LOGGER.info("Tasks module became available, registering channel");
            createTasksChannelServiceInstance(getChannelService(event.getServiceReference()));
            updateTasksChannel();
        } else if (event.getType() == ServiceEvent.UNREGISTERING) {
            tasksChannelServiceInstance = null;
        }
    }

    @PreDestroy
    public void preDestory() {
        LOGGER.info("Module is being disabled, unregistering Tasks channel");
        unregisterChannel();
    }

    private void updateTasksChannel() {
        if (tasksChannelServiceInstance != null) {
            tasksChannelServiceInstance.updateTaskChannel();
        }
    }

    private void unregisterChannel() {
        if (tasksChannelServiceInstance != null) {
            tasksChannelServiceInstance.unregisterTaskChannel();
        }
    }

    private Object getChannelService() {
        ServiceReference reference = bundleContext.getServiceReference("org.motechproject.tasks.service.ChannelService");
        return getChannelService(reference);
    }

    private Object getChannelService(ServiceReference reference) {
        return reference != null ?  bundleContext.getService(reference) : null;
    }

    private void createTasksChannelServiceInstance(Object service) {
        tasksChannelServiceInstance = new TasksChannelServiceInstance(
                service,
                new ChannelRequestBuilder(bundleContext, configService)
        );
    }
}