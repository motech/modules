package org.motechproject.commcare.web;

import com.google.gson.reflect.TypeToken;
import org.apache.http.HttpStatus;
import org.codehaus.jackson.map.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.motechproject.commcare.domain.AppStructureResponseJson;
import org.motechproject.commcare.domain.CommcareApplicationJson;
import org.motechproject.commcare.service.CommcareApplicationDataService;
import org.motechproject.commons.api.json.MotechJsonReader;
import org.springframework.test.web.server.MockMvc;
import org.springframework.test.web.server.setup.MockMvcBuilders;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.springframework.test.web.server.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.server.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.server.result.MockMvcResultMatchers.status;

public class SchemaControllerTest {

    @Mock
    private CommcareApplicationDataService commcareApplicationDataService;

    @InjectMocks
    private SchemaController schemaController = new SchemaController();

    private MockMvc controller;

    private MotechJsonReader motechJsonReader = new MotechJsonReader();
    private ObjectMapper objectMapper = new ObjectMapper();

    @Before
    public void setUp() throws Exception {
        initMocks(this);

        controller = MockMvcBuilders.standaloneSetup(schemaController).build();
    }

    @Test
    public void shouldReturnAppStructure() throws Exception {
        List<CommcareApplicationJson> apps = application();
        when(commcareApplicationDataService.retrieveAll()).thenReturn(apps);

        final String expectedResponse = objectMapper.writeValueAsString(apps);

        controller.perform(
                get("/schema")
        ).andExpect(
                status().is(HttpStatus.SC_OK)
        ).andExpect(
                content().type("application/json;charset=UTF-8")
        ).andExpect(
                content().string(expectedResponse)
        );
    }

    private List<CommcareApplicationJson> application() throws IOException {
        try (InputStream in = getClass().getClassLoader().getResourceAsStream("json/service/appStructure.json")) {
            Type appStructureResponseType = new TypeToken<AppStructureResponseJson>() {}.getType();
            return ((AppStructureResponseJson) motechJsonReader.readFromStream(in, appStructureResponseType))
                    .getApplications();
        }
    }
}
