package org.motechproject.commcare.request;

import org.apache.http.client.utils.URIBuilder;
import org.joda.time.DateTime;
import org.junit.Test;

import java.net.URI;
import java.net.URISyntaxException;

import static org.junit.Assert.assertEquals;

public class FormListRequestTest {

    private static final String BASE_URL = "https://commcarehq.org:8075/a/mydomain/api/v0.5/form";

    @Test
    public void shouldExtendUriWithParameters() throws URISyntaxException {
        URIBuilder uriBuilder = new URIBuilder(BASE_URL);
        FormListRequest formListRequest = new FormListRequest();
        formListRequest.setAppVersion("=v2.6.1");
        formListRequest.setXmlns("http://openrosa.org/formdesigner/dd3190c7dd7e9e7d469a9705709f2f6b4e27f1d8");
        formListRequest.setReceivedOnStart(new DateTime(2000, 10, 15, 9, 10, 15));
        formListRequest.setReceivedOnEnd(new DateTime(2012, 6, 4, 23, 55, 30));
        formListRequest.setPageNumber(3);
        formListRequest.setPageSize(500);
        formListRequest.setIncludeArchived(true);

        formListRequest.addQueryParams(uriBuilder);
        URI uri = uriBuilder.build();

        assertEquals(BASE_URL + "?xmlns=http%3A%2F%2Fopenrosa.org%2Fformdesigner%2Fdd3190c7dd7e9e7d469a9705709f2f6b4e27f1d8" +
                "&received_on_start=2000-10-15T09%3A10%3A15&received_on_end=2012-06-04T23%3A55%3A30" +
                "&limit=500&offset=1000" +
                "&appVersion=%3Dv2.6.1&includeArchived=true",
                uri.toString());
    }

    @Test
    public void shouldNotAddAnythingWhenNoParamsSet() throws URISyntaxException {
        URIBuilder uriBuilder = new URIBuilder(BASE_URL);

        new FormListRequest().addQueryParams(uriBuilder);
        URI uri = uriBuilder.build();

        assertEquals(BASE_URL, uri.toString());
    }
}
