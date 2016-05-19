package org.motechproject.openmrs.resource.impl;

import com.google.gson.JsonObject;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.motechproject.openmrs.domain.Concept;
import org.motechproject.openmrs.domain.ConceptListResult;
import org.mockito.Captor;
import org.mockito.Mock;
import org.motechproject.openmrs.config.Config;
import org.motechproject.openmrs.config.ConfigDummyData;
import org.motechproject.openmrs.util.JsonUtils;
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

public class ConceptResourceImplTest extends AbstractResourceImplTest {

    private static final String CONCEPT_LIST_RESPONSE_JSON = "json/concept/concept-list-response.json";
    private static final String CONCEPT_RESPONSE_JSON = "json/concept/concept-response.json";
    private static final String CONCEPT_CREATE_JSON = "json/concept/concept-create.json";

    @Mock
    private RestOperations restOperations;

    @Captor
    private ArgumentCaptor<HttpEntity<String>> requestCaptor;

    private ConceptResourceImpl conceptResource;

    private Config config;

    @Before
    public void setUp() {
        initMocks(this);
        conceptResource = new ConceptResourceImpl(restOperations);
        config = ConfigDummyData.prepareConfig("one");
    }

    @Test
    public void shouldCreateConcept() throws Exception {
        Concept concept = prepareConcept();
        URI url = config.toInstancePath("/concept");

        when(restOperations.exchange(eq(url), eq(HttpMethod.POST), any(HttpEntity.class), eq(String.class)))
                .thenReturn(getResponse(CONCEPT_RESPONSE_JSON));

        Concept created = conceptResource.createConcept(config, concept);

        verify(restOperations).exchange(eq(url), eq(HttpMethod.POST), requestCaptor.capture(), eq(String.class));

        assertThat(created, equalTo(concept));
        assertThat(requestCaptor.getValue().getHeaders(), equalTo(getHeadersForPost(config)));
        assertThat(JsonUtils.readJson(requestCaptor.getValue().getBody(), JsonObject.class),
                equalTo(readFromFile(CONCEPT_CREATE_JSON, JsonObject.class)));
    }

    @Test
    public void shouldUpdateConcept() throws Exception {
        Concept concept = prepareConcept();
        URI url = config.toInstancePathWithParams("/concept/{uuid}", concept.getUuid());

        when(restOperations.exchange(eq(url), eq(HttpMethod.POST), any(HttpEntity.class), eq(String.class)))
                .thenReturn(getResponse(CONCEPT_RESPONSE_JSON));

        Concept updated = conceptResource.updateConcept(config, concept);

        verify(restOperations).exchange(eq(url), eq(HttpMethod.POST), requestCaptor.capture(), eq(String.class));

        assertThat(updated, equalTo(concept));
        assertThat(requestCaptor.getValue().getHeaders(), equalTo(getHeadersForPost(config)));
        assertThat(JsonUtils.readJson(requestCaptor.getValue().getBody(), JsonObject.class),
                equalTo(readFromFile(CONCEPT_CREATE_JSON, JsonObject.class)));
    }

    @Test
    public void shouldGetAllConcepts() throws Exception {
        URI url = config.toInstancePath("/concept?v=full");

        when(restOperations.exchange(eq(url), eq(HttpMethod.GET), any(HttpEntity.class), eq(String.class)))
                .thenReturn(getResponse(CONCEPT_LIST_RESPONSE_JSON));

        ConceptListResult result = conceptResource.getAllConcepts(config);

        verify(restOperations).exchange(eq(url), eq(HttpMethod.GET), requestCaptor.capture(), eq(String.class));

        assertThat(result, equalTo(readFromFile(CONCEPT_LIST_RESPONSE_JSON, ConceptListResult.class)));
        assertThat(requestCaptor.getValue().getHeaders(), equalTo(getHeadersForGet(config)));
        assertThat(requestCaptor.getValue().getBody(), nullValue());
    }

    @Test
    public void shouldGetConceptById() throws Exception {
        String conceptId = "LLL";
        URI url = config.toInstancePathWithParams("/concept/{uuid}", conceptId);

        when(restOperations.exchange(eq(url), eq(HttpMethod.GET), any(HttpEntity.class), eq(String.class)))
                .thenReturn(getResponse(CONCEPT_CREATE_JSON));

        Concept concept = conceptResource.getConceptById(config, conceptId);

        verify(restOperations).exchange(eq(url), eq(HttpMethod.GET), requestCaptor.capture(), eq(String.class));

        assertThat(concept, equalTo(readFromFile(CONCEPT_CREATE_JSON, Concept.class)));
        assertThat(requestCaptor.getValue().getHeaders(), equalTo(getHeadersForGet(config)));
        assertThat(requestCaptor.getValue().getBody(), nullValue());
    }

    @Test
    public void shouldQueryForConceptByName() throws Exception {
        String query = "Test";
        URI url = config.toInstancePathWithParams("/concept?v=full&q={conceptName}", query);

        when(restOperations.exchange(eq(url), eq(HttpMethod.GET), any(HttpEntity.class), eq(String.class)))
                .thenReturn(getResponse(CONCEPT_LIST_RESPONSE_JSON));

        ConceptListResult result = conceptResource.queryForConceptsByName(config, query);

        verify(restOperations).exchange(eq(url), eq(HttpMethod.GET), requestCaptor.capture(), eq(String.class));

        assertThat(result, equalTo(readFromFile(CONCEPT_LIST_RESPONSE_JSON, ConceptListResult.class)));
        assertThat(requestCaptor.getValue().getHeaders(), equalTo(getHeadersForGet(config)));
        assertThat(requestCaptor.getValue().getBody(), nullValue());
    }

    private Concept prepareConcept() throws Exception {
        return (Concept) readFromFile(CONCEPT_RESPONSE_JSON, Concept.class);
    }
}
