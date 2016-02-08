package org.motechproject.odk.event.builder.impl;

import org.apache.commons.io.FileUtils;
import org.junit.Before;
import org.junit.Test;
import org.motechproject.event.MotechEvent;
import org.motechproject.odk.constant.EventParameters;
import org.motechproject.odk.domain.Configuration;
import org.motechproject.odk.domain.FormDefinition;
import org.motechproject.odk.domain.FormElement;
import org.motechproject.odk.event.builder.EventBuilder;
import org.motechproject.odk.parser.impl.XformParserODK;

import java.io.File;
import java.util.Iterator;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class EventBuilderOdkTest {

    private Configuration configuration;
    private String json;
    private FormDefinition formDefinition;

    @Before
    public void setup() throws Exception {
        configuration = new Configuration();
        configuration.setName("configName");

        File f = new File(getClass().getResource("/ona_nested_repeats.json").getFile());
        json = FileUtils.readFileToString(f);

        f = new File(getClass().getResource("/ona_nested_repeats.xml").getFile());
        String xml = FileUtils.readFileToString(f);

        formDefinition = new XformParserODK().parse(xml, configuration.getName());
        alterFormDef(formDefinition);
    }

    @Test
    public void testOnaNestedRepeats() throws Exception {

        EventBuilder builder = new EventBuilderOna();
        List<MotechEvent> eventList = builder.createEvents(json, formDefinition, configuration);
        System.out.println();
        assertEquals(eventList.size(), 7);
        Iterator<MotechEvent> itr = eventList.iterator();

        MotechEvent event = itr.next();
        assertEquals(event.getSubject(), "org.motechproject.odk.repeat_group.configName.ona_nested_repeats.outer_group");
        assertEquals(event.getParameters().size(), 4);
        assertEquals(event.getParameters().get("root_scope_field"), "root scope");
        assertEquals(event.getParameters().get("outer_group/outer_group_field"), "outer group 1 field");
        assertEquals(event.getParameters().get("formhub/uuid"), "517b7383ae62455482b6f8505e5646e2");
        assertEquals(event.getParameters().get("meta/instanceID"), "uuid:d9e30fd7-5f0c-4185-a331-db0f740b3547");

        event = itr.next();
        assertEquals(event.getSubject(), "org.motechproject.odk.repeat_group.configName.ona_nested_repeats.outer_group/inner_group");
        assertEquals(event.getParameters().size(), 5);
        assertEquals(event.getParameters().get("root_scope_field"), "root scope");
        assertEquals(event.getParameters().get("outer_group/outer_group_field"), "outer group 1 field");
        assertEquals(event.getParameters().get("formhub/uuid"), "517b7383ae62455482b6f8505e5646e2");
        assertEquals(event.getParameters().get("meta/instanceID"), "uuid:d9e30fd7-5f0c-4185-a331-db0f740b3547");
        assertEquals(event.getParameters().get("outer_group/inner_group/inner_group_field"), "outer group 1 inner group field 1");

        event = itr.next();
        assertEquals(event.getSubject(), "org.motechproject.odk.repeat_group.configName.ona_nested_repeats.outer_group/inner_group");
        assertEquals(event.getParameters().size(), 5);
        assertEquals(event.getParameters().get("root_scope_field"), "root scope");
        assertEquals(event.getParameters().get("outer_group/outer_group_field"), "outer group 1 field");
        assertEquals(event.getParameters().get("formhub/uuid"), "517b7383ae62455482b6f8505e5646e2");
        assertEquals(event.getParameters().get("meta/instanceID"), "uuid:d9e30fd7-5f0c-4185-a331-db0f740b3547");
        assertEquals(event.getParameters().get("outer_group/inner_group/inner_group_field"), "outer group 1 inner group field 2");

        event = itr.next();
        assertEquals(event.getSubject(), "org.motechproject.odk.repeat_group.configName.ona_nested_repeats.outer_group");
        assertEquals(event.getParameters().size(), 4);
        assertEquals(event.getParameters().get("root_scope_field"), "root scope");
        assertEquals(event.getParameters().get("outer_group/outer_group_field"), "outer group 2 field");
        assertEquals(event.getParameters().get("formhub/uuid"), "517b7383ae62455482b6f8505e5646e2");
        assertEquals(event.getParameters().get("meta/instanceID"), "uuid:d9e30fd7-5f0c-4185-a331-db0f740b3547");

        event = itr.next();
        assertEquals(event.getSubject(), "org.motechproject.odk.repeat_group.configName.ona_nested_repeats.outer_group/inner_group");
        assertEquals(event.getParameters().size(), 5);
        assertEquals(event.getParameters().get("root_scope_field"), "root scope");
        assertEquals(event.getParameters().get("outer_group/outer_group_field"), "outer group 2 field");
        assertEquals(event.getParameters().get("formhub/uuid"), "517b7383ae62455482b6f8505e5646e2");
        assertEquals(event.getParameters().get("meta/instanceID"), "uuid:d9e30fd7-5f0c-4185-a331-db0f740b3547");
        assertEquals(event.getParameters().get("outer_group/inner_group/inner_group_field"), "outer group 2 inner group field 1");

        event = itr.next();
        assertEquals(event.getSubject(), "org.motechproject.odk.repeat_group.configName.ona_nested_repeats.outer_group/inner_group");
        assertEquals(event.getParameters().size(), 5);
        assertEquals(event.getParameters().get("root_scope_field"), "root scope");
        assertEquals(event.getParameters().get("outer_group/outer_group_field"), "outer group 2 field");
        assertEquals(event.getParameters().get("formhub/uuid"), "517b7383ae62455482b6f8505e5646e2");
        assertEquals(event.getParameters().get("meta/instanceID"), "uuid:d9e30fd7-5f0c-4185-a331-db0f740b3547");
        assertEquals(event.getParameters().get("outer_group/inner_group/inner_group_field"), "outer group 2 inner group field 2");

        event = itr.next();
        assertEquals(event.getSubject(), "org.motechproject.odk.recieved_form.configName.ona_nested_repeats");
        assertEquals(event.getParameters().size(), 6);
        assertEquals(event.getParameters().get("root_scope_field"), "root scope");
        assertEquals(event.getParameters().get("meta/instanceID"), "uuid:d9e30fd7-5f0c-4185-a331-db0f740b3547");
        assertEquals(event.getParameters().get("formhub/uuid"), "517b7383ae62455482b6f8505e5646e2");
        assertEquals(event.getParameters().get("root_scope_field"), "root scope");
        assertEquals(event.getParameters().get(EventParameters.CONFIGURATION_NAME), configuration.getName());
        assertEquals(event.getParameters().get(EventParameters.FORM_TITLE), formDefinition.getTitle());
        assertNotNull(event.getParameters().get("outer_group"));

    }


    private void alterFormDef(FormDefinition formDefinition) {
        List<FormElement> formElements = formDefinition.getFormElements();

        for (FormElement formElement : formElements) {
            if (!formElement.isPartOfRepeatGroup()) {
                alterFormElementName(formElement);
            }
        }
    }

    private void alterFormElementName(FormElement formElement) {
        String formFieldName = formElement.getName();
        String name = formFieldName.substring(formFieldName.indexOf("/", 1) + 1, formFieldName.length()); // removes form title from URI
        formElement.setName(name);

        if (formElement.hasChildren()) {
            for (FormElement child : formElement.getChildren()) {
                alterFormElementName(child);
            }
        }
    }
}
