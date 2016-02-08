package org.motechproject.odk.service.impl;

import org.motechproject.odk.domain.FormFailure;
import org.motechproject.odk.repository.FormFailureDataService;
import org.motechproject.odk.service.FormFailureService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Implementation of {@link FormFailureService}.
 */
@Service("odkFormFailureService")
public class FormFailureServiceImpl implements FormFailureService {

    @Autowired
    private FormFailureDataService formFailureDataService;

    @Override
    public void create(FormFailure formFailure) {
        formFailureDataService.create(formFailure);
    }
}
