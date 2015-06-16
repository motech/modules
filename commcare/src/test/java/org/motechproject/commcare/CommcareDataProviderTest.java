package org.motechproject.commcare;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.Mock;
import org.motechproject.commcare.config.Config;
import org.motechproject.commcare.domain.CaseInfo;
import org.motechproject.commcare.domain.CommcareFixture;
import org.motechproject.commcare.domain.CommcareForm;
import org.motechproject.commcare.domain.CommcareLocation;
import org.motechproject.commcare.domain.CommcareUser;
import org.motechproject.commcare.service.impl.CommcareCaseServiceImpl;
import org.motechproject.commcare.service.impl.CommcareFixtureServiceImpl;
import org.motechproject.commcare.service.impl.CommcareFormServiceImpl;
import org.motechproject.commcare.service.impl.CommcareLocationServiceImpl;
import org.motechproject.commcare.service.impl.CommcareUserServiceImpl;
import org.motechproject.commcare.util.ConfigsUtils;
import org.motechproject.commons.api.Range;

import java.util.HashMap;
import java.util.Map;

import static junit.framework.Assert.assertNull;
import static junit.framework.Assert.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;


public class CommcareDataProviderTest {
    private static final String FIELD_KEY = "id";
    private static final String FIELD_VALUE = "12345";

    private static Map<String, String> lookupFields;

    @Mock
    private CommcareUser commcareUser;

    @Mock
    private CommcareLocation commcareLocation;

    @Mock
    private CommcareFixture commcareFixture;

    private CommcareForm commcareForm = new CommcareForm();

    private CaseInfo caseInfo =  new CaseInfo();

    @Mock
    private CommcareFormServiceImpl commcareFormService;

    @Mock
    private CommcareUserServiceImpl commcareUserService;

    @Mock
    private CommcareLocationServiceImpl commcareLocationService;

    @Mock
    private CommcareFixtureServiceImpl commcareFixtureService;

    @Mock
    private CommcareCaseServiceImpl commcareCaseService;

    private Config config = ConfigsUtils.prepareConfigOne();


    private CommcareDataProvider provider;

    @BeforeClass
    public static void setLookupFields() {
        lookupFields = new HashMap<>();
        lookupFields.put(FIELD_KEY, FIELD_VALUE);
    }

    @Before
    public void setUp() throws Exception {
        initMocks(this);

        provider = new CommcareDataProvider();
        provider.setCommcareUserService(commcareUserService);
        provider.setCommcareLocationService(commcareLocationService);
        provider.setCommcareFormService(commcareFormService);
        provider.setCommcareCaseService(commcareCaseService);
        provider.setCommcareFixtureService(commcareFixtureService);
    }

    @Test
    public void shouldReturnNullWhenClassIsNotSupported() {
        // given
        String clazz = Range.class.getSimpleName();

        // when
        Object object = provider.lookup(clazz, "id", lookupFields);

        // then
        assertNull(object);
    }

    @Test
    public void shouldReturnNullWhenMapNotContainsSupportedField() {
        // given
        String clazz = CommcareUser.class.getSimpleName();
        HashMap<String, String> fields = new HashMap<>();

        // when
        Object object = provider.lookup(clazz, "id", fields);

        // then
        assertNull(object);
    }

    @Test
    public void shouldReturnNullWhenListIsBlank() {
        // given
        String commcareUserClass = CommcareUser.class.getSimpleName() + "-" + config.getName();
        String commcareLocationClass = CommcareLocation.class.getSimpleName() + "-" + config.getName();
        String commcareFormClass = CommcareForm.class.getSimpleName() + "-" + config.getName();
        String commcareFixtureClass = CommcareFixture.class.getSimpleName() + "-" + config.getName();
        String caseInfoClass = CaseInfo.class.getSimpleName() + "-" + config.getName();

        // when
        Object userContent = provider.lookup(commcareUserClass, "id", lookupFields);
        Object locationContent = provider.lookup(commcareLocationClass, "id", lookupFields);
        Object formContent = provider.lookup(commcareFormClass, "id", lookupFields);
        Object fixtureContent = provider.lookup(commcareFixtureClass, "id", lookupFields);
        Object caseContent = provider.lookup(caseInfoClass, "id", lookupFields);

        // then
        assertNull(userContent);
        assertNull(locationContent);
        assertNull(formContent);
        assertNull(fixtureContent);
        assertNull(caseContent);
    }

    @Test
    public void shouldReturnObject() {
        // given
        String commcareUserClass = CommcareUser.class.getSimpleName() + "-" + config.getName();
        String commcareLocationClass = CommcareLocation.class.getSimpleName() + "-" + config.getName();
        String commcareFormClass = CommcareForm.class.getSimpleName() + "-" + config.getName();
        String commcareFixtureClass = CommcareFixture.class.getSimpleName() + "-" + config.getName();
        String caseInfoClass = CaseInfo.class.getSimpleName() + "-" + config.getName();

        when(commcareUserService.getCommcareUserById(FIELD_VALUE, config.getName())).thenReturn(commcareUser);
        when(commcareLocationService.getCommcareLocationById(FIELD_VALUE, config.getName())).thenReturn(commcareLocation);
        when(commcareFixtureService.getCommcareFixtureById(FIELD_VALUE, config.getName())).thenReturn(commcareFixture);
        when(commcareFormService.retrieveForm(FIELD_VALUE, config.getName())).thenReturn(commcareForm);
        when(commcareCaseService.getCaseByCaseId(FIELD_VALUE, config.getName())).thenReturn(caseInfo);

        // when
        CommcareUser commcareUser1 = (CommcareUser) provider.lookup(commcareUserClass, "id", lookupFields);
        CommcareLocation commcareLocation1 = (CommcareLocation) provider.lookup(commcareLocationClass, "id", lookupFields);
        Map<String, Object> commcareForm1 = (Map) provider.lookup(commcareFormClass, "id", lookupFields);
        CommcareFixture commcareFixture1 = (CommcareFixture) provider.lookup(commcareFixtureClass, "id", lookupFields);
        Map<String, Object> caseInfo1 = (Map) provider.lookup(caseInfoClass, "id", lookupFields);

        // then
        assertEquals(this.commcareUser, commcareUser1);
        assertEquals(this.commcareLocation, commcareLocation1);
        assertEquals(this.commcareFixture, commcareFixture1);

        assertTrue(commcareForm1.containsKey("id"));
        assertTrue(commcareForm1.containsKey("metadata"));
        assertTrue(commcareForm1.containsKey("version"));

        assertTrue(caseInfo1.containsKey("caseId"));
        assertTrue(caseInfo1.containsKey("caseName"));
        assertTrue(caseInfo1.containsKey("serverDateModified"));
    }
}
