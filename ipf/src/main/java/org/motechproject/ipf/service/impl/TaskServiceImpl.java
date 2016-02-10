package org.motechproject.ipf.service.impl;

import org.motechproject.ipf.domain.IPFRecipient;
import org.motechproject.ipf.service.IPFRecipientsService;
import org.motechproject.ipf.service.IPFTemplateService;
import org.motechproject.ipf.service.TaskService;
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

@Service
public class TaskServiceImpl implements TaskService {

    private static final Logger LOGGER = LoggerFactory.getLogger(TaskServiceImpl.class);

    @Autowired
    private ChannelService channelService;

    @Autowired
    private BundleContext bundleContext;

    @Autowired
    private IPFRecipientsService ipfRecipientsService;

    @Autowired
    private IPFTemplateService ipfTemplateService;

    @PostConstruct
    public void init() {
        updateChannel();
    }

    @Override
    public void updateChannel() {
        Collection<IPFRecipient> ipfRecipients = ipfRecipientsService.getAllRecipients();
        List<String> templateNames = ipfTemplateService.getAllTemplateNames();

        IpfChannelRequestBuilder ipfChannelRequestBuilder = new IpfChannelRequestBuilder(bundleContext, templateNames, ipfRecipients);

        LOGGER.debug("Updating IPF task channel...");
        ChannelRequest channelRequest = ipfChannelRequestBuilder.build();
        if (channelRequest.getActionTaskEvents().size() > 0) {
            channelService.registerChannel(channelRequest);
        } else {
            LOGGER.debug("No IPF Task Action, cannot register IPF Task Channel");
        }
    }
}
