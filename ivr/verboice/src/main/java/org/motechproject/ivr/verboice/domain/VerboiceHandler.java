package org.motechproject.ivr.verboice.domain;

import java.util.Map;

@Deprecated
public interface VerboiceHandler {
    String handle(Map<String, String> parameters);
}
