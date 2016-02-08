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

    /**
     * Queries for a {@link FormInstance} by its instance ID.
     * @param instanceId The unique ID for the form instance.
     * @return {@link FormInstance} if it exists.
     */
    FormInstance getByInstanceId(String instanceId);
}
