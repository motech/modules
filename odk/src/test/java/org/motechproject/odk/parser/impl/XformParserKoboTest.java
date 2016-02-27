package org.motechproject.odk.parser.impl;

import org.apache.commons.io.FileUtils;
import org.junit.Test;
import org.motechproject.odk.domain.Configuration;
import org.motechproject.odk.domain.FormDefinition;
import org.motechproject.odk.domain.FormElement;
import org.motechproject.odk.parser.impl.XformParserKobo;

import java.io.File;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class XformParserKoboTest {

    @Test
    public void testKoboTestForm() throws Exception {
        Configuration configuration = new Configuration();
        configuration.setName("configName");

        File f = new File(getClass().getResource("/kobotestform.xml").getFile());
        String xml = FileUtils.readFileToString(f);
        FormDefinition formDefinition = new XformParserKobo().parse(xml, configuration.getName());

        assertNotNull(formDefinition);
        assertEquals(formDefinition.getFormElements().size(), 5);
        assertEquals(formDefinition.getFormElements().get(0).getName(), "/KoboTestform/meta/instanceID");
        assertEquals(formDefinition.getFormElements().get(0).getLabel(),"/meta/instanceID");

        assertEquals(formDefinition.getFormElements().get(1).getName(),"/KoboTestform/group_su8of31/group_rr5fo58/Age");
        assertEquals(formDefinition.getFormElements().get(1).getLabel(),"/OuterGroup/InnerGroup/Age");

        assertEquals(formDefinition.getFormElements().get(2).getName(),"/KoboTestform/end");
        assertEquals(formDefinition.getFormElements().get(2).getLabel(),"/end");

        assertEquals(formDefinition.getFormElements().get(3).getName(),"/KoboTestform/group_rz3vx17");
        assertEquals(formDefinition.getFormElements().get(3).getLabel(),"/Names");
        assertTrue(formDefinition.getFormElements().get(3).hasChildren());
        assertEquals(formDefinition.getFormElements().get(3).getChildren().get(0).getName(), "/KoboTestform/group_rz3vx17/First_Name");
        assertEquals(formDefinition.getFormElements().get(3).getChildren().get(0).getLabel(), "/Names/First_Name");
        assertEquals(formDefinition.getFormElements().get(3).getChildren().get(1).getName(), "/KoboTestform/group_rz3vx17/Last_Name");
        assertEquals(formDefinition.getFormElements().get(3).getChildren().get(1).getLabel(), "/Names/Last_Name");

        assertEquals(formDefinition.getFormElements().get(4).getName(),"/KoboTestform/start");
        assertEquals(formDefinition.getFormElements().get(4).getLabel(),"/start");

    }
}
