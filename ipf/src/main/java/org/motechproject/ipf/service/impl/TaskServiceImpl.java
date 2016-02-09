package org.motechproject.ipf.service.impl;

import org.motechproject.ipf.domain.IPFRecipient;
import org.motechproject.ipf.service.IPFRecipientsService;
import org.motechproject.ipf.service.TaskService;
import org.motechproject.ipf.task.IpfChannelRequestBuilder;
import org.motechproject.tasks.service.ChannelService;
import org.osgi.framework.BundleContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.Collection;

import static java.util.Arrays.asList;

@Service
public class TaskServiceImpl implements TaskService {

    private static final Logger LOGGER = LoggerFactory.getLogger(TaskServiceImpl.class);

    @Autowired
    private ChannelService channelService;

    @Autowired
    private BundleContext bundleContext;

    @Autowired
    private IPFRecipientsService ipfRecipientsService;

    @PostConstruct
    public void init() {
        updateChannel();
    }

    @Override
    public void updateChannel() {
        LOGGER.debug("Loading IPF templates...");
        // TODO: load templates
        LOGGER.debug("Loading IPF recipients");
        Collection<IPFRecipient> ipfRecipients = ipfRecipientsService.getAllRecipients();
        IpfChannelRequestBuilder ipfChannelRequestBuilder = new IpfChannelRequestBuilder(bundleContext, asList("IPF_1", "IPF_2", "IPF_3"), ipfRecipients);
        LOGGER.debug("Updating IPF task channel...");
        channelService.registerChannel(ipfChannelRequestBuilder.build());
    }
}
