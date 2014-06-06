package org.motechproject.commcare.repository;

import org.ektorp.CouchDbConnector;
import org.motechproject.commcare.domain.CommcareApplication;
import org.motechproject.commcare.domain.CommcareApplicationJson;
import org.motechproject.commons.couchdb.dao.MotechBaseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class AllCommcareApplications extends MotechBaseRepository<CommcareApplication> {

    @Autowired
    protected AllCommcareApplications(@Qualifier("commcareApplicationDatabaseConnector") CouchDbConnector db) {
        super(CommcareApplication.class, db);
    }

    public void addAll(List<CommcareApplicationJson> applications) {
        for (CommcareApplicationJson application : applications) {
                add(new CommcareApplication(application.getApplicationName(), application.getResourceUri(), application.getModules()));
        }
    }
}
