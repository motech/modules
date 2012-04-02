package org.motechproject.adherence.dao;

import org.apache.commons.lang.StringUtils;
import org.ektorp.CouchDbConnector;
import org.joda.time.LocalDate;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.motechproject.adherence.domain.AdherenceLog;
import org.motechproject.adherence.domain.Concept;
import org.motechproject.adherence.service.AdherenceService;
import org.motechproject.model.MotechBaseDataObject;
import org.motechproject.util.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.*;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNull;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"/testAdherenceApplicationContext.xml"})
public class AllAdherenceLogsIT {

    @Autowired
    private AllAdherenceLogs allAdherenceLogs;

    @Autowired
    @Qualifier("adherenceDbConnector")
    private CouchDbConnector couchDbConnector;

    private List<MotechBaseDataObject> entities = new ArrayList<MotechBaseDataObject>();

    private String externalId = "externalId";
    private Concept concept;

    @Before
    public void setUp() throws Exception {
        concept = new Concept("conceptId", "tokenId");
    }

    @Test
    public void shouldPersistAdherenceLog() {
        AdherenceLog adherenceLog = new AdherenceLog();
        adherenceLog.setConcept(concept);
        allAdherenceLogs.add(adherenceLog);
        entities.add(adherenceLog);
        assertEquals(adherenceLog, allAdherenceLogs.get(adherenceLog.getId()));
    }

    @Test
    public void shouldNotInsertWhenEnclosingLogExists() {
        AdherenceLog latestLog = new AdherenceLog();
        latestLog.setDateRange(DateUtil.newDate(2011, 12, 1), DateUtil.newDate(2011, 12, 31));
        latestLog.setExternalId(externalId);

        AdherenceLog newLog = new AdherenceLog();
        newLog.setDateRange(DateUtil.newDate(2011, 12, 30), DateUtil.newDate(2011, 12, 30));
        newLog.setExternalId(externalId);

        allAdherenceLogs.add(latestLog);
        allAdherenceLogs.insert(newLog);

        entities.addAll(Arrays.asList(latestLog, newLog));
        List<AdherenceLog> results = allAdherenceLogs.getAll();
        assertEquals(1, results.size());
        entities.addAll(results);

        assertHasDateRange(results.get(0), DateUtil.newDate(2011, 12, 1), DateUtil.newDate(2011, 12, 31));
    }

    @Test
    public void shouldCutOverlappingLogs() {
        AdherenceLog latestLog = new AdherenceLog();
        latestLog.setDateRange(DateUtil.newDate(2011, 12, 1), DateUtil.newDate(2011, 12, 31));
        latestLog.setExternalId(externalId);

        AdherenceLog newLog = new AdherenceLog();
        newLog.setDateRange(DateUtil.newDate(2011, 12, 2), DateUtil.newDate(2012, 1, 1));
        newLog.setExternalId(externalId);

        allAdherenceLogs.add(latestLog);
        allAdherenceLogs.insert(newLog);

        entities.addAll(Arrays.asList(latestLog, newLog));
        List<AdherenceLog> results = allAdherenceLogs.getAll();
        assertEquals(2, results.size());
        entities.addAll(results);

        assertHasDateRange(results.get(0), DateUtil.newDate(2011, 12, 1), DateUtil.newDate(2011, 12, 31));
        assertHasDateRange(results.get(1), DateUtil.newDate(2012, 1, 1), DateUtil.newDate(2012, 1, 1));
    }

    @Test
    public void shouldPersistMetaInformation() {
        LocalDate today = DateUtil.today();
        Map<String, Object> meta = new HashMap<String, Object>() {{
            put("label", "value");
        }};
        AdherenceLog adherenceLog = AdherenceLog.create("externalId", AdherenceService.GENERIC_CONCEPT, today);
        adherenceLog.setMeta(meta);
        allAdherenceLogs.add(adherenceLog);
        entities.add(adherenceLog);
        assertEquals("value", allAdherenceLogs.findByDate("externalId", AdherenceService.GENERIC_CONCEPT, today).getMeta().get("label"));
    }

    @Test
    public void shouldRetrieveLogByDate() {
        AdherenceLog newerLog = new AdherenceLog();
        newerLog.setDateRange(DateUtil.newDate(2012, 1, 1), DateUtil.newDate(2012, 1, 30));
        newerLog.setExternalId(externalId);

        AdherenceLog currentLog = new AdherenceLog();
        currentLog.setDateRange(DateUtil.newDate(2011, 12, 1), DateUtil.newDate(2011, 12, 31));
        currentLog.setExternalId(externalId);

        AdherenceLog olderLog = new AdherenceLog();
        olderLog.setDateRange(DateUtil.newDate(2011, 11, 1), DateUtil.newDate(2011, 11, 30));
        olderLog.setExternalId(externalId);

        allAdherenceLogs.add(currentLog);
        allAdherenceLogs.add(newerLog);
        allAdherenceLogs.add(olderLog);

        entities.addAll(Arrays.asList(currentLog, newerLog, olderLog));

        assertEquals(currentLog, allAdherenceLogs.findByDate(externalId, AdherenceService.GENERIC_CONCEPT, DateUtil.newDate(2011, 12, 2)));
    }

