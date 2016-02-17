package org.motechproject.commcare.domain;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class FormXmlTest {

    private FormXml formXml;

    @Before
    public void setUp() {
        formXml = new FormXml();

        FormValueElement childElement = new FormValueElement();
        childElement.setElementName("Chris");
        childElement.addAttribute("age", "3");

        FormValueElement motherElement1 = new FormValueElement();
        motherElement1.setElementName("Jane");
        motherElement1.addAttribute("age", "21");
        motherElement1.addFormValueElement("child", childElement);

        FormValueElement motherElement2 = new FormValueElement();
        motherElement2.setElementName("Aubrey");
        motherElement2.addAttribute("age", "19");

        formXml.setFormFields(new ArrayList<>(Arrays.asList(motherElement1, motherElement2)));
    }

    @Test
    public void shouldReturnFormValueElementsIfTheyAlreadyExist() {
        FormValueElement element = formXml.getElementByPath(Arrays.asList("Jane"));
        assertNotNull(element);
        assertEquals("Jane", element.getElementName());
        assertEquals(1, element.getAttributes().size());
        assertEquals("21", element.getAttributes().get("age"));
        assertEquals(1, element.getSubElements().size());
        assertTrue(element.getSubElements().containsKey("child"));

        element = formXml.getElementByPath("Jane", "Chris");
        assertNotNull(element);
        assertEquals("Chris", element.getElementName());
        assertEquals(1, element.getAttributes().size());
        assertEquals("3", element.getAttributes().get("age"));
        assertEquals(0, element.getSubElements().size());

        element = formXml.getElementByPath(Arrays.asList("Aubrey"));
        assertNotNull(element);
        assertEquals("Aubrey", element.getElementName());
        assertEquals(1, element.getAttributes().size());
        assertEquals("19", element.getAttributes().get("age"));
        assertEquals(0, element.getSubElements().size());
    }

    @Test
    public void shouldCreateAndReturnEmptyElementIfItDoesntExist() {
        FormValueElement element = formXml.getElementByPath(Arrays.asList("Jane", "Matt"));
        assertNotNull(element);
        assertEquals("Matt", element.getElementName());
        assertTrue(element.getAttributes().isEmpty());
        assertTrue(element.getSubElements().isEmpty());
        assertNull(element.getValue());

        element = formXml.getElementByPath("Jane");
        // The parent elemet should have one existing subelement and one new element
        assertEquals(2, element.getSubElements().size());

        element = formXml.getElementByPath("New1", "New2", "New3");
        assertNotNull(element);
        assertEquals("New3", element.getElementName());
        // No child elements
        assertEquals(0, element.getSubElements().size());

        element = formXml.getElementByPath("New1", "New2");
        assertNotNull(element);
        assertEquals("New2", element.getElementName());
        // New3 should be a subelement, created in previous step
        assertEquals(1, element.getSubElements().size());

        element = formXml.getElementByPath("New1");
        assertNotNull(element);
        assertEquals("New1", element.getElementName());
        // New2 should be a subelement, created in previous step
        assertEquals(1, element.getSubElements().size());
    }
}
