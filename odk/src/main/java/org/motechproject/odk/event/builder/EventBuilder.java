package org.motechproject.odk.event.builder;

import org.motechproject.event.MotechEvent;
import org.motechproject.odk.domain.Configuration;
import org.motechproject.odk.domain.FormDefinition;
import org.motechproject.odk.event.EventBuilderException;

import java.util.List;


/**
 * Builds a list of events from incoming form data.
 *
 */
public interface EventBuilder {

    /**
     * Creates a list of events from the parameters. One event is contains the values for a whole form instance. If
     * the form instance contains a repeat group, the repeat group and its children are represented as JSON. Only top level
     * repeat groups are accessible as event parameters. Nested repeat groups are contained in the top level repeat group's
     * JSON representation.
     *
     * The remainder of the list of events are for each repeat group in the form instance. The event parameters contain
     * all of the values available to the scope of the repeat group, including the values of parent repeat groups.
     * @param json JSON representation of the form instance data
     * @param formDefinition The internal representation of the XML form.
     * @param configuration {@link Configuration}
     * @return A list of {@link MotechEvent}
     * @throws Exception If JSON is malformed.
     */
    List<MotechEvent> createEvents(String json, FormDefinition formDefinition, Configuration configuration) throws EventBuilderException;

}