    @Test
    public void shouldNotRetrieveLogBeforeFromDate() {
        AdherenceLog adherenceLog = new AdherenceLog();
        LocalDate fromDate = DateUtil.newDate(2011, 12, 1);
        adherenceLog.setExternalId(externalId);
        adherenceLog.setDateRange(fromDate, DateUtil.newDate(2011, 12, 31));
        allAdherenceLogs.add(adherenceLog);
        entities.add(adherenceLog);
        assertNull(allAdherenceLogs.findByDate(externalId, AdherenceService.GENERIC_CONCEPT, DateUtil.newDate(2011, 11, 30)));
    }

    @Test
    public void shouldRetrieveLogOnFromDate() {
        AdherenceLog adherenceLog = new AdherenceLog();
        LocalDate fromDate = DateUtil.newDate(2011, 12, 1);
        adherenceLog.setDateRange(fromDate, DateUtil.newDate(2011, 12, 31));
        adherenceLog.setExternalId(externalId);
        allAdherenceLogs.add(adherenceLog);

        entities.add(adherenceLog);
        assertEquals(adherenceLog, allAdherenceLogs.findByDate(externalId, AdherenceService.GENERIC_CONCEPT, fromDate));
    }

    @Test
    public void shouldNotRetrieveLogAfterToDate() {
        AdherenceLog adherenceLog = new AdherenceLog();
        adherenceLog.setExternalId(externalId);
        LocalDate toDate = DateUtil.newDate(2011, 12, 31);
        adherenceLog.setDateRange(DateUtil.newDate(2011, 12, 1), toDate);
        allAdherenceLogs.add(adherenceLog);
        entities.add(adherenceLog);
        assertNull(allAdherenceLogs.findByDate(externalId, AdherenceService.GENERIC_CONCEPT, DateUtil.newDate(2012, 1, 1)));
    }

    @Test
    public void shouldRetrieveLogOnToDate() {
        AdherenceLog adherenceLog = new AdherenceLog();
        adherenceLog.setExternalId(externalId);
        LocalDate toDate = DateUtil.newDate(2011, 12, 31);
        adherenceLog.setDateRange(DateUtil.newDate(2011, 12, 1), toDate);
        allAdherenceLogs.add(adherenceLog);
        entities.add(adherenceLog);
        assertEquals(adherenceLog, allAdherenceLogs.findByDate(externalId, AdherenceService.GENERIC_CONCEPT, toDate));
    }

    @Test
    public void shouldFindTheLatestLogForExternalEntity() {
        AdherenceLog newerLog = new AdherenceLog();
        newerLog.setDateRange(DateUtil.newDate(2012, 1, 1), DateUtil.newDate(2012, 1, 30));
        newerLog.setExternalId(externalId);
        newerLog.setConcept(concept);

        AdherenceLog olderLog = new AdherenceLog();
        olderLog.setDateRange(DateUtil.newDate(2011, 11, 1), DateUtil.newDate(2011, 11, 30));
        olderLog.setExternalId(externalId);
        olderLog.setConcept(concept);

        allAdherenceLogs.add(newerLog);
        allAdherenceLogs.add(olderLog);

        entities.addAll(Arrays.asList(newerLog, olderLog));

        assertEquals(newerLog, allAdherenceLogs.findLatestLog(externalId, concept));
    }

    @Test
    public void shouldFindLogsBetweenTwoDates() {
        AdherenceLog newerLog = new AdherenceLog();
        newerLog.setDateRange(DateUtil.newDate(2012, 1, 1), DateUtil.newDate(2012, 1, 30));
        newerLog.setExternalId(externalId);

        AdherenceLog currentLog = new AdherenceLog();
        currentLog.setDateRange(DateUtil.newDate(2011, 12, 1), DateUtil.newDate(2011, 12, 31));
        currentLog.setExternalId(externalId);

        AdherenceLog olderLog = new AdherenceLog();
        olderLog.setDateRange(DateUtil.newDate(2011, 11, 1), DateUtil.newDate(2011, 11, 30));
        olderLog.setExternalId(externalId);

        allAdherenceLogs.add(currentLog);
        allAdherenceLogs.add(newerLog);
        allAdherenceLogs.add(olderLog);

        entities.addAll(Arrays.asList(currentLog, newerLog, olderLog));

        assertEquals(olderLog, allAdherenceLogs.findLogsBetween(externalId, AdherenceService.GENERIC_CONCEPT, DateUtil.newDate(2011, 11, 3), DateUtil.newDate(2011, 12, 3)).get(0));
        assertEquals(currentLog, allAdherenceLogs.findLogsBetween(externalId, AdherenceService.GENERIC_CONCEPT, DateUtil.newDate(2011, 11, 3), DateUtil.newDate(2011, 12, 3)).get(1));
    }

    @Test
    public void shouldFetchLogWithGenericConceptIdGivenAConceptId() {
        AdherenceLog adherenceLog = AdherenceLog.create(externalId, AdherenceService.GENERIC_CONCEPT, DateUtil.today());
        entities.add(adherenceLog);
        allAdherenceLogs.add(adherenceLog);
        assertEquals(adherenceLog, allAdherenceLogs.findLatestLog(externalId, new Concept("refId", "tknId")));
    }

    @After
    public void tearDown() {
        for (MotechBaseDataObject entity : entities) {
            if (StringUtils.isNotEmpty(entity.getId())) {
                try {
                    couchDbConnector.delete(entity);
                } catch (Exception e) {

                }
            }
        }
        entities.removeAll(entities);
    }

    private void assertHasDateRange(AdherenceLog adherenceLog, LocalDate expectedFromDate, LocalDate expectedToDate) {
        assertEquals(expectedFromDate, adherenceLog.getFromDate());
        assertEquals(expectedToDate, adherenceLog.getToDate());
    }
}
