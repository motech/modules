package org.motechproject.cmslite.web;

import org.apache.commons.lang.ArrayUtils;
import org.motechproject.cmslite.model.CMSLiteException;
import org.motechproject.cmslite.model.Content;
import org.motechproject.cmslite.model.ContentNotFoundException;
import org.motechproject.cmslite.model.StreamContent;
import org.motechproject.cmslite.model.StringContent;
import org.motechproject.cmslite.service.CMSLiteService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.TreeSet;

import static java.util.Locale.getAvailableLocales;
import static org.apache.commons.codec.digest.DigestUtils.md5Hex;
import static org.apache.commons.lang.StringUtils.isBlank;
import static org.apache.commons.lang.StringUtils.startsWith;
import static org.apache.commons.lang.StringUtils.startsWithIgnoreCase;

/**
 * The controller responsible for serving CMS-Lite resources to users through HTTP.
 * Handles both the grid in the cms-lite UI and REST requests for particular resources.
 */
@Controller
public class ResourceController {

    private static final Logger LOGGER = LoggerFactory.getLogger(ResourceController.class);

    @Autowired
    private CMSLiteService cmsLiteService;

    /**
     * Handles the auto-complete feature for cms-lite. Will return auto-completions for the given field.
     * @param field the field to auto-complete, name and language are currently supported
     * @param term what should be auto-complete, what the user has entered
     * @return a set of auto-completion values
     * @throws CMSLiteException if the field is not supported
     */
    @RequestMapping(value = "/resource/available/{field}", method = RequestMethod.GET)
    @ResponseBody
    public Set<String> autocompleteField(@PathVariable String field, @RequestParam String term) throws CMSLiteException {
        Set<String> strings = new TreeSet<>();

        switch (field) {
            case "name":
                // TODO: MOTECH-1717 - we retrieve EVERYTHING from the database here, which can lead to obvious problems
                List<Content> contents = cmsLiteService.getAllContents();
                for (Content content : contents) {
                    if (startsWith(content.getName(), term)) {
                        strings.add(content.getName());
                    }
                }
                break;
            case "language":
                for (Locale locale : getAvailableLocales()) {
                    if (startsWithIgnoreCase(locale.getDisplayLanguage(), term)) {
                        strings.add(locale.getDisplayLanguage());
                    }
                }
                break;
            default:
                throw new CMSLiteException(String.format("\"%s\" is not a valid field name!", field));
        }

        return strings;
    }

    /**
     * Retrieves resource entries for the jqgrid. The returned object will not contain the content,
     * only their types, names and languages.
     * @param settings the settings of the grid, filtering and paging information
     * @return all resources matching the criteria
     */
    @RequestMapping(value = "/resource", method = RequestMethod.GET)
    @ResponseBody
    public Resources getContents(GridSettings settings) {
        List<Content> contents = cmsLiteService.getAllContents();
        List<ResourceDto> resourceDtos = ResourceFilter.filter(settings, contents);

        Collections.sort(resourceDtos, new ResourceComparator(settings));

        return new Resources(settings, resourceDtos);
    }

    /**
     * Returns all languages associated with any content in cms-lite.
     * @return all languages from the system
     */
    @RequestMapping(value = "/resource/all/languages", method = RequestMethod.GET)
    @ResponseBody
    public Set<String> getAllLanguages() {
        List<Content> contents = cmsLiteService.getAllContents();
        Set<String> strings = new TreeSet<>();

        for (Content content : contents) {
            strings.add(content.getLanguage());
        }

        return strings;
    }

