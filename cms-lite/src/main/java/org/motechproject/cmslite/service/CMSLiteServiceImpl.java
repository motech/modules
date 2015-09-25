package org.motechproject.cmslite.service;


import org.motechproject.cmslite.model.Content;
import org.motechproject.cmslite.model.ContentNotFoundException;
import org.motechproject.cmslite.model.StreamContent;
import org.motechproject.cmslite.model.StringContent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * The implementation of {@link org.motechproject.cmslite.service.CMSLiteService}. It uses MDS for
 * storing content in the database.
 */
@Service("cmsLiteService")
public class CMSLiteServiceImpl implements CMSLiteService {
    private StringContentService stringContentService;
    private StreamContentService streamContentService;

    @Override
    @Transactional
    public StringContent getStringContent(String language, String name) throws ContentNotFoundException {
        StringContent stringContent = stringContentService.findByLanguageAndName(language, name);
        if (stringContent == null) {
            throw new ContentNotFoundException();
        }
        return stringContent;
    }

    @Override
    @Transactional
    public StreamContent getStreamContent(String language, String name) throws ContentNotFoundException {
        StreamContent streamContent = streamContentService.findByLanguageAndName(language, name);
        if (streamContent == null) {
            throw new ContentNotFoundException();
        }
        streamContent.setContent(retrieveStreamContentData(streamContent));
        return streamContent;
    }

    @Override
    @Transactional
    public void removeStreamContent(String language, String name) throws ContentNotFoundException {
        streamContentService.delete(getStreamContent(language, name));
    }

    @Override
    @Transactional
    public void removeStringContent(String language, String name) throws ContentNotFoundException {
        stringContentService.delete(getStringContent(language, name));
    }

    @Override
    @Transactional
    public List<Content> getAllContents() {
        List<Content> contents = new ArrayList<>();
        contents.addAll(streamContentService.retrieveAll());
        contents.addAll(stringContentService.retrieveAll());

        return contents;
    }

    @Override
    @Transactional
    public StreamContent getStreamContent(long streamContentId) {
        return streamContentService.findById(streamContentId);
    }

    @Override
    @Transactional
    public StringContent getStringContent(long stringContentId) {
        return stringContentService.findById(stringContentId);
    }

    @Override
    @Transactional
    public void addContent(Content content) {
        if (content == null || content.getLanguage() == null || content.getName() == null) {
            throw new IllegalArgumentException("Content or language or name should not be null");
        }

        if (content instanceof StreamContent) {
            streamContentService.create((StreamContent) content);
        } else if (content instanceof StringContent) {
            stringContentService.create((StringContent) content);
        }
    }

    @Override
    @Transactional
    public boolean isStreamContentAvailable(String language, String name) {
        StreamContent streamContent = streamContentService.findByLanguageAndName(language, name);
        return streamContent != null;
    }

    @Override
    @Transactional
    public boolean isStringContentAvailable(String language, String name) {
        StringContent stringContent = stringContentService.findByLanguageAndName(language, name);
        return stringContent != null;
    }

    @Override
    @Transactional
    public Byte[] retrieveStreamContentData(StreamContent instance) {
        return (Byte[]) streamContentService.getDetachedField(instance, "content");
    }

    @Autowired
    public void setStringContentService(StringContentService stringContentService) {
        this.stringContentService = stringContentService;
    }

    @Autowired
    public void setStreamContentService(StreamContentService streamContentService) {
        this.streamContentService = streamContentService;
    }
}
