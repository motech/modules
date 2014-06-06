package org.motechproject.cmslite.api.service;

import org.motechproject.cmslite.api.model.StringContent;
import org.motechproject.mds.annotations.Lookup;
import org.motechproject.mds.annotations.LookupField;
import org.motechproject.mds.service.MotechDataService;

import java.util.List;

/**
 * MDS data service for {@link StringContent}s.
 */
public interface StringContentService extends MotechDataService<StringContent> {
    @Lookup
    List<StringContent> findByLanguageAndName(@LookupField(name = "language") String language, @LookupField(name = "name") String name);
}
