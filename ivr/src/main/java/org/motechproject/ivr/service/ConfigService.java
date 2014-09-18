package org.motechproject.ivr.service;


import org.motechproject.ivr.domain.Config;

import java.util.List;

/**
 * Config service, manages IVR configs. A {@link org.motechproject.ivr.domain.Config} represents the way to interact with an IVR provider.
 * See {@link org.motechproject.ivr.domain.Config}
 */
public interface ConfigService {
    Config getConfig(String name);
    List<Config> allConfigs();
    boolean hasConfig(String name);
    void updateConfigs(List<Config> configs);
}
