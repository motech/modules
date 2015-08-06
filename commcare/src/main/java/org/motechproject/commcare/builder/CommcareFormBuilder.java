package org.motechproject.commcare.builder;

import com.google.common.collect.LinkedHashMultimap;
import com.google.common.collect.Multimap;
import org.motechproject.commcare.domain.CommcareForm;
import org.motechproject.commcare.domain.FormValueElement;
import org.motechproject.commcare.domain.MetadataValue;
import org.motechproject.commcare.events.constants.EventDataKeys;
import org.motechproject.event.MotechEvent;

import java.util.HashMap;
import java.util.Map;

/**
 * Can be used for creating instances of the {@link CommcareForm} class based on the parameters stored in the passed
 * event.
 */
public class CommcareFormBuilder {

    private static final String DEFAULT_ROOT_ELEMENT = "form";

    private static final String UIVERSION_ATTRIBUTE_NAME = "uiVersion";
    private static final String VERSION_ATTRIBUTE_NAME = "version";
    private static final String INSTANCE_ID_ATTRIBUTE_NAME = "instanceID";

    /**
     * Creates an instance of the {@link CommcareForm} class based on the parameters stored in the passed event.
     *
     * @param motechEvent  the event storing form information
     * @return the instance of the {@link CommcareForm} built based on the parameters stored in the passed event
     */
    public CommcareForm buildFrom(MotechEvent motechEvent) {
        FormValueElement rootElement = toFormValueElement(motechEvent.getParameters());
        fixRootElementName(rootElement);

        CommcareForm form = new CommcareForm();
        form.setForm(rootElement);
        populateFormFields(rootElement, form, (String) motechEvent.getParameters().get(EventDataKeys.RECEIVED_ON));
        return form;
    }

    private void populateFormFields(FormValueElement rootElement, CommcareForm form, String receivedOn) {
        Map<String, MetadataValue> meta = getFormMeta(rootElement);
        form.setMetadata(meta);
        form.setUiversion(rootElement.getAttributes().get(UIVERSION_ATTRIBUTE_NAME));
        form.setVersion(rootElement.getAttributes().get(VERSION_ATTRIBUTE_NAME));
        form.setReceivedOn(receivedOn);

        MetadataValue idMeta = meta.get(INSTANCE_ID_ATTRIBUTE_NAME);
        form.setId(idMeta == null ? null : idMeta.firstValue());
    }

    private void fixRootElementName(FormValueElement rootElement) {
        if (rootElement.getElementName() ==  null) {
            rootElement.setElementName(DEFAULT_ROOT_ELEMENT);
        }
    }

    private Multimap<String, FormValueElement> toFormValueElements(Multimap<String, Map<String, Object>> elements) {
        if (elements == null) {
            return LinkedHashMultimap.create();
        }

        LinkedHashMultimap<String, FormValueElement> formValueElements = LinkedHashMultimap.create();

        for (Map.Entry<String, Map<String, Object>> entry: elements.entries()) {
            formValueElements.put(entry.getKey(), toFormValueElement(entry.getValue()));
        }

        return formValueElements;
    }

    private FormValueElement toFormValueElement(Map<String, Object> element) {
        FormValueElement formValueElement = new FormValueElement();

        formValueElement.setElementName((String) element.get(EventDataKeys.ELEMENT_NAME));
        formValueElement.setValue((String) element.get(EventDataKeys.VALUE));

        Map<String, String> attributes = (Map<String, String>) element.get(EventDataKeys.ATTRIBUTES);
        if (attributes != null) {
            formValueElement.setAttributes(attributes);
        }
        formValueElement.setSubElements(toFormValueElements((Multimap<String, Map<String, Object>>) element.get(EventDataKeys.SUB_ELEMENTS)));

        return formValueElement;
    }

    private Map<String, MetadataValue> getFormMeta(FormValueElement rootElement) {
        Map<String, MetadataValue> formMeta = new HashMap<>();
        FormValueElement metaElement = rootElement.getChildElement("meta");

        if (metaElement != null) {
            for (Map.Entry<String, FormValueElement> metaEntry : metaElement.getSubElements().entries()) {
                formMeta.put(metaEntry.getKey(), new MetadataValue(metaEntry.getValue().getValue()));
            }
        }

        return formMeta;
    }
}
