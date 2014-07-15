package org.motechproject.mtraining.service.impl;

import org.motechproject.mtraining.domain.Bookmark;
import org.motechproject.mtraining.repository.BookmarkDataService;
import org.motechproject.mtraining.service.BookmarkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * Service implementation for bookmarks
 */
@Service("bookmarkService")
public class BookmarkServiceImpl implements BookmarkService {

    @Autowired
    private BookmarkDataService bookmarkDataService;

    /**
     * Create a bookmark for a user
     * @param bookmark bookmark object to save
     * @return bookmark object created in the store
     */
    @Override
    public Bookmark createBookmark(Bookmark bookmark) {

        return bookmarkDataService.create(bookmark);
    }

    /**
     * get bookmark by bookmark id
     * @param bookmarkId id of the bookmark
     * @return bookmark object with the id
     */
    @Override
    public Bookmark getBookmarkById(long bookmarkId) {
        return bookmarkDataService.findBookmarkById(bookmarkId);
    }

    /**
     * Get the latest bookmark for the user identified by externalId
     * @param externalId external tracking id for the user
     * @return bookmark object for the user
     */
    @Override
    public Bookmark getLatestBookmarkByUserId(String externalId) {
        Bookmark toReturn  = null;
        Date latestTimestamp = null;
        List<Bookmark> bookmarks = bookmarkDataService.findBookmarksForUser(externalId);

        for (Bookmark current : bookmarks) {
            Date bookmarkDate = (Date) bookmarkDataService.getDetachedField(current, "modifiedDate");
            if (toReturn == null) {
                toReturn = current;
                latestTimestamp = bookmarkDate;
            } else {

                if (bookmarkDate.after(latestTimestamp)) {
                    toReturn = current;
                    latestTimestamp = bookmarkDate;
                }
            }
        }

        return toReturn;
    }

    /**
     * Get all the bookmarks for a user
     * @param externalId external tracking id for the user
     * @return list of bookmarks for user
     */
    @Override
    public List<Bookmark> getAllBookmarksForUser(String externalId) {
        return bookmarkDataService.findBookmarksForUser(externalId);
    }

    /**
     * Update the given bookmark for the user
     * @param bookmark bookmark to update
     * @return updated bookmark object
     */
    @Override
    public Bookmark updateBookmark(Bookmark bookmark) {

        return bookmarkDataService.update(bookmark);
    }

    /**
     * Delete a bookmark with the given id
     * @param bookmarkId id of the bookmark
     */
    @Override
    public void deleteBookmark(long bookmarkId) {

        bookmarkDataService.delete("id", bookmarkId);
    }

    /**
     * delete all bookmarks for a given user
     * @param externalId external tracking id for the user
     */
    @Override
    public void deleteAllBookmarksForUser(String externalId) {

        bookmarkDataService.delete("externalId", externalId);
    }
}
