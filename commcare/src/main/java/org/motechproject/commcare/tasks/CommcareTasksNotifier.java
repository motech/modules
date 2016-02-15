package org.motechproject.commcare.tasks;

import org.motechproject.commcare.CommcareDataProvider;
import org.motechproject.commcare.service.CommcareApplicationService;
import org.motechproject.commcare.service.CommcareConfigService;
import org.motechproject.commcare.service.CommcareSchemaService;
import org.motechproject.commcare.tasks.builder.ChannelRequestBuilder;
import org.motechproject.tasks.ex.ValidationException;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * The <code>CommcareTasksNotifier</code> class is responsible for communication
 * with the tasks module. It sends {@link org.motechproject.tasks.contract.ChannelRequest}s
 * in order to create or update tasks channel for the Commcare module. If the tasks module
 * is not present, no updates will be sent.
 */
@Component("commcareTasksNotifier")
public class CommcareTasksNotifier {

    private static final Logger LOGGER = LoggerFactory.getLogger(CommcareTasksNotifier.class);

    private BundleContext bundleContext;
    private CommcareSchemaService schemaService;
    private CommcareApplicationService applicationService;
    private CommcareDataProvider dataProvider;
    private CommcareConfigService configService;

    @Autowired
    public CommcareTasksNotifier(BundleContext bundleContext, CommcareDataProvider dataProvider,
                                 CommcareConfigService configService, CommcareApplicationService applicationService,
                                 CommcareSchemaService schemaService) {
        this.bundleContext = bundleContext;
        this.dataProvider = dataProvider;
        this.configService = configService;
        this.applicationService = applicationService;
        this.schemaService = schemaService;
    }

    @PostConstruct
    public void updateTasksInfo() {
        LOGGER.info("Updating tasks integration");

        try {
            updateChannel();
        } catch (ValidationException e) {
            LOGGER.error("Channel generated was not accepted by tasks due to validation errors", e);
        }

        try {
            dataProvider.updateDataProvider();
        } catch (ValidationException e) {
            LOGGER.error("Data Provider generated was not accepted by tasks due to validation errors", e);
        }
    }

    private void updateChannel() {
        ServiceReference serviceReference = bundleContext.getServiceReference("org.motechproject.tasks.service.ChannelService");
        if (serviceReference != null) {
            Object service = bundleContext.getService(serviceReference);
            if (service != null) {
                LOGGER.info("Registering Commcare tasks channel with the channel service");

                ChannelRequestBuilder channelRequestBuilder = new ChannelRequestBuilder(configService, applicationService,
                        schemaService, bundleContext);
                TasksChannelServiceInstance instance = new TasksChannelServiceInstance(service, channelRequestBuilder);
                instance.updateTaskChannel();
            } else {
                LOGGER.warn("No channel service present, channel not registered");
            }
        }
    }
}
