package org.motechproject.commcare.service.impl;

import org.motechproject.commcare.client.CommCareAPIHttpClient;
import org.motechproject.commcare.config.AccountConfig;
import org.motechproject.commcare.domain.CommcareForm;
import org.motechproject.commcare.domain.CommcareFormList;
import org.motechproject.commcare.parser.FormAdapter;
import org.motechproject.commcare.request.FormListRequest;
import org.motechproject.commcare.service.CommcareConfigService;
import org.motechproject.commcare.service.CommcareFormService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CommcareFormServiceImpl implements CommcareFormService {

    private CommCareAPIHttpClient commcareHttpClient;

    private CommcareConfigService configService;

    @Autowired
    public CommcareFormServiceImpl(CommCareAPIHttpClient commcareHttpClient, CommcareConfigService configService) {
        this.commcareHttpClient = commcareHttpClient;
        this.configService = configService;
    }

    @Override
    public CommcareForm retrieveForm(String id, String configName) {
        String returnJson = commcareHttpClient.formRequest(configService.getByName(configName).getAccountConfig(), id);

        CommcareForm form = FormAdapter.readJson(returnJson);
        form.setConfigName(configName);

        return form;
    }

    @Override
    public CommcareFormList retrieveFormList(FormListRequest request) {
        return retrieveFormList(request, null);
    }

    @Override
    public CommcareFormList retrieveFormList(FormListRequest request, String configName) {
        AccountConfig accountConfig = configService.getByName(configName).getAccountConfig();
        String returnJson = commcareHttpClient.formListRequest(accountConfig, request);

        CommcareFormList formList = FormAdapter.readListJson(returnJson);
        setConfigNames(formList, configName);

        return formList;
    }

    @Override
    public String retrieveFormJson(String id, String configName) {
        return commcareHttpClient.formRequest(configService.getByName(configName).getAccountConfig(), id);
    }

    @Override
    public CommcareForm retrieveForm(String id) {
        return retrieveForm(id, null);
    }

    @Override
    public String retrieveFormJson(String id) {
        return retrieveFormJson(id, null);
    }

    private void setConfigNames(CommcareFormList formList, String configName) {
        for (CommcareForm form : formList.getObjects()) {
            form.setConfigName(configName);
        }
    }
}
