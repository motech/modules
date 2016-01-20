package org.motechproject.odk.service;

import org.motechproject.odk.domain.FormInstance;

/**
 * Service for CRUD operations on {@link FormInstance}
 */
public interface FormInstanceService {

    /**
     * Saves a form instance
     * @param formInstance {@link FormInstance}
     */
    void create(FormInstance formInstance);
    FormInstance getByInstanceId(String instanceId);
}
