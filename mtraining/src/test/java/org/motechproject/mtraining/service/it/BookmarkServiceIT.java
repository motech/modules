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

import javax.inject.Inject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

/**
 * Verify that bookmark service is present and functional
 */
@RunWith(PaxExam.class)
@ExamReactorStrategy(PerSuite.class)
@ExamFactory(MotechNativeTestContainerFactory.class)
public class BookmarkServiceIT extends BasePaxIT {

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
        bookmarkService.createBookmark(new Bookmark("123456", "MyCourse", "MyChapter", null, null));
        List<Bookmark> retrieved = bookmarkService.getAllBookmarksForUser("123456");
        assertTrue(retrieved.size() > 0);
    }
}
