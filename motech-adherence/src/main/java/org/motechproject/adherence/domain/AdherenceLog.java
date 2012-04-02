package org.motechproject.adherence.domain;

import org.apache.commons.lang.StringUtils;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.ektorp.support.TypeDiscriminator;
import org.joda.time.LocalDate;
import org.motechproject.adherence.service.AdherenceService;
import org.motechproject.model.MotechBaseDataObject;

import java.util.HashMap;
import java.util.Map;

@TypeDiscriminator("doc.type === 'AdherenceLog'")
public class AdherenceLog extends MotechBaseDataObject {

    protected String externalId;
    protected int dosesTaken;
    protected int totalDoses;
    protected LocalDate fromDate;
    protected LocalDate toDate;
    protected int deltaDosesTaken;
    protected int deltaTotalDoses;
    protected Concept concept = AdherenceService.GENERIC_CONCEPT;
    private Map<String, Object> meta = new HashMap<String, Object>();

    public AdherenceLog() {
    }

    public AdherenceLog(AdherenceLog that, LocalDate fromDate, LocalDate toDate) {
        this(that);
        this.fromDate = fromDate;
        this.toDate = toDate;
    }

    public AdherenceLog(AdherenceLog that) {
        this.externalId = that.externalId;
        this.concept = that.concept;
        this.dosesTaken = that.dosesTaken;
        this.totalDoses = that.totalDoses;
        this.fromDate = that.fromDate;
        this.toDate = that.toDate;
        this.deltaDosesTaken = that.deltaDosesTaken;
        this.deltaTotalDoses = that.deltaTotalDoses;
    }

    public static AdherenceLog create(String externalId, Concept concept, int taken, int totalDoses, LocalDate fromDate, LocalDate toDate, Map<String, Object> meta, LocalDate today) {
        AdherenceLog newLog = AdherenceLog.create(externalId, concept, today).addAdherence(taken, totalDoses);
        initialize(fromDate, toDate, meta, newLog);
        return newLog;
    }

    public static AdherenceLog create(String externalId, Concept concept, LocalDate date) {
        return create(externalId, concept, date, date);
    }

    public static AdherenceLog create(String externalId, Concept concept, LocalDate fromDate, LocalDate toDate) {
        AdherenceLog newLog = new AdherenceLog();
        newLog.fromDate = fromDate;
        newLog.toDate = toDate;
        newLog.externalId = externalId;
        newLog.concept = concept;
        return newLog;
    }

    public static AdherenceLog create(String externalId, Concept concept, Map<String, Object> meta, LocalDate today, int dosesTaken) {
        AdherenceLog newLog = initialize(meta, AdherenceLog.create(externalId, concept, today), dosesTaken);
        return newLog;
    }

    public static AdherenceLog initialize(Map<String, Object> meta, AdherenceLog latestLog, int dosesTaken) {
        AdherenceLog newLog = latestLog.addAdherence(dosesTaken, 1);
        newLog.setMeta(meta);
        return newLog;
    }

    public static void initialize(LocalDate fromDate, LocalDate toDate, Map<String, Object> meta, AdherenceLog newLog) {
        newLog.setFromDate(fromDate);
        newLog.setToDate(toDate);
        newLog.setMeta(meta);
    }

    public String getExternalId() {
        return externalId;
    }

    public void setExternalId(String externalId) {
        this.externalId = externalId;
    }

    public int getDosesTaken() {
        return dosesTaken;
    }

    public void setDosesTaken(int dosesTaken) {
        this.dosesTaken = dosesTaken;
    }

    public int getTotalDoses() {
        return totalDoses;
    }

    public void setTotalDoses(int totalDoses) {
        this.totalDoses = totalDoses;
    }

    public LocalDate getFromDate() {
        return fromDate;
    }

    public void setFromDate(LocalDate fromDate) {
        this.fromDate = fromDate;
    }

    public LocalDate getToDate() {
        return toDate;
    }

    public void setToDate(LocalDate toDate) {
        this.toDate = toDate;
    }

    public void setDateRange(LocalDate fromDate, LocalDate toDate) {
        this.fromDate = fromDate;
        this.toDate = toDate;
    }

    public int getDeltaDosesTaken() {
        return deltaDosesTaken;
    }

    public void setDeltaDosesTaken(int deltaDosesTaken) {
        this.deltaDosesTaken = deltaDosesTaken;
    }

    public int getDeltaTotalDoses() {
        return deltaTotalDoses;
    }

    public void setDeltaTotalDoses(int deltaTotalDoses) {
        this.deltaTotalDoses = deltaTotalDoses;
    }

    public Concept getConcept() {
        return concept;
    }

    public void setConcept(Concept concept) {
        this.concept = concept;
    }

    public void putMeta(String key, Object value) {
        meta.put(key, value);
    }

    public Map<String, Object> getMeta() {
        return meta;
    }

    public void setMeta(Map<String, Object> meta) {
        this.meta = meta;
    }

    @JsonIgnore
    public AdherenceLog cut(AdherenceLog otherLog) {
        otherLog.setFromDate(this.getToDate().plusDays(1));
        return otherLog;
    }

    @JsonIgnore
    public boolean cutBy(LocalDate tillDate) {
        return !this.fromDate.isAfter(tillDate) && this.toDate.isAfter(tillDate);
    }

    @JsonIgnore

    public boolean overlaps(AdherenceLog that) {
        return !this.toDate.isBefore(that.fromDate) && that.toDate.isAfter(this.toDate);
    }

    @JsonIgnore
    public boolean encloses(AdherenceLog entity) {
        return !this.fromDate.isAfter(entity.fromDate) && !this.toDate.isBefore(entity.toDate);
    }

    @JsonIgnore
    public AdherenceLog addAdherence(int dosesTaken, int totalDoses) {
        AdherenceLog newLog = new AdherenceLog(this);
        newLog.setDosesTaken(this.dosesTaken + dosesTaken);
        newLog.setTotalDoses(this.totalDoses + totalDoses);
        newLog.setDeltaDosesTaken(dosesTaken);
        newLog.setDeltaTotalDoses(totalDoses);
        return newLog;
    }

    @JsonIgnore
    public boolean isNotOn(LocalDate date) {
        return date.isAfter(this.toDate);
    }

    @JsonIgnore
    public void updateDeltaDosesTaken(int deltaDosesTaken) {
        this.dosesTaken -= this.deltaDosesTaken;
        this.dosesTaken += deltaDosesTaken;
        this.deltaDosesTaken = deltaDosesTaken;
    }

    @JsonIgnore
    public void updateDeltaTotalDoses(int deltaTotalDoses) {
        this.totalDoses -= this.deltaTotalDoses;
        this.totalDoses += deltaTotalDoses;
        this.deltaTotalDoses = deltaTotalDoses;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        AdherenceLog that = (AdherenceLog) o;

        if (StringUtils.isEmpty(getId())) return StringUtils.isEmpty(that.getId());
        if (!getId().equals(that.getId())) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return getId().hashCode();
    }
}
