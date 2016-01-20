package org.motechproject.odk.it;

import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.motechproject.event.MotechEvent;
import org.motechproject.odk.constant.EventParameters;
import org.motechproject.odk.constant.OnaConstants;
import org.motechproject.odk.domain.FormDefinition;
import org.motechproject.odk.domain.FormInstance;
import org.motechproject.odk.event.EventHandler;
import org.motechproject.odk.event.builder.impl.EventBuilderOna;
import org.motechproject.testing.osgi.container.MotechNativeTestContainerFactory;
import org.ops4j.pax.exam.ExamFactory;
import org.ops4j.pax.exam.junit.PaxExam;
import org.ops4j.pax.exam.spi.reactors.ExamReactorStrategy;
import org.ops4j.pax.exam.spi.reactors.PerSuite;

import javax.inject.Inject;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

@RunWith(PaxExam.class)
@ExamReactorStrategy(PerSuite.class)
@ExamFactory(MotechNativeTestContainerFactory.class)
public class EventHandlerIT extends OdkBaseIT {

    @Inject
    private EventHandler eventHandler;
    private List<MotechEvent> events;
    private FormDefinition formDefinition;

    @Before
    public void setup() throws Exception {

        formDefinition = getFormDefinitionDataService().byConfigurationNameAndTitle(CONFIG_NAME,TITLE);
        assertEquals(getFormDefinitionDataService().retrieveAll().size(),1);
        assertNotNull(formDefinition);

        events = new EventBuilderOna().createEvents(getJson(),formDefinition,getConfiguration());
    }


    @Test
    public void testPersistFormNestedRepeats() throws Exception{
        MotechEvent persistEvent = events.get(events.size() - 1);
        eventHandler.handlePersistForm(persistEvent);
        FormInstance instance = getFormInstanceDataService().byConfigNameAndTitle(formDefinition.getConfigurationName(), formDefinition.getTitle());
        assertNotNull(instance);
        assertEquals(persistEvent.getParameters().get(EventParameters.INSTANCE_ID), instance.getInstanceId());
        assertEquals(persistEvent.getParameters().get(EventParameters.CONFIGURATION_NAME), instance.getConfigName());
        assertEquals(persistEvent.getParameters().get(EventParameters.FORM_TITLE), instance.getTitle());
        assertEquals(persistEvent.getParameters().get(OnaConstants.XFORM_ID_STRING), formDefinition.getTitle());
        assertEquals(persistEvent.getParameters().get(OnaConstants.STATUS), "submitted_via_web");;

        List<Map<String,Object>> data = new ObjectMapper().readValue((String) persistEvent.getParameters().get("outer_group"),
                new TypeReference<List<Map<String, Object>>>() {});

        Map<String, Object> outerGroup1 = data.get(0);
        assertNotNull(outerGroup1);
        assertEquals(outerGroup1.get("outer_group/outer_group_field"), "outer group 1 field");
        List<Map<String,Object>> outerGroup1InnerGroup = (List<Map<String,Object>>)outerGroup1.get("outer_group/inner_group");
        assertEquals(outerGroup1InnerGroup.get(0).get("outer_group/inner_group/inner_group_field"), "outer group 1 inner group field 1");
        assertEquals(outerGroup1InnerGroup.get(1).get("outer_group/inner_group/inner_group_field"),"outer group 1 inner group field 2");

        Map<String,Object> outerGroup2 = data.get(1);
        assertNotNull(outerGroup2);
        assertEquals(outerGroup1.get("outer_group/outer_group_field"),"outer group 1 field");
        List<Map<String,Object>> outerGroup2InnerGroup = (List<Map<String,Object>>)outerGroup2.get("outer_group/inner_group");
        assertEquals(outerGroup2InnerGroup.get(0).get("outer_group/inner_group/inner_group_field"),"outer group 2 inner group field 1");
        assertEquals(outerGroup2InnerGroup.get(1).get("outer_group/inner_group/inner_group_field"), "outer group 2 inner group field 2");

    }


}
