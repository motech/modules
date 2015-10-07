package org.motechproject.mtraining.service.ut;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.motechproject.mds.util.InstanceSecurityRestriction;
import org.motechproject.mtraining.repository.PropertiesMapQueryExecution;

import javax.jdo.Query;
import java.util.HashMap;
import java.util.Map;

import static org.mockito.Mockito.verify;
import static org.mockito.MockitoAnnotations.initMocks;

public class PropertiesMapQueryExecutionTest {

    private static final String KEY_1 = "key_1";
    private static final String KEY_2 = "key_2";
    private static final String VALUE_1 = "value_1";

    @Mock
    private Query query;

    @Mock
    private InstanceSecurityRestriction instanceSecurityRestriction;

    @Before
    public void setup() {
        initMocks(this);
    }

    @Test
    public void shouldBuildCorrectFilterWithoutMap() {
        PropertiesMapQueryExecution queryExecution = new PropertiesMapQueryExecution(null);
        queryExecution.execute(query, instanceSecurityRestriction);

        verify(query).setFilter("");
    }

    @Test
    public void shouldBuildCorrectFilter() {
        Map<String, String> properties = new HashMap<>();
        properties.put(KEY_1, VALUE_1);

        PropertiesMapQueryExecution queryExecution = new PropertiesMapQueryExecution(properties);
        queryExecution.execute(query, instanceSecurityRestriction);

        verify(query).setFilter(String.format("(properties.containsKey('key_1') && properties.get('key_1') == 'value_1')"));

        properties.put(KEY_2, "");

        queryExecution = new PropertiesMapQueryExecution(properties);
        queryExecution.execute(query, instanceSecurityRestriction);

        verify(query).setFilter(String.format("(properties.containsKey('key_2') && properties.get('key_2') == '') && (properties.containsKey('key_1') && properties.get('key_1') == 'value_1')"));
    }
}