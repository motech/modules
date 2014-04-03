package org.motechproject.transliteration.mapping;

import org.junit.Test;

import static org.junit.Assert.assertNotNull;

/**
 * Created by kosh on 3/31/14.
 */
public class MappingTests {

    CharacterMapping englishMapping = new ItransAsciiMapping();
    CharacterMapping hindiMapping = new HindiMapping();

    @Test
    public void MappingTestNotNull() {
        assertNotNull(englishMapping);
        assertNotNull(hindiMapping);
    }

    @Test
    public void GetMappingTestNotNull() {
        assertNotNull(englishMapping.getMapping());
        assertNotNull(hindiMapping.getMapping());
    }

    @Test
    public void GetAlternateMappingTestNotNull() {
        assertNotNull(englishMapping.getAlternateMapping());
        assertNotNull(hindiMapping.getAlternateMapping());
    }
}
