package org.motechproject.odk.service.impl;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.osgi.services.HttpClientBuilderFactory;
import org.apache.http.util.EntityUtils;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.JavaType;
import org.motechproject.odk.constant.FieldTypeConstants;
import org.motechproject.odk.constant.KoboConstants;
import org.motechproject.odk.constant.OnaConstants;
import org.motechproject.odk.domain.Configuration;
import org.motechproject.odk.domain.FormDefinition;
import org.motechproject.odk.domain.FormElement;
import org.motechproject.odk.domain.KoboFormInfo;
import org.motechproject.odk.domain.builder.FormElementBuilder;
import org.motechproject.odk.service.AbstractFormDefinitionImportService;
import org.motechproject.odk.service.FormDefinitionImportException;
import org.motechproject.odk.service.FormDefinitionImportService;
import org.motechproject.odk.service.FormDefinitionService;
import org.motechproject.odk.service.ParseUrlException;
import org.motechproject.odk.service.TasksService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class FormDefinitionImportServiceKobo extends AbstractFormDefinitionImportService implements FormDefinitionImportService {

    private static final String API_PATH = "/api/v1";
    private static final String FORMS_PATH = "/forms";
    private static final String OWNER_QUERY = "?owner=";
    private static final String XML_MEDIA_PATH = "/form.xml";
    private ObjectMapper objectMapper;
    private JavaType type;


    @Autowired
    public FormDefinitionImportServiceKobo(HttpClientBuilderFactory httpClientBuilderFactory, TasksService tasksService, FormDefinitionService formDefinitionService) {
        super(httpClientBuilderFactory, tasksService, formDefinitionService);
        objectMapper = new ObjectMapper();
        type = objectMapper.getTypeFactory().constructCollectionType(List.class, KoboFormInfo.class);
    }

    @Override
    protected List<String> getFormUrls(Configuration configuration) throws FormDefinitionImportException {
        try {
            HttpGet request = new HttpGet(buildFormsQuery(configuration));
            request.addHeader(generateBasicAuthHeader(request, configuration));
            HttpResponse response = getClient().execute(request);
            String responseBody = EntityUtils.toString(response.getEntity());
            return parseToUrlList(responseBody);
        } catch (IOException|ParseUrlException e) {
            throw new FormDefinitionImportException(e);
        }

    }

    @Override
    protected List<String> getXmlFormDefinitions(List<String> formUrls, Configuration configuration) throws IOException {
        List<String> formDefinitions = new ArrayList<>();

        for (String url : formUrls) {
            HttpGet request = new HttpGet(url + XML_MEDIA_PATH);
            request.addHeader(generateBasicAuthHeader(request, configuration));
            HttpResponse response = getClient().execute(request);
            String formDefinition = EntityUtils.toString(response.getEntity());
            formDefinitions.add(normalizeFormDef(formDefinition));
        }
        return formDefinitions;
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
            formElements.add(new FormElementBuilder().setName(OnaConstants.NOTES).setLabel(OnaConstants.NOTES).setType(FieldTypeConstants.STRING).createFormElement());
            formElements.add(new FormElementBuilder().setName(OnaConstants.UUID).setLabel(OnaConstants.UUID).setType(FieldTypeConstants.STRING).createFormElement());
            formElements.add(new FormElementBuilder().setName(KoboConstants.BAMBOO_DATASET_ID).setLabel(KoboConstants.BAMBOO_DATASET_ID).setType(FieldTypeConstants.STRING).createFormElement());
            formElements.add(new FormElementBuilder().setName(OnaConstants.TAGS).setLabel(OnaConstants.TAGS).setType(FieldTypeConstants.STRING_ARRAY).createFormElement());
            formElements.add(new FormElementBuilder().setName(OnaConstants.SUBMITTED_BY).setLabel(OnaConstants.SUBMITTED_BY).setType(FieldTypeConstants.STRING).createFormElement());
            formElements.add(new FormElementBuilder().setName(OnaConstants.XFORM_ID_STRING).setLabel(OnaConstants.XFORM_ID_STRING).setType(FieldTypeConstants.STRING).createFormElement());
            formElements.add(new FormElementBuilder().setName(OnaConstants.SUBMISSION_TIME).setLabel(OnaConstants.SUBMISSION_TIME).setType(FieldTypeConstants.DATE_TIME).createFormElement());
            formElements.add(new FormElementBuilder().setName(OnaConstants.GEOLOCATION).setLabel(OnaConstants.GEOLOCATION).setType(FieldTypeConstants.DOUBLE_ARRAY).createFormElement());
            formElements.add(new FormElementBuilder().setName(KoboConstants.USERFORM_ID).setLabel(KoboConstants.USERFORM_ID).setType(FieldTypeConstants.STRING).createFormElement());
            formElements.add(new FormElementBuilder().setName(OnaConstants.STATUS).setLabel(OnaConstants.STATUS).setType(FieldTypeConstants.STRING).createFormElement());
            formElements.add(new FormElementBuilder().setName(OnaConstants.ID).setLabel(OnaConstants.ID).setType(FieldTypeConstants.INT).createFormElement());
        }
    }

    private void alterFormElementName(FormElement formElement) {
        String formFieldName = formElement.getName();
        String name = formFieldName.substring(formFieldName.indexOf('/', 1) + 1, formFieldName.length());
        formElement.setName(name);

        if (formElement.hasChildren()) {
            for (FormElement child : formElement.getChildren()) {
                alterFormElementName(child);
            }
        }
    }

    @Override
    protected List<String> parseToUrlList(String responseBody) throws ParseUrlException {
        try {
            List<KoboFormInfo> koboFormInfoList = objectMapper.readValue(responseBody, type);
            List<String> urls = new ArrayList<>();

            for (KoboFormInfo koboFormInfo : koboFormInfoList) {
                urls.add(koboFormInfo.getUrl());
            }
            return urls;
        } catch (IOException e) {
            throw new ParseUrlException(e);
        }

    }

    private String buildFormsQuery(Configuration configuration) {
        return configuration.getUrl() + API_PATH + FORMS_PATH + OWNER_QUERY + configuration.getUsername();
    }

    private String normalizeFormDef(String formDef) {
        if (formDef.startsWith("<?xml version=\"1.0\" encoding=\"utf-8\"?>\n" +
                "<root>&lt;?xml version=\"1.0\" encoding=\"utf-8\"?&gt;")) {
            return formDef.replace("&lt;", "<")
                    .replace("&gt;", ">")
                    .replace("<root><?xml version=\"1.0\" encoding=\"utf-8\"?>\n", "")
                    .replace("\n</root>", "")
                    .replace("<?xml version=\"1.0\" encoding=\"utf-8\"?>\n", "");
        } else {
            return formDef;
        }


    }


}
