package org.motechproject.odk.service;


import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.impl.auth.BasicScheme;
import org.apache.http.osgi.services.HttpClientBuilderFactory;
import org.apache.http.util.EntityUtils;
import org.motechproject.odk.domain.Configuration;
import org.motechproject.odk.domain.FormDefinition;
import org.motechproject.odk.domain.ImportStatus;
import org.motechproject.odk.parser.XformParser;
import org.motechproject.odk.parser.factory.XformParserFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.xml.xpath.XPathExpressionException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Component
public abstract class AbstractFormDefinitionImportService implements FormDefinitionImportService {

    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractFormDefinitionImportService.class);
    protected static final String FORM_LIST_PATH = "/formList";

    private HttpClient client;
    private TasksService tasksService;
    private FormDefinitionService formDefinitionService;

    @Autowired
    public AbstractFormDefinitionImportService(HttpClientBuilderFactory httpClientBuilderFactory, TasksService tasksService, FormDefinitionService formDefinitionService) {
        this.client = httpClientBuilderFactory.newBuilder().build();
        this.tasksService = tasksService;
        this.formDefinitionService = formDefinitionService;
    }

    /**
     * Template method for importing form definitions.
     */
    @Override
    public ImportStatus importForms(Configuration config) {

        try {
            List<String> formUrls = getFormUrls(config);
            List<String> xmlFormDefinitions = getXmlFormDefinitions(formUrls, config);
            List<FormDefinition> formDefinitions = parseXmlFormDefinitions(xmlFormDefinitions, config);
            modifyFormDefinitionForImplementation(formDefinitions);
            updateFormDefinitions(formDefinitions, config.getName());
            tasksService.updateTasksChannel();
            return new ImportStatus(true);

        } catch (Exception e) {
            LOGGER.error(e.toString());
            return new ImportStatus(false);
        }
    }

    /**
     * Hook method that may be overridden by an implementing subclass. Parses a list of XML form definitions and
     * returns a list of {@link FormDefinition}
     * @param xmlFormDefinitions a List of XML form definitions
     * @param configuration The {@link Configuration} associated with the form defintions
     * @return A list of {@link FormDefinition}
     * @throws FormDefinitionImportException If form definitions are malformed.
     */
    protected List<FormDefinition> parseXmlFormDefinitions(List<String> xmlFormDefinitions, Configuration configuration) throws FormDefinitionImportException  {
        try {
            List<FormDefinition> formDefinitions = new ArrayList<>();
            XformParser parser = new XformParserFactory().getParser(configuration.getType());

            for (String def : xmlFormDefinitions) {
                formDefinitions.add(parser.parse(def, configuration.getName()));
            }
            return formDefinitions;
        } catch (XPathExpressionException e) {
            throw new FormDefinitionImportException(e);
        }
    }

    /**
     * Hook method that may be overridden by an implementing subclass. Makes an HTTP request to the external
     * application and returns a list of URLs for the form definitions.
     * @param configuration {@link Configuration}
     * @return A list of strings containing the URLs for each form definition.
     * @throws FormDefinitionImportException If HTTP request fails or returns a malformed XML list of URLs.
     */
    protected List<String> getFormUrls(Configuration configuration) throws FormDefinitionImportException {
        HttpGet request = new HttpGet(configuration.getUrl() + FORM_LIST_PATH);
        try {
            HttpResponse response = client.execute(request);
            String responseBody = EntityUtils.toString(response.getEntity());
            return parseToUrlList(responseBody);

        } catch (IOException|ParseUrlException e) {
            throw new FormDefinitionImportException(e);
        }
    }

    /**
     * Hook method that may be overriden by an implementing subclass. For each URL in the list of
     * URLs, this makes an HTTP request to the external application to fetch an XML form definition. This returns
     * a list of XML form definitions.
     * @param formUrls A list of URLs for each form definition.
     * @param configuration {@link Configuration}
     * @return A list of XML form definitions represented as a list of strings.
     * @throws IOException If any of the HTTP requests fail.
     */
    protected List<String> getXmlFormDefinitions(List<String> formUrls, Configuration configuration) throws IOException {
        List<String> formDefinitions = new ArrayList<>();

        for (String url : formUrls) {
            HttpGet request = new HttpGet(url);
            HttpResponse response = client.execute(request);
            String responseBody = EntityUtils.toString(response.getEntity());
            formDefinitions.add(responseBody);
        }
        return formDefinitions;
    }

    protected Header generateBasicAuthHeader(HttpUriRequest request, Configuration configuration) {
        Header basicAuthHeader;
        BasicScheme basicScheme = new BasicScheme();

        try {
            basicAuthHeader = basicScheme.authenticate(
                    new UsernamePasswordCredentials(configuration.getUsername(), configuration.getPassword()),
                    request,
                    HttpClientContext.create());
        } catch (Exception e) {
            return null;
        }
        return basicAuthHeader;
    }


    private void updateFormDefinitions(List<FormDefinition> formDefinitions, String configName) {
        formDefinitionService.deleteFormDefinitionsByConfigurationName(configName);
        for (FormDefinition formDefinition : formDefinitions) {
            formDefinitionService.create(formDefinition);
        }
    }


    /**
     * Hook method that must be overridden by an implementing subclass. Modifies the form definition based on application
     * specific details
     * @param formDefinitions A list of form definitions associated with a configuration.
     */
    protected abstract void modifyFormDefinitionForImplementation(List<FormDefinition> formDefinitions);

    /**
     * Hook method that must be overridden by an implementing subclass. Parses the response from the application
     * and returns a list of strings representing the URLs for each form definition.
     * @param responseBody
     * @return
     * @throws Exception
     */
    protected abstract List<String> parseToUrlList(String responseBody) throws ParseUrlException;

    protected HttpClient getClient() {
        return client;
    }

}
