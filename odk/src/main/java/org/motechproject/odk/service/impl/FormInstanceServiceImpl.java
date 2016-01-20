package org.motechproject.odk.service.impl;


import org.motechproject.odk.domain.FormInstance;
import org.motechproject.odk.repository.FormInstanceDataService;
import org.motechproject.odk.service.FormInstanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FormInstanceServiceImpl implements FormInstanceService {

    @Autowired
    private FormInstanceDataService formInstanceDataService;

    @Override
    public void create(FormInstance formInstance) {
        formInstanceDataService.create(formInstance);
    }

    @Override
    public FormInstance getByInstanceId(String instanceId) {
        return formInstanceDataService.byInstanceId(instanceId);
    }
}
