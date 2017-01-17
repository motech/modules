package org.motechproject.dhis2.rest.service.impl;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.http.Header;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.auth.AuthenticationException;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.config.SocketConfig;
import org.apache.http.conn.HttpClientConnectionManager;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.auth.BasicScheme;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.osgi.services.HttpClientBuilderFactory;
import org.apache.http.util.EntityUtils;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.JavaType;
import org.motechproject.admin.service.StatusMessageService;
import org.motechproject.dhis2.event.EventSubjects;
import org.motechproject.dhis2.rest.domain.BaseDto;
import org.motechproject.dhis2.rest.domain.DataElementDto;
import org.motechproject.dhis2.rest.domain.DataSetDto;
import org.motechproject.dhis2.rest.domain.DataValueSetDto;
import org.motechproject.dhis2.rest.domain.DhisDataValueStatusResponse;
import org.motechproject.dhis2.rest.domain.DhisEventDto;
import org.motechproject.dhis2.rest.domain.DhisResponse;
import org.motechproject.dhis2.rest.domain.DhisServerInfo;
import org.motechproject.dhis2.rest.domain.DhisStatus;
import org.motechproject.dhis2.rest.domain.DhisStatusResponse;
import org.motechproject.dhis2.rest.domain.EnrollmentDto;
import org.motechproject.dhis2.rest.domain.OrganisationUnitDto;
import org.motechproject.dhis2.rest.domain.PagedResourceDto;
import org.motechproject.dhis2.rest.domain.ProgramDto;
import org.motechproject.dhis2.rest.domain.ProgramStageDataElementDto;
import org.motechproject.dhis2.rest.domain.ProgramStageDto;
import org.motechproject.dhis2.rest.domain.ProgramTrackedEntityAttributeDto;
import org.motechproject.dhis2.rest.domain.ServerVersion;
import org.motechproject.dhis2.rest.domain.TrackedEntityAttributeDto;
import org.motechproject.dhis2.rest.domain.TrackedEntityDto;
import org.motechproject.dhis2.rest.domain.TrackedEntityInstanceDto;
import org.motechproject.dhis2.rest.service.DhisWebException;
import org.motechproject.dhis2.rest.service.DhisWebService;
import org.motechproject.dhis2.service.Settings;
import org.motechproject.dhis2.service.SettingsService;
import org.motechproject.event.MotechEvent;
import org.motechproject.event.listener.annotations.MotechListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

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
    private static final String DATA_VALUE_SETS_PATH = "/dataValueSets";
    private static final String SYSTEM_INFO_PATH = "/system/info.json";
    private static final String GET_FULL_INFO_SUFIX_V_22 = "fields=:identifiable";

    private static final String DATA_ELEMENTS = "dataElements";
    private static final String ORG_UNITS = "organisationUnits";
    private static final String PROGRAMS = "programs";
    private static final String PROGRAM_STAGES = "programStages";
    private static final String PROGRAM_STAGE_DATA_ELEMENTS = "programStageDataElements";
    private static final String TRACKED_ENTITIES = "trackedEntities";
    private static final String TRACKED_ENITTY_ATTRIBUTES = "trackedEntityAttributes";
    private static final String PROGRAM_TRACKED_ENITTY_ATTRIBUTES = "programTrackedEntityAttributes";

    private static final int SO_TIMEOUT = 10000;
    private static final int MAX_CONNECTIONS = 200;
    private static final int MAX_PER_ROUTE = 20;
    private static final int IDLE_TIMEOUT = 1000;
    private static final List<Integer> ACCEPTABLE_DHIS_RESPONSE_STATUSES = Arrays.asList(HttpStatus.SC_OK,
            HttpStatus.SC_ACCEPTED, HttpStatus.SC_CREATED);

    private SettingsService settingsService;
    private StatusMessageService statusMessageService;
    private CloseableHttpClient client;
    private PoolingHttpClientConnectionManager poolingHttpClientConnectionManager;
    private ServerVersion serverVersion = new ServerVersion(ServerVersion.UNKNOWN);

    @Autowired
    public DhisWebServiceImpl(@Qualifier("dhisSettingsService") SettingsService settingsService,
                              StatusMessageService statusMessageService,
                              HttpClientBuilderFactory httpClientBuilderFactory) throws InterruptedException {
        this.settingsService = settingsService;
        this.statusMessageService = statusMessageService;

        SocketConfig socketConfig = SocketConfig.custom()
                .setSoTimeout(SO_TIMEOUT)
                .build();

        poolingHttpClientConnectionManager = new PoolingHttpClientConnectionManager();
        poolingHttpClientConnectionManager.setMaxTotal(MAX_CONNECTIONS);
        poolingHttpClientConnectionManager.setDefaultMaxPerRoute(MAX_PER_ROUTE);
        poolingHttpClientConnectionManager.setDefaultSocketConfig(socketConfig);

        this.client = httpClientBuilderFactory.newBuilder()
                .setConnectionManager(poolingHttpClientConnectionManager)
                .build();

        ExecutorService executorService = Executors.newFixedThreadPool(1);
        executorService.execute(new IdleConnectionMonitorRunnable(poolingHttpClientConnectionManager));
    }

    @PostConstruct
    public void discoverServerVersion() {
        Settings settings = settingsService.getSettings();

        if (StringUtils.isNotBlank(settings.getServerURI()) && StringUtils.isNotBlank(settings.getUsername()) && StringUtils.isNotBlank(settings.getPassword())) {
            try {
                DhisServerInfo info = getDhisServerInfo();
                serverVersion = new ServerVersion(info.getVersion());
            } catch (DhisWebException e) {
                // No problem, the connection to the server has not been set or is incorrect
                LOGGER.info("The connection to the DHIS2 server could not be established. Skipping discovery of the server version.");
            }
        }
    }

    @MotechListener(subjects = EventSubjects.DHIS_SETTINGS_UPDATED)
    public void handleSettingsUpdate(MotechEvent event) {
        discoverServerVersion();
    }

    @Override
    public List<DataElementDto> getDataElements() {
        return getResources(DATA_ELEMENTS, DataElementDto.class);
    }

    @Override
    public List<DataSetDto> getDataSets() {
        return getResources("dataSets", DataSetDto.class, true);
    }

    @Override
    public DataElementDto getDataElementByHref(String href) {
        return getResource(href, DataElementDto.class);
    }

    @Override
    public DataElementDto getDataElementById(String id) {
        return getResource(getURIForResource(settingsService.getSettings().getServerURI(), DATA_ELEMENTS, id), DataElementDto.class);
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
    public OrganisationUnitDto getOrganisationUnitById(String id) {
        return getResource(getURIForResource(settingsService.getSettings().getServerURI(), ORG_UNITS, id), OrganisationUnitDto.class);
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
    public ProgramDto getProgramById(String id) {
        return getResource(getURIForResource(settingsService.getSettings().getServerURI(), PROGRAMS, id), ProgramDto.class);
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
    public ProgramStageDto getProgramStageById(String id) {
        return getResource(getURIForResource(settingsService.getSettings().getServerURI(), PROGRAM_STAGES, id), ProgramStageDto.class);
    }

    @Override
    public ProgramStageDataElementDto getProgramStageDataElementByHref(String href) {
        return getResource(href, ProgramStageDataElementDto.class);
    }

    @Override
    public ProgramStageDataElementDto getProgramStageDataElementById(String id) {
        return getResource(getURIForResource(settingsService.getSettings().getServerURI(), PROGRAM_STAGE_DATA_ELEMENTS, id), ProgramStageDataElementDto.class);
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
    public TrackedEntityDto getTrackedEntityById(String id) {
        return getResource(getURIForResource(settingsService.getSettings().getServerURI(), TRACKED_ENTITIES, id), TrackedEntityDto.class);
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
    public TrackedEntityAttributeDto getTrackedEntityAttributeById(String id) {
        return getResource(getURIForResource(settingsService.getSettings().getServerURI(), TRACKED_ENITTY_ATTRIBUTES, id), TrackedEntityAttributeDto.class);
    }

    @Override
    public ProgramTrackedEntityAttributeDto getProgramTrackedEntityAttributeByHref(String href) {
        return getResource(href, ProgramTrackedEntityAttributeDto.class);
    }

    @Override
    public ProgramTrackedEntityAttributeDto getProgramTrackedEntityAttributeById(String id) {
        return getResource(getURIForResource(settingsService.getSettings().getServerURI(), PROGRAM_TRACKED_ENITTY_ATTRIBUTES, id), ProgramTrackedEntityAttributeDto.class);
    }

    @Override
    public DhisStatusResponse createEnrollment(EnrollmentDto enrollment) {
        String json;
        if (serverVersion.isSameOrBefore(ServerVersion.V2_18)) {
            json = parseToJson(enrollment.convertTo218());
        } else if (serverVersion.isSameOrAfter(ServerVersion.V2_19) && serverVersion.isBefore(ServerVersion.V2_21)) {
            json = parseToJson(enrollment.convertTo219());
        } else {
            json = parseToJson(enrollment.convertTo221());
        }
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

    @Override
    public DhisDataValueStatusResponse sendDataValueSet(DataValueSetDto dataValueSetDto) {
        String json = parseToJson(dataValueSetDto);
        Settings settings = settingsService.getSettings();

        return importDataValues(settings, settings.getServerURI() + API_ENDPOINT + DATA_VALUE_SETS_PATH, json);
    }

    @Override
    public DhisServerInfo getDhisServerInfo() {
        Settings settings = settingsService.getSettings();

        String url = settings.getServerURI() + API_ENDPOINT + SYSTEM_INFO_PATH;
        HttpUriRequest request = generateHttpRequest(settings, url);
        CloseableHttpResponse response = getResponseForRequest(request);

        try (InputStream content = getContentForResponse(response)) {
            return new ObjectMapper().readValue(content, DhisServerInfo.class);
        } catch (IOException e) {
            String msg = String.format("Error parsing resource at uri: %s, exception: %s", url, e.toString());
            statusMessageService.warn(msg, MODULE_NAME);
            throw new DhisWebException(msg, e);
        } finally {
            closeResponse(response);
        }
    }

    @Override
    public ServerVersion getServerVersion() {
        return serverVersion;
    }

    /*Gets the resource in the form of a dto*/
    private <T extends BaseDto> T getResource(String uri, Class<T> clazz) {
        Settings settings = settingsService.getSettings();
        HttpUriRequest request = generateHttpRequest(settings, uri);

        LOGGER.debug(String.format("Initiating request for resource at uri %s, request: %s", uri, request.toString()));

        CloseableHttpResponse response = getResponseForRequest(request);

        LOGGER.debug(String.format("Received response for request: %s, response: %s", request.toString(), response.toString()));

        try (InputStream content = getContentForResponse(response)) {
            return new ObjectMapper().readValue(content, clazz);
        } catch (IOException e) {
            String msg = String.format("Error parsing resource at uri: %s, exception: %s", uri, e.toString());
            statusMessageService.warn(msg, MODULE_NAME);
            throw new DhisWebException(msg, e);
        } finally {
            closeResponse(response);
        }
    }

    private <T extends BaseDto> List<T> getResources(String resourceName, Class<T> clazz) {
        return getResources(resourceName, clazz, false);
    }

    /*Gets a list of dtos*/
    private <T extends BaseDto> List<T> getResources(String resourceName, Class<T> clazz, boolean getAllFields) {
        Settings settings = settingsService.getSettings();
        HttpUriRequest request = generateHttpRequest(settings, getURIForResource(settings.getServerURI(), resourceName,
                getAllFields));

        LOGGER.debug(String.format("Initiating request for resource: %s, request: %s", resourceName, request.toString()));

        CloseableHttpResponse response = getResponseForRequest(request);

        LOGGER.debug(String.format("Received response for request: %s, response: %s", request.toString(), response.toString()));

        List<T> resources = new ArrayList<>();

        try (InputStream content = getContentForResponse(response)) {
            ObjectMapper mapper = new ObjectMapper();

            JavaType type = mapper.getTypeFactory().constructParametricType(PagedResourceDto.class, clazz);
            PagedResourceDto pagedResource = mapper.reader(type).readValue(content);

            resources.addAll(pagedResource.getResources());

            while (pagedResource.getPager().getNextPage() != null) {
                String url = pagedResource.getPager().getNextPage();
                if (getServerVersion().isSameOrAfter(ServerVersion.V2_22)) {
                    url += String.format("&%s", GET_FULL_INFO_SUFIX_V_22);
                }
                request = generateHttpRequest(settings, url);
                response = getResponseForRequest(request);

                try (InputStream nextPageContent = getContentForResponse(response)) {
                    pagedResource = mapper.reader(type).readValue(nextPageContent);
                    resources.addAll(pagedResource.getResources());
                }
            }
        } catch (IOException e) {
            String msg = String.format("Error parsing %s resources, exception: %s", resourceName, e.toString());
            statusMessageService.warn(msg, MODULE_NAME);
            throw new DhisWebException(msg, e);
        } finally {
            closeResponse(response);
        }

        return resources;
    }

    /*Attempts to create the an entity in the DHIS2 system. Returns the response from DHIS2*/
    private DhisStatusResponse createEntity(Settings settings, String uri, String json) {
        HttpUriRequest request = generatePostRequest(settings, uri, json);
        LOGGER.debug(String.format("Initiating request to create resource: %s, request: %s", json, request.toString()));
        CloseableHttpResponse response = getResponseForRequest(request);
        return (DhisStatusResponse) getDhisResponseForHttpResponse(json, uri, response, DhisStatusResponse.class);
    }

    /*Converts the object to json*/
    private String parseToJson(Object object) {
        try {
            return new ObjectMapper().writeValueAsString(object);
        } catch (IOException e) {
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

        StringEntity entity = new StringEntity(body, "UTF-8");

        request.setEntity(entity);

        return request;
    }

    /*Attempts an HTTP request. Returns the response*/
    private CloseableHttpResponse getResponseForRequest(HttpUriRequest request) {
        CloseableHttpResponse response = null;

        try {
            response = client.execute(request);
        } catch (IOException e) {
            String msg = String.format("Error receiving response for request: %s", request.toString());
            statusMessageService.warn(msg, MODULE_NAME);
            closeResponse(response);
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
    private InputStream getContentForResponse(CloseableHttpResponse response) {
        try {
            return response.getEntity().getContent();
        } catch (IOException e) {
            String msg = String.format("Error accessing content for response: %s", response.toString());
            statusMessageService.warn(msg, MODULE_NAME);
            closeResponse(response);
            throw new DhisWebException(msg, e);
        }
    }

    /*Builds the URL for a particular resource*/
    private String getURIForResource(String baseURI, String resourceName, boolean getAllFields) {
        StringBuilder sb = new StringBuilder(baseURI);

        sb.append("/api/");
        sb.append(resourceName);

        if (getAllFields) {
            sb.append("?fields=:all");
        } else if (serverVersion.isSameOrAfter(ServerVersion.V2_22)) {
            sb.append(String.format("?%s", GET_FULL_INFO_SUFIX_V_22));
        }

        return sb.toString();
    }

    private String getURIForResource(String baseURI, String resourceName, String id) {
        StringBuilder sb = new StringBuilder(baseURI);

        sb.append("/api/");
        sb.append(resourceName);
        sb.append("/");
        sb.append(id);

        return sb.toString();
    }

    private Header generateBasicAuthHeader(HttpUriRequest request, Settings settings) {
        Header basicAuthHeader;
        BasicScheme basicScheme = new BasicScheme();
        try {
            basicAuthHeader = basicScheme.authenticate(
                    new UsernamePasswordCredentials(settings.getUsername(), settings.getPassword()),
                    request,
                    HttpClientContext.create());
        } catch (AuthenticationException e) {
            String msg = String.format("Error generating basic auth header for request: %s", request.toString());
            statusMessageService.warn(msg, MODULE_NAME);
            throw new DhisWebException(msg, e);
        }
        return basicAuthHeader;
    }

    private DhisDataValueStatusResponse importDataValues(Settings settings, String uri, String json) {
        HttpUriRequest request = generatePostRequest(settings, uri, json);
        LOGGER.debug(String.format("Initiating request to create resource: %s, request: %s", json, request.toString()));
        CloseableHttpResponse response = getResponseForRequest(request);
        return (DhisDataValueStatusResponse) getDhisResponseForHttpResponse(json, uri, response, DhisDataValueStatusResponse.class);
    }

    private DhisResponse getDhisResponseForHttpResponse(String json, String uri, CloseableHttpResponse response, Class responseType) {
        DhisResponse status;
        try (InputStream content = getContentForResponse(response)) {
            String contentString = IOUtils.toString(content);
            LOGGER.debug(String.format("Received response to create resource: %s, request: %s", contentString, json));
            if(responseType == DhisStatusResponse.class) {
                status = new ObjectMapper().readValue(contentString, DhisStatusResponse.class);
            } else {
                status = new ObjectMapper().readValue(contentString, DhisDataValueStatusResponse.class);
            }
            if (status.getStatus() == DhisStatus.ERROR) {
                String msg = String.format("Error in DHIS2 status response, error details: %s", contentString);
                throw new DhisWebException(msg);
            }
        } catch (IOException e) {
            String msg = String.format("Error parsing response from uri: %s, exception: %s", uri, e.toString());
            statusMessageService.warn(msg, MODULE_NAME);
            throw new DhisWebException(msg, e);
        } finally {
            closeResponse(response);
        }
        return status;
    }

    private void closeResponse(CloseableHttpResponse response) {
        try {
            if (response != null) {
                EntityUtils.consume(response.getEntity());
                response.close();
            }
        } catch (IOException e) {
            LOGGER.error("Error trying to close response", e);
        }
    }

    @PreDestroy
    public void cleanUp() throws IOException {
        client.close();
        poolingHttpClientConnectionManager.close();
    }

    private final class IdleConnectionMonitorRunnable implements Runnable {
        private final HttpClientConnectionManager connMgr;
        private volatile boolean shutdown;

        private IdleConnectionMonitorRunnable(PoolingHttpClientConnectionManager connMgr) {
            this.connMgr = connMgr;
        }

        @Override
        public void run() {
            try {
                while (!shutdown) {
                    synchronized (this) {
                        wait(IDLE_TIMEOUT);
                        connMgr.closeExpiredConnections();
                        connMgr.closeIdleConnections(SO_TIMEOUT, TimeUnit.MILLISECONDS);
                    }
                }
            } catch (InterruptedException ex) {
                shutdown();
            }
        }

        private void shutdown() {
            shutdown = true;
            synchronized (this) {
                notifyAll();
            }
        }
    }
}
