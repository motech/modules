package org.motechproject.ipf.service.impl;

import org.motechproject.ipf.domain.IPFRecipient;
import org.motechproject.ipf.domain.IPFTemplate;
import org.motechproject.ipf.service.IPFRecipientsService;
import org.motechproject.ipf.service.IPFTemplateDataService;
import org.motechproject.ipf.service.IPFTaskService;
import org.motechproject.ipf.task.IpfChannelRequestBuilder;
import org.motechproject.tasks.contract.ChannelRequest;
import org.motechproject.tasks.service.ChannelService;
import org.osgi.framework.BundleContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.Collection;
import java.util.List;

/**
 * Implementation of {@link org.motechproject.ipf.service.IPFTaskService}.
 */
@Service("ipfTaskService")
public class IPFTaskServiceImpl implements IPFTaskService {

    private static final Logger LOGGER = LoggerFactory.getLogger(IPFTaskServiceImpl.class);

    @Autowired
    private ChannelService channelService;

    @Autowired
    private BundleContext bundleContext;

    @Autowired
    private IPFRecipientsService ipfRecipientsService;

    @Autowired
    private IPFTemplateDataService ipfTemplateService;

    @PostConstruct
    public void init() {
        updateChannel();
    }

    @Override
    public void templateChanged(IPFTemplate ipfTemplate) {
        LOGGER.debug("Template with {} name was created/updated", ipfTemplate.getTemplateName());
        updateChannel();
    }

    @Override
    public void templateRemoved(IPFTemplate ipfTemplate) {
        updateChannel();
    }

    @Override
    public void preTemplateRemoved(IPFTemplate ipfTemplate) {
        LOGGER.debug("Template with {} name was removed", ipfTemplate.getTemplateName());
    }

    private void updateChannel() {
        Collection<IPFRecipient> ipfRecipients = ipfRecipientsService.getAllRecipients();
        List<IPFTemplate> ipfTemplates = ipfTemplateService.retrieveAll();

        IpfChannelRequestBuilder ipfChannelRequestBuilder = new IpfChannelRequestBuilder(bundleContext, ipfTemplates, ipfRecipients);

        LOGGER.debug("Updating IPF task channel...");
        ChannelRequest channelRequest = ipfChannelRequestBuilder.build();
        if (channelRequest.getActionTaskEvents().size() > 0) {
            channelService.registerChannel(channelRequest);
        } else {
            LOGGER.debug("No IPF Task Action, cannot register IPF Task Channel");
            // We must unregister channel if it exist
            channelService.unregisterChannel(bundleContext.getBundle().getSymbolicName());
        }
    }
}
