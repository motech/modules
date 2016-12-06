package org.motechproject.dhis2.service;

import java.util.Map;

public interface DhisService {

    void createEntity(Map<String, Object> parameters);

    void enrollInProgram(Map<String, Object> parameters);

    void updateProgramStage(Map<String, Object> parameters);

    void createAndEnroll(Map<String, Object> parameters);

    void sendDataValue(Map<String, Object> parameters);

    void sendDataValueSet(Map<String, Object> parameters);
}
