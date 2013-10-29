package org.motechproject.commcare.web;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.motechproject.commcare.domain.CommcareDataForwardingEndpoint;
import org.motechproject.commcare.domain.SettingsDto;
import org.motechproject.commcare.service.CommcareDataForwardingEndpointService;
import org.motechproject.server.config.SettingsFacade;
import org.motechproject.config.service.ConfigurationService;
import org.motechproject.server.config.domain.SettingsRecord;
import org.osgi.framework.BundleException;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

@RunWith(MockitoJUnitRunner.class)
public class SettingsControllerTest {
    private static final String COMMCARE_BASE_URL_KEY = "commcareBaseUrl";
    private static final String CASE_EVENT_STRATEGY_KEY = "eventStrategy";
    private static final String COMMCARE_DOMAIN_KEY = "commcareDomain";
    private static final String PASSWORD_KEY = "password";
    private static final String USERNAME_KEY = "username";

    private static final String FORWARD_CASES_KEY = "forwardCases";
    private static final String FORWARD_FORMS_KEY = "forwardForms";
    private static final String FORWARD_FORM_STUBS_KEY = "forwardFormStubs";
    private static final String FORWARD_APP_STRUCTURE_KEY = "forwardAppStructure";

    private static final String EVENT_STRATEGY_VALUE = "minimal";
    private static final String COMMCARE_BASE_URL_VALUE = "https://commcarehq.com/a/";

    private static final boolean FORWARD_CASES_VALUE = true;
    private static final boolean FORWARD_FORMS_VALUE = true;
    private static final boolean FORWARD_FORM_STUBS_VALUE = false;
    private static final boolean FORWARD_APP_STRUCTURE_VALUE = true;

    @Mock
    private SettingsFacade settingsFacade;

    @Mock
    private SettingsRecord motechSettings;

    @Mock
    private ConfigurationService settingsService;

    @Mock
    private CommcareDataForwardingEndpointService forwardingEndpointService;

    @InjectMocks
    private SettingsController controller = new SettingsController(settingsFacade);

    @Before
    public void setUp() {
        initMocks(this);
    }

    @Test
    public void testGetSettings() {
        when(settingsFacade.getProperty(COMMCARE_BASE_URL_KEY)).thenReturn(COMMCARE_BASE_URL_VALUE);
        when(settingsFacade.getProperty(COMMCARE_DOMAIN_KEY)).thenReturn(COMMCARE_DOMAIN_KEY);
        when(settingsFacade.getProperty(USERNAME_KEY)).thenReturn(USERNAME_KEY);
        when(settingsFacade.getProperty(PASSWORD_KEY)).thenReturn(PASSWORD_KEY);
        when(settingsFacade.getProperty(CASE_EVENT_STRATEGY_KEY)).thenReturn(EVENT_STRATEGY_VALUE);
        when(settingsFacade.getProperty(FORWARD_FORMS_KEY)).thenReturn(String.valueOf(FORWARD_FORMS_VALUE));
        when(settingsFacade.getProperty(FORWARD_CASES_KEY)).thenReturn(String.valueOf(FORWARD_CASES_VALUE));
        when(settingsFacade.getProperty(FORWARD_FORM_STUBS_KEY)).thenReturn(String.valueOf(FORWARD_FORM_STUBS_VALUE));
        when(settingsFacade.getProperty(FORWARD_APP_STRUCTURE_KEY)).thenReturn(String.valueOf(FORWARD_APP_STRUCTURE_VALUE));

        SettingsDto dto = controller.getSettings();

        verify(settingsFacade, times(9)).getProperty(anyString());

        assertEquals(COMMCARE_BASE_URL_VALUE, dto.getCommcareBaseUrl());
        assertEquals(COMMCARE_DOMAIN_KEY, dto.getCommcareDomain());
        assertEquals(USERNAME_KEY, dto.getUsername());
        assertEquals(PASSWORD_KEY, dto.getPassword());
        assertEquals(EVENT_STRATEGY_VALUE, dto.getEventStrategy());
        assertEquals(FORWARD_FORMS_VALUE, dto.shouldForwardForms());
        assertEquals(FORWARD_CASES_VALUE, dto.shouldForwardCases());
        assertEquals(FORWARD_FORM_STUBS_VALUE, dto.shouldForwardFormStubs());
        assertEquals(FORWARD_APP_STRUCTURE_VALUE, dto.shouldForwardAppStructure());
    }

    @Test
    public void testSaveSettings() throws BundleException {
        when(settingsService.getPlatformSettings()).thenReturn(motechSettings);
        when(motechSettings.getServerUrl()).thenReturn("http://motechtest.org");

        SettingsDto dto = new SettingsDto();
        dto.setCommcareBaseUrl(COMMCARE_BASE_URL_VALUE);
        dto.setCommcareDomain(COMMCARE_DOMAIN_KEY);
        dto.setUsername(USERNAME_KEY);
        dto.setPassword(PASSWORD_KEY);
        dto.setEventStrategy(EVENT_STRATEGY_VALUE);
        dto.setForwardForms(true);
        dto.setForwardCases(true);
        dto.setForwardFormStubs(true);

        controller.saveSettings(dto);

        verify(settingsFacade, times(8)).setProperty(anyString(), anyString());
    }
}