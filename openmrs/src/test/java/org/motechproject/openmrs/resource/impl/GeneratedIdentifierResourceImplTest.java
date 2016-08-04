package org.motechproject.openmrs.resource.impl;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpState;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.motechproject.openmrs.config.Config;
import org.motechproject.openmrs.config.ConfigDummyData;
import org.motechproject.openmrs.domain.GeneratedIdentifier;
import org.motechproject.openmrs.resource.GeneratedIdentifierResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.web.client.RestOperations;

import java.net.URI;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class GeneratedIdentifierResourceImplTest extends AbstractResourceImplTest {

    private static final String SOURCE_NAME = "source_name";
    private static final String GENERATED_IDENTIFIER = "identifier";

    @Mock
    private RestOperations restOperations;

    @Mock
    private HttpClient httpClient;

    @Captor
    private ArgumentCaptor<HttpEntity<String>> requestCaptor;

    private GeneratedIdentifierResource generatedIdentifierResource;

    private Config config;

    @Before
    public void setUp() {
        initMocks(this);
        when(httpClient.getState()).thenReturn(new HttpState());

        generatedIdentifierResource = new GeneratedIdentifierResourceImpl(restOperations, httpClient);

        config = ConfigDummyData.prepareConfig("one");
    }

    @Test
    public void shouldReturnIdentifier() {
        URI url = config.toInstancePathWithParams("/idgen/latestidentifier?sourceName={name}", SOURCE_NAME);

        when(restOperations.exchange(eq(url), eq(HttpMethod.GET), any(HttpEntity.class), eq(String.class)))
                .thenReturn(getResponse(GENERATED_IDENTIFIER));

        GeneratedIdentifier generatedIdentifier = generatedIdentifierResource.getGeneratedIdentifier(config, SOURCE_NAME);

        verify(restOperations).exchange(eq(url), eq(HttpMethod.GET), requestCaptor.capture(), eq(String.class));

        assertEquals(getHeadersForGet(config), requestCaptor.getValue().getHeaders());
        assertEquals(GENERATED_IDENTIFIER, generatedIdentifier.getValue());
    }
}
