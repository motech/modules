package org.motechproject.openmrs.domain;

import org.joda.time.DateTime;
import org.motechproject.openmrs.domain.Concept;
import org.motechproject.openmrs.domain.Observation;
import org.motechproject.openmrs.domain.Person;

public class ObservationDummyData {
    private static final String CONCEPT_UUID = "b8be9fcf-91bc-41c5-ba8e-0434343990a2";
    private static final String PERSON_UUID = "c10463d2-a811-4a4b-a3d9-76a932ba6e75";
    private static final String OBSERVATION_UUID = "01a4703c-f89f-4316-803b-dc9d08da5a0a";
    private static final String OBSERVATION_DATE_TIME = "2016-07-29T10:29:50.000+0000";
    /**
     * Prepares dummy observation.
     *
     * @return the created observation
     */
    public static Observation prepareObservationWithConceptValue() {
        Concept concept = new Concept();
        concept.setUuid(CONCEPT_UUID);

        Observation observation = new Observation(OBSERVATION_UUID, "TestConcept: 1", concept, new Person(PERSON_UUID),
                new DateTime(OBSERVATION_DATE_TIME).toDate());
        observation.setValue(new Observation.ObservationValue("conceptValueUuid", "conceptValueDisplay"));

        return observation;
    }

    /**
     * Utility class, should not be initiated.
     */
    private ObservationDummyData() {
    }
}
