package org.motechproject.odk.parser.impl;

import org.motechproject.odk.constant.FieldTypeConstants;
import org.motechproject.odk.domain.FormDefinition;
import org.motechproject.odk.domain.FormElement;
import org.motechproject.odk.domain.builder.FormElementBuilder;
import org.motechproject.odk.util.FormUtils;
import org.motechproject.odk.parser.XformParser;
import org.springframework.util.xml.SimpleNamespaceContext;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * ODK implementation of {@link XformParser}. This is also used for Ona.io XForms.
 */
public class XformParserODK implements XformParser {

    protected static final String TITLE_PATH = "/h:html/h:head/h:title";
    protected static final String BIND_ELEMENTS = "/h:html/h:head/xForms:model/xForms:bind";
    protected static final String FORM_ELEMENTS_PARENT_PATH = "/h:html/h:head/xForms:model/xForms:instance/*[@id]";
    protected static final String ROOT_PATH = "/";
    protected static final String NODE_SET = "nodeset";
    protected static final String TYPE = "type";
    protected static final String STRING = "string";
    protected static final String SLASH = "/";
    private static final Map<String, String> NAMESPACE_MAP = new HashMap<String, String>() { {
        put("xForms", "http://www.w3.org/2002/xforms");
        put("ev", "http://www.w3.org/2001/xml-events");
        put("h", "http://www.w3.org/1999/xhtml");
        put("jr", "http://openrosa.org/javarosa");
        put("orx", "http://openrosa.org/xforms");
        put("xsd", "http://www.w3.org/2001/XMLSchema");
    } };

    private XPath xPath;

    public XformParserODK() {
        XPathFactory xPathFactory = XPathFactory.newInstance();
        this.xPath = xPathFactory.newXPath();
        SimpleNamespaceContext namespaces = new SimpleNamespaceContext();
        namespaces.setBindings(NAMESPACE_MAP);
        this.xPath.setNamespaceContext(namespaces);
    }

    public FormDefinition parse(String xForm, String configurationName) throws XPathExpressionException {
        InputSource inputSource = new InputSource(new ByteArrayInputStream(xForm.getBytes()));
        Node root = getRoot(inputSource);
        FormDefinition formDefinition = parseXForm(configurationName, root);
        formDefinition.setXform(xForm);
        return formDefinition;
    }

    public XPath getxPath() {
        return xPath;
    }

    protected FormDefinition parseXForm(String configurationName, Node root) throws XPathExpressionException {
        Map<String, FormElement> formElementMap = new HashMap<>();
        String title = xPath.compile(TITLE_PATH).evaluate(root);
        FormDefinition formDefinition = new FormDefinition(configurationName);
        formDefinition.setTitle(title);

        Node node = (Node) xPath.compile(FORM_ELEMENTS_PARENT_PATH).evaluate(root, XPathConstants.NODE);
        String uri = node.getNodeName();
        NodeList formElementsList = node.getChildNodes();

        recursivelyAddFormElements(formElementMap, formElementsList, SLASH + uri);
        NodeList binds = (NodeList) xPath.compile(BIND_ELEMENTS).evaluate(root, XPathConstants.NODESET);
        addBindInformationToFormFields(formElementMap, binds);
        removeFormNameFromLabel(formElementMap);
        formDefinition.setFormElements(createFormElementListFromMap(formElementMap));
        return formDefinition;
    }

    protected Node getRoot(InputSource inputSource) throws XPathExpressionException {
        return (Node) xPath.evaluate(ROOT_PATH, inputSource, XPathConstants.NODE);
    }


    private void addBindInformationToFormFields(Map<String, FormElement> formElementMap, NodeList binds) {
        for (int i = 0; i < binds.getLength(); i++) {
            Node bind = binds.item(i);
            NamedNodeMap attributes = bind.getAttributes();
            String fieldUri = attributes.getNamedItem(NODE_SET).getNodeValue();
            FormElement formElement = formElementMap.get(fieldUri);
            Node typeNode = attributes.getNamedItem(TYPE);

            if (formElement != null) {
                String type;
                if (typeNode == null) {
                    type = STRING;
                } else {
                    type = typeNode.getNodeValue();
                }
                formElement.setType(type);
            }
        }
    }

