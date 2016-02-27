package org.motechproject.odk.web;


import org.motechproject.odk.domain.Configuration;
import org.motechproject.odk.domain.Verification;
import org.motechproject.odk.service.VerificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Controller that maps to /verify. Allows users to verfiy that their configuration can
 * connect to an external application.
 */
@Controller
@RequestMapping("/verify")
public class VerifyController {

    @Autowired
    private VerificationService verificationService;

    /**
     * Verifies that a {@link Configuration} can connect to a KoboToolbox implementation
     * @param configuration {@link Configuration}
     * @return {@link Verification} True if successful; false otherwise.
     */
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(value = "/kobo", method = RequestMethod.POST)
    public Verification verifyKobo(@RequestBody Configuration configuration) {
        return verificationService.verifyKobo(configuration);
    }


    /**
     * Verifies that a {@link Configuration} can connect to an Ona implementation
     * @param configuration {@link Configuration}
     * @return {@link Verification} True if successful; false otherwise.
     */
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(value = "/ona", method = RequestMethod.POST)
    public Verification verifyOna(@RequestBody Configuration configuration) {
        return verificationService.verifyOna(configuration);
    }

    /**
     * Verifies that a {@link Configuration} can connect to an ODK implementation
     * @param configuration {@link Configuration}
     * @return {@link Verification} True if successful; false otherwise.
     */
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(value = "/odk", method = RequestMethod.POST)
    public Verification verifyOdk(@RequestBody Configuration configuration) {
        return verificationService.verifyOdk(configuration);
    }
}
