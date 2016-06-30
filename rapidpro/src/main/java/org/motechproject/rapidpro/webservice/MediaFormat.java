package org.motechproject.rapidpro.webservice;

/**
 * Represents all supported media formats.
 */
public enum MediaFormat {

    JSON(".json", "application/json"),
    XML(".xml", "application/xml");

    private String extension;
    private String contentType;

    MediaFormat(String extension, String contentType) {
        this.extension = extension;
        this.contentType = contentType;
    }

    public String getExtension() {
        return this.extension;
    }

    public String getContentType() {
        return contentType;
    }
}
