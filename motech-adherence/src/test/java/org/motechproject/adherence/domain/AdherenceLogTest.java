package org.motechproject.adherence.domain;

import org.junit.Test;
import org.motechproject.util.DateUtil;

import static junit.framework.Assert.assertTrue;

public class AdherenceLogTest {

    @Test
    public void shouldEncloseAnotherLogWithASmallerRange() {
        AdherenceLog enclosingLog = new AdherenceLog();
        enclosingLog.setFromDate(DateUtil.newDate(2011, 12, 1));
        enclosingLog.setToDate(DateUtil.newDate(2011, 12, 31));

        AdherenceLog enclosedLog = new AdherenceLog();
        enclosedLog.setFromDate(DateUtil.newDate(2011, 12, 2));
        enclosedLog.setToDate(DateUtil.newDate(2011, 12, 30));
        assertTrue(enclosingLog.encloses(enclosedLog));
    }

    @Test
    public void shouldOverlapWithAnotherLogWithFromDateEqualToEndDateOfThisLog() {
        AdherenceLog log = new AdherenceLog();
        log.setFromDate(DateUtil.newDate(2011, 12, 1));
        log.setToDate(DateUtil.newDate(2011, 12, 31));

        AdherenceLog overlappingLog = new AdherenceLog();
        overlappingLog.setFromDate(DateUtil.newDate(2011, 12, 31));
        overlappingLog.setToDate(DateUtil.newDate(2012, 1, 1));
        assertTrue(log.overlaps(overlappingLog));
    }

    @Test
    public void shouldOverlapWithAnotherLogWithFromDateBeforeEndDateOfThisLog() {
        AdherenceLog log = new AdherenceLog();
        log.setFromDate(DateUtil.newDate(2011, 12, 1));
        log.setToDate(DateUtil.newDate(2011, 12, 31));

        AdherenceLog overlappingLog = new AdherenceLog();
        overlappingLog.setFromDate(DateUtil.newDate(2011, 12, 30));
        overlappingLog.setToDate(DateUtil.newDate(2012, 1, 1));
        assertTrue(log.overlaps(overlappingLog));
    }

    @Test
    public void shouldOverlapWithAnotherLogWithFromDateBeforeFromDateOfThisLog() {
        AdherenceLog log = new AdherenceLog();
        log.setFromDate(DateUtil.newDate(2011, 12, 1));
        log.setToDate(DateUtil.newDate(2011, 12, 31));

        AdherenceLog overlappingLog = new AdherenceLog();
        overlappingLog.setFromDate(DateUtil.newDate(2011, 12, 30));
        overlappingLog.setToDate(DateUtil.newDate(2012, 1, 1));
        assertTrue(log.overlaps(overlappingLog));
    }
}
