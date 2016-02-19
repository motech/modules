package org.motechproject.commcare.domain;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Queue;

/**
 * This class represents an XML of a Commcare form, that is sent to the Commcare server via
 * Commcare submission API.
 */
public class FormXml {

    private List<FormValueElement> formFields = new ArrayList<>();
    private Map<String, MetadataValue> metadata = new HashMap<>();
    private String name;
    private String version;
    private String uiversion;
    private String xmlns;

    /**
     * Adds a new form field to this form XML representation.
     *
     * @param field the field to add
     */
    public void addFormField(FormValueElement field) {
        formFields.add(field);
    }

    /**
     * Gets element by the provided path. If any of the elements on the path does not exist, it is created.
     *
     * @param path an array of element names, starting with the most outer one
     * @return the element from the xml representation, specified by the path and created, if it didn't exist
     */
    public FormValueElement getElementByPath(String... path) {
        return getElementByPath(Arrays.asList(path));
    }

    /**
     * Gets element by the provided path. If any of the elements on the path does not exist, it is created.
     *
     * @param path a list of element names, starting with the most outer one
     * @return the element from the xml representation, specified by the path and created, if it didn't exist
     */
    public FormValueElement getElementByPath(List<String> path) {
        Queue<String> formElementQueue = new ArrayDeque<>(path);
        String searchRoot = formElementQueue.poll();
        FormValueElement currentSubtree = null;

        // Look for the first element under "data" tag
        for (FormValueElement field : formFields) {
            if (field.getElementName().equals(searchRoot)) {
                currentSubtree = field;
            }
        }

        // If there's no such node under "data", create one
        if (currentSubtree == null) {
            currentSubtree = new FormValueElement();
            currentSubtree.setElementName(searchRoot);
            formFields.add(currentSubtree);
        }

        // Browse through the XML tree, based on the element names in queue
        while (!formElementQueue.isEmpty()) {
            String nextElement = formElementQueue.poll();
            boolean elementExists = false;

            // if element of the given name exists, save it and continue search from that subtree
            for (FormValueElement element : currentSubtree.getSubElements().values()) {
                if (element.getElementName().equals(nextElement)) {
                    currentSubtree = element;
                    elementExists = true;
                    break;
                }
            }

            // if element does not exist, crete a new one and set it as our subtree
            if (!elementExists) {
                FormValueElement newElement = new FormValueElement();
                newElement.setElementName(nextElement);
                currentSubtree.addFormValueElement(nextElement, newElement);
                currentSubtree = newElement;
            }
        }

        return currentSubtree;
    }

    public List<FormValueElement> getFormFields() {
        return formFields;
    }

    public void setFormFields(List<FormValueElement> formFields) {
        this.formFields = formFields;
    }

    public Map<String, MetadataValue> getMetadata() {
        return metadata;
    }

    public void setMetadata(Map<String, MetadataValue> metadata) {
        this.metadata = metadata;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getUiversion() {
        return uiversion;
    }

    public void setUiversion(String uiversion) {
        this.uiversion = uiversion;
    }

    public String getXmlns() {
        return xmlns;
    }

    public void setXmlns(String xmlns) {
        this.xmlns = xmlns;
    }
}
