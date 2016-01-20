package org.motechproject.odk.service.impl;

import org.motechproject.odk.domain.FormDefinition;
import org.motechproject.odk.service.FormDefinitionService;
import org.motechproject.odk.service.TasksService;
import org.motechproject.odk.tasks.ChannelRequestBuilder;
import org.motechproject.tasks.service.ChannelService;
import org.osgi.framework.BundleContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TasksServiceImpl implements TasksService {

    private ChannelService channelService;
    private BundleContext bundleContext;
    private FormDefinitionService formDefinitionService;

    @Autowired
    public TasksServiceImpl(ChannelService channelService, BundleContext bundleContext, FormDefinitionService formDefinitionService) {
        this.channelService = channelService;
        this.bundleContext = bundleContext;
        this.formDefinitionService = formDefinitionService;
    }

    @Override
    public void updateTasksChannel() {
        List<FormDefinition> formDefinitions = formDefinitionService.findAll();
        ChannelRequestBuilder channelRequestBuilder = new ChannelRequestBuilder(bundleContext, formDefinitions);
        channelService.registerChannel(channelRequestBuilder.build());
    }
}