    private void recursivelyAddFormElements(Map<String, FormElement> formElementMap, NodeList nodeList, String uri) {
        for (int i = 0; i < nodeList.getLength(); i++) {
            Node element = nodeList.item(i);
            if (hasChildElements(element)) {

                if (element.hasAttributes()) { // repeat group
                    String localUri = uri + SLASH + element.getNodeName();
                    FormElement formElement = new FormElementBuilder()
                            .setName(localUri)
                            .setLabel(localUri)
                            .setType(FieldTypeConstants.REPEAT_GROUP)
                            .setChildren(new ArrayList<FormElement>())
                            .createFormElement();
                    formElementMap.put(formElement.getName(), formElement);
                    recursivelyAddGroup(formElementMap, element.getChildNodes(), uri + SLASH + element.getNodeName(), formElement.getChildren(), formElement);
                } else {
                    recursivelyAddFormElements(formElementMap, element.getChildNodes(), uri + SLASH + element.getNodeName());

                }
            } else if (element.getNodeType() == Node.ELEMENT_NODE) {
                String localUri = uri + SLASH + element.getNodeName();
                FormElement formElement = new FormElementBuilder()
                        .setName(localUri)
                        .setLabel(localUri)
                        .createFormElement();
                formElementMap.put(formElement.getName(), formElement);
            }
        }
    }

    private void recursivelyAddGroup(Map<String, FormElement> formElementMap, NodeList nodeList, String uri, List<FormElement> group, FormElement parent) {
        for (int i = 0; i < nodeList.getLength(); i++) {
            Node element = nodeList.item(i);

            if (element.hasChildNodes()) {

                if (element.hasAttributes()) {
                    String localUri = uri + SLASH + element.getNodeName();
                    FormElement formElement = new FormElementBuilder()
                            .setName(localUri)
                            .setLabel(localUri)
                            .setChildren(new ArrayList<FormElement>())
                            .setType(FieldTypeConstants.REPEAT_GROUP)
                            .setPartOfRepeatGroup(parent.isRepeatGroup() || parent.isPartOfRepeatGroup())
                            .createFormElement();
                    formElementMap.put(formElement.getName(), formElement);
                    group.add(formElement);
                    recursivelyAddGroup(formElementMap, element.getChildNodes(), uri + SLASH + element.getNodeName(), formElement.getChildren(), formElement);

                } else {
                    recursivelyAddFormElements(formElementMap, element.getChildNodes(), uri + SLASH + element.getNodeName());
                }

            } else if (element.getNodeType() == Node.ELEMENT_NODE) {
                String localUri = uri + SLASH + element.getNodeName();
                FormElement formElement = new FormElementBuilder()
                        .setName(localUri)
                        .setLabel(localUri)
                        .setPartOfRepeatGroup(parent.isRepeatGroup() || parent.isPartOfRepeatGroup())
                        .createFormElement();
                group.add(formElement);
                formElementMap.put(formElement.getName(), formElement);
            }
        }

    }

    protected boolean hasChildElements(Node element) {
        NodeList children = element.getChildNodes();
        for (int i = 0; i < children.getLength(); i++) {
            if (children.item(i).getNodeType() == Node.ELEMENT_NODE) {
                return true;
            }
        }
        return false;
    }

    private List<FormElement> createFormElementListFromMap(Map<String, FormElement> formElementMap) {
        List<FormElement> formElements = new ArrayList<>();
        for (FormElement formElement : formElementMap.values()) {
            if (formElement.getType().equals(FieldTypeConstants.REPEAT_GROUP) || !formElement.isPartOfRepeatGroup()) {
                formElements.add(formElement);
            }
        }
        return formElements;
    }

    private void removeFormNameFromLabel(Map<String, FormElement> formElementMap) {
        for (String key : formElementMap.keySet()) {
            FormElement element = formElementMap.get(key);
            String label = FormUtils.removeFormNameFromLabel(element.getLabel());
            element.setLabel(label);
        }
    }
}


