package org.motechproject.openmrs.it.version1_12;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.motechproject.openmrs.it.MRSConceptServiceIT;
import org.motechproject.openmrs.it.MRSEncounterServiceIT;
import org.motechproject.openmrs.it.MRSLocationServiceIT;
import org.motechproject.openmrs.it.MRSObservationServiceIT;
import org.motechproject.openmrs.it.MRSPatientServiceIT;
import org.motechproject.openmrs.it.MRSPersonServiceIT;
import org.motechproject.openmrs.it.MRSProviderServiceIT;
import org.motechproject.openmrs.it.MRSTasksIntegrationBundleIT;
import org.motechproject.openmrs.it.MRSUserServiceIT;

@RunWith(Suite.class)
@Suite.SuiteClasses({MRSTaskIntegrationBundleIT.class, MRSTasksIntegrationBundleIT.class, MRSConceptServiceIT.class,
        MRSEncounterServiceIT.class, MRSLocationServiceIT.class, MRSObservationServiceIT.class, MRSPatientServiceIT.class,
        MRSPersonServiceIT.class, MRSProviderServiceIT.class, MRSUserServiceIT.class, MRSProgramEnrollmentIT.class})
public class MRSIntegrationTests {
}
