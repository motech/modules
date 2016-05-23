package org.motechproject.csd.web;

import org.apache.commons.lang.StringUtils;
import org.motechproject.csd.scheduler.CSDScheduler;
import org.motechproject.csd.service.CSDService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.xml.sax.SAXParseException;

import javax.xml.bind.UnmarshalException;
import java.io.IOException;

import static org.motechproject.csd.constants.CSDConstants.HAS_MANAGE_CSD_PERMISSION;

@Controller
public class CSDController {

    private static final Logger LOGGER = LoggerFactory.getLogger(CSDController.class);

    @Autowired
    private CSDService csdService;

    @Autowired
    private CSDScheduler csdScheduler;

    @RequestMapping(value = "/csd-consume", method = RequestMethod.POST)
    @PreAuthorize(HAS_MANAGE_CSD_PERMISSION)
    @ResponseBody
    public void consume(@RequestBody String xmlUrl) {
        csdService.fetchAndUpdate(xmlUrl);
        csdScheduler.sendCustomUpdateEventMessage(xmlUrl);
    }

    @RequestMapping(value = "/csd-getXml", method = RequestMethod.GET, produces = MediaType.APPLICATION_XML_VALUE)
    @ResponseBody
    public String getXml() {
        return csdService.getXmlContent();
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    public String handleException(Exception e) throws IOException {
        String message = e.getMessage();
        LOGGER.error(message, e);
        if (e.getCause() != null && e.getCause() instanceof UnmarshalException) {
            UnmarshalException cause = (UnmarshalException) e.getCause();
            if (cause.getLinkedException() != null && cause.getLinkedException() instanceof SAXParseException) {
                SAXParseException parseException = (SAXParseException) cause.getLinkedException();
                message += " -" + StringUtils.substringAfter(parseException.getMessage(), ":") +
                        " (line " + parseException.getLineNumber() + ")";
            }
        }
        return message;
    }
}
