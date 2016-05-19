package org.motechproject.openlmis.rest.service.impl;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.http.Header;
import org.apache.http.HttpHeaders;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.auth.AuthenticationException;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.auth.BasicScheme;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.osgi.services.HttpClientBuilderFactory;
import org.motechproject.admin.service.StatusMessageService;
import org.motechproject.openlmis.rest.domain.DosageUnitDto;
import org.motechproject.openlmis.rest.domain.FacilityDto;
import org.motechproject.openlmis.rest.domain.FacilityTypeDto;
import org.motechproject.openlmis.rest.domain.GeographicLevelDto;
import org.motechproject.openlmis.rest.domain.GeographicZoneDto;
import org.motechproject.openlmis.rest.domain.ProductCategoryDto;
import org.motechproject.openlmis.rest.domain.ProductDto;
import org.motechproject.openlmis.rest.domain.ProgramDto;
import org.motechproject.openlmis.rest.domain.ResourceListDto;
import org.motechproject.openlmis.rest.domain.StockStatusDto;
import org.motechproject.openlmis.rest.service.OpenlmisWebException;
import org.motechproject.openlmis.rest.service.OpenlmisWebService;
import org.motechproject.openlmis.service.Settings;
import org.motechproject.openlmis.service.SettingsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Implementation of {@link org.motechproject.openlmis.rest.service.OpenlmisWebService}
 */
@Service("openlmisWebService")
public class OpenlmisWebServiceImpl implements OpenlmisWebService {
    private static final Logger LOGGER = LoggerFactory.getLogger(OpenlmisWebServiceImpl.class);

    private static final String MODULE_NAME = "open-lmis";
    private static final String LOOKUP_API_ENDPOINT = "/rest-api/lookup/";
    private static final String STOCK_STATUS_ENDPOINT = "/rest-api/stock-status/";
    
    private static final String PROGRAMS = "programs";
    
    private static final String PRODUCT_CATEGORIES = "product-categories";
    private static final String DOSAGE_UNITS = "dosage-units";
    private static final String PRODUCTS = "products?paging=false";
    
    private static final String GEOGRAPHIC_LEVELS = "geographic-levels";
    private static final String GEOGRAPHIC_ZONES = "geographic-zones";
    private static final String FACILITY_TYPES = "facility-types";
    private static final String FACILITIES = "facilities?paging=false";
    
    private static final String STOCK_STATUS_MONTHLY = "monthly?month=%s&year=%s&program=%s";
    
    private SettingsService settingsService;
    private StatusMessageService statusMessageService;
    private HttpClient client;

    public static final List<Integer> ACCEPTABLE_OPEN_LMIS_RESPONSE_STATUSES = Arrays.asList(HttpStatus.SC_OK,
            HttpStatus.SC_ACCEPTED, HttpStatus.SC_CREATED);

    @Override
    public List<ProgramDto> getPrograms() {
        return getResources(LOOKUP_API_ENDPOINT, PROGRAMS, ProgramDto.class);
    }
    
    @Override
    public List<ProductCategoryDto> getProductCategories() {
        return getResources(LOOKUP_API_ENDPOINT, PRODUCT_CATEGORIES, ProductCategoryDto.class);
    }
    
    @Override
    public List<DosageUnitDto> getDosageUnits() {
        return getResources(LOOKUP_API_ENDPOINT, DOSAGE_UNITS, DosageUnitDto.class);
    }
    
    @Override
    public List<ProductDto> getProducts() {
        return getResources(LOOKUP_API_ENDPOINT, PRODUCTS, ProductDto.class);
    }
    
    @Override
    public List<GeographicLevelDto> getGeographicLevels() {
        return getResources(LOOKUP_API_ENDPOINT, GEOGRAPHIC_LEVELS, GeographicLevelDto.class);
    }
    
    @Override
    public List<GeographicZoneDto> getGeographicZones() {
        return getResources(LOOKUP_API_ENDPOINT, GEOGRAPHIC_ZONES, GeographicZoneDto.class);
    }
    
    @Override
    public List<FacilityTypeDto> getFacilityTypes() {
        return getResources(LOOKUP_API_ENDPOINT, FACILITY_TYPES, FacilityTypeDto.class);
    }
    
