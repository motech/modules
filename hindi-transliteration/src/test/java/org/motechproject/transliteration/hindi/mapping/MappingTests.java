package org.motechproject.transliteration.hindi.mapping;

import org.junit.Test;

import static org.junit.Assert.assertNotNull;

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
