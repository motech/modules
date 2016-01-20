package org.motechproject.odk.tasks;

import org.apache.commons.io.FileUtils;

import org.junit.Test;
import org.motechproject.odk.domain.Configuration;
import org.motechproject.odk.domain.FormDefinition;
import org.motechproject.odk.parser.impl.XformParserODK;
import org.motechproject.tasks.contract.TriggerEventRequest;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class FormTriggerBuilderTest {

    @Test
    public void testNestedRepeats() throws Exception{
        Configuration configuration = new Configuration();
        configuration.setName("configName");

        File f = new File(getClass().getResource("/nested_repeat.xml").getFile());
        String xml = FileUtils.readFileToString(f);
        FormDefinition formDefinition = new XformParserODK().parse(xml, configuration.getName());
        List<FormDefinition> formDefinitions = new ArrayList<>();
        formDefinitions.add(formDefinition);

        FormTriggerBuilder builder = new FormTriggerBuilder(formDefinitions);
        List<TriggerEventRequest> triggerEventRequests = builder.buildTriggers();

        assertEquals(triggerEventRequests.size(), 2);
        TriggerEventRequest request = triggerEventRequests.get(0);

        assertEquals(request.getSubject(),"org.motechproject.odk.recieved_form.configName.nested_repeat");
        assertEquals(request.getEventParameters().size(),6);

        request = triggerEventRequests.get(1);
        assertEquals(request.getSubject(),"org.motechproject.odk.form_failure");
        assertEquals(request.getEventParameters().size(),5);

    }
}
