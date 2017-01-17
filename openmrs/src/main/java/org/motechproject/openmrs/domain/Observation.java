package org.motechproject.openmrs.domain;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * Represents a single observation. An observation is a single piece of information that is recorded about a patient at
 * a moment in time.
 */
public class Observation {
    private static final String DISPLAY_KEY = "display";

    private String uuid;
    private String display;
    private Concept concept;
    private Encounter encounter;
    private ObservationValue value;
    private Date obsDatetime;
    private Person person;
    private List<Observation> groupMembers;
    private Boolean voided;
    private String valueModifier;
    private String valueCodedName;
    private Observation obsGroup;
    private Location location;
    private Order order;
    private String comment;

    //Field used in Tasks filter
    private String numberOfObservations;

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

    public List<Observation> getGroupMembers() {
        if (groupMembers == null) {
            groupMembers = new ArrayList<>();
        }

        return groupMembers;
    }

    public void setGroupMembers(List<Observation> groupMembers) {
        this.groupMembers = groupMembers;
    }

    public Boolean getVoided () {
        return voided;
    }

    public void setVoided (Boolean voided) {
        this.voided = voided;
    }

    public String getValueModifier () {
        return valueModifier;
    }

    public void setValueModifier (String valueModifier) {
        this.valueModifier = valueModifier;
    }

    public String getValueCodedName () {
        return valueCodedName;
    }

    public void setValueCodedName (String valueCodedName) {
        this.valueCodedName = valueCodedName;
    }

    public Observation getObsGroup() {
        return obsGroup;
    }

    public void setObsGroup(Observation obsGroup) {
        this.obsGroup = obsGroup;
    }

    public Location getLocation () {
        return location;
    }

    public void setLocation (Location location) {
        this.location = location;
    }

    public Order getOrder () {
        return order;
    }

    public void setOrder (Order order) {
        this.order = order;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getNumberOfObservations () {
        return numberOfObservations;
    }

    public void setNumberOfObservations (String numberOfObservations) {
        this.numberOfObservations = numberOfObservations;
    }

    @Override
    public int hashCode() {
        return Objects.hash(uuid, display, concept, encounter, value, obsDatetime, person, groupMembers, voided,
                valueModifier, valueCodedName, obsGroup, location, order, comment);
    }

    @Override //NO CHECKSTYLE Cyclomatic Complexity
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (!(o instanceof Observation)) {
            return false;
        }

        Observation other = (Observation) o;

        return Objects.equals(uuid, other.uuid) && Objects.equals(display, other.display)
                && Objects.equals(concept, other.concept) && Objects.equals(encounter, other.encounter)
                && Objects.equals(value, other.value) && Objects.equals(obsDatetime, other.obsDatetime)
                && Objects.equals(person, other.person) && Objects.equals(groupMembers, other.groupMembers)
                && Objects.equals(voided, other.voided) && Objects.equals(valueModifier, other.valueModifier)
                && Objects.equals(valueCodedName, other.valueCodedName) && Objects.equals(obsGroup, other.obsGroup)
                && Objects.equals(location, other.location) && Objects.equals(order, other.order)
                && Objects.equals(comment, other.comment);
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
            if (observation.groupMembers != null) {
                final JsonElement jsonObs = context.serialize(observation.groupMembers);
                object.add("groupMembers", jsonObs);
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
        public int hashCode() {
            return Objects.hash(display);
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

            return Objects.equals(display, other.display);
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

    /**
    * Implementation of the {@link JsonDeserializer} interface for the
    * {@link Observation.ObservationValue} class.
    */
    public static class ObservationValueDeserializer implements JsonDeserializer<ObservationValue> {
        @Override
        public ObservationValue deserialize (JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            ObservationValue observationValue = new ObservationValue(null);

            if (json.isJsonObject()) {
                JsonObject jsonObject = json.getAsJsonObject();
                observationValue.setDisplay(jsonObject.get(DISPLAY_KEY).getAsString());
            } else if (json.isJsonPrimitive()) {
                JsonPrimitive jsonPrimitive = json.getAsJsonPrimitive();
                observationValue.setDisplay(jsonPrimitive.getAsString());
            }

            return observationValue;
        }
    }
}
