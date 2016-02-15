package org.motechproject.commcare.service.impl;

import org.motechproject.commcare.domain.CommcareApplicationJson;
import org.motechproject.commcare.service.CommcareApplicationDataService;
import org.motechproject.commcare.service.CommcareApplicationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.ArrayList;

/**
 * Created by user on 12.02.16.
 */
@Service
public class CommcareApplicationServiceImpl implements CommcareApplicationService {

    private CommcareApplicationDataService commcareApplicationDataService;

    @Override
    @Transactional
    public List<CommcareApplicationJson> getByConfigName(String configName) {
        return commcareApplicationDataService.bySourceConfiguration(configName);
    }

    @Autowired
    public void setCommcareApplicationDataService(CommcareApplicationDataService commcareApplicationDataService) {
        this.commcareApplicationDataService = commcareApplicationDataService;
    }
}