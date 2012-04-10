package org.motechproject.commcare.request;

/**
 * Created by IntelliJ IDEA.
 * User: pchandra
 * Date: 4/9/12
 * Time: 10:18 PM
 * To change this template use File | Settings | File Templates.
 */
public class MetaElement {
    String xmlns;

    public MetaElement(String xmlns, String instanceId, String timeStart, String timeEnd, String userId) {
        this.xmlns = xmlns;
        this.instanceId = instanceId;
        this.timeStart = timeStart;
        this.timeEnd = timeEnd;
        this.userId = userId;
    }

    String instanceId;
    String timeStart;
    String timeEnd;
    String userId;


    public String getXmlns() {
        return xmlns;
    }

    public String getInstanceId() {
        return instanceId;
    }

    public String getTimeStart() {
        return timeStart;
    }

    public String getTimeEnd() {
        return timeEnd;
    }

    public String getUserId() {
        return userId;
    }
}
