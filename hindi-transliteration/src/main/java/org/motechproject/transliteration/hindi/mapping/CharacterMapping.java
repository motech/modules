package org.motechproject.transliteration.hindi.mapping;

import java.util.Map;

/**
 * Interface to define the mapping characteristics for a given language.
 */
public interface CharacterMapping {

    /**
     * Get mapping for all available tokens.
     * @return mapping for available tokens
     */
    Map<String, String[]> getMapping();

    /**
     * Get alternate mappings for tokens within the current language.
     * @return alternate mappings within the current language
     */
    Map<String, String[]> getAlternateMapping();
}
