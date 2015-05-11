package org.motechproject.transliteration.hindi.service;

/**
 * A service for transliterating English strings to Hindi.
 */
public interface TransliterationService {

    /**
     * Transliterates the provided English string to Hindi.
     * @param data the English string to transliterate
     * @return the input transliterated to Hindi
     */
    String transliterate(String data);
}
