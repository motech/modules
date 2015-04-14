package org.motechproject.csd.service;

import org.motechproject.csd.domain.Config;

import java.util.List;

public interface ConfigService {

    List<Config> getConfigs();

    Config getConfig(String xmlUrl);

    void updateConfigs(List<Config> configs);
}
