package org.motechproject.mtraining.service;

import org.motechproject.mtraining.domain.Bookmark;
import org.motechproject.mtraining.repository.BookmarkDataService;

import java.util.List;

/**
 * Service interface for management of course bookmarks for a user. This is used to maintain the progress of
 * a user in the curriculum
 */
public interface BookmarkService {

    /**
     * Create a bookmark for a user
     * @param bookmark bookmark object to save
     * @return bookmark object created in the store
     */
    Bookmark createBookmark(Bookmark bookmark);

    /**
     * get bookmark by bookmark id
     * @param bookmarkId id of the bookmark
     * @return bookmark object with the id
     */
    Bookmark getBookmarkById(long bookmarkId);

    /**
     * Get the latest bookmark for the user identified by externalId
     * @param externalId external tracking id for the user
     * @return bookmark object for the user or null if no bookmarks exist for user
     */
    Bookmark getLatestBookmarkByUserId(String externalId);

    /**
     * Get all the bookmarks for a user
     * @param externalId external tracking id for the user
     * @return list of bookmarks for user
     */
    List<Bookmark> getAllBookmarksForUser(String externalId);

    /**
     * Update the given bookmark for the user
     * @param bookmark bookmark to update
     * @return updated bookmark object
     */
    Bookmark updateBookmark(Bookmark bookmark);

    /**
     * Delete a bookmark with the given id
     * @param bookmarkId id of the bookmark
     */
    void deleteBookmark(long bookmarkId);

    /**
     * delete all bookmarks for a given user
     * @param externalId external tracking id for the user
     */
    void deleteAllBookmarksForUser(String externalId);

    BookmarkDataService getBookmarkDataService();

}
