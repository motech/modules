package org.motechproject.dhis2.tasks;

import org.motechproject.dhis2.domain.TrackedEntity;
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
 * Builds task action requests for tracked entity instance creation for each entity type in the DHIS2 schema.
 */
public class CreateInstanceActionBuilder {

    private static final String UNICODE = "UNICODE";
    private static final int ATTRIBUTE_COUNT = 3;


    /**
     * Takes a list of attributes and tracked entity types and builds a list of action event requests
     * for tracked entity instance creation.
     * @param attributes
     * @param trackedEntities
     * @return a list of action event requests for creating instances of each tracked entity type
     */
    public List<ActionEventRequest> build(List<TrackedEntityAttribute> attributes, List<TrackedEntity> trackedEntities) {

        List<ActionEventRequest> actionEventRequests = new ArrayList<>();
        SortedSet<ActionParameterRequest> attributeParameters = buildAttributeActionParameters(attributes);

        for (TrackedEntity entity : trackedEntities) {

            int counter = 0;
            SortedSet<ActionParameterRequest> actionParameters = new TreeSet<>();
            actionParameters.addAll(attributeParameters);


            ActionParameterRequestBuilder actionParameterBuilder = new ActionParameterRequestBuilder()
                    .setDisplayName(DisplayNames.EXTERNAL_ID)
                    .setKey(EventParams.EXTERNAL_ID)
                    .setType(UNICODE)
                    .setRequired(true)
                    .setOrder(counter++);


            actionParameters.add(actionParameterBuilder.createActionParameterRequest());

            actionParameterBuilder = new ActionParameterRequestBuilder()
                    .setDisplayName(entity.getName())
                    .setType(UNICODE)
                    .setHidden(true)
                    .setKey(EventParams.ENTITY_TYPE)
                    .setValue(entity.getUuid())
                    .setOrder(counter++);

            actionParameters.add(actionParameterBuilder.createActionParameterRequest());

            actionParameterBuilder = new ActionParameterRequestBuilder()
                    .setDisplayName(DisplayNames.ORG_UNIT)
                    .setType(UNICODE)
                    .setKey(EventParams.LOCATION)
                    .setOrder(counter++)
                    .setRequired(true);

            actionParameters.add(actionParameterBuilder.createActionParameterRequest());



            ActionEventRequestBuilder builder = new ActionEventRequestBuilder()
                    .setDisplayName(DisplayNames.CREATE_TRACKED_ENTITY_INSTANCE + " [" + entity.getName() + "]")
                    .setName(entity.getName())
                    .setSubject(EventSubjects.CREATE_ENTITY)
                    .setActionParameters(actionParameters);

            actionEventRequests.add(builder.createActionEventRequest());

        }

        return actionEventRequests;
    }

    /*adds action parameters for each tracked entity attribute in the DHIS2 schema*/
    private SortedSet<ActionParameterRequest> buildAttributeActionParameters(List<TrackedEntityAttribute> attributes) {

        int count = ATTRIBUTE_COUNT;
        SortedSet<ActionParameterRequest> attributeActionParameters = new TreeSet<>();
        ActionParameterRequestBuilder parameterRequestBuilder;

        for (TrackedEntityAttribute attribute : attributes) {

            parameterRequestBuilder = new ActionParameterRequestBuilder()
                    .setDisplayName(attribute.getName())
                    .setKey(attribute.getUuid())
                    .setType(UNICODE)
                    .setOrder(count++);

            attributeActionParameters.add(parameterRequestBuilder.createActionParameterRequest());

        }

        return attributeActionParameters;

    }
}
