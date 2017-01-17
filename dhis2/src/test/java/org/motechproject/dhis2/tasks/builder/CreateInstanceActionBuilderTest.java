package org.motechproject.dhis2.tasks.builder;


import org.junit.Test;
import org.motechproject.dhis2.domain.TrackedEntity;
import org.motechproject.dhis2.domain.TrackedEntityAttribute;
import org.motechproject.dhis2.event.EventParams;
import org.motechproject.dhis2.event.EventSubjects;
import org.motechproject.dhis2.tasks.DisplayNames;
import org.motechproject.dhis2.tasks.builder.CreateInstanceActionBuilder;
import org.motechproject.tasks.contract.ActionEventRequest;
import org.motechproject.tasks.contract.ActionParameterRequest;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.SortedSet;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class CreateInstanceActionBuilderTest {

    @Test
    public void testBuildTrackedEntities() throws Exception{

        CreateInstanceActionBuilder builder = new CreateInstanceActionBuilder();

        List<TrackedEntity> trackedEntities = new ArrayList<>();
        TrackedEntity trackedEntity1 = new TrackedEntity("name1", "ID1");
        TrackedEntity trackedEntity2 = new TrackedEntity("name2", "ID2");

        trackedEntities.add(trackedEntity1);
        trackedEntities.add(trackedEntity2);

        List<TrackedEntityAttribute> trackedEntityAttributes = new ArrayList<>();
        TrackedEntityAttribute attribute1 = new TrackedEntityAttribute("attribute1", "ID1");
        TrackedEntityAttribute attribute2 = new TrackedEntityAttribute("attribute2", "ID2");

        trackedEntityAttributes.add(attribute1);
        trackedEntityAttributes.add(attribute2);

        List<ActionEventRequest> requests = builder.build(trackedEntityAttributes, trackedEntities);
        assertNotNull(requests);

        ActionEventRequest actionEventRequest = requests.get(0);
        assertEquals(actionEventRequest.getDisplayName(), DisplayNames.CREATE_TRACKED_ENTITY_INSTANCE +
                " [" + trackedEntity1.getName() + "]");
        assertEquals(actionEventRequest.getSubject(), EventSubjects.CREATE_ENTITY);
        assertEquals(actionEventRequest.getName(),trackedEntity1.getName());

        SortedSet<ActionParameterRequest> actionParameters = actionEventRequest.getActionParameters();

        Iterator<ActionParameterRequest> itr = actionParameters.iterator();
        ActionParameterRequest request = itr.next();

        assertEquals(request.getDisplayName(),DisplayNames.EXTERNAL_ID);
        assertEquals(request.getKey(), EventParams.EXTERNAL_ID);

        request = itr.next();
        assertEquals(request.getKey(),EventParams.ENTITY_TYPE);
        assertEquals(request.getValue(),trackedEntity1.getUuid());

        request = itr.next();
        assertEquals(request.getKey(),EventParams.LOCATION);
        assertEquals(request.getDisplayName(),DisplayNames.ORG_UNIT);

        request = itr.next();
        assertEquals(request.getKey(),attribute1.getUuid());
        assertNull(request.getValue());

    }
}
