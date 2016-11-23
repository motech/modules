package org.motechproject.commcare.service.impl;

import org.motechproject.commcare.client.CommCareAPIHttpClient;
import org.motechproject.commcare.config.AccountConfig;
import org.motechproject.commcare.domain.CommcareForm;
import org.motechproject.commcare.domain.CommcareFormList;
import org.motechproject.commcare.domain.FormXml;
import org.motechproject.commcare.exception.OpenRosaParserException;
import org.motechproject.commcare.gateway.FormXmlConverter;
import org.motechproject.commcare.parser.FormAdapter;
import org.motechproject.commcare.request.FormListRequest;
import org.motechproject.commcare.response.OpenRosaResponse;
import org.motechproject.commcare.service.CommcareConfigService;
import org.motechproject.commcare.service.CommcareFormService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CommcareFormServiceImpl implements CommcareFormService {

    private static final Logger LOGGER = LoggerFactory.getLogger(CommcareFormServiceImpl.class);

    private CommCareAPIHttpClient commcareHttpClient;
    private CommcareConfigService configService;
    private FormXmlConverter converter;

    @Autowired
    public CommcareFormServiceImpl(CommCareAPIHttpClient commcareHttpClient, CommcareConfigService configService, FormXmlConverter converter) {
        this.commcareHttpClient = commcareHttpClient;
        this.configService = configService;
        this.converter = converter;
    }

    @Override
    public CommcareForm retrieveForm(String id, String configName) {
        String returnJson = commcareHttpClient.formRequest(configService.getByName(configName).getAccountConfig(), id);
        CommcareForm form = new CommcareForm();
        if (("").equals(returnJson)){
            return form;
        } else {
            form = FormAdapter.readJson(returnJson);
            form.setConfigName(configName);
            return form;
        }
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

    @Override
    public OpenRosaResponse uploadForm(FormXml formXml, String configName) {
        String xml = converter.convertToFormXml(formXml);

        LOGGER.debug("Sending the following Form XML to the Commcare server: {}", xml);
        OpenRosaResponse response = null;
        try {
            response = commcareHttpClient.submissionRequest(configService.getByName(configName).getAccountConfig(), xml);
            LOGGER.debug("Received the following response from the Commcare server. Status: {}, Message: {}", response.getStatus(), response.getMessageText());
        } catch (OpenRosaParserException e) {
            LOGGER.error("Failed to parse response from the CommCare server.", e);
        }

        return response;
    }

    @Override
    public OpenRosaResponse uploadForm(FormXml formXml) {
        return uploadForm(formXml, null);
    }

    private void setConfigNames(CommcareFormList formList, String configName) {
        for (CommcareForm form : formList.getObjects()) {
            form.setConfigName(configName);
        }
    }
}
