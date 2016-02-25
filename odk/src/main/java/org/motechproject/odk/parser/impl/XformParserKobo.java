package org.motechproject.odk.parser.impl;

import org.motechproject.odk.domain.FormDefinition;
import org.motechproject.odk.domain.FormElement;
import org.motechproject.odk.util.FormUtils;
import org.motechproject.odk.parser.XformParser;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import java.io.ByteArrayInputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Kobotoolbox implementation of {@link XformParser}
 */
public class XformParserKobo extends XformParserODK implements XformParser {

    private static final String GROUPS_PATH = "/h:html/h:body/xForms:group";
    private static final String REF = "ref";
    private static final String LABEL = "label";
    private static final String REPEAT = "repeat";

    public XformParserKobo() {
        super();
    }

    @Override
    public FormDefinition parse(String xForm, String configurationName) throws XPathExpressionException {
        InputSource inputSource = new InputSource(new ByteArrayInputStream(xForm.getBytes()));
        Node root = getRoot(inputSource);
        FormDefinition formDefinition = parseXForm(configurationName, root);
        formDefinition.setXform(xForm);
        findGroupLabels(formDefinition, root);
        return formDefinition;

    }

    private void findGroupLabels(FormDefinition formDefinition, Node root) throws XPathExpressionException {
        Map<String, FormElement> formElementMap = listToMap(formDefinition.getFormElements());
        NodeList groups = (NodeList) getxPath().compile(GROUPS_PATH).evaluate(root, XPathConstants.NODESET);
        recursivelyFindGroupRefs(groups, formElementMap, "/");
    }

    private void recursivelyFindGroupRefs(NodeList groups, Map<String, FormElement> formElementMap, String uri) {
        for (int i = 0; i < groups.getLength(); i++) {
            Node element = groups.item(i);

            if (element.getNodeType() == Node.ELEMENT_NODE) {
                if (!element.getNodeName().equals(REPEAT)) {

                    Node refAttribute = element.getAttributes().getNamedItem(REF);

                    String label = getLabel(element);
                    String localUri = uri + label;

                    if (refAttribute != null) {
                        String ref = refAttribute.getNodeValue();
                        FormElement formElement = formElementMap.get(ref);

                        if (formElement != null) {
                            formElement.setLabel(localUri);
                            setLabelForFormElementChildren(formElement);
                        }
                    }

                    if (element.hasChildNodes()) {
                        recursivelyFindGroupRefs(element.getChildNodes(), formElementMap, localUri + "/");
                    }
                } else  {
                    recursivelyFindGroupRefs(element.getChildNodes(), formElementMap, uri);
                }
            }
        }
    }

    private Map<String, FormElement> listToMap(List<FormElement> formElements) {
        Map<String, FormElement> formElementMap = new HashMap<>();
        for (FormElement formElement : formElements) {
            formElementMap.put(formElement.getName(), formElement);
        }
        return formElementMap;
    }

    private String getLabel(Node element) {
        NodeList children = element.getChildNodes();

        for (int i = 0; i < children.getLength(); i++) {
            Node child = children.item(i);

            if (child.getNodeType() == Node.ELEMENT_NODE && child.getNodeName().equals(LABEL)) {
                return child.getTextContent();
            }
        }
        return null;
    }

    private void setLabelForFormElementChildren(FormElement formElement) {
        if (formElement.getChildren() != null) {
            for (FormElement child : formElement.getChildren()) {
                String childLabel = FormUtils.createChildLabelFromParentLabel(formElement.getLabel(), child.getName());
                child.setLabel(childLabel);
            }
        }
    }
}
