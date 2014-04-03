package org.motechproject.transliteration.org.motechproject.transliteration.web;

import org.motechproject.transliteration.service.TransliterationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.Map;


/**
 * Handles http requests to {motechserver}/motech-platform-server/module/transliteration/incoming/{word}
 * when they receive an SMS
 */
@Controller
@RequestMapping(value = "/incoming")
public class TransliterationController {
    private Logger logger = LoggerFactory.getLogger(TransliterationController.class);

    @Autowired
    TransliterationService transliterationService;

    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(value = "/{word}", method = RequestMethod.GET)
    @ResponseBody
    public String handleIncoming(@PathVariable String word) {
        String hindiTest = "अभी समय है जनता";
        String result = transliterationService.transliterate(word);
        logger.info("Converting {} from English to Hindi results in {}.", word, result);

        return String.format("<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.01 Transitional//EN\"\n" +
                "\"http://www.w3.org/TR/html4/loose.dtd\">\n" +
                "<html xmlns=\"http://www.w3.org/1999/xhtml\" lang=\"en\" xml:lang=\"en\">\n" +
                "<head>\n" +
                "<title>Untitled Document</title>\n" +
                "<meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\">\n" +
                "</head>" +
                "<body>Converting %s from English to Hindi results in %s. Static: %s</body>" +
                "</html>",
                word, result, hindiTest);
    }
}
