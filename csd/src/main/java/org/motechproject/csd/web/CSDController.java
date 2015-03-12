package org.motechproject.csd.web;

import org.motechproject.csd.client.CSDHttpClient;
import org.motechproject.csd.service.CSDService;
import org.motechproject.csd.service.ConfigService;
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
            throw new IllegalArgumentException("Xml url cannot be null");
        }

        String xml = csdHttpClient.getXml(xmlUrl);

        if (xml == null) {
            throw new IllegalArgumentException("Cannot load xml");
        }

        csdService.saveFromXml(xml);
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ResponseBody
    public String handleException(Exception e) throws IOException {
        return e.getMessage();
    }
}
