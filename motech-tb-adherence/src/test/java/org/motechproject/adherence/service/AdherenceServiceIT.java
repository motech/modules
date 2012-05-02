package org.motechproject.adherence.service;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.motechproject.adherence.common.SpringIntegrationTest;
import org.motechproject.adherence.domain.DosageLog;
import org.motechproject.adherence.repository.AllDosageLogs;
import org.motechproject.util.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;

import static junit.framework.Assert.assertNotNull;

public class AdherenceServiceIT extends SpringIntegrationTest {

    @Autowired
    AllDosageLogs allDosageLogs;

    AdherenceService adherenceService;

    @Before
    public void setUp() {
        adherenceService = new AdherenceService(allDosageLogs);
    }

    @After
    public void tearDown() {
        markForDeletion(allDosageLogs.getAll().toArray());
    }

    @Test
    public void shouldRecordAdherence() {
        DosageLog dosageLog = adherenceService.recordAdherence("patientId", "treatmentCourseId", DateUtil.today(), 2, 10, null);
        assertNotNull(dosageLog.getId());
    }

}
