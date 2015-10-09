package org.motechproject.mtraining.web;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.List;

/**
 * Spring controller, responsible for providing the view layer with information
 * about available actions, for the currently logged user.
 */
@Controller
public class AvailabilityController {

    private static final String MANAGE_MTRAINING = "manageMTraining";
    private static final String VIEW_MTRAINING_LOGS = "viewMTrainingLogs";

    @RequestMapping(value = "/available/mTrainingTabs", method = RequestMethod.GET)
    @ResponseBody
    public List<String> getAvailableTabs() {
        List<String> availableTabs = new ArrayList<>();
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth.getAuthorities().contains(new SimpleGrantedAuthority(MANAGE_MTRAINING))) {
            availableTabs.add("treeView");
            availableTabs.add("courses");
            availableTabs.add("chapters");
            availableTabs.add("quizzes");
            availableTabs.add("lessons");
        }

        if (auth.getAuthorities().contains(new SimpleGrantedAuthority(VIEW_MTRAINING_LOGS))) {
            availableTabs.add("activityRecords");
            availableTabs.add("bookmarks");
        }

        return availableTabs;
    }
}

