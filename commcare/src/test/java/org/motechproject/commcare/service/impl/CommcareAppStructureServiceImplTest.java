package org.motechproject.commcare.service.impl;

import org.apache.commons.io.FileUtils;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.motechproject.commcare.client.CommCareAPIHttpClient;
import org.motechproject.commcare.domain.CommcareApplicationJson;
import org.motechproject.commcare.domain.CommcareModuleJson;
import org.motechproject.commcare.domain.FormSchemaJson;
import org.motechproject.commcare.domain.FormSchemaQuestionJson;
import org.motechproject.commcare.domain.FormSchemaQuestionOptionJson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class CommcareAppStructureServiceImplTest {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    private CommcareAppStructureServiceImpl appStructureService;

    @Mock
    private CommCareAPIHttpClient commcareHttpClient;

    @Before
    public void setUp() {
        initMocks(this);
        appStructureService = new CommcareAppStructureServiceImpl(commcareHttpClient);
    }

    @Test
    public void testAllApplications() {

        when(commcareHttpClient.appStructureRequest()).thenReturn(appStructureResponse());

        List<CommcareApplicationJson> applications = appStructureService.getAllApplications();
        assertTrue(!applications.isEmpty());

        List<CommcareModuleJson> modules = applications.get(0).getModules();
        assertTrue(!modules.isEmpty());

        List<String> caseProperties = modules.get(0).getCaseProperties();
        assertEquals(2, caseProperties.size());

        List<FormSchemaJson> formSchemas = modules.get(0).getFormSchemas();
        assertTrue(!formSchemas.isEmpty());

        Map<String, String> formNames = formSchemas.get(0).getFormNames();
        assertTrue(!formNames.isEmpty());

        List<FormSchemaQuestionJson> questions = formSchemas.get(0).getQuestions();
        assertTrue(!questions.isEmpty());

        List<FormSchemaQuestionOptionJson> options = questions.get(0).getOptions();
        assertEquals(2, options.size());

        assertEquals("name", caseProperties.get(0));
        assertEquals("user_bednet", caseProperties.get(1));
        assertEquals("myCase1", modules.get(0).getCaseType());
        assertEquals("myForm1", formNames.get("en"));
        assertEquals("questionName", questions.get(0).getQuestionLabel());
        assertEquals("", questions.get(0).getQuestionRepeat());
        assertEquals("input", questions.get(0).getQuestionTag());
        assertEquals("/data/name", questions.get(0).getQuestionValue());
        assertEquals("1. Yes", options.get(0).getLabel());
        assertEquals("yes", options.get(0).getValue());
        assertEquals("2. No", options.get(1).getLabel());
        assertEquals("no", options.get(1).getValue());
        assertEquals("myApplication", applications.get(0).getApplicationName());
        assertEquals("", applications.get(0).getResourceUri());
    }

    private String appStructureResponse() {
        try {
            URL url = this.getClass().getClassLoader().getResource("json/service/appStructure.json");
            return FileUtils.readFileToString(new File(url.getFile()));
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
            return null;
        }
    }
}
