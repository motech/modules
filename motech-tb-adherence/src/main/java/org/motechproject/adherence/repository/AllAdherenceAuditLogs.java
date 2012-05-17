package org.motechproject.adherence.repository;

import org.ektorp.CouchDbConnector;
import org.motechproject.adherence.domain.AdherenceAuditLog;
import org.motechproject.dao.MotechBaseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

@Repository
public class AllAdherenceAuditLogs extends MotechBaseRepository<AdherenceAuditLog> {

    @Autowired
    protected AllAdherenceAuditLogs(@Qualifier("adherenceDbConnector") CouchDbConnector db) {
        super(AdherenceAuditLog.class, db);
        initStandardDesignDocument();
    }
}
