package org.motechproject.dhis2.service.impl;

import org.motechproject.dhis2.rest.service.DhisWebService;
import org.motechproject.dhis2.service.DataSetService;
import org.motechproject.dhis2.service.ProgramService;
import org.motechproject.dhis2.service.StageService;
import org.motechproject.dhis2.service.TasksService;
import org.motechproject.dhis2.service.TrackedEntityAttributeService;
import org.motechproject.dhis2.service.TrackedEntityService;
import org.motechproject.dhis2.tasks.ChannelRequestBuilder;
import org.motechproject.tasks.service.ChannelService;
import org.osgi.framework.BundleContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


/**
 * Implementation of {@link org.motechproject.dhis2.service.TasksService}
 */
@Service
public class TasksServiceImpl implements TasksService {
    private static final Logger LOGGER = LoggerFactory.getLogger(TasksServiceImpl.class);

    private BundleContext bundleContext;
    private ProgramService programService;
    private StageService stageService;
    private TrackedEntityService trackedEntityService;
    private TrackedEntityAttributeService trackedEntityAttributeService;
    private ChannelService channelService;
    private DhisWebService dhisWebService;
    private DataSetService dataSetService;

    @Autowired
    public TasksServiceImpl(BundleContext bundleContext,
                            ProgramService programService,
                            StageService stageService,
                            TrackedEntityService trackedEntityService,
                            TrackedEntityAttributeService trackedEntityAttributeService,
                            ChannelService channelService,
                            DhisWebService dhisWebService,
                            DataSetService dataSetService) {
        this.bundleContext = bundleContext;
        this.programService = programService;
        this.stageService = stageService;
        this.trackedEntityService = trackedEntityService;
        this.trackedEntityAttributeService = trackedEntityAttributeService;
        this.channelService = channelService;
        this.dhisWebService = dhisWebService;
        this.dataSetService = dataSetService;
    }

    @Override
    public void updateChannel() {
        LOGGER.debug("Updating DHIS2 task channel...");
        ChannelRequestBuilder channelRequestBuilder = new ChannelRequestBuilder(bundleContext, programService,
                stageService, trackedEntityAttributeService, trackedEntityService, dhisWebService.getServerVersion(),
                dataSetService);
        channelService.registerChannel(channelRequestBuilder.build());
    }
}
