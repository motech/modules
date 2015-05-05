package org.motechproject.dhis2.tasks;

import org.motechproject.dhis2.domain.Program;
import org.motechproject.dhis2.domain.TrackedEntityAttribute;
import org.motechproject.dhis2.event.EventParams;
import org.motechproject.dhis2.event.EventSubjects;
import org.motechproject.tasks.contract.ActionEventRequest;
import org.motechproject.tasks.contract.ActionEventRequestBuilder;
import org.motechproject.tasks.contract.ActionParameterRequest;
import org.motechproject.tasks.contract.ActionParameterRequestBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;



/**
 * Builds task action requests for enrolling tracked entity instances in a program for each entity type in the DHIS2
 * schema. Also builds create + enroll combination task action requests for each
 * program and the corresponding tracked entity type.
 */
public class ProgramActionBuilder {

    private static final String UNICODE = "UNICODE";
    private int counter;

    /**
     * Builds a list of action event requests for program enrollment and create + enroll task actions.
     * @param programs
     * @return A list of action event rests pertaining to program enrollment.
     */
    public List<ActionEventRequest> build(List<Program> programs) {

        List<ActionEventRequest> actionEventRequests = new ArrayList<>();

        for (Program program : programs) {
            counter = 0;

            SortedSet<ActionParameterRequest> actionParameters = new TreeSet<>();
            SortedSet<ActionParameterRequest> actionParamsForCreateAndEnroll = new TreeSet<>();

            ActionEventRequestBuilder builder = new ActionEventRequestBuilder();

            ActionParameterRequestBuilder actionParameterBuilder = new ActionParameterRequestBuilder()
                    .setDisplayName(DisplayNames.EXTERNAL_ID)
                    .setKey(EventParams.EXTERNAL_ID)
                    .setType(UNICODE)
                    .setRequired(true)
                    .setOrder(counter++);

            actionParameters.add(actionParameterBuilder.createActionParameterRequest());
            actionParamsForCreateAndEnroll.add(actionParameterBuilder.createActionParameterRequest());


            actionParameterBuilder = new ActionParameterRequestBuilder()
                    .setDisplayName(DisplayNames.ENROLLMENT_DATE)
                    .setKey(EventParams.DATE)
                    .setOrder(counter++)
                    .setType(UNICODE);

            actionParameters.add(actionParameterBuilder.createActionParameterRequest());
            actionParamsForCreateAndEnroll.add(actionParameterBuilder.createActionParameterRequest());

            actionParameterBuilder = new ActionParameterRequestBuilder()
                    .setDisplayName(DisplayNames.PROGRAM_NAME)
                    .setKey(EventParams.PROGRAM)
                    .setValue(program.getUuid())
                    .setType(UNICODE)
                    .setOrder(counter++)
                    .setHidden(true);

            actionParameters.add(actionParameterBuilder.createActionParameterRequest());
            actionParamsForCreateAndEnroll.add(actionParameterBuilder.createActionParameterRequest());

            /*Adds all tracked entity attributes for program*/
            actionParameters.addAll(buildRequestForProgram(program));
            actionParamsForCreateAndEnroll.addAll(buildRequestForProgram(program));

            builder.setActionParameters(actionParameters)
                    .setDisplayName(DisplayNames.PROGRAM_ENROLLMENT + " [" + program.getName() + "]")
                    .setSubject(EventSubjects.ENROLL_IN_PROGRAM)
                    .setName(program.getName());


            actionEventRequests.add(builder.createActionEventRequest());


            /*Add corresponding create and enroll action*/
            actionParameterBuilder = new ActionParameterRequestBuilder()
                    .setDisplayName(program.getTrackedEntity().getName())
                    .setType(UNICODE)
                    .setHidden(true)
                    .setKey(EventParams.ENTITY_TYPE)
                    .setValue(program.getTrackedEntity().getUuid())
                    .setOrder(counter++);

            actionParamsForCreateAndEnroll.add(actionParameterBuilder.createActionParameterRequest());

            actionParameterBuilder = new ActionParameterRequestBuilder()
                    .setDisplayName(DisplayNames.ORG_UNIT)
                    .setType(UNICODE)
                    .setKey(EventParams.LOCATION)
                    .setOrder(counter++)
                    .setRequired(true);

            actionParamsForCreateAndEnroll.add(actionParameterBuilder.createActionParameterRequest());


            builder.setActionParameters(actionParamsForCreateAndEnroll)
                    .setDisplayName(DisplayNames.CREATE_TRACKED_ENTITY_INSTANCE + " [" +
                            program.getTrackedEntity().getName() + "]" + " and " + DisplayNames.PROGRAM_ENROLLMENT +
                            " [" + program.getName() + "]")
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
                    .setType(UNICODE)
                    .setOrder(counter++);

            parameterRequests.add(actionParameterBuilder.createActionParameterRequest());

        }

        return parameterRequests;

    }



}
