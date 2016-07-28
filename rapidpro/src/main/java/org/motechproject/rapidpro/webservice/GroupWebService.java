package org.motechproject.rapidpro.webservice;

import org.motechproject.rapidpro.exception.WebServiceException;
import org.motechproject.rapidpro.webservice.dto.Group;

/*
 * Webservice interface for RapidPro REST API for Groups.
 */
public interface GroupWebService {

    Group getGroupByName(String groupName) throws WebServiceException;
}
