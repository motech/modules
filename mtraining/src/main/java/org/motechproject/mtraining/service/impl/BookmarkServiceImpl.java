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
 * get the latest bookmark for a user.
 */
@Service("bookmarkService")
public class BookmarkServiceImpl implements BookmarkService {

    private BookmarkDataService bookmarkDataService;

    @Autowired
    public BookmarkServiceImpl(BookmarkDataService bookmarkDataService) {
        this.bookmarkDataService = bookmarkDataService;
    }

    @Override
    @Transactional
    public Bookmark createBookmark(Bookmark bookmark) {
        return bookmarkDataService.create(bookmark);
    }

    @Override
    @Transactional
    public Bookmark getBookmarkById(long bookmarkId) {
        return bookmarkDataService.findBookmarkById(bookmarkId);
    }

    @Override
    @Transactional
    public Bookmark getLatestBookmarkByUserId(String externalId) {
        List<Bookmark> bookmarks = bookmarkDataService.findBookmarksForUserParams(
                externalId, new QueryParams(new Order("modificationDate", Order.Direction.DESC)));
        return bookmarks.size() > 0 ? bookmarks.get(0) : null;
    }

    @Override
    @Transactional
    public List<Bookmark> getAllBookmarksForUser(String externalId) {
        return bookmarkDataService.findBookmarksForUser(externalId);
    }

    @Override
    @Transactional
    public Bookmark updateBookmark(Bookmark bookmark) {
        return bookmarkDataService.update(bookmark);
    }

    @Override
    @Transactional
    public void deleteBookmark(long bookmarkId) {
        bookmarkDataService.delete("id", bookmarkId);
    }

    @Override
    @Transactional
    public void deleteAllBookmarksForUser(String externalId) {
        for (Bookmark current : getAllBookmarksForUser(externalId)) {
            bookmarkDataService.delete(current);
        }
    }
}
