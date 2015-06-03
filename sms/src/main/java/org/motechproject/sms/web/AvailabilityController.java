package org.motechproject.sms.web;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.List;

import static org.motechproject.sms.util.Constants.MANAGE_SMS_PERMISSION;
import static org.motechproject.sms.util.Constants.VIEW_SMS_LOGS_PERMISSION;

/**
 * Spring controller, responsible for providing the view layer with information
 * about available actions, for the currently logged user.
 */
@Controller
public class AvailabilityController {

    @RequestMapping(value = "/available/smsTabs", method = RequestMethod.GET)
    @ResponseBody
    public List<String> getAvailableTabs() {
        List<String> availableTabs = new ArrayList<>();
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth.getAuthorities().contains(new SimpleGrantedAuthority(MANAGE_SMS_PERMISSION))) {
            availableTabs.add("send");
            availableTabs.add("settings");
        }

        if (auth.getAuthorities().contains(new SimpleGrantedAuthority(VIEW_SMS_LOGS_PERMISSION))) {
            if (availableTabs.isEmpty()) {
                availableTabs.add("log");
            } else {
                availableTabs.add(1, "log");
            }
        }

        return availableTabs;
    }
}
