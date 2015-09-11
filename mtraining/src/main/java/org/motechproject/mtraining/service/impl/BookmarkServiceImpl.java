package org.motechproject.mtraining.service.impl;

import org.motechproject.mds.query.QueryParams;
import org.motechproject.mds.util.Order;
import org.motechproject.mtraining.domain.Bookmark;
import org.motechproject.mtraining.repository.BookmarkDataService;
import org.motechproject.mtraining.service.BookmarkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Service implementation for bookmarks. This helps retrieve bookmarks for user by id and
 * get the latest bookmark for a user
 */
@Service("bookmarkService")
public class BookmarkServiceImpl implements BookmarkService {

    private BookmarkDataService bookmarkDataService;

    @Autowired
    public BookmarkServiceImpl(BookmarkDataService bookmarkDataService) {
        this.bookmarkDataService = bookmarkDataService;
    }

    /**
     * Create a bookmark for a user
     * @param bookmark bookmark object to save
     * @return bookmark object created in the store
     */
    @Override
    @Transactional
    public Bookmark createBookmark(Bookmark bookmark) {
        return bookmarkDataService.create(bookmark);
    }

    /**
     * get bookmark by bookmark id
     * @param bookmarkId id of the bookmark
     * @return bookmark object with the id
     */
    @Override
    @Transactional
    public Bookmark getBookmarkById(long bookmarkId) {
        return bookmarkDataService.findBookmarkById(bookmarkId);
    }

    /**
     * Get the latest bookmark for the user identified by externalId
     * @param externalId external tracking id for the user
     * @return bookmark object for the user or null if no bookmarks exist for user
     */
    @Override
    @Transactional
    public Bookmark getLatestBookmarkByUserId(String externalId) {
        List<Bookmark> bookmarks = bookmarkDataService.findBookmarksForUserParams(
                externalId, new QueryParams(new Order("modificationDate", Order.Direction.DESC)));
        return bookmarks.size() > 0 ? bookmarks.get(0) : null;
    }

    /**
     * Get all the bookmarks for a user
     * @param externalId external tracking id for the user
     * @return list of bookmarks for user
     */
    @Override
    @Transactional
    public List<Bookmark> getAllBookmarksForUser(String externalId) {
        return bookmarkDataService.findBookmarksForUser(externalId);
    }

    /**
     * Update the given bookmark for the user
     * @param bookmark bookmark to update
     * @return updated bookmark object
     */
    @Override
    @Transactional
    public Bookmark updateBookmark(Bookmark bookmark) {
        return bookmarkDataService.update(bookmark);
    }

    /**
     * Delete a bookmark with the given id
     * @param bookmarkId id of the bookmark
     */
    @Override
    @Transactional
    public void deleteBookmark(long bookmarkId) {
        bookmarkDataService.delete("id", bookmarkId);
    }

    /**
     * delete all bookmarks for a given user
     * @param externalId external tracking id for the user
     */
    @Override
    @Transactional
    public void deleteAllBookmarksForUser(String externalId) {
        for (Bookmark current : getAllBookmarksForUser(externalId)) {
            bookmarkDataService.delete(current);
        }
    }

    @Override
    public BookmarkDataService getBookmarkDataService() {
        return bookmarkDataService;
    }
}
