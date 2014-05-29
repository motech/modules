package org.motechproject.mtraining.service;

import org.motechproject.mtraining.domain.Bookmark;

/**
 * Created by kosh on 5/29/14.
 */
public interface BookmarkService {

    Bookmark createBookmark(Bookmark bookmark);

    Bookmark getBookmark(String bookmarkId);

    Bookmark updateBookmark(Bookmark bookmark);

    void deleteBookmark(Bookmark bookmark);
}
