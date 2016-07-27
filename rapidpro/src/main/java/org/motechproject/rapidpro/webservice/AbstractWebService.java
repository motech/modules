package org.motechproject.rapidpro.webservice;

import org.codehaus.jackson.type.TypeReference;
import org.motechproject.rapidpro.exception.JsonUtilException;
import org.motechproject.rapidpro.exception.RapidProClientException;
import org.motechproject.rapidpro.exception.WebServiceException;
import org.motechproject.rapidpro.util.JsonUtils;
import org.motechproject.rapidpro.webservice.dto.PaginatedResponse;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * Abstract superclass for all webservice implementations.
 */
public class AbstractWebService<T> {

    protected static final String UUID = "uuid";
    protected static final String NAME = "name";
    private static final String MORE_THAN_ONE_QUERY = "Query Returned more than one element.Parameters:  ";

    private RapidProHttpClient client;

    @Autowired
    public AbstractWebService(RapidProHttpClient client) {
        this.client = client;
    }

    protected RapidProHttpClient getClient() {
        return client;
    }

    /**
     * Returns a matching instance of type T, if it exists.
     *
     * @param key The field of the RapidPro Entity
     * @param value The value to be matched
     * @param endpoint The RapidPro HTTP API endpoint
     * @param typeRef The type reference for T
     * @return One instance of type T.
     * @throws WebServiceException
     * @throws RapidProClientException
     * @throws JsonUtilException
     */
    protected T getOneWithParams(String key, String value, String endpoint, TypeReference typeRef) throws WebServiceException, RapidProClientException, JsonUtilException {
        Map<String, String> params = new HashMap<>();
        params.put(key, value);
        InputStream response = client.executeGet(endpoint, MediaFormat.JSON, params);
        PaginatedResponse<T> paginatedResponse = (PaginatedResponse<T>) JsonUtils.toObject(response, typeRef);
        if (paginatedResponse.getResults().size() == 0) {
            return null;

        } else if (paginatedResponse.getResults().size() == 1) {
            return paginatedResponse.getResults().get(0);

        } else {
            throw new WebServiceException(MORE_THAN_ONE_QUERY + params.toString());
        }
    }
}
