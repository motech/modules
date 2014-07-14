package org.motechproject.mtraining.repository;

import org.motechproject.mds.annotations.Lookup;
import org.motechproject.mds.annotations.LookupField;
import org.motechproject.mds.service.MotechDataService;
import org.motechproject.mtraining.domain.Bookmark;

import java.util.List;

/**
 * Interface for repository that persists simple records and allows CRUD.
 * MotechDataService base class will provide the implementation of this class as well
 * as methods for adding, deleting, saving and finding all instances.  In this class we
 * define and custom lookups we may need.
 */
public interface BookmarkDataService extends MotechDataService<Bookmark> {

    @Lookup
    Bookmark findBookmarkById(@LookupField(name = "id") long id);

    @Lookup
    List<Bookmark> findBookmarksForUser(@LookupField(name = "externalId") String externalId);
}
