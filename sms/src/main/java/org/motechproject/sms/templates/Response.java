package org.motechproject.sms.templates;

//todo: handle malformed template files (ie: resulting in exceptions in the regex parsing) in a useful way for implementers?

import org.apache.http.HttpStatus;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.apache.commons.lang.StringUtils.isBlank;
import static org.apache.commons.lang.StringUtils.isNotBlank;

/**
 * How to to deal with provider-specific http responses
 */
public class Response {

    private static final int HTTP_SUCCESS_MIN = HttpStatus.SC_OK;
    private static final int HTTP_99 = 99;
    private static final int HTTP_SUCCESS_MAX = HttpStatus.SC_OK + HTTP_99;

    /**
     * Whether recipients are provided in multiple lines.
     */
    private Boolean multiLineRecipientResponse = false;

    /**
     * Whether the provider has a different type of response for single recipient
     * messages.
     */
    private Boolean singleRecipientResponse = false;

    /**
     * The success status expected.
     */
    private String successStatus;

    /**
     * If not empty, indicates that the provider sends responses for successful
     * requests.
     */
    private String successResponse;

    /**
     * The regex pattern that can be used for extracting ID from single success messages.
     */
    private String extractSingleSuccessMessageId;

    /**
     * The regex pattern that can be used for extracting single failure messages.
     */
    private String extractSingleFailureMessage;

    /**
     * The regex pattern used for extracting the general failure message.
     */
    private String extractGeneralFailureMessage;

    /**
     * The regex pattern used for extracting the success message ID and its recipient.
     */
    private String extractSuccessMessageIdAndRecipient;

    /**
     * The regex pattern used for extracting the failure message and its recipient.
     */
    private String extractFailureMessageAndRecipient;

    /**
     * The name of the HTTP header containing the message ID.
     */
    private String headerMessageId;

    // These patterns are compiled from the strings above, when needed

    private Pattern successResponsePattern;
    private Pattern extractSingleSuccessMessageIdPattern;
    private Pattern extractSingleFailureMessagePattern;
    private Pattern extractGeneralFailureMessagePattern;
    private Pattern extractSuccessMessageIdAndRecipientPattern;
    private Pattern extractFailureMessageAndRecipientPattern;

    /**
     * Checks whether the given status is a success status.
     * @param status the status to check
     * @return true if this is a success status, false otherwise
     */
    public Boolean isSuccessStatus(Integer status) {
        if (isBlank(successStatus)) {
            return (status >= HTTP_SUCCESS_MIN && status <= HTTP_SUCCESS_MAX);
        }
        return status.toString().matches(successStatus);
    }

    /**
     * @return true if this provider returns responses for successful requests
     */
    public Boolean hasSuccessResponse() {
        return isNotBlank(successResponse);
    }

    public String getSuccessStatus() {
        return successStatus;
    }

    /**
     * Checks whether the given response is a success response.
     * @param response the response to check
     * @return true if it is a success response, false otherwise
     */
    public Boolean checkSuccessResponse(String response) {
        if (successResponsePattern == null) {
            successResponsePattern = Pattern.compile(successResponse, Pattern.DOTALL);
        }
        Matcher matcher = successResponsePattern.matcher(response);
        return matcher.matches();
    }

    /**
     * @return true if this provider returns recipient responses in multiple lines, false otherwise
     */
    public Boolean supportsMultiLineRecipientResponse() {
        return multiLineRecipientResponse;
    }

    /**
     * @return true if this provider supports a different response type for an SMS sent to a single recipient, false otherwise
     */
    public Boolean supportsSingleRecipientResponse() {
        return singleRecipientResponse;
    }

    /**
     * @return true if the id for successfully sent messages is included in the response
     */
    public Boolean hasSingleSuccessMessageId() {
        return isNotBlank(extractSingleSuccessMessageId);
    }

    /**
     * Extracts the single line success message ID from the response.
     * @param response the response to parse
     * @return the extracted ID
     */
    public String extractSingleSuccessMessageId(String response) {
        if (extractSingleSuccessMessageIdPattern == null) {
            extractSingleSuccessMessageIdPattern = Pattern.compile(extractSingleSuccessMessageId);
        }
        Matcher m = extractSingleSuccessMessageIdPattern.matcher(response);
        if (m.groupCount() != 1) {
            throw new IllegalStateException(String.format("Template error, extractSingleSuccessMessageId: " +
                    "Invalid number of search groups, expected: 1, actual: %s.", m.groupCount()));
        }
        if (m.find()) {
            return m.group(1);
        }
        return null;
    }

