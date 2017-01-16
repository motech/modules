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

    private static final String DATA_ELEMENTS_ENDPOINT = "/dataElements";
    private static final String DATA_SETS_ENDPOINT = "/dataSets";
    private static final String ORGANISATION_UNITS_ENDPOINT = "/organisationUnits";
    private static final String PROGRAMS_ENDPOINT = "/programs";
    private static final String PROGRAM_STAGES_ENDPOINT = "/programStages";
    private static final String PROGRAM_STAGE_DATA_ELEMENTS_ENDPOINT = "/programStageDataElements";
    private static final String TRACKED_ENTITIES_ENDPOINT = "/trackedEntities";
    private static final String TRACKED_ENTITY_ATTRIBUTES_ENDPOINT = "/trackedEntityAttributes";
    private static final String PROGRAM_TRACKED_ENTITY_ATTRIBUTES_ENDPOINT = "/programTrackedEntityAttributes";


    private Server server;

    public Dhis2DummyServer() {
        server = new Server(9780);
    }

    public void start() {
        try {
            server.setHandler(new Dhis2RequestsHandler());
            server.start();
        } catch (Exception e) {
            LOGGER.error("Failed to start the Jetty server");
        }
    }

    public void stop() {
        try {
            server.stop();
        } catch (Exception e) {
            LOGGER.error("Failed to stop the Jetty server");
        }
    }

    private static class Dhis2RequestsHandler extends AbstractHandler {
        @Override
        public void handle(String s, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, int i) throws IOException, ServletException {
            if (httpServletRequest.getRequestURI().toString().contains(DATA_ELEMENTS_ENDPOINT)) {
                writeResponse(httpServletResponse, "/dhis2/data-elements-response.json");
            } else if (httpServletRequest.getRequestURI().toString().contains(DATA_SETS_ENDPOINT)) {
                writeResponse(httpServletResponse, "/dhis2/data-sets-response.json");
            } else if (httpServletRequest.getRequestURI().toString().contains(ORGANISATION_UNITS_ENDPOINT)) {
                writeResponse(httpServletResponse, "/dhis2/organisation-units-response.json");
            } else if (httpServletRequest.getRequestURI().toString().contains(PROGRAMS_ENDPOINT)) {
                writeResponse(httpServletResponse, "/dhis2/programs-response.json");
            } else if (httpServletRequest.getRequestURI().toString().contains(PROGRAM_STAGES_ENDPOINT)) {
                writeResponse(httpServletResponse, "/dhis2/program-stages-response.json");
            } else if (httpServletRequest.getRequestURI().toString().contains(PROGRAM_STAGE_DATA_ELEMENTS_ENDPOINT)) {
                writeResponse(httpServletResponse, "/dhis2/program-stage-data-elements-response.json");
            } else if (httpServletRequest.getRequestURI().toString().contains(TRACKED_ENTITIES_ENDPOINT)) {
                writeResponse(httpServletResponse, "/dhis2/tracked-entities-response.json");
            } else if (httpServletRequest.getRequestURI().toString().contains(TRACKED_ENTITY_ATTRIBUTES_ENDPOINT)) {
                writeResponse(httpServletResponse, "/dhis2/tracked-entity-attributes-response.json");
            } else if (httpServletRequest.getRequestURI().toString().contains(PROGRAM_TRACKED_ENTITY_ATTRIBUTES_ENDPOINT)) {
                writeResponse(httpServletResponse, "/dhis2/program-tracked-entity-attributes-response.json");
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
