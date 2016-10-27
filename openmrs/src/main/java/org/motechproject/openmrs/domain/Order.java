package org.motechproject.openmrs.domain;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.annotations.Expose;

import java.lang.reflect.Type;
import java.util.Objects;
import java.util.SortedSet;
import java.util.TreeSet;

/**
 * Represents a single order. An order represents the instructions for patient in which order and how long
 * should one take a medications.
 */
public class Order {
    public static final String DEFAULT_TYPE = "order";

    private String uuid;
    private String display;

    @Expose
    private String type;
    @Expose
    private Encounter encounter;
    @Expose
    private Provider orderer;
    @Expose
    private Patient patient;
    @Expose
    private Concept concept;
    @Expose
    private CareSetting careSetting;

    public Order() {
    }

    public Order(String type, Encounter encounter, Provider orderer, Patient patient, Concept concept, CareSetting careSetting) {
        this.type = type;
        this.encounter = encounter;
        this.orderer = orderer;
        this.patient = patient;
        this.concept = concept;
        this.careSetting = careSetting;
    }

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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Encounter getEncounter() {
        return encounter;
    }

    public void setEncounter(Encounter encounter) {
        this.encounter = encounter;
    }

    public Provider getOrderer() {
        return orderer;
    }

    public void setOrderer(Provider orderer) {
        this.orderer = orderer;
    }

    public Patient getPatient() {
        return patient;
    }

    public void setPatient(Patient patient) {
        this.patient = patient;
    }

    public Concept getConcept() {
        return concept;
    }

    public void setConcept(Concept concept) {
        this.concept = concept;
    }

    public CareSetting getCareSetting() {
        return careSetting;
    }

    public void setCareSetting(CareSetting careSetting) {
        this.careSetting = careSetting;
    }

    public enum CareSetting {
        INPATIENT, OUTPATIENT;

        public static SortedSet<String> getValuesAsStringSet() {
            SortedSet<String> values = new TreeSet<>();

            for (CareSetting careSetting : CareSetting.values()) {
                values.add(careSetting.toString());
            }

            return values;
        }
    }

    /**
     * Implementation of the {@link JsonDeserializer} interface for the {@link CareSetting} class.
     */
    public static class CareSettingDeserializer implements JsonDeserializer<CareSetting> {
        @Override
        public CareSetting deserialize(JsonElement src, Type typeOfT, JsonDeserializationContext context) {
            JsonObject jsonCareSetting = src.getAsJsonObject();
            JsonElement display = jsonCareSetting.get("display");

            return CareSetting.valueOf(display.getAsString().toUpperCase());
        }
    }

    @Override
    public int hashCode() {
        return Objects.hash(uuid, display, encounter, orderer, patient, concept, careSetting);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (!(o instanceof Order)) {
            return false;
        }

        Order other = (Order) o;

        return Objects.equals(uuid, other.uuid) &&
                Objects.equals(display, other.display) &&
                Objects.equals(encounter, other.encounter) &&
                Objects.equals(orderer, other.orderer) &&
                Objects.equals(patient, other.patient) &&
                Objects.equals(concept, other.concept) &&
                Objects.equals(careSetting, other.careSetting);
    }
}
