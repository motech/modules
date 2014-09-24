package org.motechproject.commcare.tasks;

import org.motechproject.commcare.service.CommcareApplicationDataService;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
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

    private BundleContext bundleContext;
    private CommcareApplicationDataService commcareApplicationDataService;

    @Autowired
    public CommcareTasksNotifier(BundleContext bundleContext, CommcareApplicationDataService commcareApplicationDataService) {
        this.bundleContext = bundleContext;
        this.commcareApplicationDataService = commcareApplicationDataService;
    }

    @PostConstruct
    public void updateTaskChannel() {
        ServiceReference serviceReference = bundleContext.getServiceReference("org.motechproject.tasks.service.ChannelService");
        if (serviceReference != null) {
            Object service = bundleContext.getService(serviceReference);
            if (service != null) {
                ChannelRequestBuilder channelRequestBuilder = new ChannelRequestBuilder(commcareApplicationDataService, bundleContext);
                TasksChannelServiceInstance instance = new TasksChannelServiceInstance(service, channelRequestBuilder);
                instance.updateTaskChannel();
            }
        }
    }
}
