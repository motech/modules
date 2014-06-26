package org.motechproject.mtraining.service;

import org.motechproject.mtraining.domain.Bookmark;

/**
 * Service interface for management of course bookmarks for a user
 */
public interface BookmarkService {

    Bookmark createBookmark(Bookmark bookmark);

    Bookmark getBookmark(String bookmarkId);

    Bookmark updateBookmark(Bookmark bookmark);

    void deleteBookmark(String bookmarkId);
}
