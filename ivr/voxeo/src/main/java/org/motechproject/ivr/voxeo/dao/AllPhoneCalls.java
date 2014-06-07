package org.motechproject.ivr.voxeo.dao;

import org.ektorp.CouchDbConnector;
import org.motechproject.commons.couchdb.dao.MotechBaseRepository;
import org.motechproject.ivr.voxeo.domain.PhoneCall;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

/**
 * AllPhoneCalls represents phone call repository -- aggregate of phone calls
 */

@Repository
public class AllPhoneCalls extends MotechBaseRepository<PhoneCall> {
    @Autowired
    public AllPhoneCalls(@Qualifier("voxeoDbConnector") CouchDbConnector db) {
        super(PhoneCall.class, db);
    }

    public PhoneCall findBySessionId(String sessionId) {
        return db.get(PhoneCall.class, sessionId);
    }
}
