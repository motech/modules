package org.motechproject.atomclient.repository;

import org.motechproject.atomclient.domain.FeedRecord;
import org.motechproject.mds.annotations.Lookup;
import org.motechproject.mds.annotations.LookupField;
import org.motechproject.mds.service.MotechDataService;

public interface FeedRecordDataService extends MotechDataService<FeedRecord> {
    @Lookup
    FeedRecord findByURL(@LookupField(name = "url") String url);
}
