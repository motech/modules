package org.motechproject.adherence.repository;

import org.ektorp.ComplexKey;
import org.ektorp.CouchDbConnector;
import org.ektorp.ViewQuery;
import org.ektorp.support.View;
import org.joda.time.LocalDate;
import org.motechproject.adherence.domain.AdherenceAuditLog;
import org.motechproject.adherence.domain.AdherenceLog;
import org.motechproject.dao.MotechBaseRepository;
import org.motechproject.util.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class AllAdherenceLogs extends MotechBaseRepository<AdherenceLog> {

    private AllAdherenceAuditLogs allAdherenceAuditLogs;

    @Autowired
    protected AllAdherenceLogs(@Qualifier("adherenceDbConnector") CouchDbConnector db, AllAdherenceAuditLogs allAdherenceAuditLogs) {
        super(AdherenceLog.class, db);
        this.allAdherenceAuditLogs = allAdherenceAuditLogs;
    }

    @Override
    public void add(AdherenceLog adherenceLog){
        throw new RuntimeException("this method should not be called.");
    }

    public void add(AdherenceLog adherenceLog, String user, String source) {
        AdherenceLog existingLog = findLogBy(adherenceLog.externalId(), adherenceLog.treatmentId(), adherenceLog.doseDate());
        String adherenceLogId = null;
        if (null == existingLog) {
            super.add(adherenceLog);
            adherenceLogId = adherenceLog.getId();
        } else {
            existingLog.status(adherenceLog.status());
            update(existingLog);
            adherenceLogId = existingLog.getId();
        }

        AdherenceAuditLog auditLog = new AdherenceAuditLog(user, source, adherenceLogId, adherenceLog.status(), DateUtil.now());
        allAdherenceAuditLogs.add(auditLog);
    }

    public void update(AdherenceLog adherenceLog, String user, String source) {
        update(adherenceLog);
        AdherenceAuditLog auditLog = new AdherenceAuditLog(user, source, adherenceLog.getId(), adherenceLog.status(), DateUtil.now());
        allAdherenceAuditLogs.add(auditLog);
    }

    @View(name = "by_externaId_treatmentId_andDosageDate", map = "function(doc) {if (doc.type =='AdherenceLog') {emit([doc.externalId, doc.treatmentId, doc.doseDate], doc._id);}}")
    public List<AdherenceLog> findLogsBy(String externalId, String treatmentId, LocalDate asOf) {
        final ComplexKey startKey = ComplexKey.of(externalId, treatmentId, null);
        final ComplexKey endKey = ComplexKey.of(externalId, treatmentId, asOf);
        ViewQuery q = createQuery("by_externaId_treatmentId_andDosageDate").startKey(startKey).endKey(endKey).inclusiveEnd(true).includeDocs(true);
        return db.queryView(q, AdherenceLog.class);
    }


    public List<AdherenceLog> findLogsBy(String externalId, String treatmentId, LocalDate fromDate, LocalDate toDate) {
        final ComplexKey startKey = ComplexKey.of(externalId, treatmentId, fromDate);
        final ComplexKey endKey = ComplexKey.of(externalId, treatmentId, toDate);
        ViewQuery q = createQuery("by_externaId_treatmentId_andDosageDate").startKey(startKey).endKey(endKey).inclusiveEnd(true).includeDocs(true);
        return db.queryView(q, AdherenceLog.class);
    }

    protected AdherenceLog findLogBy(String externalId, String treatmentId, LocalDate asOf) {
        final ComplexKey key = ComplexKey.of(externalId, treatmentId, asOf);
        ViewQuery q = createQuery("by_externaId_treatmentId_andDosageDate").key(key).includeDocs(true);
        return singleResult(db.queryView(q, AdherenceLog.class));
    }

    @View(name = "by_dosageDate", map = "function(doc) {if (doc.type =='AdherenceLog') {emit(doc.doseDate, doc._id);}}")
    public List<AdherenceLog> findLogsAsOf(LocalDate asOf) {
        ViewQuery q = createQuery("by_dosageDate").endKey(asOf).inclusiveEnd(true).includeDocs(true);
        return db.queryView(q, AdherenceLog.class);
    }
}
