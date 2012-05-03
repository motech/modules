package org.motechproject.adherence.repository;

import org.ektorp.ComplexKey;
import org.ektorp.CouchDbConnector;
import org.ektorp.ViewQuery;
import org.ektorp.support.View;
import org.joda.time.DateTime;
import org.motechproject.adherence.domain.AdherenceLog;
import org.motechproject.dao.MotechBaseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class AllAdherenceLogs extends MotechBaseRepository<AdherenceLog> {

    @Autowired
    protected AllAdherenceLogs(@Qualifier("adherenceDbConnector") CouchDbConnector db) {
        super(AdherenceLog.class, db);
    }

    @Override
    public void add(AdherenceLog adherenceLog) {
        AdherenceLog existingLog = findLogBy(adherenceLog.externalId(), adherenceLog.treatmentId(), adherenceLog.asOf());
        if (null == existingLog) {
            super.add(adherenceLog);
        } else {
            existingLog.doseCounts(adherenceLog.doseTaken(), adherenceLog.doseMissed());
            update(existingLog);
        }
    }

    @View(name = "by_externaId_treatmentId_asOf", map = "function(doc) {if (doc.type =='AdherenceLog') {emit([doc.externalId, doc.treatmentId, doc.asOf], doc._id);}}")
    public List<AdherenceLog> findLogsBy(String externalId, String treatmentId, DateTime asOf) {
        final ComplexKey startKey = ComplexKey.of(externalId, treatmentId, null);
        final ComplexKey endKey = ComplexKey.of(externalId, treatmentId, asOf);
        ViewQuery q = createQuery("by_externaId_treatmentId_asOf").startKey(startKey).endKey(endKey).inclusiveEnd(true).includeDocs(true);
        return db.queryView(q, AdherenceLog.class);
    }

    protected AdherenceLog findLogBy(String externalId, String treatmentId, DateTime asOf) {
        final ComplexKey key = ComplexKey.of(externalId, treatmentId, asOf);
        ViewQuery q = createQuery("by_externaId_treatmentId_asOf").key(key).includeDocs(true);
        return singleResult(db.queryView(q, AdherenceLog.class));
    }

}
