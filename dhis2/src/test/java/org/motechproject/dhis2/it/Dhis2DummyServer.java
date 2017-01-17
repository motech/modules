package org.motechproject.dhis2.it;

import org.apache.commons.io.IOUtils;
import org.mortbay.jetty.Server;
import org.mortbay.jetty.handler.AbstractHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.nio.charset.Charset;

public class Dhis2DummyServer {

    private static final Logger LOGGER = LoggerFactory.getLogger(Dhis2DummyServer.class);

    private static final String PROGRAM_ENDPOINT = "/programs/programId";
    private static final String DATA_ELEMENTS_ENDPOINT = "/dataElements";
    private static final String DATA_SETS_ENDPOINT = "/dataSets";
    private static final String ORGANISATION_UNITS_ENDPOINT = "/organisationUnits";
    private static final String PROGRAMS_ENDPOINT = "/programs";
    private static final String TRACKED_ENTITIES_ENDPOINT = "/trackedEntities";
    private static final String TRACKED_ENTITY_ATTRIBUTES_ENDPOINT = "/trackedEntityAttributes";

    private static final String PROGRAM_RESPONSE = "/json/program-response.json";
    private static final String DATA_ELEMENTS_RESPONSE = "/json/data-elements-response.json";
    private static final String DATA_SETS_RESPONSE = "/json/data-sets-response.json";
    private static final String ORGANISATION_UNITS_RESPONSE = "/json/organisation-units-response.json";
    private static final String PROGRAMS_RESPONSE = "/json/programs-response.json";
    private static final String TRACKED_ENTITIES_RESPONSE = "/json/tracked-entities-response.json";
    private static final String TRACKED_ENTITY_ATTRIBUTES_RESPONSE = "/json/tracked-entity-attributes-response.json";

    private Server server;

    public Dhis2DummyServer() {
        server = new Server(9780);
    }

    public void start() {
        try {
            server.setHandler(new Dhis2RequestsHandler());
            server.start();
        } catch (Exception e) {
            LOGGER.error("Failed to start the Jetty server.", e);
        }
    }

    public void stop() {
        try {
            server.stop();
        } catch (Exception e) {
            LOGGER.error("Failed to stop the Jetty server.", e);
        }
    }

    private static class Dhis2RequestsHandler extends AbstractHandler {
        @Override
        public void handle(String s, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, int i) throws IOException, ServletException {
            String requestURI = httpServletRequest.getRequestURI().toString();

            if (requestURI.contains(PROGRAM_ENDPOINT)) {
                writeResponse(httpServletResponse, PROGRAM_RESPONSE);
            } else if (requestURI.contains(DATA_ELEMENTS_ENDPOINT)) {
                writeResponse(httpServletResponse, DATA_ELEMENTS_RESPONSE);
            } else if (requestURI.contains(DATA_SETS_ENDPOINT)) {
                writeResponse(httpServletResponse, DATA_SETS_RESPONSE);
            } else if (requestURI.contains(ORGANISATION_UNITS_ENDPOINT)) {
                writeResponse(httpServletResponse, ORGANISATION_UNITS_RESPONSE);
            } else if (requestURI.contains(PROGRAMS_ENDPOINT)) {
                writeResponse(httpServletResponse, PROGRAMS_RESPONSE);
            } else if (requestURI.contains(TRACKED_ENTITIES_ENDPOINT)) {
                writeResponse(httpServletResponse, TRACKED_ENTITIES_RESPONSE);
            } else if (requestURI.contains(TRACKED_ENTITY_ATTRIBUTES_ENDPOINT)) {
                writeResponse(httpServletResponse, TRACKED_ENTITY_ATTRIBUTES_RESPONSE);
            }
        }

        private void writeResponse(HttpServletResponse response, String filePath) throws IOException {
            response.setContentType("application/json; charset=utf-8");
            response.setStatus(HttpServletResponse.SC_OK);

            try (InputStream inputStream = getClass().getResourceAsStream(filePath);
                 PrintWriter responseWriter = response.getWriter()) {
                responseWriter.println(IOUtils.toString(inputStream, Charset.forName("UTF-8")));
            }
        }
    }
}
