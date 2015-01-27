package org.motechproject.commcare.web;

import org.apache.commons.lang.StringUtils;
import org.codehaus.jackson.map.ObjectMapper;
import org.motechproject.commcare.domain.CommcareAccountSettings;
import org.motechproject.commcare.domain.CommcareDataForwardingEndpoint;
import org.motechproject.commcare.domain.CommcarePermission;
import org.motechproject.commcare.exception.CommcareAuthenticationException;
import org.motechproject.commcare.exception.CommcareConnectionFailureException;
import org.motechproject.commcare.service.CommcareAccountService;
import org.motechproject.commcare.service.CommcareDataForwardingEndpointService;
import org.motechproject.event.MotechEvent;
import org.motechproject.event.listener.EventRelay;
import org.motechproject.server.config.SettingsFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.io.IOException;
import java.util.List;

import static java.util.Arrays.asList;
import static org.motechproject.commcare.events.constants.EventSubjects.SCHEMA_CHANGE_EVENT;

@Controller
@RequestMapping("/connection")
public class StubConnectionController {

    private static final String COMMCARE_BASE_URL_KEY = "commcareBaseUrl";
    private static final String COMMCARE_DOMAIN_KEY = "commcareDomain";
    private static final String USERNAME_KEY = "username";
    private static final String PASSWORD_KEY = "password";


    @Autowired
    private SettingsFacade settingsFacade;

    @Autowired
    private CommcareDataForwardingEndpointService forwardingEndpointService;

    @Autowired
    private EventRelay eventRelay;

    private static final String FORWARD_CASES_KEY = "forwardCases";
    private static final String FORWARD_FORMS_KEY = "forwardForms";
    private static final String FORWARD_FORM_STUBS_KEY = "forwardFormStubs";
    private static final String FORWARD_APP_STRUCTURE_KEY = "forwardAppStructure";

    private CommcareAccountService commcareAccountService;

    @Autowired
    public StubConnectionController(CommcareAccountService commcareAccountService) {
        this.commcareAccountService = commcareAccountService;
    }

    @ResponseStatus(HttpStatus.CREATED)
    @RequestMapping(value = "/verify", method = RequestMethod.POST)
    public void saveSettings(@RequestBody CommcareAccountSettings settings) throws CommcareAuthenticationException, CommcareConnectionFailureException {
        settingsFacade.setProperty(COMMCARE_BASE_URL_KEY, settings.getCommcareBaseUrl());
        settingsFacade.setProperty(COMMCARE_DOMAIN_KEY, settings.getCommcareDomain());
        settingsFacade.setProperty(USERNAME_KEY, settings.getUsername());
        settingsFacade.setProperty(PASSWORD_KEY, settings.getPassword());

        if (commcareAccountService.verifySettings()) {
            checkForwardingSettings();
            eventRelay.sendEventMessage(new MotechEvent(SCHEMA_CHANGE_EVENT));
        }
    }

    @RequestMapping(value = "/permissions", method = RequestMethod.GET)
    @ResponseBody
    public List<CommcarePermission> getPermissions() throws CommcareAuthenticationException, CommcareConnectionFailureException {
        return asList(new CommcarePermission("modify CommCareHQ settings", true), new CommcarePermission("make API calls", true));
    }

    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    @ResponseBody
    public String handleIllegalArgumentException(IllegalArgumentException exception) throws IOException {
        return new ObjectMapper().writeValueAsString(new ErrorText(exception.getMessage()));
    }

    @ExceptionHandler(CommcareConnectionFailureException.class)
    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    @ResponseBody
    public String handleCommcareConnectionFailureException(CommcareConnectionFailureException exception) throws IOException {
        return new ObjectMapper().writeValueAsString(new ErrorText(exception.getMessage()));
    }

    @ExceptionHandler(CommcareAuthenticationException.class)
    @ResponseStatus(value = HttpStatus.UNAUTHORIZED)
    @ResponseBody
    public String handleCommcareAuthenticationException(CommcareAuthenticationException exception) throws IOException {
        return new ObjectMapper().writeValueAsString(new ErrorText(exception.getMessage()));
    }

    private void checkForwardingSettings() {
        for (CommcareDataForwardingEndpoint endpoint : forwardingEndpointService.getAllDataForwardingEndpoints()) {
            String endpointUrl = endpoint.getUrl();

            if (StringUtils.equals(endpointUrl, getCasesUrl())) {
                settingsFacade.setProperty(FORWARD_CASES_KEY, String.valueOf(true));
            } else if (StringUtils.equals(endpointUrl, getFormsUrl())) {
                settingsFacade.setProperty(FORWARD_FORMS_KEY, String.valueOf(true));
            } else if (StringUtils.equals(endpointUrl, getFormStubsUrl())) {
                settingsFacade.setProperty(FORWARD_FORM_STUBS_KEY, String.valueOf(true));
            } else if (StringUtils.equals(endpointUrl, getAppStructureUrl())) {
                settingsFacade.setProperty(FORWARD_APP_STRUCTURE_KEY, String.valueOf(true));
            }
        }
    }

    private String getCasesUrl() {
        return settingsFacade.getPlatformSettings().getServerUrl() + "/module/commcare/cases/";
    }

    private String getFormsUrl() {
        return settingsFacade.getPlatformSettings().getServerUrl() + "/module/commcare/forms/";
    }

    private String getFormStubsUrl() {
        return settingsFacade.getPlatformSettings().getServerUrl() + "/module/commcare/stub/";
    }

    private String getAppStructureUrl() {
        return settingsFacade.getPlatformSettings().getServerUrl() + "/module/commcare/appSchemaChange/";
    }
}

class ErrorText {

    private String message;

    ErrorText(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
