package org.motechproject.odk.service.impl;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.osgi.services.HttpClientBuilderFactory;
import org.apache.http.util.EntityUtils;
import org.motechproject.odk.constant.FieldTypeConstants;
import org.motechproject.odk.constant.OnaConstants;
import org.motechproject.odk.domain.Configuration;
import org.motechproject.odk.domain.FormDefinition;
import org.motechproject.odk.domain.FormElement;
import org.motechproject.odk.domain.builder.FormElementBuilder;
import org.motechproject.odk.service.AbstractFormDefinitionImportService;
import org.motechproject.odk.service.FormDefinitionImportException;
import org.motechproject.odk.service.FormDefinitionImportService;
import org.motechproject.odk.service.FormDefinitionService;
import org.motechproject.odk.service.ParseUrlException;
import org.motechproject.odk.service.TasksService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.xml.SimpleNamespaceContext;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class FormDefinitionImportServiceOna extends AbstractFormDefinitionImportService implements FormDefinitionImportService {


    @Autowired
    public FormDefinitionImportServiceOna(HttpClientBuilderFactory httpClientBuilderFactory, TasksService tasksService, FormDefinitionService formDefinitionService) {
        super(httpClientBuilderFactory, tasksService, formDefinitionService);
    }

    @Override
    protected List<String> getFormUrls(Configuration configuration) throws FormDefinitionImportException {
        try {
            HttpGet request = new HttpGet(configuration.getUrl() + "/" + configuration.getUsername() + FORM_LIST_PATH);
            HttpResponse response = getClient().execute(request);
            String responseBody = EntityUtils.toString(response.getEntity());
            return parseToUrlList(responseBody);
        } catch (IOException|ParseUrlException e) {
            throw new FormDefinitionImportException(e);
        }

    }

    @Override
    protected void modifyFormDefinitionForImplementation(List<FormDefinition> formDefinitions) {
        for (FormDefinition formDefinition : formDefinitions) {
            List<FormElement> formElements = formDefinition.getFormElements();

            for (FormElement formElement : formElements) {
                if (!formElement.isPartOfRepeatGroup()) {
                    alterFormElementName(formElement);
                }
            }

            formElements.add(new FormElementBuilder().setName(OnaConstants.NOTES).setLabel(OnaConstants.NOTES).setType(FieldTypeConstants.STRING_ARRAY).createFormElement());
            formElements.add(new FormElementBuilder().setName(OnaConstants.TAGS).setLabel(OnaConstants.TAGS).setType(FieldTypeConstants.STRING_ARRAY).createFormElement());
            formElements.add(new FormElementBuilder().setName(OnaConstants.XFORM_ID_STRING).setLabel(OnaConstants.XFORM_ID_STRING).setType(FieldTypeConstants.STRING).createFormElement());
            formElements.add(new FormElementBuilder().setName(OnaConstants.UUID).setLabel(OnaConstants.UUID).setType(FieldTypeConstants.STRING).createFormElement());
            formElements.add(new FormElementBuilder().setName(OnaConstants.STATUS).setLabel(OnaConstants.STATUS).setType(FieldTypeConstants.STRING).createFormElement());
            formElements.add(new FormElementBuilder().setName(OnaConstants.SUBMISSION_TIME).setLabel(OnaConstants.SUBMISSION_TIME).setType(FieldTypeConstants.DATE_TIME).createFormElement());
            formElements.add(new FormElementBuilder().setName(OnaConstants.VERSION).setLabel(OnaConstants.VERSION).setType(FieldTypeConstants.STRING).createFormElement());
            formElements.add(new FormElementBuilder().setName(OnaConstants.GEOLOCATION).setLabel(OnaConstants.GEOLOCATION).setType(FieldTypeConstants.DOUBLE_ARRAY).createFormElement());
            formElements.add(new FormElementBuilder().setName(OnaConstants.SUBMITTED_BY).setLabel(OnaConstants.SUBMITTED_BY).setType(FieldTypeConstants.STRING).createFormElement());
        }
    }

    private void alterFormElementName(FormElement formElement) {
        String formFieldName = formElement.getName();
        String name = formFieldName.substring(formFieldName.indexOf('/', 1) + 1, formFieldName.length()); // removes form title from URI
        formElement.setName(name);

        if (formElement.hasChildren()) {
            for (FormElement child : formElement.getChildren()) {
                alterFormElementName(child);
            }
        }
    }

    protected List<String> parseToUrlList(String responseBody) throws ParseUrlException {
        try {
            XPathFactory xPathFactory = XPathFactory.newInstance();
            XPath xPath = xPathFactory.newXPath();
            SimpleNamespaceContext namespaces = new SimpleNamespaceContext();
            Map<String, String> namespaceMap = new HashMap<String, String>();
            namespaceMap.put("x", "http://openrosa.org/xforms/xformsList");
            namespaces.setBindings(namespaceMap);
            xPath.setNamespaceContext(namespaces);

            InputSource inputSource = new InputSource(new ByteArrayInputStream(responseBody.getBytes()));
            NodeList nodeList = (NodeList) xPath.compile("/x:xforms/x:xform/x:downloadUrl").evaluate(inputSource, XPathConstants.NODESET);
            List<String> urls = new ArrayList<>();

            for (int i = 0; i < nodeList.getLength(); i++) {
                Node node = nodeList.item(i).getFirstChild();
                urls.add(node.getNodeValue());
            }
            return urls;

        } catch (XPathExpressionException e) {
            throw new  ParseUrlException(e);
        }
    }
}
