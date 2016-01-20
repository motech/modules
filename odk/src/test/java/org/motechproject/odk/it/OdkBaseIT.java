package org.motechproject.odk.it;

import org.apache.commons.io.IOUtils;
import org.junit.After;
import org.junit.Before;
import org.motechproject.odk.constant.FieldTypeConstants;
import org.motechproject.odk.constant.OnaConstants;
import org.motechproject.odk.domain.Configuration;
import org.motechproject.odk.domain.ConfigurationType;
import org.motechproject.odk.domain.FormDefinition;
import org.motechproject.odk.domain.FormElement;
import org.motechproject.odk.domain.builder.FormElementBuilder;
import org.motechproject.odk.parser.XformParser;
import org.motechproject.odk.parser.impl.XformParserODK;
import org.motechproject.odk.repository.FormDefinitionDataService;
import org.motechproject.odk.repository.FormInstanceDataService;
import org.motechproject.odk.service.ConfigurationService;
import org.motechproject.testing.osgi.BasePaxIT;

import javax.inject.Inject;
import java.io.InputStream;
import java.util.List;

public abstract class OdkBaseIT extends BasePaxIT {

    @Inject
    private FormDefinitionDataService formDefinitionDataService;
    @Inject
    private FormInstanceDataService formInstanceDataService;
    @Inject
    private ConfigurationService configurationService;

    private Configuration configuration;
    private String json;

    protected static final String CONFIG_NAME = "ona";
    protected static final String TITLE = "ona_nested_repeats";



    @Before
    public void baseSetup() throws Exception {
        clearDb();
        configuration = new Configuration("url","motech","motech",CONFIG_NAME,ConfigurationType.ONA,true);
        configurationService.addOrUpdateConfiguration(configuration);
        String xml;

        try (InputStream in = getClass().getClassLoader().getResourceAsStream("/ona_nested_repeats.xml")) {
            xml = IOUtils.toString(in);
        }

        try (InputStream in = getClass().getClassLoader().getResourceAsStream("/ona_nested_repeats.json")) {
            json = IOUtils.toString(in);
        }


        XformParser parser = new XformParserODK();
        FormDefinition formDefinition = parser.parse(xml, configuration.getName());
        alterFormDef(formDefinition);
        formDefinitionDataService.create(formDefinition);
    }

    @After
    public void baseTeardown() {
        clearDb();
    }

    private void clearDb() {
        formDefinitionDataService.deleteAll();
        formInstanceDataService.deleteAll();
    }

    private void alterFormDef(FormDefinition formDefinition) {
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
        formElements.add(new FormElementBuilder().setName(OnaConstants.FORMHUB_UUID).setLabel(OnaConstants.FORMHUB_UUID).setType(FieldTypeConstants.STRING).createFormElement());
        formElements.add(new FormElementBuilder().setName(OnaConstants.SUBMISSION_TIME).setLabel(OnaConstants.SUBMISSION_TIME).setType(FieldTypeConstants.DATE_TIME).createFormElement());
        formElements.add(new FormElementBuilder().setName(OnaConstants.VERSION).setLabel(OnaConstants.VERSION).setType(FieldTypeConstants.STRING).createFormElement());
        formElements.add(new FormElementBuilder().setName(OnaConstants.GEOLOCATION).setLabel(OnaConstants.GEOLOCATION).setType(FieldTypeConstants.DOUBLE_ARRAY).createFormElement());
        formElements.add(new FormElementBuilder().setName(OnaConstants.SUBMITTED_BY).setLabel(OnaConstants.SUBMITTED_BY).setType(FieldTypeConstants.STRING).createFormElement());
    }

    private void alterFormElementName(FormElement formElement) {
        String formFieldName = formElement.getName();
        String name = formFieldName.substring(formFieldName.indexOf("/", 1) + 1, formFieldName.length()); // removes form title from URI
        formElement.setName(name);

        if (formElement.hasChildren()) {
            for (FormElement child : formElement.getChildren()) {
                alterFormElementName(child);
            }
        }
    }

    public FormDefinitionDataService getFormDefinitionDataService() {
        return formDefinitionDataService;
    }


    public FormInstanceDataService getFormInstanceDataService() {
        return formInstanceDataService;
    }


    public Configuration getConfiguration() {
        return configuration;
    }

    public String getJson() {
        return json;
    }
}
