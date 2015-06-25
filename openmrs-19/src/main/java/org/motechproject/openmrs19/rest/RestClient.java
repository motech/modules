package org.motechproject.openmrs19.rest;

import org.motechproject.openmrs19.exception.HttpException;

import java.net.URI;

/**
 * Interface for communication with the OpenMRS server. It provides methods for sending/fetching data to/from the
 * OpenMRS server.
 */
public interface RestClient {

    /**
     * Fetches the data from the given URI.
     *
     * @param uri  the URI from which data should be fetched
     * @return the JSON representation of the returned data as a string
     * @throws HttpException if there were problems while fetching data
     */
    String getJson(URI uri) throws HttpException;

    /**
     * Sends the given data, represented as a JSON formatted string, to the given URL.
     *
     * @param url  the URL to send the given data to
     * @param json  the data, as JSON formatted string, that should be send to the given URL
     * @return the server response body as JSON formatted string
     * @throws HttpException if there were problems while sending data
     */
    String postForJson(URI url, String json) throws HttpException;

    /**
     * Sends the given data, represented as a JSON formatted string, to the given URL. This method doesn't return the
     * response body.
     *
     * @param url  the URL to send the given data to
     * @param json  the data, as JSON formatted string, that should be send to the given URL
     * @throws HttpException if there were problems while sending data
     */
    void postWithEmptyResponseBody(URI url, String json) throws HttpException;

    /**
     * Sends a delete request to the the given URI.
     *
     * @param uri  the URI to sent the delete request to
     * @throws HttpException if there were problems while sending request
     */
    void delete(URI uri) throws HttpException;
}
