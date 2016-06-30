package org.motechproject.rapidpro.web;

import org.motechproject.rapidpro.domain.Settings;
import org.motechproject.rapidpro.service.SettingsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * REST Controller for {@link Settings}
 */

@Controller
@RequestMapping("/settings")
public class SettingsController {

    @Autowired
    private SettingsService settingsService;

    /**
     * Gets the current settings
     *
     * @return {@link Settings}
     */
    @RequestMapping(method = RequestMethod.GET)
    @ResponseBody
    @PreAuthorize("hasRole('configureRapidpro')")
    public Settings getSetttings() {
        return settingsService.getSettings();
    }

    /**
     * Updates the current settings
     *
     * @param settings The new settings
     */
    @RequestMapping(method = RequestMethod.POST)
    @ResponseBody
    @PreAuthorize("hasRole('configureRapidpro')")
    public void updateSettings(@RequestBody Settings settings) {
        settingsService.updateSettings(settings);
    }

    /**
     * Deletes the current settings
     */
    @RequestMapping(method = RequestMethod.DELETE)
    @PreAuthorize("hasRole('configureRapidpro')")
    public void deleteSettings() {
        settingsService.deleteSettings();
    }
}
