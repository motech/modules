package org.motechproject.ivr.verboice.domain;

@Deprecated
public class Hangup implements VerboiceVerb {

    @Override
    public String toXMLString() {
        return "<Hangup/>";
    }
}
