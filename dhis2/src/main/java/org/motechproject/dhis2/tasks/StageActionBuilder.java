package org.motechproject.dhis2.tasks;

import org.motechproject.dhis2.domain.DataElement;
import org.motechproject.dhis2.domain.Stage;
import org.motechproject.dhis2.event.EventParams;
import org.motechproject.dhis2.event.EventSubjects;
import org.motechproject.tasks.contract.ActionEventRequest;
import org.motechproject.tasks.contract.ActionParameterRequest;
import org.motechproject.tasks.contract.builder.ActionEventRequestBuilder;
import org.motechproject.tasks.contract.builder.ActionParameterRequestBuilder;
import org.motechproject.tasks.domain.enums.ParameterType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;


/**
 * Builds task action requests for program stage events for each program stage.
 */
public class StageActionBuilder {

    private int counter;
    private static final SortedSet<String> EVENT_STATUSES = new TreeSet<>();

    static {
        EVENT_STATUSES.addAll(Arrays.asList("ACTIVE", "COMPLETED", "VISITED", "SCHEDULE", "OVERDUE", "SKIPPED"));
    }

    /**
     * Builds a list of action event request from a list of program stages.
     * @param stages
     * @return a list of ActionEventRequests
     */
    public List<ActionEventRequest> build(List<Stage> stages) {

        List<ActionEventRequest> actionEventRequests = new ArrayList<>();

        for (Stage stage : stages) {
            counter = 0;
            SortedSet<ActionParameterRequest> actionParameters = new TreeSet<>();
            ActionEventRequestBuilder builder = new ActionEventRequestBuilder();

            ActionParameterRequestBuilder actionParameterBuilder;
            String registration;

            if (stage.hasRegistration()) {
                registration = "true";
                actionParameterBuilder = new ActionParameterRequestBuilder()
                        .setDisplayName(DisplayNames.EXTERNAL_ID)
                        .setKey(EventParams.EXTERNAL_ID)
                        .setType(ParameterType.UNICODE.getValue())
                        .setRequired(true)
                        .setOrder(counter++);

                actionParameters.add(actionParameterBuilder.createActionParameterRequest());

            } else {
                registration = "false";
            }

             /*Program details*/
            actionParameterBuilder = new ActionParameterRequestBuilder()
                    .setKey(EventParams.REGISTRATION)
                    .setValue(registration)
                    .setHidden(true)
                    .setOrder(counter++)
                    .setDisplayName(EventParams.REGISTRATION);

            actionParameters.add(actionParameterBuilder.createActionParameterRequest());



            /*Program details*/
            actionParameterBuilder = new ActionParameterRequestBuilder()
                    .setKey(EventParams.PROGRAM)
                    .setValue(stage.getProgram())
                    .setHidden(true)
                    .setOrder(counter++)
                    .setDisplayName(stage.getProgram());

            actionParameters.add(actionParameterBuilder.createActionParameterRequest());

            actionParameterBuilder = new ActionParameterRequestBuilder()
                    .setKey(EventParams.STAGE)
                    .setValue(stage.getUuid())
                    .setHidden(true)
                    .setOrder(counter++)
                    .setDisplayName(stage.getProgram());

            actionParameters.add(actionParameterBuilder.createActionParameterRequest());


            /*Date*/
            actionParameterBuilder = new ActionParameterRequestBuilder()
                    .setDisplayName(DisplayNames.EVENT_DATE)
                    .setOrder(counter++)
                    .setKey(EventParams.DATE)
                    .setType(ParameterType.UNICODE.getValue())
                    .setRequired(true);

            actionParameters.add(actionParameterBuilder.createActionParameterRequest());

            actionParameterBuilder = new ActionParameterRequestBuilder()
                    .setDisplayName(DisplayNames.ORG_UNIT)
                    .setType(ParameterType.UNICODE.getValue())
                    .setKey(EventParams.LOCATION)
                    .setOrder(counter++)
                    .setRequired(true);

            actionParameters.add(actionParameterBuilder.createActionParameterRequest());

            actionParameterBuilder = new ActionParameterRequestBuilder()
                    .setDisplayName(DisplayNames.STATUS)
                    .setType(ParameterType.SELECT.getValue())
                    .setOptions(EVENT_STATUSES)
                    .setKey(EventParams.STATUS)
                    .setOrder(counter++)
                    .setRequired(false);

            actionParameters.add(actionParameterBuilder.createActionParameterRequest());


            actionParameters.addAll(buildRequestForStage(stage));

            builder.setActionParameters(actionParameters)
                    .setDisplayName(DisplayNames.STAGE_EVENT + " [" + stage.getName() + "]")
                    .setName(stage.getName())
                    .setSubject(EventSubjects.UPDATE_PROGRAM_STAGE);

            actionEventRequests.add(builder.createActionEventRequest());


        }


        return actionEventRequests;
    }

    /*adds ActionParameterRequests for each Data Element in the program stage*/
    private List<ActionParameterRequest> buildRequestForStage(Stage stage) {

        List<ActionParameterRequest> parameterRequests = new ArrayList<>();


        for (DataElement element : stage.getDataElements()) {
            ActionParameterRequestBuilder actionParameterBuilder = new ActionParameterRequestBuilder()
                    .setDisplayName(element.getName())
                    .setKey(element.getUuid())
                    .setType(ParameterType.UNICODE.getValue())
                    .setOrder(counter++);

            parameterRequests.add(actionParameterBuilder.createActionParameterRequest());

        }


        return parameterRequests;
    }
}
