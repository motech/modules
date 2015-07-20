package org.motechproject.cmslite.service;

import org.motechproject.cmslite.model.StringContent;
import org.motechproject.mds.annotations.Lookup;
import org.motechproject.mds.annotations.LookupField;
import org.motechproject.mds.service.MotechDataService;

/**
 * Data Service interface for {@link StringContent}s. The implementation
 * is provided by the Motech Data Services module.
 */
public interface StringContentService extends MotechDataService<StringContent> {

    /**
     * Finds content by language and name.
     * @param language the language of the content
     * @param name the name of content
     * @return the matching content or null if doesn't exist
     */
    @Lookup
    StringContent findByLanguageAndName(@LookupField(name = "language") String language, @LookupField(name = "name") String name);
}
