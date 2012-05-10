package org.motechproject.adherence.repository;

import org.ektorp.ComplexKey;
import org.ektorp.CouchDbConnector;
import org.ektorp.ViewQuery;
import org.ektorp.support.View;
import org.joda.time.LocalDate;
import org.motechproject.adherence.domain.AdherenceAuditLog;
import org.motechproject.adherence.domain.AdherenceLog;
import org.motechproject.dao.MotechBaseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class AllAdherenceAuditLogs extends MotechBaseRepository<AdherenceAuditLog> {

    @Autowired
    protected AllAdherenceAuditLogs(@Qualifier("adherenceDbConnector") CouchDbConnector db) {
        super(AdherenceAuditLog.class, db);
    }
}
