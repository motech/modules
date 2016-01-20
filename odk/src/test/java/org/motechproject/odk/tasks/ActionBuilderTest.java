package org.motechproject.odk.tasks;

import org.apache.commons.io.FileUtils;
import org.junit.Test;
import org.motechproject.odk.constant.DisplayNames;
import org.motechproject.odk.constant.EventParameters;
import org.motechproject.odk.domain.Configuration;
import org.motechproject.odk.domain.FormDefinition;
import org.motechproject.odk.parser.impl.XformParserODK;
import org.motechproject.tasks.contract.ActionEventRequest;
import org.motechproject.tasks.contract.ActionParameterRequest;
import org.motechproject.tasks.domain.ActionParameter;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static org.junit.Assert.assertEquals;


public class ActionBuilderTest {


    @Test
    public void testNestedRepeats() throws Exception{
        Configuration configuration = new Configuration();
        configuration.setName("configName");

        File f = new File(getClass().getResource("/nested_repeat.xml").getFile());
        String xml = FileUtils.readFileToString(f);
        FormDefinition formDefinition = new XformParserODK().parse(xml, configuration.getName());
        List<FormDefinition> formDefinitions = new ArrayList<>();
        formDefinitions.add(formDefinition);

        ActionBuilder builder = new ActionBuilder(formDefinitions);
        List<ActionEventRequest> requests = builder.build();
        System.out.println();
        assertEquals(requests.size(), 2);

        ActionEventRequest request = requests.get(0);
        assertEquals(request.getName(),"configName_nested_repeat_persist_form_instance");
        assertEquals(request.getSubject(),"persist_form_instance");
        assertEquals(request.getActionParameters().size(),7);
        Iterator<ActionParameterRequest> itr = request.getActionParameters().iterator();

        ActionParameterRequest parameter = itr.next();
        assertEquals(parameter.getKey(), EventParameters.FORM_TITLE);
        assertEquals(parameter.getDisplayName(), DisplayNames.FORM_TITLE);

        parameter = itr.next();
        assertEquals(parameter.getKey(),EventParameters.CONFIGURATION_NAME);
        assertEquals(parameter.getDisplayName(),DisplayNames.CONFIG_NAME);

        parameter = itr.next();
        assertEquals(parameter.getKey(),EventParameters.INSTANCE_ID);
        assertEquals(parameter.getDisplayName(),DisplayNames.INSTANCE_ID);
    }
}
