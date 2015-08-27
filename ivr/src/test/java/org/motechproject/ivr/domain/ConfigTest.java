package org.motechproject.ivr.domain;

import org.codehaus.jackson.map.ObjectMapper;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.*;

import static org.junit.Assert.*;

/**
 * Config Unit Tests
 */
public class ConfigTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(ConfigTest.class);

    @Test
    public void shouldIgnoreFields() {

        Config config = new Config(null, false, null, null, new ArrayList<>(Arrays.asList("foo")), null, null, null, null, false, null, false, null);
        assertTrue(config.shouldIgnoreField("foo"));
        assertFalse(config.shouldIgnoreField("bar"));
    }

    @Test
    public void verifyMappedStatusFields() {
        Config config = new Config(null, false, null, null, null, "foo:bar", null, null, null, false, null, false, null);
        assertEquals("bar", config.mapStatusField("foo"));
        assertEquals("zee", config.mapStatusField("zee"));
    }

    @Test
    public void verifyCallStatusMapping() {
        Config config = new Config(null, false, null, null, null, "foo:bar", null, "1: Finished (complete), 2 : Failed (No VOTO Credit) ", null, false, null, false, null);
        assertEquals("Finished (complete)", config.getCallStatusMapping().get("1"));
        assertEquals("Failed (No VOTO Credit)", config.getCallStatusMapping().get("2"));
    }

    @Test
    public void configToJson() {
        Config config1 = new Config("myConfig1", false, null, null, Arrays.asList("foo", "bar"), "foo:bar", null, null, HttpMethod.GET,
                false, "http://foo.com/bar", false, null);
        Config config2 = new Config("myConfig2", false, null, null, Arrays.asList("foo", "bar"), "foo:bar", null, null, HttpMethod.GET,
                false, "http://foo.com/bar", false, null);
        List<Config> configs = Arrays.asList(config1, config2);

        ObjectMapper objectMapper = new ObjectMapper();
        String json = "";
        try {
            json = objectMapper.writeValueAsString(configs);
        } catch (IOException e) {
            e.printStackTrace();
        }
        LOGGER.debug("config = {}", json);
    }
}
