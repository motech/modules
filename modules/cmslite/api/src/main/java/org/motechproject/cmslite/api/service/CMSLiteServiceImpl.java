package org.motechproject.cmslite.api.service;


import org.motechproject.cmslite.api.model.CMSLiteException;
import org.motechproject.cmslite.api.model.Content;
import org.motechproject.cmslite.api.model.ContentNotFoundException;
import org.motechproject.cmslite.api.model.StreamContent;
import org.motechproject.cmslite.api.model.StringContent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service("cmsLiteService")
public class CMSLiteServiceImpl implements CMSLiteService {
    private StringContentService stringContentService;
    private StreamContentService streamContentService;

    @Override
    public StringContent getStringContent(String language, String name) throws ContentNotFoundException {
        List<StringContent> stringContents = stringContentService.findByLanguageAndName(language, name);
        if (stringContents == null || stringContents.size() == 0) {
            throw new ContentNotFoundException();
        }
        return stringContents.get(0);
    }

    @Override
    public StreamContent getStreamContent(String language, String name) throws ContentNotFoundException {
        List<StreamContent> streamContents = streamContentService.findByLanguageAndName(language, name);
        if (streamContents == null || streamContents.size() == 0) {
            throw new ContentNotFoundException();
        }
        StreamContent streamContent = streamContents.get(0);
        streamContent.setContent(retrieveStreamContentData(streamContent));
        return streamContent;
    }

    @Override
    public void removeStreamContent(String language, String name) throws ContentNotFoundException {
        streamContentService.delete(getStreamContent(language, name));
    }

    @Override
    public void removeStringContent(String language, String name) throws ContentNotFoundException {
        stringContentService.delete(getStringContent(language, name));
    }

    @Override
    public List<Content> getAllContents() {
        List<Content> contents = new ArrayList<>();
        contents.addAll(streamContentService.retrieveAll());
        contents.addAll(stringContentService.retrieveAll());

        return contents;
    }

    public StreamContent getStreamContent(String streamContentId) {
        return streamContentService.retrieve("id", streamContentId);
    }
    public StringContent getStringContent(String stringContentId) {
        return stringContentService.retrieve("id", stringContentId);
    }

    @Override
    public void addContent(Content content) throws CMSLiteException {
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
    public boolean isStreamContentAvailable(String language, String name) {
        List<StreamContent> streamContents = streamContentService.findByLanguageAndName(language, name);
        return !(streamContents == null || streamContents.size() == 0);
    }

    @Override
    public boolean isStringContentAvailable(String language, String name) {
        List<StringContent> stringContents = stringContentService.findByLanguageAndName(language, name);
        return !(stringContents == null || stringContents.size() == 0);
    }

    @Override
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
