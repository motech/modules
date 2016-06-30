package org.motechproject.openlmis.service;


/**
 * Retrieves and updates settings for the module
 * @see Settings
 */
public interface SettingsService {
    Settings getSettings();
    void updateSettings(Settings settings);
}
