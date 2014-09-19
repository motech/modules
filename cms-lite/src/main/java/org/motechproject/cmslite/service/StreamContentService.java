package org.motechproject.cmslite.service;

import org.motechproject.cmslite.model.StreamContent;
import org.motechproject.mds.annotations.Lookup;
import org.motechproject.mds.annotations.LookupField;
import org.motechproject.mds.service.MotechDataService;

import java.util.List;

/**
 * Data Service interface for {@link StreamContent}s. The implementation is provided
 * by the Motech Data Services module.
 */
public interface StreamContentService extends MotechDataService<StreamContent> {
    @Lookup
    List<StreamContent> findByLanguageAndName(@LookupField(name = "language") String language, @LookupField(name = "name") String name);
}
