package org.motechproject.dhis2.tasks;

import org.motechproject.dhis2.domain.Program;
import org.motechproject.dhis2.domain.Stage;
import org.motechproject.dhis2.domain.TrackedEntity;
import org.motechproject.dhis2.domain.TrackedEntityAttribute;
import org.motechproject.dhis2.event.EventParams;
import org.motechproject.dhis2.event.EventSubjects;
import org.motechproject.dhis2.service.ProgramService;
import org.motechproject.dhis2.service.StageService;
import org.motechproject.dhis2.service.TrackedEntityAttributeService;
import org.motechproject.dhis2.service.TrackedEntityService;
import org.motechproject.tasks.contract.ActionEventRequest;
import org.motechproject.tasks.contract.ActionEventRequestBuilder;
import org.motechproject.tasks.contract.ActionParameterRequest;
import org.motechproject.tasks.contract.ActionParameterRequestBuilder;
import org.motechproject.tasks.contract.ChannelRequest;
import org.motechproject.tasks.contract.TriggerEventRequest;
import org.osgi.framework.BundleContext;

import java.util.ArrayList;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

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

        actions.add(addSendDataValueSet());
        actions.add(addSendDataValue());

        return new ChannelRequest(DisplayNames.DHIS2_DISPLAY_NAME, bundleContext.getBundle().getSymbolicName(),
                bundleContext.getBundle().getVersion().toString(), null, new ArrayList<TriggerEventRequest>(), actions);

    }



    private ActionEventRequest addSendDataValueSet() {
        int order = 0;
        ActionParameterRequestBuilder builder = new ActionParameterRequestBuilder();
        SortedSet<ActionParameterRequest> actionParameterRequests = new TreeSet<>();

        builder.setDisplayName(DisplayNames.DATA_SET)
                .setKey(EventParams.DATA_SET)
                .setOrder(order++);
        actionParameterRequests.add(builder.createActionParameterRequest());

        builder = new ActionParameterRequestBuilder();
        builder.setDisplayName(DisplayNames.COMPLETE_DATE)
                .setKey(EventParams.COMPLETE_DATE)
                .setOrder(order++);
        actionParameterRequests.add(builder.createActionParameterRequest());

        builder = new ActionParameterRequestBuilder();
        builder.setDisplayName(DisplayNames.PERIOD)
                .setKey(EventParams.PERIOD)
                .setOrder(order++)
                .setRequired(true);
        actionParameterRequests.add(builder.createActionParameterRequest());

        builder = new ActionParameterRequestBuilder();
        builder.setDisplayName(DisplayNames.ORG_UNIT)
                .setKey(EventParams.LOCATION)
                .setOrder(order++)
                .setRequired(true);
        actionParameterRequests.add(builder.createActionParameterRequest());

        builder = new ActionParameterRequestBuilder();
        builder.setDisplayName(DisplayNames.CATEGORY_OPTION_COMBO)
                .setKey(EventParams.CATEGORY_OPTION_COMBO)
                .setOrder(order++);
        actionParameterRequests.add(builder.createActionParameterRequest());

        builder = new ActionParameterRequestBuilder();
        builder.setDisplayName(DisplayNames.COMMENT)
                .setKey(EventParams.COMMENT)
                .setOrder(order++);
        actionParameterRequests.add(builder.createActionParameterRequest());

        builder = new ActionParameterRequestBuilder();
        builder.setDisplayName(DisplayNames.ATTRIBUTE_OPTION_COMBO)
                .setKey(EventParams.ATTRIBUTE_OPTION_COMBO)
                .setOrder(order++);
        actionParameterRequests.add(builder.createActionParameterRequest());

        builder = new ActionParameterRequestBuilder();
        builder.setDisplayName(DisplayNames.DATA_VALUES)
                .setKey(EventParams.DATA_VALUES)
                .setOrder(order++)
                .setType("MAP")
                .setRequired(true);

        actionParameterRequests.add(builder.createActionParameterRequest());

        ActionEventRequestBuilder eventRequestBuilder = new ActionEventRequestBuilder();
        eventRequestBuilder.setActionParameters(actionParameterRequests)
                .setDisplayName(DisplayNames.SEND_DATA_VALUE_SET)
                .setSubject(EventSubjects.SEND_DATA_VALUE_SET)
                .setName(DisplayNames.SEND_DATA_VALUE_SET);

        return eventRequestBuilder.createActionEventRequest();

    }

    private ActionEventRequest addSendDataValue() {
        int order = 0;
        SortedSet<ActionParameterRequest> actionParameterRequests = new TreeSet<>();
        ActionParameterRequestBuilder builder = new ActionParameterRequestBuilder();

        builder.setDisplayName(DisplayNames.DATA_ELEMENT)
                .setKey(EventParams.DATA_ELEMENT)
                .setRequired(true)
                .setOrder(order++);
        actionParameterRequests.add(builder.createActionParameterRequest());

        builder = new ActionParameterRequestBuilder();
        builder.setDisplayName(DisplayNames.PERIOD)
                .setKey(EventParams.PERIOD)
                .setRequired(true)
                .setOrder(order++);
        actionParameterRequests.add(builder.createActionParameterRequest());

        builder = new ActionParameterRequestBuilder();
        builder.setDisplayName(DisplayNames.ORG_UNIT)
                .setKey(EventParams.LOCATION)
                .setRequired(true)
                .setOrder(order++);
        actionParameterRequests.add(builder.createActionParameterRequest());

        builder = new ActionParameterRequestBuilder();
        builder.setDisplayName(DisplayNames.VALUE)
                .setKey(EventParams.VALUE)
                .setRequired(true)
                .setOrder(order++);
        actionParameterRequests.add(builder.createActionParameterRequest());


        builder = new ActionParameterRequestBuilder();
        builder.setDisplayName(DisplayNames.CATEGORY_OPTION_COMBO)
                .setKey(EventParams.CATEGORY_OPTION_COMBO)
                .setOrder(order++);
        actionParameterRequests.add(builder.createActionParameterRequest());

        builder = new ActionParameterRequestBuilder();
        builder.setDisplayName(DisplayNames.COMMENT)
                .setKey(EventParams.COMMENT)
                .setOrder(order++);
        actionParameterRequests.add(builder.createActionParameterRequest());

        ActionEventRequestBuilder eventRequestBuilder = new ActionEventRequestBuilder();
        eventRequestBuilder.setActionParameters(actionParameterRequests)
                .setDisplayName(DisplayNames.SEND_DATA_VALUE)
                .setSubject(EventSubjects.SEND_DATA_VALUE)
                .setName(DisplayNames.SEND_DATA_VALUE);

        return eventRequestBuilder.createActionEventRequest();
    }
}
