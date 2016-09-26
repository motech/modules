package org.motechproject.dhis2.service.impl;

import com.google.gson.JsonIOException;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.motechproject.config.SettingsFacade;
import org.motechproject.dhis2.service.Settings;
import org.motechproject.event.listener.EventRelay;

import java.io.IOException;
import java.io.InputStream;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class SettingsServiceImplTest {

    private static final String SETTINGS_FILE_NAME = "dhis2-settings.json";
    private static final String SETTINGS_JSON = "json/settings/settings.json";

    private Settings settings;

    @Mock
    private EventRelay eventRelay;

    @Mock
    private SettingsFacade settingsFacade;

    private SettingsServiceImpl settingsService;

    @Before
    public void setUp() {
        initMocks(this);
        settings = new Settings("localhost:8080/dhis/", "admin", "district");
        try (InputStream is = getClass().getClassLoader().getResourceAsStream(SETTINGS_JSON)) {
            when(settingsFacade.getRawConfig(SETTINGS_FILE_NAME)).thenReturn(is);
            settingsService = new SettingsServiceImpl(settingsFacade, eventRelay);
        } catch (IOException e) {
            String message = "There was an error loading json from the DHIS2 settings.";
            throw new JsonIOException(message, e);
        }
    }

    @Test
    public void shouldRemoveTrailingSlashes() {
        try (InputStream is = getClass().getClassLoader().getResourceAsStream(SETTINGS_JSON)) {
            when(settingsFacade.getRawConfig(SETTINGS_FILE_NAME)).thenReturn(is);
            settingsService.updateSettings(settings);
            String expected = "localhost:8080/dhis";
            assertThat(settingsService.getSettings().getServerURI(), equalTo(expected));
        } catch (IOException e) {
            String message = "There was an error loading json from the DHIS2 settings.";
            throw new JsonIOException(message, e);
        }
    }
}
