package org.motechproject.csd.web;

import org.motechproject.csd.client.CSDHttpClient;
import org.motechproject.csd.service.CSDService;
import org.motechproject.csd.service.ConfigService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.io.IOException;

@Controller
public class CSDController {

    private static final Logger LOGGER = LoggerFactory.getLogger(CSDController.class);

    private ConfigService configService;

    private CSDHttpClient csdHttpClient;

    private CSDService csdService;

    @Autowired
    public CSDController(@Qualifier("configService") ConfigService configService, CSDHttpClient csdHttpClient, CSDService csdService) {
        this.configService = configService;
        this.csdHttpClient = csdHttpClient;
        this.csdService = csdService;
    }

    @RequestMapping(value = "/csd-consume", method = RequestMethod.GET)
    @ResponseBody
    public void consume() {
        String xmlUrl = configService.getConfig().getXmlUrl();

        if (xmlUrl == null) {
            throw new IllegalArgumentException("The CSD Registry URL is empty");
        }

        String xml = csdHttpClient.getXml(xmlUrl);

        if (xml == null) {
            throw new IllegalArgumentException("Couldn't load XML");
        }

        csdService.saveFromXml(xml);
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    public String handleException(Exception e) throws IOException {
-        LOGGER.error(e.getMessage(), e);
        return e.getMessage();
    }
}
