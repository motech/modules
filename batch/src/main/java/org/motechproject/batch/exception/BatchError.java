package org.motechproject.batch.exception;

import org.motechproject.batch.util.BatchUtils;

import javax.xml.bind.annotation.XmlRootElement;
import java.util.Date;

/**
 * JSON element for Error when there is an exception in rest call.
 *
 * @author naveen
 */
@XmlRootElement(name = "Error")
public class BatchError {

    /**
     * The code of this error
     */
    private String errorCode;

    /**
     * A short error message.
     */
    private String errorMessage;

    /**
     * The name of the application that caused the error.
     */
    private String application;

    /**
     * The timestamp of this error.
     */
    private Date timeStamp;

    /**
     * The hostname of the server on which this error occurred.
     */
    private String hostName;

    /**
     * Constructs the error, setting the timestamp to now, and the hostname
     * to the one of this instance.
     */
    public BatchError() {
        timeStamp = BatchUtils.getCurrentDateTime();
        hostName = BatchUtils.getNetworkHostName();
    }

    /**
     * @return the timestamp of this error
     */
    public Date getTimeStamp() {
        return timeStamp;
    }

    /**
     * @param timeStamp the timestamp of this error
     */
    public void setTimeStamp(Date timeStamp) {
        this.timeStamp = timeStamp;
    }

    /**
     * @return the hostname of the server on which this error occurred
     */
    public String getHostName() {
        return hostName;
    }

    /**
     * @param hostName the hostname of the server on which this error occurred
     */
    public void setHostName(String hostName) {
        this.hostName = hostName;
    }

    /**
     * @return the code of this error
     */
    public String getErrorCode() {
        return errorCode;
    }

    /**
     * @param errorCode the code of this error
     */
    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    /**
     * @return a short error message
     */
    public String getErrorMessage() {
        return errorMessage;
    }

    /**
     * @param errorMessage a short error message
     */
    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    /**
     * @return the name of the application that caused the error
     */
    public String getApplication() {
        return application;
    }

    /**
     * @param application the name of the application that caused the error
     */
    public void setApplication(String application) {
        this.application = application;
    }

}
