package org.motechproject.openmrs19.tasks;

import org.motechproject.event.MotechEvent;
import org.motechproject.event.listener.annotations.MotechListener;
import org.motechproject.openmrs19.service.OpenMRSConfigService;
import org.motechproject.openmrs19.tasks.builder.ChannelRequestBuilder;
import org.motechproject.openmrs19.tasks.constants.EventSubjects;
import org.osgi.framework.BundleContext;
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
public class OpenMRSTasksNotifier {

    private static final Logger LOGGER = LoggerFactory.getLogger(OpenMRSTasksNotifier.class);

    private BundleContext bundleContext;
    private OpenMRSConfigService configService;

    @Autowired
    public OpenMRSTasksNotifier(BundleContext bundleContext, OpenMRSConfigService configService) {
        this.bundleContext = bundleContext;
        this.configService = configService;
    }

    @PostConstruct
    public void postConstruct() {
        LOGGER.info("OpenMRSTasksNotifier created, updating tasks channel");
        updateTasksChannel();
    }

    @MotechListener(subjects = EventSubjects.CONFIG_CHANGE_EVENT)
    public void updateTasksChannel(MotechEvent motechEvent) {
        LOGGER.info("OpenMRS update requested through MotechEvent", motechEvent);
        updateTasksChannel();
    }

    @PreDestroy
    public void unregisterChannel() {
        Object service = getChannelService();
        if (service != null) {
            LOGGER.info("Unregistering OpenMRS tasks channel through the channel service");
            getTasksChannelServiceInstance(service).unregisterTaskChannel();
        } else {
            LOGGER.warn("No channel service present, channel not unregistered");
        }
    }

    private void updateTasksChannel() {
        LOGGER.info("Updating tasks integration");
        Object service = getChannelService();
        if (service != null) {
            LOGGER.info("Registering OpenMRS tasks channel through the channel service");
            getTasksChannelServiceInstance(service).updateTaskChannel();
        } else {
            LOGGER.warn("No channel service present, channel not registered");
        }
    }

    private Object getChannelService() {
        ServiceReference serviceReference = bundleContext.getServiceReference("org.motechproject.tasks.service.ChannelService");
        return serviceReference != null ?  bundleContext.getService(serviceReference) : null;
    }

    private TasksChannelServiceInstance getTasksChannelServiceInstance(Object service) {
        ChannelRequestBuilder channelRequestBuilder = new ChannelRequestBuilder(bundleContext, configService);
        return new TasksChannelServiceInstance(service, channelRequestBuilder);
    }
}