    /**
     * Retrieves a content of a given type for the UI.
     * @param type the type of the content, either <b>stream</b> or <b>string</b>
     * @param language the language of the content
     * @param name the name of the content
     * @return the matching content object
     * @throws ContentNotFoundException if the content does not exist
     * @throws CMSLiteException if the provided content type is invalid
     */
    @RequestMapping(value = "/resource/{type}/{language}/{name}", method = RequestMethod.GET)
    @ResponseBody
    public Content getContent(@PathVariable String type, @PathVariable String language, @PathVariable String name) throws ContentNotFoundException, CMSLiteException {
        Content content;

        switch (type) {
            case "stream":
                content = cmsLiteService.getStreamContent(language, name);
                break;
            case "string":
                content = cmsLiteService.getStringContent(language, name);
                break;
            default:
                throw new CMSLiteException(String.format("\"%s\" is not a valid content type!", type));
        }

        return content;
    }

    /**
     * Updates an existing string content in the CMS system. Performed by the UI.
     * @param language the language for the content
     * @param name the name of the content
     * @param value the value of the content
     * @throws ContentNotFoundException if the content does not exist
     */
    @RequestMapping(value = "/resource/string/{language}/{name}", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    public void editStringContent(@PathVariable String language, @PathVariable String name,
                                  @RequestParam String value) throws ContentNotFoundException {
        StringContent stringContent = cmsLiteService.getStringContent(language, name);
        stringContent.setValue(value);

        cmsLiteService.addContent(stringContent);
    }

    /**
     * Updates an existing stream content in the CMS system. Performed by the UI.
     * @param language the language for the content
     * @param name the name of the content
     * @param contentFile the file that should be persisted as this content
     * @throws ContentNotFoundException if the content does not exist
     * @throws IOException if there were IO issues with the uploaded file
     */
    @RequestMapping(value = "/resource/stream/{language}/{name}", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    public void editStreamContent(@PathVariable String language, @PathVariable String name,
                                  @RequestParam MultipartFile contentFile) throws ContentNotFoundException, IOException {
        StreamContent streamContent = cmsLiteService.getStreamContent(language, name);

        streamContent.setChecksum(md5Hex(contentFile.getBytes()));
        streamContent.setContentType(contentFile.getContentType());
        streamContent.setContent(ArrayUtils.toObject(contentFile.getBytes()));

        cmsLiteService.addContent(streamContent);
    }

    /**
     * Creates a new content in the system. Performed by the UI.
     * @param type the type of the content, either <b>string</b> or <b>stream</b>
     * @param name the name of the content
     * @param language the language of the content
     * @param value the value of the content in case of a string content
     * @param contentFile the file to be saved as the content in case of a stream content
     * @throws CMSLiteException if a wrong combination of parameters was passed
     * @throws IOException if there were IO issues with the uploaded file
     */
    @RequestMapping(value = "/resource", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    public void addContent(@RequestParam String type,
                           @RequestParam String name,
                           @RequestParam String language,
                           @RequestParam(required = false) String value,
                           @RequestParam(required = false) MultipartFile contentFile) throws CMSLiteException, IOException {
        if (isBlank(type)) {
            throw new CMSLiteException("Resource type is required");
        }

        if (isBlank(name)) {
            throw new CMSLiteException("Resource name is required");
        }

        if (isBlank(language)) {
            throw new CMSLiteException("Resource language is required");
        }

        switch (type) {
            case "string":
                if (isBlank(value)) {
                    throw new CMSLiteException("Resource content is required");
                }

                if (cmsLiteService.isStringContentAvailable(language, name)) {
                    throw new CMSLiteException(String.format("Resource %s in %s language already exists.", name, language));
                }

                cmsLiteService.addContent(new StringContent(language, name, value));
                break;
            case "stream":
                if (null == contentFile) {
                    throw new CMSLiteException("Resource content is required");
                }

                if (cmsLiteService.isStreamContentAvailable(language, name)) {
                    throw new CMSLiteException(String.format("Resource %s in %s language already exists.", name, language));
                }

                String checksum = md5Hex(contentFile.getBytes());
                String contentType = contentFile.getContentType();

                cmsLiteService.addContent(new StreamContent(language, name, ArrayUtils.toObject(contentFile.getBytes()), checksum, contentType));
                break;
            default:
                throw new CMSLiteException(String.format("\"%s\" is not a valid content type!", type));
        }
    }

    /**
     * Removes the content of the given type from the CMS system. Performed by the UI.
     * @param type the type of the content
     * @param language the language of the content
     * @param name the name of the content
     * @throws ContentNotFoundException if the content does not exist
     * @throws CMSLiteException if a wrong type was provided
     */
    @RequestMapping(value = "/resource/{type}/{language}/{name}", method = RequestMethod.DELETE)
    @ResponseStatus(HttpStatus.OK)
    public void removeContent(@PathVariable String type, @PathVariable String language, @PathVariable String name) throws ContentNotFoundException, CMSLiteException {
        switch (type) {
            case "stream":
                cmsLiteService.removeStreamContent(language, name);
                break;
            case "string":
                cmsLiteService.removeStringContent(language, name);
                break;
            default:
                throw new CMSLiteException(String.format("\"%s\" is not a valid content type!", type));
        }
    }

    /**
     * Retrieves the given stream content in raw form. Used by external callers to retrieve the content.
     * This writes the content to the HttpServletResponse object and also sets the mime on that response to the type
     * of the content.
     * @param language the language of the content
     * @param name the name of the content
     * @param response the http response into which the content will be written to
     * @throws ContentNotFoundException if the content does not exist
     * @throws IOException if there was an IO error writing to the response
     */
    @RequestMapping(value = "/stream/{language}/{name}", method = RequestMethod.GET)
    public void getStreamContent(@PathVariable String language, @PathVariable String name, HttpServletResponse response)
            throws ContentNotFoundException, IOException {
        LOGGER.info(String.format("Getting resource for : stream:%s:%s", language, name));

        try (OutputStream out = response.getOutputStream()) {

            StreamContent streamContent = cmsLiteService.getStreamContent(language, name);

            response.setContentLength(streamContent.getContent().length);
            response.setContentType(streamContent.getContentType());
            response.setHeader("Accept-Ranges", "bytes");
            response.setStatus(HttpServletResponse.SC_OK);

            out.write(ArrayUtils.toPrimitive(streamContent.getContent()));
        }
    }

    /**
     * Retrieves the given string content in raw form. Used by external callers to retrieve the content.
     * This writes the content as text to the HttpServletResponse object and also sets the mime to text/plain.
     * @param language the language of the content
     * @param name the name of the content
     * @param response the http response into which the content will be written to
     * @throws ContentNotFoundException if the content does not exist
     * @throws IOException if there was an IO error writing to the response
     */
    @RequestMapping(value = "/string/{language}/{name}", method = RequestMethod.GET)
    public void getStringContent(@PathVariable String language, @PathVariable String name, HttpServletResponse response)
            throws ContentNotFoundException, IOException {
        LOGGER.info(String.format("Getting resource for : string:%s:%s", language, name));

        try (PrintWriter writer = response.getWriter()) {

            StringContent stringContent = cmsLiteService.getStringContent(language, name);

            response.setContentLength(stringContent.getValue().length());
            response.setContentType("text/plain");
            response.setStatus(HttpServletResponse.SC_OK);

            writer.print(stringContent.getValue());
        }
    }

    /**
     * Handler for the {@link org.motechproject.cmslite.model.ContentNotFoundException}.
     * Will return 404 (Not Found) as the HTTP response code and the message from the exception as the body.
     * @param e the exception to handle
     * @return the message from the exception, to be displayed as the response body
     */
    @ExceptionHandler(ContentNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ResponseBody
    public String handleContentNotFoundException(Exception e) {
        return e.getMessage();
    }

    /**
     * Handler for the {@link org.motechproject.cmslite.model.CMSLiteException}.
     * Will return 400 (Bad Request) as the HTTP response code and the message from the exception as the body.
     * @param e the exception to handle
     * @return the message from the exception, to be displayed as the response body
     */
    @ExceptionHandler(CMSLiteException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public String handleCMSLiteException(Exception e) {
        return e.getMessage();
    }
}
