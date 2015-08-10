package org.motechproject.commcare.parser;

import org.apache.xerces.parsers.DOMParser;
import org.motechproject.commcare.exception.CaseParserException;
import org.motechproject.commcare.response.OpenRosaResponse;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.io.StringReader;

/**
 * Class responsible for parsing CommCareHQ server responses into instances of the {@link OpenRosaResponse} class.
 */
public class OpenRosaResponseParser {

    /**
     * Parses the given CommCareHQ server response into an instance of the {@link OpenRosaResponse} class.
     *
     * @param response  the response to be parsed
     * @return the parsed response
     * @throws CaseParserException if there where problems while parsing the response
     */
    public OpenRosaResponse parseResponse(String response)
            throws CaseParserException {
        DOMParser parser = new DOMParser();

        OpenRosaResponse openRosaResponse = new OpenRosaResponse();

        InputSource inputSource = new InputSource();
        inputSource.setCharacterStream(new StringReader(response));

        try {
            parser.parse(inputSource);
        } catch (IOException ex) {
            throw new CaseParserException(ex, "Could not parse: IOException");
        } catch (SAXException ex) {
            throw new CaseParserException(ex, "Could not parse: SAXException");
        }

        Document document = parser.getDocument();

        Element openRosaElement = (Element) document.getElementsByTagName(
                "OpenRosaResponse").item(0);

        if (openRosaElement == null) {
            return null;
        }

        Element messageElement = (Element) document.getElementsByTagName(
                "message").item(0);

        if (messageElement != null) {
            String messageNature = messageElement.getAttribute("nature");
            String messageText = messageElement.getTextContent();

            openRosaResponse.setMessageNature(messageNature);
            openRosaResponse.setMessageText(messageText);
        }

        return openRosaResponse;
    }
}
