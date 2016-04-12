package org.motechproject.openmrs19.domain;

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.google.gson.annotations.Expose;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Objects;

/**
 * Represents a single concept. Concepts are individual data points collected from a population of patients.
 */
public class Concept {

    private String uuid;

    @Expose
    private String display;
    private ConceptName name;
    private List<ConceptName> names;
    @Expose
    private DataType datatype;
    @Expose
    private ConceptClass conceptClass;

    /**
     * Default constructor.
     */
    public Concept() {
        this(null);
    }

    /**
     * Creates a concept with the given {@code name}.
     *
     * @param name  the name of the concept
     */
    public Concept(ConceptName name) {
        this.name = name;
    }

    public static class DataType {

        private String display;

        public DataType(String display) {
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
        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }

            if (obj == null || getClass() != obj.getClass()) {
                return false;
            }

            DataType other = (DataType) obj;

            return Objects.equals(this.display, other.display);
        }
    }

    /**
     * Implementation of the {@link JsonSerializer} for the
     * {@link DataType} class.
     */
    public static class DataTypeSerializer implements JsonSerializer<DataType> {

        @Override
        public JsonElement serialize(DataType src, Type typeOfSrc, JsonSerializationContext context) {
            return new JsonPrimitive(src.getDisplay());
        }
    }

    public static class ConceptClass {
        private String display;

        public ConceptClass(String display) {
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
        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }

            if (obj == null || getClass() != obj.getClass()) {
                return false;
            }

            ConceptClass other = (ConceptClass) obj;

            return Objects.equals(this.display, other.display);
        }
    }

    /**
     * Implementation of the {@link JsonSerializer} interface for the
     * {@link ConceptClass} class.
     */
    public static class ConceptClassSerializer implements JsonSerializer<ConceptClass> {

        @Override
        public JsonElement serialize(ConceptClass src, Type typeOfSrc, JsonSerializationContext context) {
            return new JsonPrimitive(src.getDisplay());
        }
    }

    /**
     * Implementation of the {@link JsonSerializer} interface for the {@link Concept} class. It represents the concept
     * as its ID.
     */
    public static class ConceptSerializer implements JsonSerializer<Concept> {

        @Override
        public JsonElement serialize(Concept concept, Type type, JsonSerializationContext jsonSerializationContext) {
            return new JsonPrimitive(concept.getUuid());
        }
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

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }

        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }

        Concept other = (Concept) obj;

        return Objects.equals(this.name, other.name);
    }

    @Override
    public String toString() {
        return "Concept{" +
                "uuid='" + uuid + '\'' +
                ", display='" + display + '\'' +
                ", name=" + name +
                ", names=" + names +
                ", datatype=" + datatype +
                ", conceptClass=" + conceptClass +
                '}';
    }
}
