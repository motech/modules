package org.motechproject.commcare.parser;

import org.apache.commons.lang.StringUtils;
import org.apache.xerces.parsers.DOMParser;
import org.motechproject.commcare.domain.FormValueElement;
import org.motechproject.commcare.exception.FullFormParserException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.io.StringReader;

public class FullFormParser {
    private String xmlDoc;

    public static final String FORM_DATA_ELEMENT = "data";
    public static final String DEVICE_REPORT_ELEMENT = "device_report";
    public static final String XMLNS_ELEMENT = "xmlns";
    public static final String FORM = "form";

    public FullFormParser(String xmlDoc) {
        this.xmlDoc = xmlDoc;
    }

    /**
     * Method to parse incoming "full" XML forms from CommCareHQ.
     * Parser assumes the form has an element with the tag "data".
     * If the form does not, and instead has a device report, a null
     * form is returned. Otherwise an exception is thrown indicating
     * an unknown or faulty form XML.
     * @return The parsed form, null if a device report
     * @throws FullFormParserException Thrown if the form does not parse correctly and is not a device report form
     */
    public FormValueElement parse() throws FullFormParserException {
        DOMParser parser = new DOMParser();

        InputSource inputSource = new InputSource();
        inputSource.setCharacterStream(new StringReader(xmlDoc));
        FormValueElement root;

        try {
            parser.parse(inputSource);

            Document document = parser.getDocument();
            Node item = document.getElementsByTagName(FORM_DATA_ELEMENT).item(0);

            if (item == null && document.getElementsByTagName(DEVICE_REPORT_ELEMENT).item(0) != null) {
                return null;
            }

            root = new FormValueElement();
            root.setElementName(FORM);
            root.setValue(FORM_DATA_ELEMENT);
            addAttributes(root, item.getAttributes());
            addSubElements(root, item.getChildNodes());
        } catch (SAXException | IOException | NullPointerException ex) {
            throw new FullFormParserException(ex, "Exception while trying to parse formXml: " + xmlDoc);
        }

        return root;
    }

    private void addAttributes(FormValueElement element, NamedNodeMap attributes) {
        for (int i = 0; i < attributes.getLength(); ++i) {
            Node attr = attributes.item(i);
            String key = attr.getNodeName();

            if (key.startsWith(XMLNS_ELEMENT)) {
                key = key.substring(0, 5);
            }

            if (!element.containsAttribute(key)) {
                element.addAttribute(key, attr.getNodeValue());
            }
        }
    }

    private void addSubElements(FormValueElement element, NodeList children) {
        for (int i = 0; i < children.getLength(); ++i) {
            Node child = children.item(i);

            if (child.getNodeType() == Node.ELEMENT_NODE) {
                FormValueElement childElement = new FormValueElement();
                childElement.setElementName(child.getLocalName());

                String value = getTextValue((Element) child);

                if (StringUtils.isNotBlank(value)) {
                    childElement.setValue(value);
                } else {
                    addAttributes(childElement, child.getAttributes());
                    addSubElements(childElement, child.getChildNodes());
                }

                element.addFormValueElement(childElement.getElementName(), childElement);
            }
        }
    }

    private String getTextValue(Element ele) {
        Node textNode = ele.getFirstChild();
        String textVal = null;

        if (textNode != null) {
            textVal = textNode.getNodeValue();
        }

        return textVal;
    }
}
