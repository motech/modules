package org.motechproject.rapidpro.it;

import org.junit.After;
import org.junit.Before;
import org.motechproject.rapidpro.domain.Settings;
import org.motechproject.rapidpro.service.SettingsService;
import org.motechproject.testing.osgi.BasePaxIT;

import javax.inject.Inject;

/**
 * Base class for all Rapidpro integration tests.
 */
public abstract class RapidproBaseIT extends BasePaxIT {

    private static final String API_KEY= "API-key";
    private static final String VERSION = "v1";

    @Inject
    private SettingsService settingsService;

    @Before
    public void setup() {
        Settings settings = new Settings();
        settings.setApiKey(API_KEY);
        settings.setVersion(VERSION);
        settingsService.updateSettings(settings);
    }

    @After
    public void teardown() {
        settingsService.deleteSettings();
    }
}
