package org.motechproject.commcare.request;

/**
 * Created by IntelliJ IDEA.
 * User: pchandra
 * Date: 4/9/12
 * Time: 10:15 PM
 * To change this template use File | Settings | File Templates.
 */
public class CommcareRequestData {
    String xmlns;
    MetaElement meta;
    CaseRequest ccCase;

    public CommcareRequestData(String xmlns, MetaElement meta, CaseRequest ccCase) {
        this.xmlns = xmlns;
        this.meta = meta;
        this.ccCase = ccCase;
    }

    public CaseRequest getCcCase() {
        return ccCase;

    }
    public MetaElement getMeta() {
        return meta;
    }

    public void setMeta(MetaElement meta) {
        this.meta = meta;
    }

    public String getXmlns() {
        return xmlns;
    }

    public void setXmlns(String xmlns) {
        this.xmlns = xmlns;
    }
}
