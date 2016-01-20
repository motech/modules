package org.motechproject.odk.tasks;

import org.apache.commons.io.FileUtils;
import org.junit.Test;
import org.motechproject.odk.domain.Configuration;
import org.motechproject.odk.domain.FormDefinition;
import org.motechproject.odk.parser.impl.XformParserODK;
import org.motechproject.tasks.contract.EventParameterRequest;
import org.motechproject.tasks.contract.TriggerEventRequest;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class RepeatGroupTriggerBuilderTest {

    @Test
    public void testNestedRepeats() throws Exception {
        Configuration configuration = new Configuration();
        configuration.setName("configName");

        File f = new File(getClass().getResource("/nested_repeat.xml").getFile());
        String xml = FileUtils.readFileToString(f);
        FormDefinition formDefinition = new XformParserODK().parse(xml, configuration.getName());
        List<FormDefinition> formDefinitions = new ArrayList<>();
        formDefinitions.add(formDefinition);

        RepeatGroupTriggerBuilder builder = new RepeatGroupTriggerBuilder(formDefinitions);
        List<TriggerEventRequest> requests = builder.buildTriggers();
        System.out.println();

        assertEquals(requests.size(), 2);
        TriggerEventRequest request = requests.get(0);
        assertEquals(request.getSubject(), "org.motechproject.odk.repeat_group.configName.nested_repeat./data/outer_repeat_group");
        assertEquals(request.getEventParameters().size(),4);

        assertEquals(request.getEventParameters().get(0).getEventKey(),"/data/root_scope_field");
        assertEquals(request.getEventParameters().get(1).getEventKey(),"/data/non_repeating_group/non_repeating_group_field");
        assertEquals(request.getEventParameters().get(2).getEventKey(),"/data/meta/instanceID");
        assertEquals(request.getEventParameters().get(3).getEventKey(), "/data/outer_repeat_group/outer_repeat_group_field");

        request = requests.get(1);
        assertEquals(request.getSubject(), "org.motechproject.odk.repeat_group.configName.nested_repeat./data/outer_repeat_group/inner_repeat_group");

        assertEquals(request.getEventParameters().size(),5);
        assertEquals(request.getEventParameters().get(0).getEventKey(),"/data/root_scope_field");
        assertEquals(request.getEventParameters().get(1).getEventKey(),"/data/non_repeating_group/non_repeating_group_field");
        assertEquals(request.getEventParameters().get(2).getEventKey(),"/data/meta/instanceID");
        assertEquals(request.getEventParameters().get(3).getEventKey(),"/data/outer_repeat_group/outer_repeat_group_field");
        assertEquals(request.getEventParameters().get(4).getEventKey(),"/data/outer_repeat_group/inner_repeat_group/inner_repeat_group_field");
    }
}
