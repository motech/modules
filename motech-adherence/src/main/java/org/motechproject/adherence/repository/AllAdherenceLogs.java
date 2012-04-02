package org.motechproject.adherence.repository;

import org.ektorp.ComplexKey;
import org.ektorp.CouchDbConnector;
import org.ektorp.ViewQuery;
import org.ektorp.support.View;
import org.joda.time.LocalDate;
import org.motechproject.adherence.domain.AdherenceLog;
import org.motechproject.adherence.domain.Concept;
import org.motechproject.dao.MotechBaseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.List;

@Component
public class AllAdherenceLogs extends MotechBaseRepository<AdherenceLog> {

    @Autowired
    protected AllAdherenceLogs(@Qualifier("adherenceDbConnector") CouchDbConnector db) {
        super(AdherenceLog.class, db);
    }

    public void insert(AdherenceLog entity) {
        AdherenceLog latestLog = findLatestLog(entity.getExternalId(), entity.getConcept());
        if (latestLog == null) {
            add(entity);
        } else if (latestLog.overlaps(entity)) {
            entity = latestLog.cut(entity);
            add(entity);
        } else if (!latestLog.encloses(entity)) {
            add(entity);
        }
    }

    @View(name = "find_by_date", map = "function(doc) {if(doc.type == 'AdherenceLog') {emit([doc.externalId, doc.concept, doc.toDate], doc._id);} }")
    public AdherenceLog findByDate(String externalId, Concept concept, LocalDate date) {
        ViewQuery query = createQuery("find_by_date").startKey(ComplexKey.of(externalId, concept, date)).limit(1).includeDocs(true);
        List<AdherenceLog> adherenceLogs = db.queryView(query, AdherenceLog.class);
        if (CollectionUtils.isEmpty(adherenceLogs)) {
            return null;
        } else {
            AdherenceLog adherenceLog = adherenceLogs.get(0);
            if (!adherenceLog.getFromDate().isAfter(date)) {
                return adherenceLog;
            }
            return null;
        }
    }

    public AdherenceLog findLatestLog(String externalId, Concept concept) {
        ViewQuery query = createQuery("find_by_date").startKey(ComplexKey.of(externalId, concept, ComplexKey.emptyObject())).limit(1).descending(true).includeDocs(true);
        List<AdherenceLog> adherenceLogs = db.queryView(query, AdherenceLog.class);
        return CollectionUtils.isEmpty(adherenceLogs) ? null : adherenceLogs.get(0);
    }

    @View(name = "find_all_between_date_range", map = "function(doc) {if(doc.type == 'AdherenceLog') {emit([doc.externalId, doc.concept, doc.fromDate, doc.toDate], doc._id);} }")
    public List<AdherenceLog> findLogsBetween(String externalId, Concept concept, LocalDate startDate, LocalDate endDate) {
        ViewQuery query = createQuery("find_all_between_date_range").startKey(ComplexKey.of(externalId, concept, null, startDate)).endKey(ComplexKey.of(externalId, concept, endDate, ComplexKey.emptyObject())).inclusiveEnd(true).includeDocs(true);
        return db.queryView(query, AdherenceLog.class);
    }
}
