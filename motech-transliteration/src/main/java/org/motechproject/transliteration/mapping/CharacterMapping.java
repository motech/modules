package org.motechproject.transliteration.mapping;

import java.util.HashMap;

/**
 * Created by kosh on 3/17/14.
 */
// Interface to define the mapping characteristics for a given language
public interface CharacterMapping {

    // Get mapping for all available tokens
    public HashMap<String, String[]> getMapping();

    // get alternate mappings for tokens within the current language
    public HashMap<String, String[]> getAlternateMapping();
}
