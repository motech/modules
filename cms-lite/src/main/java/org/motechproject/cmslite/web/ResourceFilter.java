package org.motechproject.cmslite.web;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.motechproject.cmslite.model.Content;
import org.motechproject.cmslite.model.StreamContent;
import org.motechproject.cmslite.model.StringContent;

import java.util.ArrayList;
import java.util.List;

import static org.apache.commons.lang.StringUtils.equalsIgnoreCase;

/**
 * This class is responsible for filtering content instances based on the provided grid settings.
 */
public final class ResourceFilter {

    /**
     * Filters the given list of contents based on the grid settings.
     * @param settings the grid settings to use
     * @param contents a list of all contents
     * @return a new list with the matching content instances
     */
    public static List<ResourceDto> filter(GridSettings settings, final List<Content> contents) {
        List<ResourceDto> resourceDtos = new ArrayList<>();

        for (final Content content : contents) {
            if (settings.isCorrect(content.getName(), content.getLanguage(), getContentType(content))) {
                ResourceDto dto = (ResourceDto) CollectionUtils.find(resourceDtos, new Predicate() {
                    @Override
                    public boolean evaluate(Object object) {
                        return object instanceof ResourceDto &&
                                equalsContent((ResourceDto) object, content.getName(), getContentType(content));
                    }
                });

                if (dto == null) {
                    resourceDtos.add(new ResourceDto(content));
                } else {
                    dto.addLanguage(content.getLanguage());
                }
            }
        }

        return resourceDtos;
    }

    /**
     * Returns the type, as in string or stream, for this content.
     * @param content the content to check
     * @return <b>string</b> for string content, <b>stream</b> for stream content and a blank string for other cases (null content)
     */
    public static String getContentType(Content content) {
        String contentType;

        if (content instanceof StringContent) {
            contentType = "string";
        } else if (content instanceof StreamContent) {
            contentType = "stream";
        } else {
            contentType = "";
        }

        return contentType;
    }

    /**
     * Checks whether the {@link org.motechproject.cmslite.web.ResourceDto} matches the given content name and content type.
     * @param dto the dto to check
     * @param contentName the expected name
     * @param contentType the expected type, <b>string</b> or <b>stream</b>
     * @return true if the dto matches the parameters, false otherwise
     */
    public static boolean equalsContent(ResourceDto dto, String contentName, String contentType) {
        return dto.getName().equals(contentName) && equalsIgnoreCase(dto.getType(), contentType);
    }

    private ResourceFilter() {
    }
}
