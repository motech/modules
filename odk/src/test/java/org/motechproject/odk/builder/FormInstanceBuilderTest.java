package org.motechproject.odk.builder;

import org.codehaus.jackson.map.ObjectMapper;
import org.junit.Test;
import org.motechproject.odk.constant.EventParameters;
import org.motechproject.odk.constant.FieldTypeConstants;
import org.motechproject.odk.domain.FormDefinition;
import org.motechproject.odk.domain.FormElement;
import org.motechproject.odk.domain.FormInstance;
import org.motechproject.odk.domain.FormValueGroup;
import org.motechproject.odk.domain.builder.FormElementBuilder;
import org.motechproject.odk.domain.builder.FormInstanceBuilder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class FormInstanceBuilderTest {

    private static final String CONFIG_NAME = "configname";
    private static final String TITLE = "title";
    private static final String CHILD_1 = "child1";
    private static final String CHILD_2 = "child2";
    private static final String INNER_GROUP = "innerGroup";
    private static final String CHILD_OUTER_GROUP = "childOuterGroup";
    private static final String FORM_ELEMENT_OUTER_GROUP = "formElementOuterGroup";
    private static final String FORM_ELEMENT_STRING = "formElementString";
    private static final String INSTANCE_ID = "instanceID";

    @Test
    public void testBuildFormInstanceWithRepeatGroups() throws Exception {

        FormDefinition formDefinition = buildFormDefWithGroups();
        Map<String, Object> params = buildParams();
        FormInstanceBuilder builder = new FormInstanceBuilder(formDefinition, params, INSTANCE_ID);
        FormInstance instance = builder.build();

        assertNotNull(instance);
        assertEquals(instance.getTitle(), TITLE);
        assertEquals(instance.getInstanceId(), INSTANCE_ID);
        assertEquals(instance.getFormValues().size(), 2);
        assertEquals(instance.getFormValues().get(0).getName(),FORM_ELEMENT_STRING);
        assertEquals(instance.getFormValues().get(1).getName(),FORM_ELEMENT_OUTER_GROUP);
        FormValueGroup group = (FormValueGroup) instance.getFormValues().get(1);

        assertEquals(group.getChildren().size(),2);
        assertEquals(group.getChildren().get(0).getName(),INNER_GROUP);
        assertEquals(instance.getFormValues().get(1).getName(), FORM_ELEMENT_OUTER_GROUP);

        FormValueGroup innerGroup = (FormValueGroup) group.getChildren().get(0);
        assertEquals(innerGroup.getChildren().size(),2);
        assertEquals(innerGroup.getChildren().get(0).getName(),CHILD_2);
        assertEquals(innerGroup.getChildren().get(1).getName(),CHILD_1);
    }

    private FormDefinition buildFormDefWithGroups() {
        FormDefinition formDefinition = new FormDefinition();

        formDefinition.setConfigurationName(CONFIG_NAME);
        formDefinition.setTitle(TITLE);

        FormElement formElementChild1 = new FormElementBuilder()
                .setName(CHILD_1)
                .setLabel("child 1")
                .setType(FieldTypeConstants.STRING)
                .setPartOfRepeatGroup(true)
                .createFormElement();

        FormElement formElementChild2 = new FormElementBuilder()
                .setName(CHILD_2)
                .setLabel("child 2")
                .setType(FieldTypeConstants.STRING)
                .setPartOfRepeatGroup(true)
                .createFormElement();

        FormElement formElementInnerGroup = new FormElement();
        formElementInnerGroup.setName(INNER_GROUP);
        formElementInnerGroup.setType("Inner Group");
        formElementInnerGroup.setType(FieldTypeConstants.REPEAT_GROUP);
        List<FormElement> formElements = new ArrayList<>();
        formElements.add(formElementChild1);
        formElements.add(formElementChild2);
        formElementInnerGroup.setChildren(formElements);
        formElementInnerGroup.setPartOfRepeatGroup(true);

        List<FormElement> outerGroupFormElements = new ArrayList<>();

        FormElement childOuterGroup = new FormElement();
        childOuterGroup.setName(CHILD_OUTER_GROUP);
        childOuterGroup.setLabel("Child Outer Group");
        childOuterGroup.setType(FieldTypeConstants.STRING);
        childOuterGroup.setPartOfRepeatGroup(true);

        FormElement formElementOuterGroup = new FormElement();
        formElementOuterGroup.setName(FORM_ELEMENT_OUTER_GROUP);
        formElementOuterGroup.setLabel("Form Element Outer Group");
        formElementOuterGroup.setType(FieldTypeConstants.REPEAT_GROUP);

        outerGroupFormElements.add(childOuterGroup);
        outerGroupFormElements.add(formElementInnerGroup);
        formElementOuterGroup.setChildren(outerGroupFormElements);

        List<FormElement> formDefElements = new ArrayList<>();

        FormElement formElementString = new FormElement();
        formElementString.setName(FORM_ELEMENT_STRING);
        formElementString.setLabel("form element String");
        formElementString.setType(FieldTypeConstants.STRING);
        formDefElements.add(formElementString);
        formDefElements.add(formElementOuterGroup);

        formDefinition.setFormElements(formDefElements);

        return formDefinition;

    }

    private Map<String, Object> buildParams() throws Exception {
        Map<String, Object> innerGroup = new HashMap<>();
        innerGroup.put(CHILD_1, "child 1");
        innerGroup.put(CHILD_2, "child 2");
        List<Map<String,Object>> innerGroupList = new ArrayList<>();
        innerGroupList.add(innerGroup);

        Map<String, Object> formElementOuterGroup = new HashMap<>();
        formElementOuterGroup.put(INNER_GROUP, innerGroupList);
        formElementOuterGroup.put(CHILD_OUTER_GROUP, "child outer group");
        List<Map<String,Object>> outerGroupList = new ArrayList<>();
        outerGroupList.add(formElementOuterGroup);

        Map<String, Object> params = new HashMap<>();
        params.put(EventParameters.CONFIGURATION_NAME, CONFIG_NAME);
        params.put(EventParameters.FORM_TITLE, TITLE);
        String json = new ObjectMapper().writeValueAsString(outerGroupList);
        params.put(FORM_ELEMENT_OUTER_GROUP, json);
        params.put(FORM_ELEMENT_STRING, "string value");

        return params;
    }
}
