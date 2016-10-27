package org.motechproject.openmrs.resource.impl;

import com.google.gson.JsonObject;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpState;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.motechproject.openmrs.config.Config;
import org.motechproject.openmrs.config.ConfigDummyData;
import org.motechproject.openmrs.domain.Order;
import org.motechproject.openmrs.util.JsonUtils;
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

public class OrderResourceImplTest extends AbstractResourceImplTest {

    private static final String ORDER_RESPONSE = "json/order/order-response.json";
    private static final String PREPARE_ORDER_JSON = "json/order/order-prepare.json";
    private static final String CREATE_ORDER_JSON = "json/order/order-create.json";

    @Mock
    private RestOperations restOperations;

    @Mock
    private HttpClient httpClient;

    @Captor
    private ArgumentCaptor<HttpEntity<String>> requestCaptor;

    private OrderResourceImpl orderResource;

    private Config config;

    @Before
    public void setUp() {
        initMocks(this);
        when(httpClient.getState()).thenReturn(new HttpState());

        orderResource = new OrderResourceImpl(restOperations, httpClient);
        config = ConfigDummyData.prepareConfig("one");
    }

    @Test
    public void shouldCreateOrder() throws Exception {
        Order order = prepareOrder();
        URI url = config.toInstancePath("/order");

        when(restOperations.exchange(eq(url), eq(HttpMethod.POST), any(HttpEntity.class), eq(String.class)))
                .thenReturn(getResponseFromFile(ORDER_RESPONSE));

        Order created = orderResource.createOrder(config, order);

        verify(restOperations).exchange(eq(url), eq(HttpMethod.POST), requestCaptor.capture(), eq(String.class));

        assertEquals(order.getType(), created.getType());
        assertEquals(order.getEncounter(), created.getEncounter());
        assertEquals(order.getOrderer(), created.getOrderer());
        assertEquals(order.getConcept().getUuid(), created.getConcept().getUuid());
        assertEquals(order.getPatient(), created.getPatient());

        assertEquals(requestCaptor.getValue().getHeaders(), getHeadersForPost(config));
        assertEquals(JsonUtils.readJson(requestCaptor.getValue().getBody(), JsonObject.class),
                readFromFile(CREATE_ORDER_JSON, JsonObject.class));
    }

    private Order prepareOrder() throws Exception {
        return (Order) readFromFile(PREPARE_ORDER_JSON, Order.class);
    }
}