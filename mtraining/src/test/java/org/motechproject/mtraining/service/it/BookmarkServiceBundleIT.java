package org.motechproject.mtraining.service.it;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.motechproject.mtraining.domain.Bookmark;
import org.motechproject.mtraining.service.BookmarkService;
import org.motechproject.testing.osgi.BasePaxIT;
import org.motechproject.testing.osgi.container.MotechNativeTestContainerFactory;
import org.ops4j.pax.exam.ExamFactory;
import org.ops4j.pax.exam.junit.PaxExam;
import org.ops4j.pax.exam.spi.reactors.ExamReactorStrategy;
import org.ops4j.pax.exam.spi.reactors.PerSuite;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;

import javax.inject.Inject;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

/**
 * Verify that bookmark service is present and functional
 */
@RunWith(PaxExam.class)
@ExamReactorStrategy(PerSuite.class)
@ExamFactory(MotechNativeTestContainerFactory.class)
public class BookmarkServiceBundleIT extends BasePaxIT {

    @Inject
    private BookmarkService bookmarkService;

    @Test
    public void testBookmarkServiceInstance() throws Exception {
        assertNotNull(bookmarkService);
    }

    @Test
    public void testBookmarkCreation() throws Exception {
        Bookmark newbie = bookmarkService.createBookmark(new Bookmark("123456", "MyCourse", "MyChapter", null, null));

        assertEquals("123456", newbie.getExternalId());
        assertEquals("MyCourse", newbie.getCourseIdentifier());
        assertEquals("MyChapter", newbie.getChapterIdentifier());
        assertNull(newbie.getLessonIdentifier());
        assertNull(newbie.getProgress());
    }

    @Test
    public void testBookmarkProgress() throws Exception {
        Map<String, Object> customProgress = new HashMap<>();
        customProgress.put("ResumeTime", "0:20:25");
        Bookmark newbie = bookmarkService.createBookmark(new Bookmark("123456", "MyCourse", "MyChapter", "MyLesson", customProgress));

        assertEquals("123456", newbie.getExternalId());
        assertEquals("MyCourse", newbie.getCourseIdentifier());
        assertEquals("MyChapter", newbie.getChapterIdentifier());
        assertEquals("MyLesson", newbie.getLessonIdentifier());
        assertNotNull(newbie.getProgress());

        Map<String, Object> storedProgress = newbie.getProgress();
        assertEquals(storedProgress.get("ResumeTime"), "0:20:25");
    }

    @Test
    public void testGetAllBookmarks() throws Exception {
        int existingBookmarkCount = bookmarkService.getAllBookmarksForUser("123456").size();
        bookmarkService.createBookmark(new Bookmark("123456", "MyCourse", "MyChapter", null, null));
        bookmarkService.createBookmark(new Bookmark("123456", "MyCourse", "MyChapter", null, null));
        bookmarkService.createBookmark(new Bookmark("123456", "MyCourse", "MyChapter", null, null));
        int newBookmarkCount = bookmarkService.getAllBookmarksForUser("123456").size();
        assertEquals(existingBookmarkCount + 3, newBookmarkCount);
    }

    @Test
    public void testUpdateBookmarks() throws Exception {
        final Bookmark original = bookmarkService.createBookmark(new Bookmark("11111", "MyCourse", "MyChapter", "MyLesson", null));
        assertEquals(original.getLessonIdentifier(), "MyLesson");

        bookmarkService.getBookmarkDataService().doInTransaction(new TransactionCallbackWithoutResult() {
            @Override
            protected void doInTransactionWithoutResult(TransactionStatus transactionStatus) {
                Bookmark bookmarkToUpdate = bookmarkService.getBookmarkById(original.getId());
                bookmarkToUpdate.setLessonIdentifier("MyUpdatedLesson");
                bookmarkService.updateBookmark(bookmarkToUpdate);
            }
        });

        Bookmark updated = bookmarkService.getBookmarkById(original.getId());
        assertEquals(updated.getLessonIdentifier(), "MyUpdatedLesson");
    }

    @Test
    public void testDeleteBookmark() throws Exception {
        Bookmark original = bookmarkService.createBookmark(new Bookmark("11111", "MyCourse", "MyChapter", "MyLesson", null));
        assertNotNull(original);
        bookmarkService.deleteBookmark(original.getId());
        assertNull(bookmarkService.getBookmarkById(original.getId()));
    }

    @Test
    public void testDeleteAllBookmarks() throws Exception {
        bookmarkService.createBookmark(new Bookmark("1212", "MyCourse", "MyChapter", "MyLesson", null));
        bookmarkService.createBookmark(new Bookmark("1212", "MyCourse", "MyChapter", "MyLesson", null));
        bookmarkService.createBookmark(new Bookmark("1414", "MyCourse", "MyChapter", "MyLesson", null));

        assertTrue(bookmarkService.getAllBookmarksForUser("1212").size() > 0);
        assertTrue(bookmarkService.getAllBookmarksForUser("1414").size() > 0);

        bookmarkService.deleteAllBookmarksForUser("1212");
        assertTrue(bookmarkService.getAllBookmarksForUser("1212").size() == 0);
        assertTrue(bookmarkService.getAllBookmarksForUser("1414").size() > 0);
    }

    @Test
    public void testLatestBookmark() throws Exception {
        bookmarkService.createBookmark(new Bookmark("1212", "MyCourse", "MyChapter", "MyLesson", null));
        Thread.sleep(2000);
        bookmarkService.createBookmark(new Bookmark("1212", "MyCourse", "MyChapter", "MyLessonLatest", null));

        Bookmark latest = bookmarkService.getLatestBookmarkByUserId("1212");
        assertEquals("MyLessonLatest", latest.getLessonIdentifier());
    }
}
