package org.motechproject.ivr.verboice.domain;

@Deprecated
public class Redirect implements VerboiceVerb {
    private String url;

    public Redirect(String url) {
        this.url = url;
    }

    @Override
    public String toXMLString() {
        return "<Redirect method=\"POST\">" + url + "</Redirect>";
    }
}
