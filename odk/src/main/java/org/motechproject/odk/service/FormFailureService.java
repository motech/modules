package org.motechproject.odk.service;

import org.motechproject.odk.domain.FormFailure;

/**
 * Service for CRUD operations on {@link FormFailure}
 */
public interface FormFailureService {

    /**
     * Creates a {@link FormFailure}
     *
     * @param formFailure
     */
    void create(FormFailure formFailure);
}
