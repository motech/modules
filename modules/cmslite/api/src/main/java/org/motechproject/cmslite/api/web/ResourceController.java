package org.motechproject.cmslite.api.web;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.ArrayUtils;
import org.motechproject.cmslite.api.model.CMSLiteException;
import org.motechproject.cmslite.api.model.Content;
import org.motechproject.cmslite.api.model.ContentNotFoundException;
import org.motechproject.cmslite.api.model.StreamContent;
import org.motechproject.cmslite.api.model.StringContent;
import org.motechproject.cmslite.api.service.CMSLiteService;
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
import java.util.Arrays;
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

@Controller
public class ResourceController {

    private static final String NOT_FOUND_RESPONSE = "Content not found";

    private static final Logger LOG = LoggerFactory.getLogger(ResourceController.class);

    @Autowired
    private CMSLiteService cmsLiteService;

    @RequestMapping(value = "/resource/available/{field}", method = RequestMethod.GET)
    @ResponseBody
    public Set<String> availableField(@PathVariable String field, @RequestParam String term) {
        Set<String> strings = new TreeSet<>();

        switch (field) {
            case "name":
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
        }

        return strings;
    }

    @RequestMapping(value = "/resource", method = RequestMethod.GET)
    @ResponseBody
    public Resources getContents(GridSettings settings) {
        List<Content> contents = cmsLiteService.getAllContents();
        List<ResourceDto> resourceDtos = ResourceFilter.filter(settings, contents);

        Collections.sort(resourceDtos, new ResourceComparator(settings));

        return new Resources(settings, resourceDtos);
    }

    @RequestMapping(value = "/resource/all/languages", method = RequestMethod.GET)
    @ResponseBody
    public Set<String> getAllLanguages() throws ContentNotFoundException {
        List<Content> contents = cmsLiteService.getAllContents();
        Set<String> strings = new TreeSet<>();

        for (Content content : contents) {
            strings.add(content.getLanguage());
        }

        return strings;
    }

    @RequestMapping(value = "/resource/{type}/{language}/{name}", method = RequestMethod.GET)
    @ResponseBody
    public Content getContent(@PathVariable String type, @PathVariable String language, @PathVariable String name) throws ContentNotFoundException {
        Content content = null;

        switch (type) {
            case "stream":
                content = cmsLiteService.getStreamContent(language, name);
                break;
            case "string":
                content = cmsLiteService.getStringContent(language, name);
                break;
            default:
        }

        return content;
    }

    @RequestMapping(value = "/resource/string/{language}/{name}", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    public void editStringContent(@PathVariable String language, @PathVariable String name,
                                  @RequestParam String value) throws ContentNotFoundException, CMSLiteException, IOException {
        StringContent stringContent = cmsLiteService.getStringContent(language, name);
        stringContent.setValue(value);

        cmsLiteService.addContent(stringContent);
    }

    @RequestMapping(value = "/resource/stream/{language}/{name}", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    public void editStreamContent(@PathVariable String language, @PathVariable String name,
                                  @RequestParam MultipartFile contentFile) throws ContentNotFoundException, CMSLiteException, IOException {
        StreamContent streamContent = cmsLiteService.getStreamContent(language, name);

        streamContent.setChecksum(md5Hex(contentFile.getBytes()));
        streamContent.setContentType(contentFile.getContentType());
        streamContent.setContent(ArrayUtils.toObject(contentFile.getBytes()));

        cmsLiteService.addContent(streamContent);
    }

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
        }
    }

    @RequestMapping(value = "/resource/{type}/{language}/{name}", method = RequestMethod.DELETE)
    @ResponseStatus(HttpStatus.OK)
    public void removeContent(@PathVariable String type, @PathVariable String language, @PathVariable String name) throws ContentNotFoundException {
        switch (type) {
            case "stream":
                cmsLiteService.removeStreamContent(language, name);
                break;
            case "string":
                cmsLiteService.removeStringContent(language, name);
                break;
            default:
        }
    }

    @RequestMapping(value = "/stream/{language}/{name}", method = RequestMethod.GET)
    public void getStreamContent(@PathVariable String language, @PathVariable String name, HttpServletResponse response)
            throws IOException {
        LOG.info(String.format("Getting resource for : stream:%s:%s", language, name));

        try (OutputStream out = response.getOutputStream()) {

            StreamContent streamContent = cmsLiteService.getStreamContent(language, name);

            response.setContentLength(streamContent.getContent().length);
            response.setContentType(streamContent.getContentType());
            response.setHeader("Accept-Ranges", "bytes");
            response.setStatus(HttpServletResponse.SC_OK);

            out.write(ArrayUtils.toPrimitive(streamContent.getContent()));
        } catch (Exception e) {
            LOG.error(String.format("Content not found for : stream:%s:%s%n:%s", language, name,
                    Arrays.toString(e.getStackTrace())));
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, NOT_FOUND_RESPONSE);
        }
    }

    @RequestMapping(value = "/string/{language}/{name}", method = RequestMethod.GET)
    public void getStringContent(@PathVariable String language, @PathVariable String name, HttpServletResponse response)
            throws IOException {
        LOG.info(String.format("Getting resource for : string:%s:%s", language, name));

        PrintWriter writer = null;

        try {
            writer = response.getWriter();

            StringContent stringContent = cmsLiteService.getStringContent(language, name);

            response.setContentLength(stringContent.getValue().length());
            response.setContentType("text/plain");
            response.setStatus(HttpServletResponse.SC_OK);

            writer.print(stringContent.getValue());
        } catch (Exception e) {
            LOG.error(String.format("Content not found for : string:%s:%s%n:%s", language, name,
                    Arrays.toString(e.getStackTrace())));
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, NOT_FOUND_RESPONSE);
        } finally {
            IOUtils.closeQuietly(writer);
        }
    }

    @ExceptionHandler({ContentNotFoundException.class, CMSLiteException.class, IOException.class })
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ResponseBody
    public String handleException(Exception e) {
        return e.getMessage();
    }

}