    /**
     * Extracts the single failure message from the response.
     * @param response the response to parse
     * @return the failure message
     */
    public String extractSingleFailureMessage(String response) {
        if (extractSingleFailureMessage != null && extractSingleFailureMessage.length() > 0) {
            if (extractSingleFailureMessagePattern == null) {
                extractSingleFailureMessagePattern = Pattern.compile(extractSingleFailureMessage);
            }
            Matcher m = extractSingleFailureMessagePattern.matcher(response);
            if (m.groupCount() != 1) {
                throw new IllegalStateException(String.format("Template error, extractSingleFailureMessage: " +
                        "Invalid number of search groups, expected: 1, actual: %s.", m.groupCount()));
            }
            if (m.find()) {
                return m.group(1);
            }
        }
        return null;
    }

    /**
     * Extracts the general failure message from the response.
     * @param response the response to parse
     * @return the extracted failure message
     */
    public String extractGeneralFailureMessage(String response) {
        if (extractGeneralFailureMessagePattern == null) {
            extractGeneralFailureMessagePattern = Pattern.compile(extractGeneralFailureMessage);
        }
        Matcher m = extractGeneralFailureMessagePattern.matcher(response);
        if (m.groupCount() != 1) {
            throw new IllegalStateException(String.format("Template error, extractGeneralFailureMessage: " +
                    "Invalid number of search groups, expected: 1, actual: %s.", m.groupCount()));
        }
        if (m.find()) {
            return m.group(1);
        }
        return null;
    }

    //todo: what if bad or wrong number of regex groups ? ie (only one extract)

    /**
     * Extracts the success message ID and recipient from the response.
     * @param response the response to parse
     * @return a string array, where the success message ID is the first element and the recipient is the second one
     */
    public String[] extractSuccessMessageIdAndRecipient(String response) {
        if (extractSuccessMessageIdAndRecipientPattern == null) {
            extractSuccessMessageIdAndRecipientPattern = Pattern.compile(extractSuccessMessageIdAndRecipient);
        }
        Matcher m = extractSuccessMessageIdAndRecipientPattern.matcher(response);
        if (m.groupCount() != 2) {
            throw new IllegalStateException(String.format("Template error, extractSuccessMessageIdAndRecipient: " +
                    "Invalid number of search groups, expected: 2, actual: %s.", m.groupCount()));
        }
        if (m.find()) {
            return new String[] {m.group(1), m.group(2)};
        }
        return null;
    }

    /**
     * Extracts the failure message  and recipient from the response.
     * @param response the response to parse
     * @return a string array, where the failure message is the first element and the recipient is the second one
     */
    public String[] extractFailureMessageAndRecipient(String response) {
        if (extractFailureMessageAndRecipientPattern == null) {
            extractFailureMessageAndRecipientPattern = Pattern.compile(extractFailureMessageAndRecipient);
        }
        Matcher m = extractFailureMessageAndRecipientPattern.matcher(response);
        if (m.groupCount() != 2) {
            throw new IllegalStateException(String.format("Template error, extractFailureMessageAndRecipient: " +
                    "Invalid number of search groups, expected: 2, actual: %s.", m.groupCount()));
        }
        if (m.find()) {
            return new String[] {m.group(1), m.group(2)};
        }
        return null;
    }

    /**
     * @return true if this provider returns message IDs as a header, false otherwise
     */
    public boolean hasHeaderMessageId() {
        return isNotBlank(headerMessageId);
    }

    /**
     * @return the header name for the message ID
     */
    public String getHeaderMessageId() {
        return headerMessageId;
    }

    @Override
    public String toString() {
        return "Response{" +
                "headerMessageId='" + headerMessageId + '\'' +
                ", multiLineRecipientResponse=" + multiLineRecipientResponse +
                ", singleRecipientResponse=" + singleRecipientResponse +
                ", successStatus='" + successStatus + '\'' +
                ", successResponse='" + successResponse + '\'' +
                ", extractSingleSuccessMessageId='" + extractSingleSuccessMessageId + '\'' +
                ", extractSingleFailureMessage='" + extractSingleFailureMessage + '\'' +
                ", extractGeneralFailureMessage='" + extractGeneralFailureMessage + '\'' +
                ", extractSuccessMessageIdAndRecipient='" + extractSuccessMessageIdAndRecipient + '\'' +
                ", extractFailureMessageAndRecipient='" + extractFailureMessageAndRecipient + '\'' +
                '}';
    }
}
