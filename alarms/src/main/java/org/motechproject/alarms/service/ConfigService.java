package org.motechproject.alarms.service;

import org.motechproject.alarms.domain.Config;

public interface ConfigService {

    Config getConfig();

    void updateConfig(Config config);
}
