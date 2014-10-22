package org.motechproject.commcare.tasks.action;

public interface CommcareValidatingChannel {

    void execute(String pregnant, String dobKnown);

    boolean hasExecuted();

    boolean verify();
}
