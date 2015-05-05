package org.motechproject.dhis2.rest.service.impl;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.auth.BasicScheme;
import org.apache.http.osgi.services.HttpClientBuilderFactory;
import org.motechproject.admin.service.StatusMessageService;
import org.motechproject.dhis2.service.Settings;
import org.motechproject.dhis2.rest.domain.BaseDto;
import org.motechproject.dhis2.rest.domain.DataElementDto;
import org.motechproject.dhis2.rest.domain.DhisEventDto;
import org.motechproject.dhis2.rest.domain.DhisStatusResponse;
import org.motechproject.dhis2.rest.domain.EnrollmentDto;
import org.motechproject.dhis2.rest.domain.OrganisationUnitDto;
import org.motechproject.dhis2.rest.domain.PagedResourceDto;
import org.motechproject.dhis2.rest.domain.ProgramDto;
import org.motechproject.dhis2.rest.domain.ProgramStageDto;
import org.motechproject.dhis2.rest.domain.TrackedEntityAttributeDto;
import org.motechproject.dhis2.rest.domain.TrackedEntityDto;
import org.motechproject.dhis2.rest.domain.TrackedEntityInstanceDto;
import org.motechproject.dhis2.rest.service.DhisWebService;
import org.motechproject.dhis2.rest.service.DhisWebException;
import org.motechproject.dhis2.service.SettingsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Implementation of {@link org.motechproject.dhis2.rest.service.DhisWebService}
 */
@Service("dhisWebService")
public class DhisWebServiceImpl implements DhisWebService {
    private static final Logger LOGGER = LoggerFactory.getLogger(DhisWebServiceImpl.class);

    private static final String MODULE_NAME = "dhis2";
    private static final String API_ENDPOINT = "/api";
    private static final String ENROLLMENTS_PATH = "/enrollments";
    private static final String EVENTS_PATH = "/events";
    private static final String TRACKED_ENTITY_INSTANCES_PATH = "/trackedEntityInstances";

    private static final String DATA_ELEMENTS = "dataElements";
    private static final String ORG_UNITS = "organisationUnits";
    private static final String PROGRAMS = "programs";
    private static final String PROGRAM_STAGES = "programStages";
    private static final String TRACKED_ENTITIES = "trackedEntities";
    private static final String TRACKED_ENITTY_ATTRIBUTES = "trackedEntityAttributes";

    private SettingsService settingsService;
    private StatusMessageService statusMessageService;
    private HttpClient client;

    public static final List<Integer> ACCEPTABLE_DHIS_RESPONSE_STATUSES = Arrays.asList(HttpStatus.SC_OK,
            HttpStatus.SC_ACCEPTED, HttpStatus.SC_CREATED);

    @Autowired
    public DhisWebServiceImpl(@Qualifier("dhisSettingsService") SettingsService settingsService,
                              StatusMessageService statusMessageService,
                              HttpClientBuilderFactory httpClientBuilderFactory) {
        this.settingsService = settingsService;
        this.statusMessageService = statusMessageService;
        this.client = httpClientBuilderFactory.newBuilder().build();
    }

    @Override
    public List<DataElementDto> getDataElements() {
        return getResources(DATA_ELEMENTS, DataElementDto.class);
    }

    @Override
    public DataElementDto getDataElementByHref(String href) {
        return getResource(href, DataElementDto.class);
    }

    @Override
    public List<OrganisationUnitDto> getOrganisationUnits() {
        return getResources(ORG_UNITS, OrganisationUnitDto.class);
    }

    @Override
    public OrganisationUnitDto getOrganisationUnitByHref(String href) {
        return getResource(href, OrganisationUnitDto.class);
    }

    @Override
    public List<ProgramDto> getPrograms() {
        return getResources(PROGRAMS, ProgramDto.class);
    }

    @Override
    public ProgramDto getProgramByHref(String href) {
        return getResource(href, ProgramDto.class);
    }

    @Override
    public List<ProgramStageDto> getProgramStages() {
        return getResources(PROGRAM_STAGES, ProgramStageDto.class);
    }

    @Override
    public ProgramStageDto getProgramStageByHref(String href) {
        return getResource(href, ProgramStageDto.class);
    }

