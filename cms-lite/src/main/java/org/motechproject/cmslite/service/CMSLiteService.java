package org.motechproject.cmslite.service;

import org.motechproject.cmslite.model.Content;
import org.motechproject.cmslite.model.ContentNotFoundException;
import org.motechproject.cmslite.model.StreamContent;
import org.motechproject.cmslite.model.StringContent;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * CMS Lite is a lightweight content management based on MDS storage. It supports storing and retrieving of stream / text along with
 * custom metadata for each language.
 */
@Component
public interface CMSLiteService {
    /**
     * Get Stream content with the given name and language.
     *
     * @param language the language of the content
     * @param name the name of the content
     * @return StreamContent that matches the parameters
     * @throws ContentNotFoundException if no such content exists
     */
    StreamContent getStreamContent(String language, String name) throws ContentNotFoundException;

    /**
     * Get Text Content for given name and language.
     *
     * @param language the language of the content
     * @param name the name of the content
     * @return StringContent that matches the parameters
     * @throws ContentNotFoundException if no such content exists
     */
    StringContent getStringContent(String language, String name) throws ContentNotFoundException;

    /**
     * Remove stream content for a given name and language.
     *
     * @param language the language of the content to remove
     * @param name the name of the content to remove
     * @throws ContentNotFoundException if no such content exists
     */
    void removeStreamContent(String language, String name) throws ContentNotFoundException;

    /**
     * Remove text content for given name and language.
     *
     * @param language the language of the content to remove
     * @param name the name of the content to remove
     * @throws ContentNotFoundException if no such content exists
     */
    void removeStringContent(String language, String name) throws ContentNotFoundException;

    /**
     * Add content to CMS data-store.
     *
     * @param content the content to add
     * @see org.motechproject.cmslite.model.StreamContent
     * @see org.motechproject.cmslite.model.StringContent
     */
    void addContent(Content content);

    /**
     * Check if a stream content with the given name and language exists.
     *
     * @param language the language of the content
     * @param name the name of the content
     * @return true if a matching stream content exists, false otherwise
     */
    boolean isStreamContentAvailable(String language, String name);

    /**
     * Check if a text content with the given name and language exists.
     *
     * @param language the language of the content
     * @param name the name of the content
     * @return true if a matching text content exists, false otherwise
     */
    boolean isStringContentAvailable(String language, String name);

    /**
     * Retrieves a list of all String and Stream contents. Returned list is defined
     * as a list of generic Content instances, containing basic information such as language, name and metadata.
     * To make better use of instances retrieved this way an <code>instanceof</code> check should be
     * performed, followed by a cast to either <code>StringContent</code> or <code>StreamContent</code>
     *
     * @see org.motechproject.cmslite.model.StringContent
     * @see org.motechproject.cmslite.model.StreamContent
     *
     * @return list of all contents
     */
    List<Content> getAllContents();

    /**
     * Retrieved <code>StringContent</code> by its ID.
     *
     * @see org.motechproject.cmslite.model.StringContent
     *
     * @param stringContentId ID of a StringContent to retrieved
     * @return StringContent of a given ID
     */
    StringContent getStringContent(long stringContentId);

    /**
     * Retrieved <code>StreamContent</code> by its ID.
     *
     * @see org.motechproject.cmslite.model.StreamContent
     *
     * @param streamContentId ID of a StreamContent to retrieved
     * @return StreamContent of a given ID
     */
    StreamContent getStreamContent(long streamContentId);

    /**
     * Helper method, retrieving data of a <code>StreamContent</code>. The data is returned
     * as a byte array.
     *
     * @param instance Instance of a StreamContent
     * @return byte array, containing data of a StreamContent
     */
    Byte[] retrieveStreamContentData(StreamContent instance);
}
