package org.motechproject.hub.web;

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

import javax.servlet.http.HttpServletResponse;
import java.util.List;

import static org.apache.commons.lang.StringUtils.isNotBlank;

/**
 * The controller responsible for subscribing/distributing content and
 * hub settings management.
 */
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

    /**
     * Creates an instance of HubController using autowired SettingsFacade instance.
     *
     * @param settingsFacade autowired SettingsFacade settings
     */
    @Autowired
    public HubController(
            @Qualifier("hubSettings") final SettingsFacade settingsFacade) {
        this.settingsFacade = settingsFacade;
    }

    /**
     * Gets the {@link org.motechproject.hub.validation.HubValidator} responsible
     * for request input parameters validation.
     *
     * @return the hub validator
     */
    public HubValidator getHubValidator() {
        return hubValidator;
    }

    /**
     * Sets the {@link org.motechproject.hub.validation.HubValidator} responsible
     * for request input parameters validation.
     *
     * @param hubValidator <code>HubValidator</code> to be set
     */
    public void setHubValidator(HubValidator hubValidator) {
        this.hubValidator = hubValidator;
    }

    /**
     * Gets the <code>SubscriptionService</code> responsible for performing
     * subscribe and unsubscribe actions.
     *
     * @return the subscription service
     */
    public SubscriptionService getSubscriptionService() {
        return subscriptionService;
    }

    /**
     * Sets the subscription service responsible for performing
     * subscribe and unsubscribe actions.
     *
     * @param subscriptionService the <code>SubscriptionService</code> to be set
     */
    public void setSubscriptionService(SubscriptionService subscriptionService) {
        this.subscriptionService = subscriptionService;
    }

    /**
     * Gets the <code>ContentDistributionService</code> responsible for distributing
     * content to subscribers.
     *
     * @return the content distribution service
     */
    public ContentDistributionService getContentDistributionService() {
        return contentDistributionService;
    }

    /**
     * Sets the content distribution service responsible for distributing
     * content to subscribers.
     *
     * @param contentDistributionService the <code>ContentDistributionService</code> to be set
     */
    public void setContentDistributionService(
            ContentDistributionService contentDistributionService) {
        this.contentDistributionService = contentDistributionService;
    }

    /**
     * Gets hub settings. The settings are returned in form
     * of {@link org.motechproject.hub.model.SettingsDTO}
     *
     * @return hub settings as a <code>SettingsDTO</code>
     */
    @RequestMapping(value = "/settings", method = RequestMethod.GET)
    @ResponseBody
    public SettingsDTO getSettings() {
        SettingsDTO dto = new SettingsDTO();
        dto.setHubBaseUrl(getPropertyValue(HUB_BASE_URL));
        return dto;
    }

    /**
     * Saves hub settings passed as a parameter. The settings must be passed as a
     * {@link org.motechproject.hub.model.SettingsDTO}, from which hub base URL is
     * taken and set.
     *
     * @param settings {@link org.motechproject.hub.model.SettingsDTO} containing hub base URL
     * @throws BundleException if setting hub base url encounters a bundle lifecycle error
     */
    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(value = "/settings", method = RequestMethod.POST)
    public void saveSettings(@RequestBody SettingsDTO settings)
            throws BundleException {

        settingsFacade.setProperty(HUB_BASE_URL, settings.getHubBaseUrl());
    }

    /**
     * Subscribes to topic using passed parameters.
     *
     * @param callbackUrl
     *            - a <code>String</code> representing the subscriber's callback
     *            URL where notifications should be delivered
     * @param mode
     *            - represents the literal <code>String</code> "subscribe" or
     *            "unsubscribe", depending on the goal of the request
     * @param topic
     *            - a <code>String</code> representing the topic URL that the
     *            subscriber wishes to subscribe to or unsubscribe from
     * @param leaseSeconds
     *            - a <code>String</code> representing the number of seconds for
     *            which the subscriber would like to have the subscription
     *            active. This is an optional parameter
     * @param secret
     *            - a <code>String</code> representing the secret value which
     *            will be used by hub to compute an HMAC digest for authorized
     *            content distribution. Secret parameter must be less than 200
     *            bytes in length.
     * @throws HubException if one or more parameters is not valid
     */
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

    /**
     * Distibutes content fetched from URL passed as a parameter.
     *
     * @param mode
     *            - represents the literal <code>String</code> "publish"
     * @param url
     *            - a <code>String</code> representing the url of the topic that
     *            was updated
     * @throws HubException if one or more parameters is not valid
     */
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

    /**
     * Handles incoming {@link org.motechproject.hub.exception.RestException} when
     * it occurs.
     *
     * @param ex the exception to be handled
     * @param response http servlet response
     * @return
     *         - {@link org.motechproject.hub.exception.HubError} object containing
     *         detailed information about the problem that occured
     */
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