    @Override
    public List<FacilityDto> getFacilities() {
        return getResources(LOOKUP_API_ENDPOINT, FACILITIES, FacilityDto.class);
    }
    
    @Override
    public List<StockStatusDto> getStockStatus(int month, int year, String program) {
        return getResources(STOCK_STATUS_ENDPOINT, STOCK_STATUS_MONTHLY, StockStatusDto.class, month, year, program);
    }

    @Autowired
    public OpenlmisWebServiceImpl(@Qualifier("openlmisSettingsService") SettingsService settingsService,
                              StatusMessageService statusMessageService,
                              HttpClientBuilderFactory httpClientBuilderFactory) {
        this.settingsService = settingsService;
        this.statusMessageService = statusMessageService;
        BasicCookieStore cookieStore = new BasicCookieStore();
        this.client = httpClientBuilderFactory.newBuilder().setDefaultCookieStore(cookieStore).build();
    }

    /*Gets a list of dtos*/
    private <T> List<T> getResources(String controllerName, String resourceName, Class<T> clazz, Object... queryparams) {
        Settings settings = settingsService.getSettings();
        HttpUriRequest request = generateHttpRequest(settings, getURIForResource(settings.getServerURI(), 
                controllerName, resourceName, queryparams));

        LOGGER.debug(String.format("Initiating request for resource: %s, request: %s", resourceName, request.getRequestLine()));

        HttpResponse response = getResponseForRequest(request);

        LOGGER.debug(String.format("Received response for request: %s, response: %s", request.getURI(), response.getStatusLine()));

        List<T> resources = new ArrayList<T>();

        try (InputStream content = getContentForResponse(response)) {
            ObjectMapper mapper = new ObjectMapper();
            JavaType type = mapper.getTypeFactory().constructParametricType(ResourceListDto.class, clazz);
            ResourceListDto resourceList = mapper.reader(type).readValue(content);
            resources.addAll(resourceList.getResources());

        } catch (Exception e) {
            String msg = String.format("Error parsing %s resources, exception: %s", resourceName, e.toString());
            statusMessageService.warn(msg, MODULE_NAME);
            throw new OpenlmisWebException(msg, e);
        }

        return resources;
    }
    
    /*Converts the object to json*/
    @SuppressWarnings("unused")
    private String writeToJson(Object object) {
        try {
            return new ObjectMapper().writeValueAsString(object);
        } catch (Exception e) {
            String msg = String.format("Error parsing object: %s to json, exception: %s", object.toString(), e.toString());
            statusMessageService.warn(msg, MODULE_NAME);
            throw new OpenlmisWebException(msg, e);
        }
    }

    /*Generates an HTTP get request*/
    private HttpUriRequest generateHttpRequest(Settings settings, String url) {
        HttpGet request = new HttpGet(url);
        request.addHeader(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE);
        request.addHeader(generateBasicAuthHeader(request, settings));
        return request;
    }

    /*Generates an HTTP post request*/
    @SuppressWarnings("unused")
    private HttpUriRequest generatePostRequest(Settings settings, String url, String body) {
        HttpPost request = new HttpPost(url);
        request.addHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
        request.addHeader(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE);
        request.addHeader(generateBasicAuthHeader(request, settings));
        StringEntity entity = new StringEntity(body, "UTF-8");

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
            throw new OpenlmisWebException(msg, e);
        }

        StatusLine statusLine = response.getStatusLine();

        if (!ACCEPTABLE_OPEN_LMIS_RESPONSE_STATUSES.contains(statusLine.getStatusCode())) {
            String msg = String.format("Error making OpenLMIS request: %s", statusLine.toString());
            statusMessageService.warn(msg, MODULE_NAME);
            throw new OpenlmisWebException(msg);
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
            throw new OpenlmisWebException(msg, e);
        }
    }

    /*Builds the URL for a particular resource*/
    private String getURIForResource(String baseURI, String controllerName, String resourceName, Object... queryparams) {
        return String.format(baseURI + controllerName + resourceName, queryparams);
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
            throw new OpenlmisWebException(msg, e);
        }
        return basicAuthHeader;
    }

}
