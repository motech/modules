package org.motechproject.dhis2.tasks.builder;

import org.motechproject.dhis2.domain.Program;
import org.motechproject.dhis2.domain.TrackedEntityAttribute;
import org.motechproject.dhis2.event.EventParams;
import org.motechproject.dhis2.event.EventSubjects;
import org.motechproject.dhis2.rest.domain.ServerVersion;
import org.motechproject.dhis2.tasks.DisplayNames;
import org.motechproject.tasks.contract.ActionEventRequest;
import org.motechproject.tasks.contract.builder.ActionEventRequestBuilder;
import org.motechproject.tasks.contract.ActionParameterRequest;
import org.motechproject.tasks.contract.builder.ActionParameterRequestBuilder;
import org.motechproject.tasks.domain.enums.MethodCallManner;
import org.motechproject.tasks.domain.enums.ParameterType;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;



/**
 * Builds task action requests for enrolling tracked entity instances in a program for each entity type in the DHIS2
 * schema. Also builds create + enroll combination task action requests for each
 * program and the corresponding tracked entity type.
 */
@Component
public class ProgramActionBuilder {

    private int counter;

    /**
     * Builds a list of action event requests for program enrollment and create + enroll task actions.
     * @param programs
     * @return A list of action event rests pertaining to program enrollment.
     */
    @Transactional
    public List<ActionEventRequest> build(List<Program> programs, ServerVersion version) {

        List<ActionEventRequest> actionEventRequests = new ArrayList<>();

        for (Program program : programs) {
            counter = 0;

            SortedSet<ActionParameterRequest> actionParameters = new TreeSet<>();
            SortedSet<ActionParameterRequest> actionParamsForCreateAndEnroll = new TreeSet<>();

            ActionEventRequestBuilder builder = new ActionEventRequestBuilder();

            ActionParameterRequestBuilder actionParameterBuilder = new ActionParameterRequestBuilder()
                    .setDisplayName(DisplayNames.EXTERNAL_ID)
                    .setKey(EventParams.EXTERNAL_ID)
                    .setType(ParameterType.UNICODE.getValue())
                    .setRequired(true)
                    .setOrder(counter++);

            actionParameters.add(actionParameterBuilder.createActionParameterRequest());
            actionParamsForCreateAndEnroll.add(actionParameterBuilder.createActionParameterRequest());


            actionParameterBuilder = new ActionParameterRequestBuilder()
                    .setDisplayName(DisplayNames.ENROLLMENT_DATE)
                    .setKey(EventParams.DATE)
                    .setOrder(counter++)
                    .setType(ParameterType.DATE.getValue());

            actionParameters.add(actionParameterBuilder.createActionParameterRequest());
            actionParamsForCreateAndEnroll.add(actionParameterBuilder.createActionParameterRequest());

            actionParameterBuilder = new ActionParameterRequestBuilder()
                    .setDisplayName(DisplayNames.PROGRAM_NAME)
                    .setKey(EventParams.PROGRAM)
                    .setValue(program.getUuid())
                    .setType(ParameterType.UNICODE.getValue())
                    .setOrder(counter++)
                    .setHidden(true);

            actionParameters.add(actionParameterBuilder.createActionParameterRequest());
            actionParamsForCreateAndEnroll.add(actionParameterBuilder.createActionParameterRequest());

            actionParameterBuilder = new ActionParameterRequestBuilder()
                    .setDisplayName(DisplayNames.ORG_UNIT)
                    .setType(ParameterType.TEXTAREA.getValue())
                    .setKey(EventParams.LOCATION)
                    .setOrder(counter++)
                    .setRequired(true);

            actionParamsForCreateAndEnroll.add(actionParameterBuilder.createActionParameterRequest());
            if (version.isSameOrAfter(ServerVersion.V2_19)) {
                // orgUnit became necessary since DHIS 2.19, so we add it to the enroll action if the criteria is met
                actionParameters.add(actionParameterBuilder.createActionParameterRequest());
            }

            /*Adds all tracked entity attributes for program*/
            actionParameters.addAll(buildRequestForProgram(program));
            actionParamsForCreateAndEnroll.addAll(buildRequestForProgram(program));

            builder.setActionParameters(actionParameters)
                    .setDisplayName(DisplayNames.PROGRAM_ENROLLMENT + " [" + program.getName() + "]")
                    .setServiceInterface(ChannelRequestBuilder.ACTION_PROXY_SERVICE)
                    .setServiceMethod("enrollInProgram")
                    .setServiceMethodCallManner(MethodCallManner.MAP.name())
                    .setSubject(EventSubjects.ENROLL_IN_PROGRAM)
                    .setName(program.getName());


            actionEventRequests.add(builder.createActionEventRequest());


            /*Add corresponding create and enroll action*/
            actionParameterBuilder = new ActionParameterRequestBuilder()
                    .setDisplayName(program.getTrackedEntity().getName())
                    .setType(ParameterType.TEXTAREA.getValue())
                    .setHidden(true)
                    .setKey(EventParams.ENTITY_TYPE)
                    .setValue(program.getTrackedEntity().getUuid())
                    .setOrder(counter++);

            actionParamsForCreateAndEnroll.add(actionParameterBuilder.createActionParameterRequest());

            builder.setActionParameters(actionParamsForCreateAndEnroll)
                    .setDisplayName(DisplayNames.CREATE_TRACKED_ENTITY_INSTANCE + " [" +
                            program.getTrackedEntity().getName() + "]" + " and " + DisplayNames.PROGRAM_ENROLLMENT +
                            " [" + program.getName() + "]")
                    .setServiceInterface(ChannelRequestBuilder.ACTION_PROXY_SERVICE)
                    .setServiceMethod("createAndEnroll")
                    .setServiceMethodCallManner(MethodCallManner.MAP.name())
                    .setSubject(EventSubjects.CREATE_AND_ENROLL)
                    .setName(program.getTrackedEntity().getName()  + ", " + program.getName());


            actionEventRequests.add(builder.createActionEventRequest());

        }

        return actionEventRequests;

    }

    /*Adds program tracked entity attributes for each program*/
    private List<ActionParameterRequest> buildRequestForProgram(Program program) {

        List<ActionParameterRequest> parameterRequests = new ArrayList<>();
        ActionParameterRequestBuilder actionParameterBuilder;

        for (TrackedEntityAttribute attribute : program.getAttributes()) {

            actionParameterBuilder = new ActionParameterRequestBuilder()
                    .setDisplayName(attribute.getName())
                    .setKey(attribute.getUuid())
                    .setType(ParameterType.TEXTAREA.getValue())
                    .setOrder(counter++);

            parameterRequests.add(actionParameterBuilder.createActionParameterRequest());

        }

        return parameterRequests;

    }



}
