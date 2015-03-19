package org.motechproject.dhis2.tasks;

import org.motechproject.dhis2.domain.Program;
import org.motechproject.dhis2.domain.Stage;
import org.motechproject.dhis2.domain.TrackedEntity;
import org.motechproject.dhis2.domain.TrackedEntityAttribute;
import org.motechproject.dhis2.service.ProgramService;
import org.motechproject.dhis2.service.StageService;
import org.motechproject.dhis2.service.TrackedEntityAttributeService;
import org.motechproject.dhis2.service.TrackedEntityService;
import org.motechproject.tasks.contract.ActionEventRequest;
import org.motechproject.tasks.contract.ChannelRequest;
import org.motechproject.tasks.contract.TriggerEventRequest;
import org.osgi.framework.BundleContext;

import java.util.ArrayList;
import java.util.List;

/**
 * Builds a channel request from the records in MDS pertaining to the DHIS2 instance schema.
 *
 */

public class ChannelRequestBuilder  {

    private BundleContext bundleContext;
    private ProgramService programService;
    private StageService stageService;
    private TrackedEntityAttributeService trackedEntityAttributeService;
    private TrackedEntityService trackedEntityService;

    public ChannelRequestBuilder(BundleContext bundleContext,
                                 ProgramService programService,
                                 StageService stageService,
                                 TrackedEntityAttributeService trackedEntityAttributeService,
                                 TrackedEntityService trackedEntityService) {
        this.bundleContext = bundleContext;
        this.programService = programService;
        this.stageService = stageService;
        this.trackedEntityAttributeService = trackedEntityAttributeService;
        this.trackedEntityService = trackedEntityService;
    }

    /**
     * Creates task action event requests for tracked entity instance creation,
     * program enrollment, and program stage events.
     * @return the new Channel Request
     */
    public ChannelRequest build() {

        ProgramActionBuilder programActionBuilder = new ProgramActionBuilder();
        CreateInstanceActionBuilder createInstanceActionBuilder = new CreateInstanceActionBuilder();
        StageActionBuilder stageActionBuilder = new StageActionBuilder();

        List<ActionEventRequest> actions = new ArrayList<>();

        List<Program> programs = programService.findByRegistration(true);
        actions.addAll(programActionBuilder.build(programs));

        List<Stage> stages = stageService.findAll();
        actions.addAll(stageActionBuilder.build(stages));

        List<TrackedEntityAttribute> attributes = trackedEntityAttributeService.findAll();
        List<TrackedEntity> trackedEntities = trackedEntityService.findAll();
        actions.addAll(createInstanceActionBuilder.build(attributes, trackedEntities));

        return new ChannelRequest(DisplayNames.DHIS2_DISPLAY_NAME, bundleContext.getBundle().getSymbolicName(),
                bundleContext.getBundle().getVersion().toString(), null, new ArrayList<TriggerEventRequest>(), actions);

    }
}
