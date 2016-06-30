package org.motechproject.rapidpro.repository;

import org.motechproject.mds.annotations.Lookup;
import org.motechproject.mds.annotations.LookupField;
import org.motechproject.mds.service.MotechDataService;
import org.motechproject.rapidpro.domain.ContactMapping;

import java.util.UUID;


/**
 * Data Service for {@link ContactMapping}.
 */
public interface ContactMapperDataService extends MotechDataService<ContactMapping> {

    @Lookup
    ContactMapping findByExternalId(@LookupField(name = "externalId") String externalId);

    @Lookup
    ContactMapping findByRapidproUUID(@LookupField(name = "rapidproUUID") UUID rapidproUUID);
}
