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
import org.motechproject.openmrs.domain.Form;
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

public class FormResourceImplTest extends AbstractResourceImplTest {

    private static final String FORM_RESPONSE_JSON = "json/form/form-response.json";

    @Mock
    private RestOperations restOperations;

    @Mock
    private HttpClient httpClient;

    @Captor
    private ArgumentCaptor<HttpEntity<String>> requestCaptor;

    private FormResourceImpl formResource;

    private Config config;

    @Before
    public void setUp() {
        initMocks(this);
        when(httpClient.getState()).thenReturn(new HttpState());

        formResource = new FormResourceImpl(restOperations, httpClient);
        config = ConfigDummyData.prepareConfig("one");
    }

    @Test
    public void shouldGetFormByUuid() throws Exception {
        Form form = buildForm();
        URI url = config.toInstancePathWithParams("/form/{uuid}", form.getUuid());

        when(restOperations.exchange(eq(url), eq(HttpMethod.GET), any(HttpEntity.class), eq(String.class)))
                .thenReturn(getResponseFromFile(FORM_RESPONSE_JSON));

        Form created = formResource.getFormByUuid(config, form.getUuid());

        verify(restOperations).exchange(eq(url), eq(HttpMethod.GET), requestCaptor.capture(), eq(String.class));

        assertEquals(form, created);
        assertEquals(getHeadersForGet(config), requestCaptor.getValue().getHeaders());
    }

    private Form buildForm() throws Exception {
        return (Form) readFromFile(FORM_RESPONSE_JSON, Form.class);
    }


}
