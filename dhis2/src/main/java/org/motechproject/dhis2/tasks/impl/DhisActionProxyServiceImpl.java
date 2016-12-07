package org.motechproject.dhis2.tasks.impl;

import org.motechproject.dhis2.service.DhisService;
import org.motechproject.dhis2.tasks.DhisActionProxyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service("dhisActionProxyService")
public class DhisActionProxyServiceImpl implements DhisActionProxyService {

    @Autowired
    private DhisService dhisService;

    @Override
    public void enrollInProgram(Map<String, Object> parameters) {
        dhisService.enrollInProgram(parameters);
    }

    @Override
    public void createEntity(Map<String, Object> parameters) {
        dhisService.createEntity(parameters);
    }

    @Override
    public void updateProgramStage(Map<String, Object> parameters) {
        dhisService.updateProgramStage(parameters);
    }

    @Override
    public void createAndEnroll(Map<String, Object> parameters) {
        dhisService.createAndEnroll(parameters);
    }

    @Override
    public void sendDataValue(Map<String, Object> parameters) {
        dhisService.sendDataValue(parameters);
    }

    @Override
    public void sendDataValueSet(Map<String, Object> parameters) {
        dhisService.sendDataValueSet(parameters);
    }
}
