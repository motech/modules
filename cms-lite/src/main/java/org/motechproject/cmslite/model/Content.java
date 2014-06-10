package org.motechproject.cmslite.model;

import java.util.Map;

/**
 * Abstract representation of CMS Lite content. Identified by name and language.
 */
public interface Content {

    Map<String, String> getMetadata();

    String getLanguage();

    String getName();
}
