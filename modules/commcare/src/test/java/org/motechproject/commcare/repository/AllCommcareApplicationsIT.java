package org.motechproject.commcare.repository;

import com.google.gson.reflect.TypeToken;
import org.ektorp.CouchDbConnector;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.motechproject.commcare.domain.AppStructureResponseJson;
import org.motechproject.commcare.domain.CommcareApplication;
import org.motechproject.commcare.domain.CommcareApplicationJson;
import org.motechproject.commcare.domain.CommcareModuleJson;
import org.motechproject.commons.api.json.MotechJsonReader;
import org.motechproject.testing.utils.SpringIntegrationTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.util.List;

import static junit.framework.Assert.assertEquals;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath*:/META-INF/motech/*.xml")
public class AllCommcareApplicationsIT extends SpringIntegrationTest {

    private static final String APPLICATION_NAME = "myApplication";
    private static final String RESOURCE_URI = "";
    private static final String CASE_TYPE = "myCase1";
    private static final String CASE_PROPERTY_1 = "name";
    private static final String CASE_PROPERTY_2 = "user_bednet";

    @Autowired
    AllCommcareApplications allCommcareApplications;

    @Autowired
    @Qualifier("commcareApplicationDatabaseConnector")
    private CouchDbConnector connector;

    private MotechJsonReader motechJsonReader = new MotechJsonReader();

    @Test
    public void shouldSaveAndRetrieveCommcareApplications() throws Exception {
        List<CommcareApplicationJson> commcareApplicationJsonList = application();

        allCommcareApplications.addAll(commcareApplicationJsonList);
        List<CommcareApplication> commcareApplications = allCommcareApplications.getAll();

        assertEquals(commcareApplications.size(), 1);

        CommcareApplication application = commcareApplications.get(0);
        assertEquals(application.getApplicationName(), APPLICATION_NAME);
        assertEquals(application.getResourceUri(), RESOURCE_URI);
        assertEquals(application.getModules().size(), 1);

        CommcareModuleJson commcareModule = application.getModules().get(0);
        assertEquals(commcareModule.getCaseType(), CASE_TYPE);
        assertEquals(commcareModule.getCaseProperties().size(), 2);
        assertEquals(commcareModule.getFormSchemas().size(), 1);
        assertEquals(commcareModule.getCaseProperties().get(0), CASE_PROPERTY_1);
        assertEquals(commcareModule.getCaseProperties().get(1), CASE_PROPERTY_2);
    }

    @Override
    public CouchDbConnector getDBConnector() {
        return connector;
    }

    @After
    public void tearDown() {
        allCommcareApplications.removeAll();
    }

    private List<CommcareApplicationJson> application() throws IOException {
        try (InputStream in = getClass().getClassLoader().getResourceAsStream("json/service/appStructure.json")) {
            Type appStructureResponseType = new TypeToken<AppStructureResponseJson>() {}.getType();
            return ((AppStructureResponseJson) motechJsonReader.readFromStream(in, appStructureResponseType))
                    .getApplications();
        }
    }
}
