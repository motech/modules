package org.motechproject.commcare.service.impl;

import org.motechproject.commcare.domain.CommcareAccountSettings;
import org.motechproject.commcare.exception.CommcareAuthenticationException;
import org.motechproject.commcare.exception.CommcareConnectionFailureException;
import org.motechproject.commcare.service.CommcareAccountService;
import org.motechproject.commcare.util.CommCareAPIHttpClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class StubCommcareAccountServiceImpl implements CommcareAccountService {

    @Autowired
    private CommCareAPIHttpClient commcareHttpClient;

    @Override
    public boolean verifySettings(CommcareAccountSettings commcareAccountSettings) throws CommcareConnectionFailureException, CommcareAuthenticationException {
        if(commcareHttpClient.verifyConnection()) {
            return true;
        } else {
            throw new CommcareAuthenticationException("Motech was unable to authenticate to CommCareHQ. Please verify your account settings.");
        }
    }
}
