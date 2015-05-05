package org.motechproject.dhis2.service;


/**
 * Retrieves and updates settings for the module
 * @see Settings
 */
public interface SettingsService {
    Settings getSettings();
    void updateSettings(Settings settings);
}
