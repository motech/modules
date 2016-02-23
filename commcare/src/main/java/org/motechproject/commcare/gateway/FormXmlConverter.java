package org.motechproject.commcare.gateway;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.naming.NoNameCoder;
import com.thoughtworks.xstream.io.xml.DomDriver;
import org.motechproject.commcare.domain.FormXml;
import org.motechproject.commcare.request.converter.FormElementConverter;
import org.springframework.stereotype.Component;

/**
 * Converts instances of {@link FormXml} to XML representation, returned as {@link String}. The conversion
 * rules are dictated by {@link FormElementConverter}.
 */
@Component
public class FormXmlConverter {

    private static final String XML_START_TAG = "<?xml version='1.0' ?>\n";

    public String convertToFormXml(FormXml form) {
        XStream xstream = new XStream(new DomDriver("UTF-8", new NoNameCoder()));

        xstream.registerConverter(new FormElementConverter());
        xstream.alias("data", FormXml.class);

        return XML_START_TAG + xstream.toXML(form);
    }
}
