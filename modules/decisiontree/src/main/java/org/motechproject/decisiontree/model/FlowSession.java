package org.motechproject.decisiontree.model;

import org.motechproject.decisiontree.model.Node;
import org.motechproject.ivr.domain.CallDetailRecord;

import java.io.Serializable;

/**
 * Represents a call flow session. Holds information related to the call, such as id, language, number
 * and the call detail record. Also contains information about the current position in the decision tree.
 * Used by IVR implementations to represent a call.
 */
public interface FlowSession {
    String getSessionId();
    String getLanguage();
    void setLanguage(String language);
    String getPhoneNumber();
    <T extends Serializable> T get(String key);
    <T extends Serializable> void set(String key, T value);
    Node getCurrentNode();
    void setCurrentNode(Node node);
    CallDetailRecord getCallDetailRecord();
}
