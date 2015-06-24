package org.motechproject.openmrs19.resource.model;

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents a single concept. Concepts are individual data points collected from a population of patients. It's a part
 * of the OpenMRS model.
 */
public class Concept {

    private String uuid;
    private String display;
    private ConceptName name;
    private List<ConceptName> names = new ArrayList<>();
    private DataType datatype;
    private ConceptClass conceptClass;

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

    public ConceptName getName() {
        return name;
    }

    public void setName(ConceptName name) {
        this.name = name;
    }

    public List<ConceptName> getNames() {
        return names;
    }

    public void setNames(List<ConceptName> names) {
        this.names = names;
    }

    public DataType getDatatype() {
        return datatype;
    }

    public void setDatatype(DataType datatype) {
        this.datatype = datatype;
    }

    public ConceptClass getConceptClass() {
        return conceptClass;
    }

    public void setConceptClass(ConceptClass conceptClass) {
        this.conceptClass = conceptClass;
    }

    /**
     * Implementation of the {@link JsonSerializer} interface for the {@link Concept} class. It represents the concept
     * as its display name.
     */
    public static class ConceptSerializer implements JsonSerializer<Concept> {

        @Override
        public JsonElement serialize(Concept src, Type typeOfSrc, JsonSerializationContext context) {
            return new JsonPrimitive(src.getDisplay());
        }
    }

    /**
     * Implementation of the {@link JsonSerializer} interface for the {@link Concept} class. It represents the concept
     * as its ID.
     */
    public static class ConceptUuidSerializer implements JsonSerializer<Concept> {

        @Override
        public JsonElement serialize(Concept concept, Type type, JsonSerializationContext jsonSerializationContext) {
            return new JsonPrimitive(concept.getUuid());
        }
    }

    /**
     * Represents a single concept name. Also stores information about its locale and type.
     */
    public static class ConceptName {

        private String name;
        private String locale = "en";
        private String conceptNameType = "FULLY_SPECIFIED";

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getLocale() {
            return locale;
        }

        public void setLocale(String locale) {
            this.locale = locale;
        }

        public String getConceptNameType() {
            return conceptNameType;
        }

        public void setConceptNameType(String conceptNameType) {
            this.conceptNameType = conceptNameType;
        }
    }

    /**
     * Represents a single data type. It can be one of the following: Numeric, Coded, Text, N/A, Document, Date, Time,
     * DateTime, Boolean, Rule, Structured Numeric or Complex.
     */
    public static class DataType {

        private String display;

        public String getDisplay() {
            return display;
        }

        public void setDisplay(String display) {
            this.display = display;
        }
    }


    /**
     * Implementation of the {@link JsonSerializer} for the
     * {@link org.motechproject.openmrs19.resource.model.Concept.DataType} class.
     */
    public static class DataTypeSerializer implements JsonSerializer<DataType> {

        @Override
        public JsonElement serialize(DataType src, Type typeOfSrc, JsonSerializationContext context) {
            return new JsonPrimitive(src.getDisplay());
        }
    }

    /**
     * Represents a single concept class. It can be one of the following: Test, Procedure, Drug, Diagnosis, Finding,
     * Anatomy, Question, LabSet, MedSet, ConvSet, Misc, Symptom, Symptom/Finding, Specimen or Misc Order.
     */
    public static class ConceptClass {
        private String display;

        public String getDisplay() {
            return display;
        }

        public void setDisplay(String display) {
            this.display = display;
        }
    }

    /**
     * Implementation of the {@link JsonSerializer} interface for the
     * {@link org.motechproject.openmrs19.resource.model.Concept.ConceptClass} class.
     */
    public static class ConceptClassSerializer implements JsonSerializer<ConceptClass> {

        @Override
        public JsonElement serialize(ConceptClass src, Type typeOfSrc, JsonSerializationContext context) {
            return new JsonPrimitive(src.getDisplay());
        }
    }
}
