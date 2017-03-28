package org.motechproject.openmrs.repository;

import org.motechproject.mds.annotations.Lookup;
import org.motechproject.mds.annotations.LookupField;
import org.motechproject.mds.service.MotechDataService;
import org.motechproject.openmrs.domain.FeedRecord;


/**
 * Exposes MDS CRUD operations to the module code and creates the findByURL lookup
 */
public interface FeedRecordDataService extends MotechDataService<FeedRecord> {
    @Lookup
    FeedRecord findByURL(@LookupField(name = "url") String url);
}
