package org.motechproject.dhis2.tasks.builder;

import org.motechproject.dhis2.domain.Program;
import org.motechproject.dhis2.domain.Stage;
import org.motechproject.dhis2.domain.TrackedEntity;
import org.motechproject.dhis2.domain.TrackedEntityAttribute;
import org.motechproject.dhis2.event.EventParams;
import org.motechproject.dhis2.event.EventSubjects;
import org.motechproject.dhis2.rest.domain.ServerVersion;
import org.motechproject.dhis2.rest.service.DhisWebService;
import org.motechproject.dhis2.service.DataSetService;
import org.motechproject.dhis2.service.ProgramService;
import org.motechproject.dhis2.service.StageService;
import org.motechproject.dhis2.service.TrackedEntityAttributeService;
import org.motechproject.dhis2.service.TrackedEntityService;
import org.motechproject.dhis2.tasks.DisplayNames;
import org.motechproject.tasks.contract.ActionEventRequest;
import org.motechproject.tasks.contract.builder.ActionEventRequestBuilder;
import org.motechproject.tasks.contract.ActionParameterRequest;
import org.motechproject.tasks.contract.builder.ActionParameterRequestBuilder;
import org.motechproject.tasks.contract.ChannelRequest;
import org.motechproject.tasks.domain.enums.MethodCallManner;
import org.osgi.framework.BundleContext;
import org.motechproject.tasks.domain.enums.ParameterType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

/**
 * Builds a channel request from the records in MDS pertaining to the DHIS2 instance schema.
 *
 */
@Component
public class ChannelRequestBuilder  {
    public static final String ACTION_PROXY_SERVICE = "org.motechproject.dhis2.tasks.DhisActionProxyService";

    @Autowired
    private BundleContext bundleContext;
    @Autowired
    private ProgramService programService;
    @Autowired
    private StageService stageService;
    @Autowired
    private TrackedEntityAttributeService trackedEntityAttributeService;
    @Autowired
    private TrackedEntityService trackedEntityService;
    @Autowired
    private DhisWebService dhisWebService;
    @Autowired
    private DataSetService dataSetService;

    @Autowired
    private ProgramActionBuilder programActionBuilder;
    @Autowired
    private CreateInstanceActionBuilder createInstanceActionBuilder;
    @Autowired
    private StageActionBuilder stageActionBuilder;
    @Autowired
    private SendDataValueSetActionBuilder sendDataValueSetActionBuilder;

    private ServerVersion serverVersion;

    /**
     * Creates task action event requests for tracked entity instance creation,
     * program enrollment, and program stage events.
     * @return the new Channel Request
     */
    @Transactional
    public ChannelRequest build() {
        serverVersion = dhisWebService.getServerVersion();

        List<ActionEventRequest> actions = new ArrayList<>();

        List<Program> programs = programService.findByRegistration(true);
        actions.addAll(programActionBuilder.build(programs, serverVersion));

        List<Stage> stages = stageService.findAll();
        actions.addAll(stageActionBuilder.build(stages));

        List<TrackedEntityAttribute> attributes = trackedEntityAttributeService.findAll();
        List<TrackedEntity> trackedEntities = trackedEntityService.findAll();
        actions.addAll(createInstanceActionBuilder.build(attributes, trackedEntities));
        actions.addAll(sendDataValueSetActionBuilder.addSendDataValueSetActions(dataSetService.findAll()));
        actions.add(addSendDataValue());

        return new ChannelRequest(DisplayNames.DHIS2_DISPLAY_NAME, bundleContext.getBundle().getSymbolicName(),
                bundleContext.getBundle().getVersion().toString(), null, new ArrayList<>(), actions);

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
                .setType(ParameterType.PERIOD.getValue())
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
                .setType(ParameterType.TEXTAREA.getValue())
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
                .setServiceInterface(ChannelRequestBuilder.ACTION_PROXY_SERVICE)
                .setServiceMethod("sendDataValue")
                .setServiceMethodCallManner(MethodCallManner.MAP.name())
                .setSubject(EventSubjects.SEND_DATA_VALUE)
                .setName(DisplayNames.SEND_DATA_VALUE);

        return eventRequestBuilder.createActionEventRequest();
    }
}