    @Override
    public List<TrackedEntityDto> getTrackedEntities() {
        return getResources(TRACKED_ENTITIES, TrackedEntityDto.class);
    }

    @Override
    public TrackedEntityDto getTrackedEntityByHref(String href) {
        return getResource(href, TrackedEntityDto.class);
    }

    @Override
    public List<TrackedEntityAttributeDto> getTrackedEntityAttributes() {
        return getResources(TRACKED_ENITTY_ATTRIBUTES, TrackedEntityAttributeDto.class);
    }

    @Override
    public TrackedEntityAttributeDto getTrackedEntityAttributeByHref(String href) {
        return getResource(href, TrackedEntityAttributeDto.class);
    }

    @Override
    public DhisStatusResponse createEnrollment(EnrollmentDto enrollment) {
        String json = parseToJson(enrollment);
        Settings settings = settingsService.getSettings();

        return createEntity(settings, settings.getServerURI() + API_ENDPOINT + ENROLLMENTS_PATH, json);
    }

    @Override
    public DhisStatusResponse createEvent(DhisEventDto event) {
        String json = parseToJson(event);
        Settings settings = settingsService.getSettings();

        return createEntity(settings, settings.getServerURI() + API_ENDPOINT + EVENTS_PATH, json);
    }

    @Override
    public DhisStatusResponse createTrackedEntityInstance(TrackedEntityInstanceDto trackedEntity) {
        String json = parseToJson(trackedEntity);
        Settings settings = settingsService.getSettings();

        return createEntity(settings, settings.getServerURI() + API_ENDPOINT + TRACKED_ENTITY_INSTANCES_PATH, json);
    }

    /*Gets the resource in the form of a dto*/
    private <T extends BaseDto> T getResource(String uri, Class<T>  clazz) {
        Settings settings = settingsService.getSettings();
        HttpUriRequest request = generateHttpRequest(settings, uri);

        LOGGER.debug(String.format("Initiating request for resource at uri %s, request: %s", uri, request.toString()));

        HttpResponse response = getResponseForRequest(request);

        LOGGER.debug(String.format("Received response for request: %s, response: %s", request.toString(), response.toString()));

        T resource;

        try (InputStream content = getContentForResponse(response)){
            resource = new ObjectMapper().readValue(content, clazz);
        } catch (Exception e) {
            String msg = String.format("Error parsing resource at uri: %s, exception: %s", uri, e.toString());
            statusMessageService.warn(msg, MODULE_NAME);
            throw new DhisWebException(msg, e);
        }

        return resource;
    }

    /*Gets a list of dtos*/
    private <T extends BaseDto> List<T> getResources(String resourceName, Class<T> clazz) {
        Settings settings = settingsService.getSettings();
        HttpUriRequest request = generateHttpRequest(settings, getURIForResource(settings.getServerURI(), resourceName));

        LOGGER.debug(String.format("Initiating request for resource: %s, request: %s", resourceName, request.toString()));

        HttpResponse response = getResponseForRequest(request);

        LOGGER.debug(String.format("Received response for request: %s, response: %s", request.toString(), response.toString()));

        List<T> resources = new ArrayList<>();

        try (InputStream content = getContentForResponse(response)) {
            ObjectMapper mapper = new ObjectMapper();

            JavaType type = mapper.getTypeFactory().constructParametricType(PagedResourceDto.class, clazz);
            PagedResourceDto pagedResource = mapper.reader(type).readValue(content);

            resources.addAll(pagedResource.getResources());

            while (pagedResource.getPager().getNextPage() != null) {
                request = generateHttpRequest(settings, pagedResource.getPager().getNextPage());
                response = getResponseForRequest(request);

                try (InputStream nextPageContent = getContentForResponse(response)) {
                    pagedResource = mapper.reader(type).readValue(nextPageContent);
                    resources.addAll(pagedResource.getResources());
                }
            }
        } catch (Exception e) {
            String msg = String.format("Error parsing %s resources, exception: %s", resourceName, e.toString());
            statusMessageService.warn(msg, MODULE_NAME);
            throw new DhisWebException(msg, e);
        }

        return resources;
    }

