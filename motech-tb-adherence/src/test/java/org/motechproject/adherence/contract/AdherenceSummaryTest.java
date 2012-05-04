package org.motechproject.adherence.contract;

import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;
import org.motechproject.adherence.domain.AdherenceLog;
import org.motechproject.util.DateUtil;

import java.util.List;

import static java.util.Arrays.asList;
import static junit.framework.Assert.assertEquals;

public class AdherenceSummaryTest {

    List<AdherenceLog> adherenceLogs;
    List<AdherenceLog> anotherSetOfLogs;

    String externalId = "externalId";
    String treatmentId = "treatmentId";
    DateTime asOf = DateUtil.now();

    @Before
    public void setup() {
        adherenceLogs = asList(
                new AdherenceLog(externalId, treatmentId, asOf).doseCounts(1, 1),
                new AdherenceLog(externalId, treatmentId, asOf).doseCounts(2, 2)
        );
        anotherSetOfLogs = asList(
                new AdherenceLog(externalId, treatmentId, asOf).doseCounts(2, 2),
                new AdherenceLog(externalId, treatmentId, asOf).doseCounts(2, 2)
        );
    }

    @Test
    public void shouldSummarizeDosesTaken() {
        assertEquals(3, new AdherenceSummary(externalId, treatmentId, asOf, adherenceLogs).totalDosesTaken());
        assertEquals(4, new AdherenceSummary(externalId, treatmentId, asOf, anotherSetOfLogs).totalDosesTaken());
    }

    @Test
    public void shouldSummarizeIdealDoses() {
        assertEquals(6, new AdherenceSummary(externalId, treatmentId, asOf, adherenceLogs).totalIdealDoses());
        assertEquals(8, new AdherenceSummary(externalId, treatmentId, asOf, anotherSetOfLogs).totalIdealDoses());
    }

    @Test
    public void shouldBeIdempotentOnDosesTaken() {
        AdherenceSummary adherenceSummary = new AdherenceSummary(externalId, treatmentId, asOf, adherenceLogs);
        assertEquals(3, adherenceSummary.totalDosesTaken());
        assertEquals(3, adherenceSummary.totalDosesTaken());
    }

    @Test
    public void shouldBeIdempotentOnIdealDoses() {
        AdherenceSummary adherenceSummary = new AdherenceSummary(externalId, treatmentId, asOf, adherenceLogs);
        assertEquals(6, adherenceSummary.totalIdealDoses());
        assertEquals(6, adherenceSummary.totalIdealDoses());
    }

}
