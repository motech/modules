package org.motechproject.cmslite.service;

import org.motechproject.cmslite.model.StreamContent;
import org.motechproject.mds.annotations.Lookup;
import org.motechproject.mds.annotations.LookupField;
import org.motechproject.mds.service.MotechDataService;

/**
 * Data Service interface for {@link StreamContent}s. The implementation is provided
 * by the Motech Data Services module.
 */
public interface StreamContentService extends MotechDataService<StreamContent> {

    /**
     * Finds content by language and name.
     * @param language the language of the content
     * @param name the name of content
     * @return the matching content or null if it doesn't exist
     */
    @Lookup
    StreamContent findByLanguageAndName(@LookupField(name = "language") String language, @LookupField(name = "name") String name);
}
