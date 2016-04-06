package org.motechproject.odk.parser.impl;

import org.apache.commons.io.FileUtils;
import org.junit.Test;
import org.motechproject.odk.domain.Configuration;
import org.motechproject.odk.domain.FormDefinition;
import org.motechproject.odk.domain.FormElement;
import org.motechproject.odk.domain.FormValueGroup;
import org.motechproject.odk.parser.impl.XformParserODK;

import java.io.File;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class XformParserODKTest {

    @Test
    public void testWidgets() throws Exception {
        Configuration configuration = new Configuration();
        configuration.setName("configName");
        File f = new File(getClass().getResource("/widgets.xml").getFile());
        String xml = FileUtils.readFileToString(f);
        FormDefinition formDefinition = new XformParserODK().parse(xml, configuration.getName());
        assertEquals(formDefinition.getConfigurationName(),"configName");
        assertEquals(formDefinition.getTitle(),"Widgets");
        assertEquals(formDefinition.getFormElements().size(),57);
    }

    @Test
    public void testNestedRepeatGroups() throws Exception {
        Configuration configuration = new Configuration();
        configuration.setName("configName");

        File f = new File(getClass().getResource("/nested_repeat.xml").getFile());
        String xml = FileUtils.readFileToString(f);
        FormDefinition formDefinition = new XformParserODK().parse(xml, configuration.getName());

        assertNotNull(formDefinition);
        assertEquals(formDefinition.getTitle(), "nested_repeat");
        assertEquals(formDefinition.getConfigurationName(), configuration.getName());
        assertEquals(formDefinition.getFormElements().size(), 5);
        assertEquals(formDefinition.getFormElements().get(0).getName(), "/data/root_scope_field");
        assertEquals(formDefinition.getFormElements().get(1).getName(),"/data/non_repeating_group/non_repeating_group_field");
        assertEquals(formDefinition.getFormElements().get(2).getName(),"/data/meta/instanceID");
        assertEquals(formDefinition.getFormElements().get(3).getName(), "/data/outer_repeat_group/inner_repeat_group");
        assertEquals(formDefinition.getFormElements().get(4).getName(),"/data/outer_repeat_group");

        FormElement innerGroup = formDefinition.getFormElements().get(3);
        assertNotNull(innerGroup.getChildren());
        assertEquals(innerGroup.getChildren().get(0).getName(), "/data/outer_repeat_group/inner_repeat_group/inner_repeat_group_field");

        FormElement outerGroup = formDefinition.getFormElements().get(4);
        assertNotNull(outerGroup.getChildren());
        assertEquals(outerGroup.getChildren().get(0).getName(), "/data/outer_repeat_group/inner_repeat_group");
        assertEquals(outerGroup.getChildren().get(1).getName(),"/data/outer_repeat_group/outer_repeat_group_field");
    }
}
