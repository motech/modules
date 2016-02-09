package org.motechproject.ipf.service.impl;

import org.motechproject.ipf.service.TaskService;
import org.motechproject.ipf.task.IpfChannelRequestBuilder;
import org.motechproject.tasks.service.ChannelService;
import org.osgi.framework.BundleContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

import static java.util.Arrays.asList;

@Service
public class TaskServiceImpl implements TaskService {

    private static final Logger LOGGER = LoggerFactory.getLogger(TaskServiceImpl.class);

    @Autowired
    private ChannelService channelService;

    @Autowired
    private BundleContext bundleContext;

    @PostConstruct
    public void init() {
        updateChannel();
    }

    @Override
    public void updateChannel() {
        LOGGER.debug("Loading IPF templates...");
        // TODO: load templates
        LOGGER.debug("Updating IPF task channel...");
        IpfChannelRequestBuilder ipfChannelRequestBuilder = new IpfChannelRequestBuilder(bundleContext, asList("IPF_1", "IPF_2", "IPF_3"));
        channelService.registerChannel(ipfChannelRequestBuilder.build());
    }
}
