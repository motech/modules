package org.motechproject.sms.tasks;



import org.motechproject.sms.configs.Config;
import org.motechproject.sms.service.ConfigService;
import org.motechproject.sms.tasks.builder.ChannelRequestBuilder;
import org.motechproject.tasks.ex.ValidationException;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.SortedSet;
import java.util.TreeSet;

/**
 * The <code>SmsTasksNotifier</code> class is responsible for communication
 * with the tasks module. It sends {@link org.motechproject.tasks.contract.ChannelRequest}s
 * in order to update tasks channel for the sms module. If the tasks module
 * is not present, no updates will be sent.
 */
@Component("smsTaskNotifier")
public class SmsTasksNotifier {

    private static final Logger LOGGER = LoggerFactory.getLogger(SmsTasksNotifier.class);

    private BundleContext bundleContext;
    private ConfigService configService;

    @Autowired
    public SmsTasksNotifier(BundleContext bundleContext, ConfigService configService) {
        this.bundleContext = bundleContext;
        this.configService = configService;
    }

    @PostConstruct
    public void updateTasksInfo() {
        LOGGER.info("Updating tasks integration");
        SortedSet<String> configOptions = new TreeSet<String>();
        for (Config config : configService.getConfigList()) {
            configOptions.add(config.getName());
        }
        try {
            updateChannel(configOptions);
        } catch (ValidationException e) {
            LOGGER.error("Channel generated was not accepted by tasks due to validation errors", e);
        }
    }

    private void  updateChannel(SortedSet<String> configOptions) {
        ServiceReference serviceReference = bundleContext.getServiceReference("org.motechproject.tasks.service.ChannelService");
        if (serviceReference != null) {
            Object service = bundleContext.getService(serviceReference);
            if (service != null) {
                LOGGER.info("Registering Sms tasks channel with the channel service");
                ChannelRequestBuilder channelRequestBuilder = new ChannelRequestBuilder(bundleContext, configOptions);
                TasksChannelServiceInstance tasksChannelServiceInstance = new TasksChannelServiceInstance(service, channelRequestBuilder);
                tasksChannelServiceInstance.updateTaskChannel();
            } else {
                LOGGER.warn("No channel service present, channel not registered");
            }
        }
    }
}
