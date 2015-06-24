package org.motechproject.pillreminder.domain;

import org.joda.time.DateTime;
import org.junit.After;
import org.junit.Test;
import org.motechproject.commons.date.model.Time;
import org.motechproject.commons.date.util.DateUtil;

import java.util.HashSet;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.motechproject.testing.utils.TimeFaker.fakeNow;
import static org.motechproject.testing.utils.TimeFaker.stopFakingTime;

public class DosageCapturedTest {

    @After
    public void tearDown() {
        stopFakingTime();
    }

    @Test
    public void isDosageCapturedForToday() {
        DateTime now = new DateTime(2012, 4, 16, 8, 30);
        Dosage dosage = new Dosage(new Time(9, 5), new HashSet<Medicine>());

        fakeNow(now);

        assertFalse(dosage.isTodaysDosageResponseCaptured());

        dosage.setResponseLastCapturedDate(DateUtil.today());
        assertTrue(dosage.isTodaysDosageResponseCaptured());
    }

    @Test
    public void isDosageCapturedForTodayWhenItIsYesterdaysDoseAndPillReminderCallsSpillOverToToday() {
        DateTime now = new DateTime(2012, 4, 16, 0, 30);
        Dosage dosage = new Dosage(new Time(23, 5), new HashSet<Medicine>());

        fakeNow(now);

        assertFalse(dosage.isTodaysDosageResponseCaptured());

        dosage.setResponseLastCapturedDate(DateUtil.today().minusDays(1));
        assertTrue(dosage.isTodaysDosageResponseCaptured());
    }

    @Test
    public void isDosageCapturedForTodayIsFalse_WhenCapturedForYesterday_AndTimeIsAfterPillTime() {
        DateTime now = new DateTime(2012, 4, 16, 9, 30);
        Dosage dosage = new Dosage(new Time(9, 5), new HashSet<Medicine>());

        fakeNow(now);

        dosage.setResponseLastCapturedDate(DateUtil.today().minusDays(1));
        assertFalse(dosage.isTodaysDosageResponseCaptured());
    }

    @Test
    public void isDosageCapturedForTodayIsTrue_WhenCapturedForYesterday_AndTimeIsBeforePillTime() {
        DateTime now = new DateTime(2012, 4, 16, 9, 4);
        Dosage dosage = new Dosage(new Time(9, 5), new HashSet<Medicine>());

        fakeNow(now);

        dosage.setResponseLastCapturedDate(DateUtil.today().minusDays(1));
        assertTrue(dosage.isTodaysDosageResponseCaptured());
    }
}