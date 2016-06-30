package org.motechproject.rapidpro.service;

import org.motechproject.rapidpro.domain.Settings;

/**
 * A service for CRUD operations on {@link Settings}
 */
public interface SettingsService {

    /**
     * Retrieves the Settings object, if it exists.
     *
     * @return {@link java.util.Set}
     */
    Settings getSettings();

    /**
     * Updates the current Settings.
     *
     * @param settings {@link java.util.Set}
     */
    void updateSettings(Settings settings);

    /**
     * Deletes the current Settings.
     */
    void deleteSettings();
}
