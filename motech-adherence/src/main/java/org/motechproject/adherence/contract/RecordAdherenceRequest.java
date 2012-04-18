package org.motechproject.adherence.contract;

import org.joda.time.DateTime;

import java.util.List;

public class RecordAdherenceRequest {

    private String externalId;

    private String referenceId;

    private DateTime from;

    private DateTime to;

    private int dosesTaken;

    private int totalDoses;

    private List<String> tags;
}
