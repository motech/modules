package org.motechproject.ivr.utils;

/**
 * Utility Class that implements an extremely simple HTTP server that returns a predictable response at a given URI
 */

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.util.HashSet;
import java.util.Set;

/**
 * Super Simple HTTP Server
 *
 * inspired from from http://stackoverflow.com/questions/3732109/simple-http-server-in-java-using-only-java-se-api
 */
public class SimpleHttpServer {

    private static final int MIN_PORT = 8080;
    private static final int MAX_PORT = 9080;

    private int port = MIN_PORT;
    private Set<HttpServer> servers = new HashSet<>();
    private static Logger logger = LoggerFactory.getLogger(SimpleHttpServer.class);
    private static SimpleHttpServer simpleHttpServer = new SimpleHttpServer();

    private SimpleHttpServer() {  }

    public static SimpleHttpServer getInstance() {
        return simpleHttpServer;
    }

    public String start(String resource, int responseCode, String responseBody) {
        //logger.debug(String.format("********** start(resource='%s', responseCode=%d, responseBody='%s')", resource, responseCode, responseBody));

        HttpServer server = null;
        // Ghetto low tech: loop to find an open port
        do {
            try {
                //logger.debug("********** Trying to create HttpServer at port {}", port);
                server = HttpServer.create(new InetSocketAddress(port), 0);
                servers.add(server);
            }
            catch (IOException e) {
                port++;
            }
        } while (null == server && port < MAX_PORT);

        if (port < MAX_PORT) {
            //logger.debug("********** SimpleHttpServer created at port {}", port);
            try {
                server.createContext(String.format("/%s", resource), new SimpleHttpHandler(responseCode, responseBody));
                server.setExecutor(null);
                server.start();
                String uri = String.format("http://localhost:%d/%s", port, resource);
                //logger.debug(String.format("********** SimpleHttpServer listening at '%s' will return HTTP %d/%s", uri, responseCode, responseBody));
                // Increase port number for the next guy...
                port++;
                return uri;
            }
            catch (Exception e) {
                throw new RuntimeException("Unable to start server: " + e);
            }
        }

        throw new RuntimeException("Unable to find an open port");
    }

    private class SimpleHttpHandler implements HttpHandler {
        private Logger logger = LoggerFactory.getLogger(SimpleHttpHandler.class);
        private int responseCode;
        private String responseBody;

        public SimpleHttpHandler(int responseCode, String responseBody) {
            //logger.debug("********** SimpleHttpHandler(responseCode={}, responseBody={})", responseCode, responseBody);
            this.responseCode = responseCode;
            this.responseBody = responseBody;
        }

        @Override
        public void handle(HttpExchange httpExchange) throws IOException {
            //logger.debug("handle(httpExchange={})", httpExchange);
            //logger.debug("responseCode=%d, responseBody='%s'", responseCode, responseBody);
            httpExchange.sendResponseHeaders(responseCode, responseBody.length());
            OutputStream os = httpExchange.getResponseBody();
            os.write(responseBody.getBytes());
            os.close();
        }
    }
}