package org.motechproject.commcare.tasks.action;

import org.motechproject.commcare.util.ResponseXML;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

@Service("commcareValidatingChannelImpl")
public class CommcareValidatingChannelImpl implements CommcareValidatingChannel {

    private static final Logger LOG = LoggerFactory.getLogger(CommcareValidatingChannelImpl.class);

    private boolean executed;
    private String pregnant;
    private String dob;
    private String caseId;

    @PostConstruct
    public void setUp() {
        executed = false;
        pregnant = "";
        dob = "";
        caseId = "";
    }

    @Override
    public void execute(String pregnant, String dob, String caseId) {
        executed = true;
        this.pregnant = pregnant;
        this.dob = dob;
        this.caseId = caseId;
    }

    @Override
    public boolean hasExecuted() {
        return executed;
    }

    @Override
    public boolean verify() {
        return pregnant.equals(ResponseXML.ATTR1_VALUE) && dob.equals(ResponseXML.ATTR2_VALUE) && caseId.equals(ResponseXML.CASE_ID);
    }
}
