package org.motechproject.commcare.service.impl;

import com.google.common.reflect.TypeToken;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.motechproject.commcare.config.Config;
import org.motechproject.commcare.domain.CommcareApplicationJson;
import org.motechproject.commcare.service.CommcareApplicationDataService;
import org.motechproject.commcare.tasks.builder.model.CaseTypeWithApplicationName;
import org.motechproject.commcare.util.ConfigsUtils;
import org.motechproject.commons.api.json.MotechJsonReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import static junit.framework.Assert.assertNotNull;
import static org.junit.Assert.assertFalse;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class CommcareSchemaServiceImplTest {
    private static final Logger LOGGER = LoggerFactory.getLogger(CommcareCaseServiceImplTest.class);
    private static final String SINGLE_APPLICATION_JSON = "json/service/single-application.json";

    private MotechJsonReader motechJsonReader = new MotechJsonReader();
    private Config config;

    @Mock
    private CommcareApplicationDataService commcareApplicationDataService;

    @InjectMocks
    private CommcareSchemaServiceImpl schemaService;


    @Before
    public void setUp() {
        config = ConfigsUtils.prepareConfigOne();

        schemaService = new CommcareSchemaServiceImpl();
        initMocks(this);
    }

    @Test
    public void shouldCreateCaseOmmitingParentProperties() throws IOException {
        when(commcareApplicationDataService.bySourceConfiguration(config.getName())).thenReturn(readApplicationFromFile(SINGLE_APPLICATION_JSON));

        Set<CaseTypeWithApplicationName> caseTypeLists = schemaService.getCaseTypesWithApplicationName(config.getName());

        Iterator iterator = caseTypeLists.iterator();
        CaseTypeWithApplicationName firstCase = (CaseTypeWithApplicationName) iterator.next();

        assertNotNull(firstCase);
        assertFalse(firstCase.getCaseProperties().contains("parent"));
    }

    private List<CommcareApplicationJson> readApplicationFromFile(String fileName) throws IOException {
        List<CommcareApplicationJson> result = new ArrayList<>();

        try (InputStream in = getClass().getClassLoader().getResourceAsStream(fileName)) {
            Type applicationJsonType = new TypeToken<CommcareApplicationJson>() {}.getType();
            result.add((CommcareApplicationJson) motechJsonReader.readFromStreamOnlyExposeAnnotations(in, applicationJsonType));

        } catch (IOException e) {
            LOGGER.error(e.getMessage(), e);
        }

        return result;
    }
}
