package org.motechproject.commcare.gateway;

import org.apache.commons.io.IOUtils;
import org.junit.Test;
import org.motechproject.commcare.domain.FormValueElement;
import org.motechproject.commcare.domain.FormXml;
import org.motechproject.commcare.domain.MetadataValue;
import org.motechproject.commcare.exception.MalformedFormXmlException;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static org.custommonkey.xmlunit.XMLAssert.assertXMLEqual;
import static org.junit.Assert.assertNotNull;

public class FormXmlConverterTest {

    private FormXmlConverter formXmlConverter = new FormXmlConverter();

    @Test
    public void shouldSerializeCommcareFormToXml() throws IOException, SAXException {
        FormXml form = new FormXml();
        form.setName("MCH Registration");
        form.setUiversion("1");
        form.setVersion("2");

        FormValueElement registeredElement = new FormValueElement();
        registeredElement.setElementName("registered");
        registeredElement.setValue("yes");

        FormValueElement dobElement = new FormValueElement();
        dobElement.setElementName("dob");
        dobElement.setValue("2016-02-25");

        FormValueElement childElement = new FormValueElement();
        childElement.setElementName("child");
        childElement.addAttribute("name", "John");
        childElement.addFormValueElement("registered", registeredElement);
        childElement.addFormValueElement("dob", dobElement);

        FormValueElement childElement2 = new FormValueElement();
        childElement2.setElementName("child");
        childElement2.addAttribute("name", "Mark");
        childElement2.addFormValueElement("registered", registeredElement);
        childElement2.addFormValueElement("dob", dobElement);

        FormValueElement motherElement = new FormValueElement();
        motherElement.setElementName("mother");
        motherElement.addAttribute("name", "Jane");
        motherElement.addFormValueElement("child", childElement);
        motherElement.addFormValueElement("child", childElement2);

        Map<String, MetadataValue> metadata = new HashMap<>();
        metadata.put("start_date", new MetadataValue(Arrays.asList("2016-01-01")));

        form.setForm(motherElement);
        form.setMetadata(metadata);

        String formXml = formXmlConverter.convertToFormXml(form);

        assertNotNull(formXml);
        assertXMLEqual(loadSampleFormXml(), formXml);
    }

    @Test(expected = MalformedFormXmlException.class)
    public void shouldThrowExceptionWhenParsingFormWithEmptyElementName() {
        FormXml form = new FormXml();
        form.setName("MCH Registration");
        form.setUiversion("1");
        form.setVersion("2");

        FormValueElement motherElement = new FormValueElement();
        motherElement.setElementName("");
        motherElement.addAttribute("name", "Jane");

        form.setForm(motherElement);

        formXmlConverter.convertToFormXml(form);
    }

    private String loadSampleFormXml() throws IOException {
        try (InputStream in = getClass().getClassLoader().getResourceAsStream("xml/sample_form_xml.xml")) {
            return IOUtils.toString(in);
        }
    }
}
