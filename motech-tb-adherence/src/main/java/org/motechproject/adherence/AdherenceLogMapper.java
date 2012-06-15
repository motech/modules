package org.motechproject.adherence;

import org.motechproject.adherence.contract.AdherenceData;
import org.motechproject.adherence.domain.AdherenceLog;

import java.util.ArrayList;
import java.util.List;

public class AdherenceLogMapper {

    public AdherenceLog map(AdherenceData adherenceData) {
        AdherenceLog log = new AdherenceLog(adherenceData.externalId(),adherenceData.treatmentId(),adherenceData.doseDate());
        log.meta(adherenceData.meta());
        return log;
    }

    public List<AdherenceLog> map(List<AdherenceData> adherenceData) {
        List<AdherenceLog> logs = new ArrayList();
        for(AdherenceData datum  : adherenceData) {
            logs.add(map(datum));
        }
        return logs;
    }
}
