package org.motechproject.hub.exception;

import org.motechproject.hub.util.HubUtils;

import javax.xml.bind.annotation.XmlRootElement;
import java.util.Date;

/**
 * JSON element for Hub Error when there is an exception in rest call.
 *
 */
@XmlRootElement(name = "Error")
public class HubError {

    private String errorCode;
    private String errorMessage;
    private String application;
    private Date timeStamp;
    private String hostName;

    /**
     * Creates a new instance of <code>HubError</code> setting the timestamp to now, and the hostname
     * to the one of this instance.
     */
    public HubError() {
        timeStamp = HubUtils.getCurrentDateTime();
        hostName = HubUtils.getNetworkHostName();
    }

    /**
     * Gets timestamp of this error.
     *
     * @return the timestamp of this error
     */
    public Date getTimeStamp() {
        return timeStamp;
    }

    /**
     * Sets timestamp of this error.
     *
     * @param timeStamp timestamp to be set
     */
    public void setTimeStamp(Date timeStamp) {
        this.timeStamp = timeStamp;
    }

    /**
     * Gets hostname of the server on which this error occurred.
     *
     * @return the hostname of the server
     */
    public String getHostName() {
        return hostName;
    }

    /**
     * Sets hostname of the server on which this error occurred
     * for this <code>HubError</code> instance.
     *
     * @param hostName host name to be set
     */
    public void setHostName(String hostName) {
        this.hostName = hostName;
    }

    /**
     * Gets the code of this error.
     *
     * @return the code of this error
     */
    public String getErrorCode() {
        return errorCode;
    }

    /**
     * Sets the code of this error for this <code>HubError</code> instance.
     *
     * @param errorCode the code to be set
     */
    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    /**
     * Gets a message describing the error.
     *
     * @return a message describing the error
     */
    public String getErrorMessage() {
        return errorMessage;
    }

    /**
     * Sets a message describing the error for this <code>HubError</code> instance.
     *
     * @param errorMessage
     */
    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    /**
     * Gets the name of the application that caused the error.
     *
     * @return the name of the application that caused the error
     */
    public String getApplication() {
        return application;
    }

    /**
     * Sets the name of the application that caused the error.
     *
     * @param application the name of the application that caused the error
     */
    public void setApplication(String application) {
        this.application = application;
    }

}
