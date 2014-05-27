package org.motechproject.decisiontree.model;

import org.codehaus.jackson.annotate.JsonTypeInfo;

@JsonTypeInfo(include = JsonTypeInfo.As.PROPERTY, use = JsonTypeInfo.Id.CLASS, property = "@type")
public interface INodeOperation {
    void perform(String userInput, FlowSession session);
}
