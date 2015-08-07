package org.motechproject.commcare.request;

/**
 * Represents a meta element in an XML document.
 */
public class MetaElement {

    private String xmlns;
    private String instanceID;
    private String timeStart;
    private String timeEnd;
    private String userID;

    /**
     * Creates a meta XML element with maximum of five sub-elements - xmlns, instanceId, timeStart, timeEnd, userId.
     * Values of those elements will be equal to the given parameters. If any of the parameters is null the related
     * element won't be added.
     *
     * @param xmlns  the value of the xmlns element, which should contain the namespace of the XML element
     * @param instanceID  the value of the instanceId element, which should contain the ID of the instance
     * @param timeStart  the value of the timeStart element, which should contain the start of the time range for which
     *                   cases will be fetched
     * @param timeEnd  the value of the timeEnd element, which should contain the end of the time range for which cases
     *                 will be fetched
     * @param userId  the value of the userId element, which should contain the ID of the user
     */
    public MetaElement(String xmlns, String instanceID, String timeStart,
            String timeEnd, String userId) {
        this.xmlns = xmlns;
        this.instanceID = instanceID;
        this.timeStart = timeStart;
        this.timeEnd = timeEnd;
        this.userID = userId;
    }

    public String getXmlns() {
        return this.xmlns;
    }

    public String getInstanceID() {
        return this.instanceID;
    }

    public String getTimeStart() {
        return this.timeStart;
    }

    public String getTimeEnd() {
        return this.timeEnd;
    }

    public String getUserID() {
        return this.userID;
    }
}
