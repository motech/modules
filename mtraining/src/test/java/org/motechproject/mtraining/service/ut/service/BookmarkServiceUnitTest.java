package org.motechproject.mtraining.service.ut.service;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.motechproject.mds.query.QueryParams;
import org.motechproject.mtraining.domain.Bookmark;
import org.motechproject.mtraining.repository.BookmarkDataService;
import org.motechproject.mtraining.service.BookmarkService;
import org.motechproject.mtraining.service.impl.BookmarkServiceImpl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Matchers.anyString;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.mock;

/**
 * Unit tests for Bookmark service
 */
public class BookmarkServiceUnitTest {
    @Mock
    private BookmarkService bookmarkService;

    @Mock
    private BookmarkDataService bookmarkDataService;

    @Before
    public void setup() {
        initMocks(this);
        bookmarkService = new BookmarkServiceImpl(bookmarkDataService);
    }

    @Test
    public void getBookmarksForUser() {
        List<Bookmark> bookmarks = (List<Bookmark>) mock(List.class);
        when(bookmarkDataService.findBookmarksForUser("1313")).thenReturn(bookmarks);
        assertEquals(bookmarks, bookmarkService.getAllBookmarksForUser("1313"));
    }

    @Test
    public void getEmptyBookmarksForUser() {
        assertEquals(0, bookmarkService.getAllBookmarksForUser("12").size());
    }

    @Test
    public void getBookmarkById() {
        Bookmark bookmark = new Bookmark();
        when(bookmarkDataService.findBookmarkById(new Long(1))).thenReturn(bookmark);
        assertEquals(bookmark, bookmarkService.getBookmarkById(1));
    }

    @Test
    public void getLatestBookmarkForUser() {
        Bookmark first = new Bookmark();
        List<Bookmark> bookmarks = new ArrayList<>(Arrays.asList(first));
        when(bookmarkDataService.findBookmarksForUserParams(anyString(), any(QueryParams.class))).thenReturn(bookmarks);
        assertEquals(first, bookmarkService.getLatestBookmarkByUserId("1212"));
    }

    @Test
    public void getLatestBookmarkNullForUser() {
        List<Bookmark> bookmarks = new ArrayList<>();
        when(bookmarkDataService.findBookmarksForUserParams(anyString(), any(QueryParams.class))).thenReturn(bookmarks);
        assertEquals(null, bookmarkService.getLatestBookmarkByUserId("1212"));
    }
}
