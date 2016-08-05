package org.motechproject.rapidpro.webservice;

import org.motechproject.rapidpro.exception.WebServiceException;
import org.motechproject.rapidpro.webservice.dto.Group;

/*
 * Webservice interface for RapidPro REST API for Groups.
 */
public interface GroupWebService {

    /**
     * Finds a group by its name, if it exists.
     * @param groupName The name of the Group.
     * @return {@link Group}
     * @throws WebServiceException If there is an error communicating with RapidPro.
     */
    Group getGroupByName(String groupName) throws WebServiceException;
}
