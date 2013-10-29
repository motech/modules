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
import org.motechproject.config.service.ConfigurationService;
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
    @Autowired
    private ConfigurationService settingsService;

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
    public void verifySettings(@RequestBody CommcareAccountSettings settings) throws CommcareAuthenticationException, CommcareConnectionFailureException {
        settingsFacade.setProperty(FORWARD_CASES_KEY, String.valueOf(false));
        settingsFacade.setProperty(FORWARD_FORMS_KEY, String.valueOf(false));
        settingsFacade.setProperty(FORWARD_FORM_STUBS_KEY, String.valueOf(false));
        settingsFacade.setProperty(FORWARD_APP_STRUCTURE_KEY, String.valueOf(false));

        if (commcareAccountService.verifySettings(settings)) {
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
        for(CommcareDataForwardingEndpoint endpoint : forwardingEndpointService.getAllDataForwardingEndpoints()) {
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
        return settingsService.getPlatformSettings().getServerUrl() + "/module/commcare/cases/";
    }

    private String getFormsUrl() {
        return settingsService.getPlatformSettings().getServerUrl() + "/module/commcare/forms/";
    }

    private String getFormStubsUrl() {
        return settingsService.getPlatformSettings().getServerUrl() + "/module/commcare/stub/";
    }

    private String getAppStructureUrl() {
        return settingsService.getPlatformSettings().getServerUrl() + "/module/commcare/appSchemaChange/";
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
