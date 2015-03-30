package org.motechproject.csd.client;

import org.joda.time.DateTime;
import org.motechproject.csd.domain.GetModificationsRequest;
import org.motechproject.csd.domain.GetModificationsResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ws.client.core.support.WebServiceGatewaySupport;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

public class SOAPClient extends WebServiceGatewaySupport {

    private static final Logger LOGGER = LoggerFactory.getLogger(SOAPClient.class);

    public GetModificationsResponse getModifications(String url, DateTime lastModified) {

        GetModificationsRequest getModificationsRequest = new GetModificationsRequest();
        try {
            XMLGregorianCalendar c = DatatypeFactory.newInstance().newXMLGregorianCalendar(
                    lastModified.toGregorianCalendar());
            getModificationsRequest.setLastModified(c);
        } catch (DatatypeConfigurationException e) {
            LOGGER.error("Could not parse lastModified date");
            return null;
        }

        GetModificationsResponse response = (GetModificationsResponse) getWebServiceTemplate()
                .marshalSendAndReceive(url, getModificationsRequest);
        return response;
    }
}
