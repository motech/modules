package org.motechproject.openmrs19.resource.impl;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.motechproject.openmrs19.config.Config;
import org.motechproject.openmrs19.config.ConfigDummyData;
import org.motechproject.openmrs19.domain.RelationshipListResult;
import org.motechproject.openmrs19.resource.RelationshipResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.web.client.RestOperations;

import java.net.URI;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class RelationshipResourceImplTest extends AbstractResourceImplTest {

    private static final String CONCEPT_LIST_RESPONSE_JSON = "json/relationship-list-response.json";
    @Mock
    private RestOperations restOperations;

    @Captor
    private ArgumentCaptor<HttpEntity<String>> requestCaptor;

    private RelationshipResource relationshipResource;

    private Config config;

    @Before
    public void setUp() {
        initMocks(this);
        relationshipResource = new RelationshipResourceImpl(restOperations);
        config = ConfigDummyData.prepareConfig("one");
    }

    @Test
    public void shouldGetRelationshipsByPersonUuid() throws Exception {
        String personBUuid = "personBUuid";
        URI url = config.toInstancePathWithParams("/relationship?person={personUuid}&v=full", personBUuid);

        when(restOperations.exchange(eq(url), eq(HttpMethod.GET), any(HttpEntity.class), eq(String.class)))
                .thenReturn(getResponse(CONCEPT_LIST_RESPONSE_JSON));

        RelationshipListResult result = relationshipResource.getByPersonUuid(config, personBUuid);

        verify(restOperations).exchange(eq(url), eq(HttpMethod.GET), requestCaptor.capture(), eq(String.class));

        assertThat(result, equalTo(readFromFile(CONCEPT_LIST_RESPONSE_JSON, RelationshipListResult.class)));
        assertThat(requestCaptor.getValue().getHeaders(), equalTo(getHeadersForGet(config)));
        assertThat(requestCaptor.getValue().getBody(), nullValue());
    }

}