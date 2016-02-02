package org.motechproject.commcare.client;

import org.apache.commons.httpclient.HttpClient;
import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.motechproject.commcare.config.AccountConfig;
import org.motechproject.commcare.request.FormListRequest;

import static java.lang.String.format;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertThat;

public class CommCareAPIHttpClientTest {

    @Mock
    private HttpClient httpClient;

    final String baseUrl = "https://www.commcarehq.org/a";
    final String domain = "somedomain";
    final String apiVersion = "0.5";
    private AccountConfig accountConfig;
    private CommCareAPIHttpClient commCareAPIHttpClient;

    @Before
    public void setUp() {
        accountConfig = new AccountConfig();
        accountConfig.setBaseUrl(baseUrl);
        accountConfig.setDomain(domain);

        commCareAPIHttpClient = new CommCareAPIHttpClient(httpClient);
    }

    @Test
    public void shouldConstructCommcareUserUrl() {
        assertThat(commCareAPIHttpClient.commcareUsersUrl(accountConfig, 100, 2), equalTo(format("%s/%s/api/v%s/user/?format=json&limit=100&offset=100", baseUrl, domain, apiVersion)));
    }

    @Test
    public void shouldConstructCommcareLocationUrl() {
        final String locationId = "999";
        assertThat(commCareAPIHttpClient.commcareLocationUrl(accountConfig, locationId), equalTo(format("%s/%s/api/v0.5/location/%s/?format=json", baseUrl, domain, locationId)));
    }

    @Test
    public void shouldConstructCommcareLocationsUrl() {
        assertThat(commCareAPIHttpClient.commcareLocationsUrl(accountConfig, 100, 2), equalTo(format("%s/%s/api/v0.5/location/?format=json&limit=100&offset=100", baseUrl, domain)));
    }

    @Test
    public void shouldConstructCommcareFormUrl() {
        final String formId = "123";
        assertThat(commCareAPIHttpClient.commcareFormUrl(accountConfig, formId), equalTo(format("%s/%s/api/v%s/form/%s/?format=json", baseUrl, domain, apiVersion, formId)));
    }

    @Test
    public void shouldConstructCommcareFixturesUrl() {
        assertThat(commCareAPIHttpClient.commcareFixturesUrl(accountConfig), equalTo(format("%s/%s/api/v%s/fixture/", baseUrl, domain, apiVersion)));
    }

    @Test
    public void shouldConstructCommcareFixtureUrl() {
        String fixtureId = "123";
        assertThat(commCareAPIHttpClient.commcareFixtureUrl(accountConfig, fixtureId), equalTo(format("%s/%s/api/v%s/fixture/%s/", baseUrl, domain, apiVersion, fixtureId)));
    }

    @Test
    public void shouldConstructCommcareCasesUrl() {
        assertThat(commCareAPIHttpClient.commcareCasesUrl(accountConfig), equalTo(format("%s/%s/api/v%s/case/", baseUrl, domain, apiVersion)));
    }

    @Test
    public void shouldConstructCommcareCaseUrl() {
        String caseId = "123";
        assertThat(commCareAPIHttpClient.commcareCaseUrl(accountConfig, caseId), equalTo(format("%s/%s/api/v%s/case/%s/", baseUrl, domain, apiVersion, caseId)));
    }

    @Test
    public void shouldConstructCommcareUploadUrl() {
        assertThat(commCareAPIHttpClient.commcareCaseUploadUrl(accountConfig), equalTo(format("%s/%s/receiver/", baseUrl, domain)));
    }

    @Test
    public void shouldConstructFormListUrlWhenNoRequestPassed() {
        assertThat(commCareAPIHttpClient.commcareFormListUrl(accountConfig, null), equalTo(format("%s/%s/api/v%s/form", baseUrl, domain, apiVersion)));
    }

    @Test
    public void shouldConstructCommcareStockTransactionUrl() {
        assertThat(commCareAPIHttpClient.commcareStockTransactionsUrl(accountConfig),
                equalTo(format("%s/%s/api/v%s/stock_transaction/?format=json", baseUrl, domain, apiVersion)));
    }

    @Test
    public void shouldConstructCommcareStockTransactionUrlWithPaginationParams() {
        Integer pageSize = 5;
        Integer pageNumber = 5;
        String expectedUrl = "%s/%s/api/v%s/stock_transaction/?format=json&limit=5&offset=20";
        assertThat(commCareAPIHttpClient.commcareStockTransactionsUrl(accountConfig, pageSize, pageNumber),
                equalTo(format(expectedUrl, baseUrl, domain, apiVersion)));
    }

    @Test
    public void shouldConstructFormListUrlsWithDates() {
        final DateTime start = new DateTime(1988, 6, 3, 10, 45, 22);
        final DateTime end = new DateTime(2005, 10, 11, 9, 30, 11);

        final String expectedStartParam = "received_on_start=1988-06-03T10%3A45%3A22";
        final String expectedStopParam = "received_on_end=2005-10-11T09%3A30%3A11";

        FormListRequest request = new FormListRequest();
        request.setReceivedOnStart(start);
        request.setReceivedOnEnd(end);
        request.setPageSize(100);
        request.setPageNumber(5);

        // start and end
        assertThat(commCareAPIHttpClient.commcareFormListUrl(accountConfig, request),
                equalTo(format("%s/%s/api/v%s/form?%s&%s&limit=100&offset=400", baseUrl, domain, apiVersion,
                        expectedStartParam, expectedStopParam)));

        // only start
        request.setReceivedOnEnd(null);

        assertThat(commCareAPIHttpClient.commcareFormListUrl(accountConfig, request),
                equalTo(format("%s/%s/api/v%s/form?%s&limit=100&offset=400", baseUrl, domain, apiVersion,
                        expectedStartParam)));

        // only end
        request.setReceivedOnStart(null);
        request.setReceivedOnEnd(end);

        assertThat(commCareAPIHttpClient.commcareFormListUrl(accountConfig, request),
                equalTo(format("%s/%s/api/v%s/form?%s&limit=100&offset=400", baseUrl, domain, apiVersion,
                        expectedStopParam)));

        // no start, no end
        request.setReceivedOnEnd(null);

        assertThat(commCareAPIHttpClient.commcareFormListUrl(accountConfig, request),
                equalTo(format("%s/%s/api/v%s/form?limit=100&offset=400", baseUrl, domain, apiVersion)));
    }
}
