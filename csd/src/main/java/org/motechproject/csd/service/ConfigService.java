package org.motechproject.csd.service;

import org.motechproject.csd.domain.Config;

public interface ConfigService {

    Config getConfig();

    void updateConfig(Config config);
}