    /*Attempts to create the an entity in the DHIS2 system. Returns the response from DHIS2*/
    private DhisStatusResponse createEntity(Settings settings, String uri, String json) {
        HttpUriRequest request = generatePostRequest(settings, uri, json);

        LOGGER.debug(String.format("Initiating request to create resource: %s, request: %s", json, request.toString()));

        HttpResponse response = getResponseForRequest(request);

        LOGGER.debug(String.format("Received response to create resource: %s, request: %s", json, response));

        DhisStatusResponse status;

        try (InputStream content = getContentForResponse(response)){
            status = new ObjectMapper().readValue(content, DhisStatusResponse.class);
        } catch (Exception e) {
            String msg = String.format("Error parsing response from uri: %s, exception: %s", uri, e.toString());
            statusMessageService.warn(msg, MODULE_NAME);
            throw new DhisWebException(msg, e);
        }

        return status;
    }

    /*Converts the object to json*/
    private String parseToJson(Object object) {
        try {
            return new ObjectMapper().writeValueAsString(object);
        } catch (Exception e) {
            String msg = String.format("Error parsing object: %s to json, exception: %s", object.toString(), e.toString());
            statusMessageService.warn(msg, MODULE_NAME);
            throw new DhisWebException(msg, e);
        }
    }

    /*Generates an HTTP get request*/
    private HttpUriRequest generateHttpRequest(Settings settings, String url) {
        HttpGet request = new HttpGet(url);
        request.addHeader("accept", MediaType.APPLICATION_JSON_VALUE);
        request.addHeader(generateBasicAuthHeader(request, settings));

        return request;
    }

    /*Generates an HTTP post request*/
    private HttpUriRequest generatePostRequest(Settings settings, String url, String body) {
        HttpPost request = new HttpPost(url);
        request.addHeader("Content-Type", "application/json");
        request.addHeader("accept", "application/json");
        request.addHeader(generateBasicAuthHeader(request, settings));

        StringEntity entity;

        try {
            entity = new StringEntity(body, "UTF-8");
        } catch (Exception e) {
            String msg = String.format("Error creating entity from body: %s, exception: %s", body, e.toString());
            statusMessageService.warn(msg, MODULE_NAME);
            throw new DhisWebException(msg, e);
        }

        request.setEntity(entity);

        return request;
    }

    /*Attempts an HTTP request. Returns the response*/
    private HttpResponse getResponseForRequest(HttpUriRequest request) {
        HttpResponse response;

        try {
            response = client.execute(request);
        } catch (Exception e) {
            String msg = String.format("Error receiving response for request: %s", request.toString());
            statusMessageService.warn(msg, MODULE_NAME);
            throw new DhisWebException(msg, e);
        }

        StatusLine statusLine = response.getStatusLine();

        if (!ACCEPTABLE_DHIS_RESPONSE_STATUSES.contains(statusLine.getStatusCode())) {
            String msg = String.format("Error making DHIS request: %s", statusLine.toString());
            statusMessageService.warn(msg, MODULE_NAME);
            throw new DhisWebException(msg);
        }

        return response;
    }

    /*Gets an input stream from the HTTP response*/
    private InputStream getContentForResponse(HttpResponse response) {
        try {
            return response.getEntity().getContent();
        } catch (Exception e) {
            String msg = String.format("Error accessing content for response: %s", response.toString());
            statusMessageService.warn(msg, MODULE_NAME);
            throw new DhisWebException(msg, e);
        }
    }

    /*Builds the URL for a particular resource*/
    private String getURIForResource(String baseURI, String resourceName) {
        return String.format(baseURI + "/api/%s", resourceName);
    }

    private Header generateBasicAuthHeader(HttpUriRequest request, Settings settings) {
        Header basicAuthHeader;
        BasicScheme basicScheme = new BasicScheme();
        try {
            basicAuthHeader = basicScheme.authenticate(
                    new UsernamePasswordCredentials(settings.getUsername(), settings.getPassword()),
                    request,
                    HttpClientContext.create());
        } catch (Exception e) {
            String msg = String.format("Error generating basic auth header for request: %s", request.toString());
            statusMessageService.warn(msg, MODULE_NAME);
            throw new DhisWebException(msg, e);
        }
        return basicAuthHeader;
    }
}
