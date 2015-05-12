package org.motechproject.cmslite.model;

import java.util.Map;

/**
 * Abstract representation of CMS Lite content. Identified by name and language.
 */
public interface Content {

    /**
     * Returns the metadata for this content. The metadata is a string-string map.
     * @return the metadata for this content
     */
    Map<String, String> getMetadata();

    /**
     * Returns the language of this content. Each content should have a language associated with it.
     * @return the language of this content
     */
    String getLanguage();

    /**
     * Returns the name identifying this content.
     * @return the name of this content
     */
    String getName();
}
