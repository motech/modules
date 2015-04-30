package org.motechproject.commcare.client;

import org.apache.commons.httpclient.HttpClient;
import org.hamcrest.core.IsEqual;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.motechproject.commcare.config.AccountConfig;

import static org.junit.Assert.assertThat;

public class CommCareAPIHttpClientTest {

    @Mock
    private HttpClient httpClient;

    final String baseUrl = "https://www.commcarehq.org/a";
    final String domain = "somedomain";
    final String apiVersion = "0.4";
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
        assertThat(commCareAPIHttpClient.commcareUsersUrl(accountConfig, 100, 2), IsEqual.equalTo(String.format("%s/%s/api/v%s/user/?format=json&limit=100&offset=100", baseUrl, domain, apiVersion)));
    }

    @Test
    public void shouldConstructCommcareFormUrl() {
        final String formId = "123";
        assertThat(commCareAPIHttpClient.commcareFormUrl(accountConfig, formId), IsEqual.equalTo(String.format("%s/%s/api/v%s/form/%s/?format=json", baseUrl, domain, apiVersion, formId)));
    }

    @Test
    public void shouldConstructCommcareFixturesUrl() {
        assertThat(commCareAPIHttpClient.commcareFixturesUrl(accountConfig), IsEqual.equalTo(String.format("%s/%s/api/v%s/fixture/", baseUrl, domain, apiVersion)));
    }

    @Test
    public void shouldConstructCommcareFixtureUrl() {
        String fixtureId = "123";
        assertThat(commCareAPIHttpClient.commcareFixtureUrl(accountConfig, fixtureId), IsEqual.equalTo(String.format("%s/%s/api/v%s/fixture/%s/", baseUrl, domain, apiVersion, fixtureId)));
    }

    @Test
    public void shouldConstructCommcareCasesUrl() {
        assertThat(commCareAPIHttpClient.commcareCasesUrl(accountConfig), IsEqual.equalTo(String.format("%s/%s/api/v%s/case/", baseUrl, domain, apiVersion)));
    }

    @Test
    public void shouldConstructCommcareCaseUrl() {
        String caseId = "123";
        assertThat(commCareAPIHttpClient.commcareCaseUrl(accountConfig, caseId), IsEqual.equalTo(String.format("%s/%s/api/v%s/case/%s/", baseUrl, domain, apiVersion, caseId)));
    }

    @Test
    public void shouldConstructCommcareCaseUploadUrl() {
        assertThat(commCareAPIHttpClient.commcareCaseUploadUrl(accountConfig), IsEqual.equalTo(String.format("%s/%s/receiver/", baseUrl, domain, apiVersion)));
    }
}
