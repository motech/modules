package org.motechproject.commcare.it;

import com.google.gson.reflect.TypeToken;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.motechproject.commcare.domain.AppStructureResponseJson;
import org.motechproject.commcare.domain.CommcareApplicationJson;
import org.motechproject.commcare.domain.CommcareModuleJson;
import org.motechproject.commcare.service.CommcareApplicationDataService;
import org.motechproject.commons.api.json.MotechJsonReader;
import org.motechproject.testing.osgi.BasePaxIT;
import org.motechproject.testing.osgi.container.MotechNativeTestContainerFactory;
import org.ops4j.pax.exam.ExamFactory;
import org.ops4j.pax.exam.junit.PaxExam;
import org.ops4j.pax.exam.spi.reactors.ExamReactorStrategy;
import org.ops4j.pax.exam.spi.reactors.PerSuite;

import javax.inject.Inject;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.util.List;

import static junit.framework.Assert.assertEquals;

@RunWith(PaxExam.class)
@ExamReactorStrategy(PerSuite.class)
@ExamFactory(MotechNativeTestContainerFactory.class)
public class CommcareApplicationDataServiceIT extends BasePaxIT {

    private static final String APPLICATION_NAME = "myApplication";
    private static final String RESOURCE_URI = "";
    private static final String CASE_TYPE = "myCase1";
    private static final String CASE_PROPERTY_1 = "name";
    private static final String CASE_PROPERTY_2 = "user_bednet";

    @Inject
    private CommcareApplicationDataService commareApplicationDataService;

    private MotechJsonReader motechJsonReader = new MotechJsonReader();

    @Test
    public void shouldSaveAndRetrieveCommcareApplications() throws Exception {
        List<CommcareApplicationJson> commcareApplicationJsonList = application();

        for (CommcareApplicationJson app : commcareApplicationJsonList) {
            commareApplicationDataService.create(app);
        }

        List<CommcareApplicationJson> commcareApplications = commareApplicationDataService.retrieveAll();

        assertEquals(1, commcareApplications.size());

        CommcareApplicationJson application = commcareApplications.get(0);
        assertEquals(APPLICATION_NAME, application.getApplicationName());
        assertEquals(RESOURCE_URI, application.getResourceUri());
        assertEquals(1, application.getModules().size());

        CommcareModuleJson commcareModule = application.getModules().get(0);
        assertEquals(CASE_TYPE, commcareModule.getCaseType());
        assertEquals(2, commcareModule.getCaseProperties().size());
        assertEquals(1, commcareModule.getFormSchemas().size());
        assertEquals(CASE_PROPERTY_1, commcareModule.getCaseProperties().get(0));
        assertEquals(CASE_PROPERTY_2, commcareModule.getCaseProperties().get(1));
    }

    @Before
    public void setUp() {
        commareApplicationDataService.deleteAll();
    }

    @After
    public void tearDown() {
        commareApplicationDataService.deleteAll();
    }

    private List<CommcareApplicationJson> application() throws IOException {
        try (InputStream in = getClass().getClassLoader().getResourceAsStream("json/service/appStructure.json")) {
            Type appStructureResponseType = new TypeToken<AppStructureResponseJson>() {}.getType();
            return ((AppStructureResponseJson) motechJsonReader.readFromStream(in, appStructureResponseType))
                    .getApplications();
        }
    }
}
