package org.motechproject.hub.web;

import static org.apache.commons.lang.StringUtils.isNotBlank;

import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.time.StopWatch;
import org.motechproject.hub.exception.ApplicationErrors;
import org.motechproject.hub.exception.HubError;
import org.motechproject.hub.exception.HubException;
import org.motechproject.hub.exception.RestException;
import org.motechproject.hub.model.Modes;
import org.motechproject.hub.model.SettingsDTO;
import org.motechproject.hub.service.ContentDistributionService;
import org.motechproject.hub.service.SubscriptionService;
import org.motechproject.hub.util.HubConstants;
import org.motechproject.hub.validation.HubValidator;
import org.motechproject.server.config.SettingsFacade;
import org.osgi.framework.BundleException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@Controller
@RequestMapping("/")
public class HubController {

    private static final Logger LOGGER = LoggerFactory
            .getLogger(HubController.class);

    private static final String HUB_BASE_URL = "hubBaseUrl";

    @Autowired
    private HubValidator hubValidator;

    @Autowired
    private SubscriptionService subscriptionService;

    @Autowired
    private ContentDistributionService contentDistributionService;

    private SettingsFacade settingsFacade;

    @Autowired
    public HubController(
            @Qualifier("hubSettings") final SettingsFacade settingsFacade) {
        this.settingsFacade = settingsFacade;
    }

    public HubValidator getHubValidator() {
        return hubValidator;
    }

    public void setHubValidator(HubValidator hubValidator) {
        this.hubValidator = hubValidator;
    }

    public SubscriptionService getSubscriptionService() {
        return subscriptionService;
    }

    public void setSubscriptionService(SubscriptionService subscriptionService) {
        this.subscriptionService = subscriptionService;
    }

    public ContentDistributionService getContentDistributionService() {
        return contentDistributionService;
    }

    public void setContentDistributionService(
            ContentDistributionService contentDistributionService) {
        this.contentDistributionService = contentDistributionService;
    }

    @RequestMapping(value = "/settings", method = RequestMethod.GET)
    @ResponseBody
    public SettingsDTO getSettings() {
        SettingsDTO dto = new SettingsDTO();
        dto.setHubBaseUrl(getPropertyValue(HUB_BASE_URL));
        return dto;
    }

    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(value = "/settings", method = RequestMethod.POST)
    public void saveSettings(@RequestBody SettingsDTO settings)
            throws BundleException {

        settingsFacade.setProperty(HUB_BASE_URL, settings.getHubBaseUrl());
    }

    @ResponseStatus(value = HttpStatus.ACCEPTED)
    @RequestMapping(method = RequestMethod.POST, headers = "Content-Type=application/x-www-form-urlencoded", params = {
            HubConstants.HUB_CALLBACK_PARAM, HubConstants.HUB_MODE_PARAM,
            HubConstants.HUB_TOPIC_PARAM })
    @ResponseBody
    public void subscribe(
            @RequestParam(value = HubConstants.HUB_CALLBACK_PARAM) String callbackUrl,
            @RequestParam(value = HubConstants.HUB_MODE_PARAM) String mode,
            @RequestParam(value = HubConstants.HUB_TOPIC_PARAM) String topic,
            @RequestParam(value = HubConstants.HUB_LEASE_SECONDS_PARAM, required = false) String leaseSeconds,
            @RequestParam(value = HubConstants.HUB_SECRET_PARAM, required = false) String secret)
            throws HubException {

        LOGGER.info("Request to {} started for topic {} from subscriber's callback url '{}'", mode, topic, callbackUrl);
        StopWatch sw = new StopWatch();
        sw.start();
        try {
            List<String> errors = hubValidator.validateSubscription(
                    callbackUrl, mode, topic, leaseSeconds, secret);
            if (!errors.isEmpty()) {
                throw new HubException(ApplicationErrors.BAD_REQUEST,
                        errors.toString());
            }
            Modes hubMode = Modes.valueOf(mode.toUpperCase());

            subscriptionService.subscribe(callbackUrl, hubMode, topic,
                    leaseSeconds, secret);

        } catch (HubException e) {
            LOGGER.error("Error occured while processing request to {} for topic {} from subscriber's callback url {}",
                    mode, topic, callbackUrl);
            throw new RestException(e, e.getMessage() + e.getReason());
        } finally {
            LOGGER.info("Request to {} ended for topic {} from subscriber's callback url '{}'. Time taken (ms) = {}",
                    mode, topic, callbackUrl, sw.getTime());
            sw.stop();
        }
    }

    @ResponseStatus(value = HttpStatus.OK)
    @RequestMapping(method = RequestMethod.POST, headers = "Content-Type=application/x-www-form-urlencoded", params = {
            HubConstants.HUB_MODE_PARAM, HubConstants.HUB_URL_PARAM })
    @ResponseBody
    public void publish(
            @RequestParam(value = HubConstants.HUB_MODE_PARAM) String mode,
            @RequestParam(value = HubConstants.HUB_URL_PARAM) String url)
            throws HubException {

        LOGGER.info("Request to {} started for resource {}", mode, url);
        StopWatch sw = new StopWatch();
        sw.start();
        try {
            List<String> errors = hubValidator.validatePing(mode, url);
            if (!errors.isEmpty()) {
                throw new HubException(ApplicationErrors.BAD_REQUEST,
                        errors.toString());
            }

            contentDistributionService.distribute(url);

        } catch (HubException e) {
            LOGGER.error("Error occurred while processing request to {} the resource {}", mode, url);
            throw new RestException(e, e.getMessage() + e.getReason());
        } finally {
            LOGGER.info("Request to {} ended for resource {}. Time taken (ms) = {}", mode, url, sw.getTime());
            sw.stop();
        }
    }

    @ExceptionHandler(value = { RestException.class })
    @ResponseBody
    public HubError restExceptionHandler(RestException ex,
            HttpServletResponse response) {
        HubError error = new HubError();

        response.setStatus(ex.getHttpStatus().value());
        error.setErrorCode(String.valueOf(ex.getHubException().getErrorCode()));
        error.setErrorMessage(ex.getHubException().getErrorMessage());
        error.setApplication(HubConstants.APP_NAME);

        return error;
    }

    private String getPropertyValue(final String propertyKey) {
        String propertyValue = settingsFacade.getProperty(propertyKey);
        return isNotBlank(propertyValue) ? propertyValue : null;
    }
}
