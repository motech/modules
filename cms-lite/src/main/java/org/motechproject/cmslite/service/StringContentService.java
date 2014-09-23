package org.motechproject.cmslite.service;

import org.motechproject.cmslite.model.StringContent;
import org.motechproject.mds.annotations.Lookup;
import org.motechproject.mds.annotations.LookupField;
import org.motechproject.mds.service.MotechDataService;

import java.util.List;

/**
 * Data Service interface for {@link StringContent}s. The implementation
 * is provided by the Motech Data Services module.
 */
public interface StringContentService extends MotechDataService<StringContent> {
    @Lookup
    List<StringContent> findByLanguageAndName(@LookupField(name = "language") String language, @LookupField(name = "name") String name);
}
