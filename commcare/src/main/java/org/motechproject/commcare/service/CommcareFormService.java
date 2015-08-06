package org.motechproject.commcare.service;

import org.motechproject.commcare.domain.CommcareForm;
import org.motechproject.commcare.domain.CommcareFormList;
import org.motechproject.commcare.request.FormListRequest;

public interface CommcareFormService {

    /**
     * Retrieves form with the given ID from server represented by the given configuration.
     *
     * @param id  the ID of the form
     * @param configName  the name of the configuration used for connecting to the CommcareHQ server, null means default
     *                    configuration
     * @return  the form with the given ID
     */
    CommcareForm retrieveForm(String id, String configName);

    /**
     * Same as {@link #retrieveForm(String, String) retrieveForm} but uses default Commcare configuration.
     */
    CommcareForm retrieveForm(String id);

    /**
     * Retrieves a list of forms from Commcare HQ along with metadata. Can be used for browsing through forms.
     * @param request the request specifying the details of the query
     * @return the form list received from Commcare HQ
     */
    CommcareFormList retrieveFormList(FormListRequest request);

    /**
     * Retrieves a list of forms from Commcare HQ along with metadata. Can be used for browsing through forms.
     * @param request the request specifying the details of the query
     * @param configName the name of the configuration used for connecting to the CommcareHQ server, null means default
     *                   configuration
     * @return the form list received from Commcare HQ
     */
    CommcareFormList retrieveFormList(FormListRequest request, String configName);

    /**
     * Retrieves form with the given ID from server represented by the given configuration.
     *
     * @param id  the ID of the form
     * @param configName  the name of the configuration used for connecting to the CommcareHQ server, null means default
     *                    configuration
     * @return  the form with the given ID as JSON
     */
    String retrieveFormJson(String id, String configName);

    /**
     * Same as {@link #retrieveForm(String, String) retrieveFormJson} but uses default Commcare configuration.
     */
    String retrieveFormJson(String id);
}
