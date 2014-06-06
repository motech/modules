package org.motechproject.cmslite.api.service;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.ArrayUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.motechproject.cmslite.api.model.CMSLiteException;
import org.motechproject.cmslite.api.model.ContentNotFoundException;
import org.motechproject.cmslite.api.model.StreamContent;
import org.motechproject.cmslite.api.model.StringContent;
import org.motechproject.testing.osgi.BasePaxIT;
import org.motechproject.testing.osgi.container.MotechNativeTestContainerFactory;
import org.ops4j.pax.exam.ExamFactory;
import org.ops4j.pax.exam.junit.PaxExam;
import org.ops4j.pax.exam.spi.reactors.ExamReactorStrategy;
import org.ops4j.pax.exam.spi.reactors.PerSuite;

import javax.inject.Inject;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

@RunWith(PaxExam.class)
@ExamReactorStrategy(PerSuite.class)
@ExamFactory(MotechNativeTestContainerFactory.class)
public class CMSLiteServiceIT extends BasePaxIT {
    public static final String NAME = "testName";
    public static final String LANGUAGE = "en";

    @Inject
    private StreamContentService streamContentService;
    @Inject
    private StringContentService stringContentService;
    @Inject
    private CMSLiteService cmsLiteService;

    @Override
    protected boolean shouldFakeModuleStartupEvent() {
        return true;
    }

    @Before
    public void setUp() {
        streamContentService.deleteAll();
        stringContentService.deleteAll();
    }

    @Test
    public void shouldPerformCrudOperationsOnStreamContent() throws IOException, CMSLiteException, InterruptedException, ContentNotFoundException {
        String pathToFile = "/10.wav";
        byte[] content = IOUtils.toByteArray(this.getClass().getResourceAsStream(pathToFile));
        cmsLiteService.addContent(new StreamContent(LANGUAGE, NAME, ArrayUtils.toObject(content), "checksum", "audio/x-wav"));
        assertEquals(1, streamContentService.retrieveAll().size());

        assertTrue(cmsLiteService.isStreamContentAvailable(LANGUAGE, NAME));
        StreamContent streamContent = cmsLiteService.getStreamContent(LANGUAGE, NAME);
        assertEquals("audio/x-wav", streamContent.getContentType());

        assertEquals(ArrayUtils.toString(content), ArrayUtils.toString(cmsLiteService.retrieveStreamContentData(streamContent)));

        streamContent.setContentType("plain/text");
        cmsLiteService.addContent(streamContent);
        streamContent = cmsLiteService.getStreamContent(LANGUAGE, NAME);
        assertEquals("plain/text", streamContent.getContentType());
        assertEquals(1, streamContentService.retrieveAll().size());

        cmsLiteService.removeStreamContent(LANGUAGE, NAME);
        assertFalse(cmsLiteService.isStreamContentAvailable(LANGUAGE, NAME));
        assertEquals(0, streamContentService.retrieveAll().size());
    }

    @Test
    public void shouldPerformCrudOperationsOnStringContent() throws IOException, CMSLiteException, InterruptedException, ContentNotFoundException {
        cmsLiteService.addContent(new StringContent(LANGUAGE, NAME, "Test content"));
        assertEquals(1, stringContentService.retrieveAll().size());

        assertTrue(cmsLiteService.isStringContentAvailable(LANGUAGE, NAME));
        StringContent stringContent = cmsLiteService.getStringContent(LANGUAGE, NAME);
        assertEquals("Test content", stringContent.getValue());

        stringContent.setValue("New content");
        cmsLiteService.addContent(stringContent);

        assertEquals(1, stringContentService.retrieveAll().size());
        stringContent = cmsLiteService.getStringContent(LANGUAGE, NAME);
        assertEquals("New content", stringContent.getValue());

        cmsLiteService.removeStringContent(LANGUAGE, NAME);
        assertFalse(cmsLiteService.isStringContentAvailable(LANGUAGE, NAME));
        assertEquals(0, stringContentService.retrieveAll().size());
    }
}