package org.motechproject.transliteration.hindi.mapping;

import java.util.Map;

/**
 * Created by kosh on 3/17/14.
 */
// Interface to define the mapping characteristics for a given language
public interface CharacterMapping {

    // Get mapping for all available tokens
    Map<String, String[]> getMapping();

    // get alternate mappings for tokens within the current language
    Map<String, String[]> getAlternateMapping();
}
