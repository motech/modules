package org.motechproject.openmrs19.domain;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.builder.HashCodeBuilder;

import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Represents a single observation. An observation is a single piece of information that is recorded about a patient at
 * a moment in time.
 */
public class Observation {

    private String uuid;
    private String display;
    private Concept concept;
    private Encounter encounter;
    private ObservationValue value;
    private Date obsDatetime;
    private Person person;
    private List<Observation> groupsMembers;

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getDisplay() {
        return display;
    }

    public void setDisplay(String display) {
        this.display = display;
    }

    public Concept getConcept() {
        return concept;
    }

    public void setConcept(Concept concept) {
        this.concept = concept;
    }

    public boolean hasConceptByName(String conceptName) {
        return concept.getDisplay().equals(conceptName);
    }

    public Encounter getEncounter() {
        return encounter;
    }

    public void setEncounter(Encounter encounter) {
        this.encounter = encounter;
    }

    public ObservationValue getValue() {
        return value;
    }

    public void setValue(ObservationValue value) {
        this.value = value;
    }

    public Date getObsDatetime() {
        return obsDatetime;
    }

    public void setObsDatetime(Date obsDatetime) {
        this.obsDatetime = obsDatetime;
    }

    public Person getPerson() {
        return person;
    }

    public void setPerson(Person person) {
        this.person = person;
    }

    public List<Observation> getGroupsMembers() {
        return groupsMembers;
    }

    public void setGroupsMembers(List<Observation> groupsMembers) {
        this.groupsMembers = groupsMembers;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (!(o instanceof Observation)) {
            return false;
        }

        Observation other = (Observation) o;

        return ObjectUtils.equals(uuid, other.uuid) && ObjectUtils.equals(display, other.display)
                && ObjectUtils.equals(concept, other.concept) && ObjectUtils.equals(encounter, other.encounter)
                && ObjectUtils.equals(value, other.value) && ObjectUtils.equals(obsDatetime, other.obsDatetime)
                && ObjectUtils.equals(person, other.person) && ObjectUtils.equals(groupsMembers, other.groupsMembers);
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(uuid).append(display).append(concept).append(encounter).append(value)
                .append(obsDatetime).append(person).append(groupsMembers).toHashCode();
    }

    /**
     * Implementation of the {@link JsonSerializer} interface for the {@link Observation} class.
     */
    public static class ObservationSerializer implements JsonSerializer<Observation> {

        @Override
        public JsonElement serialize(Observation observation, Type typeOfSrc, JsonSerializationContext context) {

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");

            JsonObject object = new JsonObject();
            if (observation.concept != null) {
                object.addProperty("concept", observation.concept.getUuid());
            }
            if (observation.value != null) {
                object.addProperty("value", observation.value.getDisplay());
            }
            if (observation.obsDatetime != null) {
                object.addProperty("obsDatetime", sdf.format(observation.obsDatetime));
            }

            return object;
        }
    }

    /**
     * Represents a single value of the observation.
     */
    public static class ObservationValue {
        private String display;

        public ObservationValue(String display) {
            this.display = display;
        }

        public String getDisplay() {
            return display;
        }

        public void setDisplay(String display) {
            this.display = display;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }

            if (!(o instanceof ObservationValue)) {
                return false;
            }

            ObservationValue other = (ObservationValue) o;

            return ObjectUtils.equals(display, other.display);
        }

        @Override
        public int hashCode() {
            return new HashCodeBuilder().append(display).toHashCode();
        }
    }

    /**
     * Implementation of the {@link JsonSerializer} interface for the
     * {@link Observation.ObservationValue} class.
     */
    public static class ObservationValueSerializer implements JsonSerializer<ObservationValue> {
        @Override
        public JsonElement serialize(ObservationValue src, Type typeOfSrc, JsonSerializationContext context) {
            return new JsonPrimitive(src.getDisplay());
        }
    }
}
