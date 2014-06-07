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
    private Boolean multiLineRecipientResponse = false;
    private Boolean singleRecipientResponse = false;
    private String successStatus;
    private String successResponse;
    private String extractSingleSuccessMessageId;
    private String extractSingleFailureMessage;
    private String extractGeneralFailureMessage;
    private String extractSuccessMessageIdAndRecipient;
    private String extractFailureMessageAndRecipient;
    private String headerMessageId;
    private Pattern successResponsePattern;
    private Pattern extractSingleSuccessMessageIdPattern;
    private Pattern extractSingleFailureMessagePattern;
    private Pattern extractGeneralFailureMessagePattern;
    private Pattern extractSuccessMessageIdAndRecipientPattern;
    private Pattern extractFailureMessageAndRecipientPattern;

    public Boolean isSuccessStatus(Integer status) {
        if (isBlank(successStatus)) {
            return (status >= HTTP_SUCCESS_MIN && status <= HTTP_SUCCESS_MAX);
        }
        return status.toString().matches(successStatus);
    }

    public Boolean hasSuccessResponse() {
        return isNotBlank(successResponse);
    }

    public Boolean checkSuccessResponse(String response) {
        if (successResponsePattern == null) {
            successResponsePattern = Pattern.compile(successResponse, Pattern.DOTALL);
        }
        Matcher matcher = successResponsePattern.matcher(response);
        return matcher.matches();
    }

    public Boolean supportsMultiLineRecipientResponse() {
        return multiLineRecipientResponse;
    }

    public Boolean supportsSingleRecipientResponse() {
        return singleRecipientResponse;
    }

    public Boolean hasSingleSuccessMessageId() {
        return isNotBlank(extractSingleSuccessMessageId);
    }

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

    public boolean hasHeaderMessageId() {
        return isNotBlank(headerMessageId);
    }

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
