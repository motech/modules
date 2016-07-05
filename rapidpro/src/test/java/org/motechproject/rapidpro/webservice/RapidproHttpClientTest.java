package org.motechproject.rapidpro.webservice;

import org.apache.http.HttpEntity;
import org.apache.http.StatusLine;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.osgi.services.HttpClientBuilderFactory;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.Mock;
import org.motechproject.rapidpro.domain.Settings;
import org.motechproject.rapidpro.exception.RapidProClientException;
import org.motechproject.rapidpro.service.SettingsService;

import java.io.ByteArrayInputStream;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

/**
 * Test class for {@link RapidProHttpClient}
 */
public class RapidproHttpClientTest {

    private static final String ENDPOINT = "/endpoint";
    private RapidProHttpClient rapidProHttpClient;
    @Mock
    private HttpClientBuilderFactory factory;
    @Mock
    private CloseableHttpClient client;
    @Mock
    private HttpClientBuilder builder;
    @Mock
    SettingsService settingsService;
    @Mock
    CloseableHttpResponse response;
    @Mock
    StatusLine statusLine;
    @Mock
    HttpEntity entity;

    @Rule
    public ExpectedException expected = ExpectedException.none();

    @Before
    public void setup() throws Exception {
        initMocks(this);
        Settings settings = getSettings();
        String content = "content";
        when(factory.newBuilder()).thenReturn(builder);
        when(builder.build()).thenReturn(client);
        rapidProHttpClient = new RapidProHttpClient(factory,settingsService);

        when(response.getStatusLine()).thenReturn(statusLine);
        when(entity.getContent()).thenReturn(new ByteArrayInputStream(content.getBytes()));
        when(response.getEntity()).thenReturn(entity);
        when(settingsService.getSettings()).thenReturn(settings);
        when(client.execute(any(HttpUriRequest.class))).thenReturn(response);
    }

    @Test
    public void shouldExecuteNormally() throws Exception{
        when(statusLine.getStatusCode()).thenReturn(200);
        rapidProHttpClient.executeGet(ENDPOINT, MediaFormat.JSON, null);
    }

    @Test
    public void shouldThrowThreeHundredError() throws Exception {
        when(statusLine.getStatusCode()).thenReturn(300);
        expected.expect(RapidProClientException.class);
        expected.expectMessage("Redirection: 300");
        rapidProHttpClient.executeGet(ENDPOINT, MediaFormat.JSON, null);
    }

    @Test
    public void shouldThrowFourHundredError() throws Exception {
        when(statusLine.getStatusCode()).thenReturn(400);
        expected.expect(RapidProClientException.class);
        expected.expectMessage("Client Error: 400");
        rapidProHttpClient.executeGet(ENDPOINT, MediaFormat.JSON, null);
    }

    @Test
    public void shouldThrowFiveHundredError() throws Exception {
        when(statusLine.getStatusCode()).thenReturn(500);
        expected.expect(RapidProClientException.class);
        expected.expectMessage("Server Error: 500");
        rapidProHttpClient.executeGet(ENDPOINT, MediaFormat.JSON, null);
    }

    @Test
    public void shouldThrowNoSettings() throws Exception {
        when(settingsService.getSettings()).thenReturn(null);
        expected.expect(RapidProClientException.class);
        expected.expectMessage("Error building URI for request");
        rapidProHttpClient.executeGet(ENDPOINT, MediaFormat.JSON, null);
    }

    @Test
    public void shouldThrowNoApiKey() throws Exception {
        when(settingsService.getSettings()).thenReturn(new Settings(null, "v1"));
        expected.expect(RapidProClientException.class);
        expected.expectMessage("No API key");
        rapidProHttpClient.executeGet(ENDPOINT, MediaFormat.JSON, null);
    }

    @Test
    public void shouldThrowNoVersion() throws Exception {
        when(settingsService.getSettings()).thenReturn(new Settings("apiKey", null));
        expected.expect(RapidProClientException.class);
        expected.expectMessage("Error building URI for request");
        rapidProHttpClient.executeGet(ENDPOINT, MediaFormat.JSON, null);
    }

    private Settings getSettings() {
        Settings settings = new Settings();
        settings.setApiKey("apiKey");
        settings.setVersion("v1");
        return settings;
    }
}
