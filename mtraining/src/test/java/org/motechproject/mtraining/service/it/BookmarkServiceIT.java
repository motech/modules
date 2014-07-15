package org.motechproject.mtraining.service.it;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.motechproject.mtraining.service.BookmarkService;
import org.motechproject.testing.osgi.BasePaxIT;
import org.motechproject.testing.osgi.container.MotechNativeTestContainerFactory;
import org.ops4j.pax.exam.ExamFactory;
import org.ops4j.pax.exam.junit.PaxExam;
import org.ops4j.pax.exam.spi.reactors.ExamReactorStrategy;
import org.ops4j.pax.exam.spi.reactors.PerSuite;

import javax.inject.Inject;

import static org.junit.Assert.assertNotNull;

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
}
