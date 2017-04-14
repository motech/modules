package org.motechproject.openmrs.config;

import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.builder.HashCodeBuilder;

import java.util.HashMap;
import java.util.Map;

public class FeedConfig {

    private Map<String, String> atomFeeds;
    private boolean patientAtomFeed;
    private boolean encounterAtomFeed;

    public FeedConfig() {
        this(new HashMap<>(), false, false);
    }

    public FeedConfig(Map<String, String> atomFeeds, boolean patientAtomFeed, boolean encounterAtomFeed) {
        this.atomFeeds = atomFeeds;
        this.patientAtomFeed = patientAtomFeed;
        this.encounterAtomFeed = encounterAtomFeed;
    }

    public Map<String, String> getAtomFeeds() {
        return atomFeeds;
    }

    public void setAtomFeeds(Map<String, String> atomFeeds) {
        this.atomFeeds = atomFeeds;
    }

    public void setPatientAtomFeed(boolean patientAtomFeed) {
        this.patientAtomFeed = patientAtomFeed;
    }

    public void setEncounterAtomFeed(boolean encounterAtomFeed) {
        this.encounterAtomFeed = encounterAtomFeed;
    }

    public boolean isPatientAtomFeed() {
        return this.patientAtomFeed;
    }

    public boolean isEncounterAtomFeed() {
        return this.encounterAtomFeed;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (!(o instanceof FeedConfig)) {
            return false;
        }

        FeedConfig other = (FeedConfig) o;

        return ObjectUtils.equals(atomFeeds, other.atomFeeds) && ObjectUtils.equals(patientAtomFeed, other.patientAtomFeed)
                && ObjectUtils.equals(encounterAtomFeed, other.encounterAtomFeed);
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(atomFeeds).append(patientAtomFeed).append(encounterAtomFeed).toHashCode();
    }
}
