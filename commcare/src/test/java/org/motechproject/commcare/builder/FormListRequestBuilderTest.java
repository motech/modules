package org.motechproject.commcare.builder;

import org.joda.time.DateTime;
import org.junit.Test;
import org.motechproject.commcare.request.FormListRequest;
import org.motechproject.commons.date.util.DateUtil;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class FormListRequestBuilderTest {

    private FormListRequestBuilder requestBuilder = new FormListRequestBuilder();

    @Test
    public void shouldBuildRequest() {
        final DateTime now = DateUtil.now();
        FormListRequest request = requestBuilder.withXmlns("xmlns").withReceivedOnStart(now)
                .withReceivedOnEnd(now.plusDays(1)).withAppVersion("app").withArchived(true)
                .withPageSize(200).withPageNumber(2).build();

        assertEquals("xmlns", request.getXmlns());
        assertEquals("app", request.getAppVersion());
        assertEquals(now, request.getReceivedOnStart());
        assertEquals(now.plusDays(1), request.getReceivedOnEnd());
        assertEquals(Integer.valueOf(200), request.getPageSize());
        assertEquals(Integer.valueOf(2), request.getPageNumber());
        assertTrue(request.isIncludeArchived());
    }

    @Test
    public void shouldIncrementAndDecrementPage() {
        requestBuilder.withNextPage();
        assertEquals(Integer.valueOf(1), requestBuilder.build().getPageNumber());

        requestBuilder.withNextPage();
        assertEquals(Integer.valueOf(2), requestBuilder.build().getPageNumber());

        requestBuilder.withPreviousPage();
        assertEquals(Integer.valueOf(1), requestBuilder.build().getPageNumber());
    }

    @Test(expected = IllegalStateException.class)
    public void shouldThrowIllegalStateExceptionWhenDecreasingNullPage() {
        requestBuilder.withPreviousPage();
    }


    @Test(expected = IllegalStateException.class)
    public void shouldThrowIllegalStateExceptionWhenDecreasingPageBelow1() {
        requestBuilder.withPageNumber(1);
        requestBuilder.withPreviousPage();
    }
}
