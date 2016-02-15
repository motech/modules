package org.motechproject.commcare.request.converter;

import com.thoughtworks.xstream.converters.Converter;
import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;
import org.apache.commons.lang.StringUtils;
import org.motechproject.commcare.domain.FormValueElement;
import org.motechproject.commcare.domain.FormXml;
import org.motechproject.commcare.domain.MetadataValue;
import org.motechproject.commcare.exception.MalformedFormXmlException;

import java.util.Map;

/**
 * This class dictates the rules for parsing {@link FormXml} instances into XML. It implements
 * XStream {@link Converter} and currently only supports marshalling objects to XML. Unmarshalling
 * always returns null.
 */
public class FormElementConverter implements Converter {

    private static final String XMLNS_JRM_KEY = "xmlns:jrm";
    private static final String XMLNS_JRM_VALUE = "http://dev.commcarehq.org/jr/xforms";

    private static final String NAME = "name";
    private static final String UI_VERSION = "uiVersion";
    private static final String VERSION = "version";
    private static final String XMLNS = "xmlns";

    private static final String METADATA_NODE = "n0:meta";
    private static final String METADATA_XMLNS_KEY = "xmlns:n0";
    private static final String METADATA_XMLNS_VALUE = "http://openrosa.org/jr/xforms";
    private static final String METADATA_PREFIX = "n0:";

    private static final String METADATA_APP_VERSION = "n1:appVersion";
    private static final String METADATA_XMLNS = "xmlns:n1";
    private static final String METADATA_XFORMS = "http://commcarehq.org/xforms";


    @Override
    public void marshal(Object o, HierarchicalStreamWriter writer, MarshallingContext marshallingContext) {
        FormXml formXml = (FormXml) o;

        addAttrIfNotEmpty(writer, NAME, formXml.getName());
        addAttrIfNotEmpty(writer, UI_VERSION, formXml.getUiversion());
        addAttrIfNotEmpty(writer, VERSION, formXml.getVersion());
        addAttrIfNotEmpty(writer, XMLNS, formXml.getXmlns());
        addAttrIfNotEmpty(writer, XMLNS_JRM_KEY, XMLNS_JRM_VALUE);

        if (formXml.getFormFields() != null && !formXml.getFormFields().isEmpty()) {
            for (FormValueElement formField : formXml.getFormFields()) {
                marshalFormValues(formField, writer);
            }
        }

        if (!formXml.getMetadata().isEmpty()) {
            marshalFormMetadata(formXml.getMetadata(), writer);
        }
    }

    @Override
    public Object unmarshal(HierarchicalStreamReader hierarchicalStreamReader, UnmarshallingContext unmarshallingContext) {
        return null;
    }

    @Override
    public boolean canConvert(Class clazz) {
        return clazz.equals(FormXml.class);
    }

    private void marshalFormMetadata(Map<String, MetadataValue> metadata, HierarchicalStreamWriter writer) {
        writer.startNode(METADATA_NODE);
        writer.addAttribute(METADATA_XMLNS_KEY, METADATA_XMLNS_VALUE);

        for (Map.Entry<String, MetadataValue> entry : metadata.entrySet()) {
            for (String metadataValue : entry.getValue().getValues()) {
                writer.startNode(METADATA_PREFIX + entry.getKey());
                writer.setValue(metadataValue);
                writer.endNode();
            }
        }

        writer.startNode(METADATA_APP_VERSION);
        writer.addAttribute(METADATA_XMLNS, METADATA_XFORMS);
        writer.setValue("2.0");
        writer.endNode();

        writer.endNode();
    }

    private void marshalFormValues(FormValueElement form, HierarchicalStreamWriter writer) {
        if (StringUtils.isBlank(form.getElementName())) {
            throw new MalformedFormXmlException("The parsing of a form has failed. Element name is required in Form Elements.");
        }

        // Starts the element tag... <tag>
        writer.startNode(form.getElementName());

        // Adds element attributes... <tag attribute="value">
        for (Map.Entry<String, String> entry : form.getAttributes().entrySet()) {
            writer.addAttribute(entry.getKey(), entry.getValue());
        }

        // Calls the same method recursively for child elements... <tag><child_tag></child_tag>
        for (Map.Entry<String, FormValueElement> entry : form.getSubElements().entries()) {
            marshalFormValues(entry.getValue(), writer);
        }

        // Sets plain element value, if there's any... <tag>value</tag>
        if (StringUtils.isNotBlank(form.getValue())) {
            writer.setValue(form.getValue());
        }

        // Closes element tag... </tag>
        writer.endNode();
    }

    private void addAttrIfNotEmpty(HierarchicalStreamWriter writer, String attrName, String attrValue) {
        if (StringUtils.isNotBlank(attrValue)) {
            writer.addAttribute(attrName, attrValue);
        }
    }
}
