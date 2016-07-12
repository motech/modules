package org.motechproject.http.agent.handler;

import java.io.IOException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.client.ResponseErrorHandler;
import org.springframework.http.client.ClientHttpResponse;
import org.motechproject.http.agent.utility.RestUtility;

public class HttpResponseErrorHandler implements ResponseErrorHandler{

    private static final Logger LOGGER = LoggerFactory.getLogger(HttpResponseErrorHandler.class);


    @Override
    public void handleError(ClientHttpResponse response) throws IOException {
        LOGGER.error("Http response error: {} {}", response.getStatusCode(), response.getStatusText());
    }

    @Override
    public boolean hasError(ClientHttpResponse response) throws IOException {
        return RestUtility.isError(response.getStatusCode());
    }
}