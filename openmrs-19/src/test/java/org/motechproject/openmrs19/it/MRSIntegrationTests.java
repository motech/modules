package org.motechproject.openmrs19.it;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({MRSConceptServiceIT.class, MRSEncounterServiceIT.class, MRSFacilityServiceIT.class,
        MRSObservationServiceIT.class, MRSPatientServiceIT.class, MRSPersonServiceIT.class, MRSProviderServiceIT.class,
        MRSUserServiceIT.class})
public class MRSIntegrationTests {
}