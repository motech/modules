package org.motechproject.adherence.service;

import org.joda.time.LocalDate;
import org.motechproject.adherence.domain.AdherenceLog;
import org.motechproject.adherence.domain.Concept;
import org.motechproject.adherence.domain.ErrorFunction;

import java.util.List;
import java.util.Map;

public interface AdherenceService {
    void recordUnitAdherence(String externalId, Concept concept, boolean taken, ErrorFunction errorFunction, Map<String, Object> meta);

    void recordAdherence(String externalId, Concept concept, int taken, int totalDoses, LocalDate logDate, ErrorFunction errorFunction, Map<String, Object> meta);

    void recordAdherence(String externalId, Concept concept, int taken, int totalDoses, LocalDate fromDate, LocalDate toDate, ErrorFunction errorFunction, Map<String, Object> meta);

    double getRunningAverageAdherence(String externalId, Concept concept);

    double getRunningAverageAdherence(String externalId, Concept concept, LocalDate on);

    double getDeltaAdherence(String externalId, Concept concept);

    double getDeltaAdherence(String externalId, Concept concept, LocalDate fromDate, LocalDate toDate);

    void updateLatestAdherence(String externalId, Concept concept, int dosesTaken, int totalDoses);

    LocalDate getLatestAdherenceDate(String externalId, Concept concept);

    List<AdherenceLog> rollBack(String externalId, Concept concept, LocalDate tillDate);

    Map<String, Object> getMetaOn(String externalId, Concept concept, LocalDate date);
}
