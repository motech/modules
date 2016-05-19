package org.motechproject.openlmis.web;

import org.motechproject.openlmis.service.Settings;
import org.motechproject.openlmis.service.SettingsService;
import org.motechproject.openlmis.service.SyncService;
import org.motechproject.openlmis.service.TasksService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * The controller that handles both changes to module settings and syncing to the Open LMIS server.
 * @see org.motechproject.open-lmis.service.SyncService
 * @see org.motechproject.open-lmis.service.SettingsService
 */
@Controller
public class OpenlmisSettingsController {
    @Autowired
    @Qualifier("openlmisSettingsService")
    private SettingsService settingsService;

    @Autowired
    private SyncService syncService;

    @Autowired
    private TasksService tasksService;

    /**
     * Returns the settings for the module
     * @return an {@link Settings} object for the Open LMIS module
     */
    @RequestMapping(value = "/settings", method = RequestMethod.GET)
    @ResponseBody
    @PreAuthorize("hasRole('configureOpenlmis')")
    public Settings getSettings() {
        return settingsService.getSettings();
    }

    /**
     * Updates the settings to the configuration specified in the parameters
     * @param settings the new settings configuration
     * @return the new settings configuration
     */
    @RequestMapping(value = "/settings", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    @PreAuthorize("hasRole('configureOpenlmis')")
    public Settings updateSettings(@RequestBody Settings settings) {
        settingsService.updateSettings(settings);
        return settingsService.getSettings();
    }

    /**
     * Attempts to sync the module to the Open LMIS server. If successful, updates
     * the task channel to reflect any changes to the Open LMIS schema
     * @return a boolean value indicating success or failure
     */
    @RequestMapping(value = "/sync", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    @PreAuthorize("hasRole('syncOpenlmis')")
    public boolean sync() {
        boolean success = syncService.sync();

        if (success) {
            tasksService.updateChannel();
        }

        return success;
    }
}
