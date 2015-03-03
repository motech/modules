package org.motechproject.cmslite;

import org.motechproject.cmslite.model.ContentNotFoundException;
import org.motechproject.cmslite.model.StreamContent;
import org.motechproject.cmslite.model.StringContent;
import org.motechproject.cmslite.service.CMSLiteService;
import org.motechproject.commons.api.AbstractDataProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class CMSDataProvider extends AbstractDataProvider {
    private static final String ID_FIELD = "cmslite.id";
    private static final String NAME_FIELD = "cmslite.dataname";
    private static final String LANGUAGE_FIELD = "cmslite.language";

    private CMSLiteService cmsLiteService;

    @Autowired
    public void setCmsLiteService(CMSLiteService cmsLiteService) {
        this.cmsLiteService = cmsLiteService;
    }

    @Autowired
    public CMSDataProvider(ResourceLoader resourceLoader) {
        Resource resource = resourceLoader.getResource("task-data-provider.json");
        if (resource != null) {
            setBody(resource);
        }
    }

    @Override
    public String getName() {
        return "cmslite";
    }

    @Override
    public Object lookup(String type, String lookupName, Map<String, String> lookupFields) {
        Object obj = null;
        try {
            if (supports(type)) {
                if (lookupFields.containsKey(ID_FIELD)) {
                    String id = lookupFields.get(ID_FIELD);
                    Class<?> cls = getClassForType(type);
                    obj = getContent(cls, id);

                } else if (lookupFields.containsKey(NAME_FIELD) && lookupFields.containsKey(LANGUAGE_FIELD)) {
                    String name = lookupFields.get(NAME_FIELD);
                    String language = lookupFields.get(LANGUAGE_FIELD);
                    Class<?> cls = getClassForType(type);
                    obj = getContent(cls, language, name);
                }
            }
        } catch (ClassNotFoundException | ContentNotFoundException e) {
            getLogger().error("Cannot lookup object: {type: %s, fields: %s}", type, lookupFields.keySet(), e);
        }
        return obj;
    }

    @Override
    public List<Class<?>> getSupportClasses() {
        List<Class<?>> list = new ArrayList<>();
        list.add(StringContent.class);
        list.add(StreamContent.class);
        return list;
    }

    @Override
    public String getPackageRoot() {
        return "org.motechproject.cmslite.model";
    }

    private Object getStringContent(String stringContentId) {
        return cmsLiteService.getStringContent(stringContentId);
    }

    private Object getStreamContent(String streamContentId) {
        return cmsLiteService.getStreamContent(streamContentId);
    }

    private Object getStringContent(String stringContentLanguage, String stringContentName) throws ContentNotFoundException {
        return cmsLiteService.getStringContent(stringContentLanguage, stringContentName);
    }

    private Object getStreamContent(String streamContentLanguage, String streamContentName) throws ContentNotFoundException {
        return cmsLiteService.getStreamContent(streamContentLanguage, streamContentName);
    }

    private Object getContent(Class<?> cls, String language, String name) throws ContentNotFoundException {
        if (StringContent.class.isAssignableFrom(cls)) {
            return getStringContent(language, name);
        } else if (StreamContent.class.isAssignableFrom(cls)) {
            return getStreamContent(language, name);
        }
        throw new ContentNotFoundException();
    }

    private Object getContent(Class<?> cls, String id) throws ContentNotFoundException {
        if (StringContent.class.isAssignableFrom(cls)) {
            return getStringContent(id);
        } else if (StreamContent.class.isAssignableFrom(cls)) {
            return getStreamContent(id);
        }
        throw new ContentNotFoundException();
    }
}
