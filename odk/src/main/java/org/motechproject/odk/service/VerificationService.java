package org.motechproject.odk.service;

import org.motechproject.odk.domain.Configuration;
import org.motechproject.odk.domain.Verification;

/**
 * A service that verifies connections to the various external applications.
 */
public interface VerificationService {

    /**
     * Verifies that a connection to a KoboToolBox service is working.
     * @param configuration {@link Configuration}
     * @return A {@link Verification}. True if working; false otherwise.
     */
    Verification verifyKobo(Configuration configuration);


    /**
     * Verifies that a connection to an Ona service is working.
     * @param configuration {@link Configuration}
     * @return A {@link Verification}. True if working; false otherwise.
     */
    Verification verifyOna(Configuration configuration);


    /**
     * Verifies that a connection to an ODK service is working.
     * @param configuration {@link Configuration}
     * @return A {@link Verification}. True if working; false otherwise.
     */
    Verification verifyOdk(Configuration configuration);


}
