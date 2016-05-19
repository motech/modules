package org.motechproject.openmrs.domain;

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.google.gson.annotations.Expose;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Objects;

/**
 * Represents a single program. A program is typically used when a patient is identified as belonging to a group
 * for which a particular set of coordinated interventions is to be followed.
 */
public class Program {

    private String uuid;

    @Expose
    private String name;

    @Expose
    private String description;

    @Expose
    private Concept concept;

    @Expose
    private List<Workflow> allWorkflows;

    public static class Workflow {

        private String uuid;

        @Expose
        private String description;

        @Expose
        private Concept concept;

        @Expose
        private List<State> states;

        public String getUuid() {
            return uuid;
        }

        public void setUuid(String uuid) {
            this.uuid = uuid;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public Concept getConcept() {
            return concept;
        }

        public void setConcept(Concept concept) {
            this.concept = concept;
        }

        public List<State> getStates() {
            return states;
        }

        public void setStates(List<State> states) {
            this.states = states;
        }

        @Override
        public int hashCode() {
            return Objects.hash(description, concept, states);
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }

            if (obj == null || getClass() != obj.getClass()) {
                return false;
            }

            Workflow other = (Workflow) obj;

            return Objects.equals(this.description, other.description) && Objects.equals(this.concept, other.concept) &&
                    Objects.equals(this.states, other.states);
        }
    }

    public static class State {

        private String uuid;

        @Expose
        private String description;

        @Expose
        private Concept concept;

        public String getUuid() {
            return uuid;
        }

        public void setUuid(String uuid) {
            this.uuid = uuid;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public Concept getConcept() {
            return concept;
        }

        public void setConcept(Concept concept) {
            this.concept = concept;
        }

        @Override
        public int hashCode() {
            return Objects.hash(description, concept);
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }

            if (obj == null || getClass() != obj.getClass()) {
                return false;
            }

            State other = (State) obj;

            return Objects.equals(this.description, other.description) && Objects.equals(this.concept, other.concept);
        }
    }

    public static class ProgramSerializer implements JsonSerializer<Program> {

        @Override
        public JsonElement serialize(Program src, Type typeOfSrc, JsonSerializationContext context) {
            return new JsonPrimitive(src.getUuid());
        }
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Concept getConcept() {
        return concept;
    }

    public void setConcept(Concept concept) {
        this.concept = concept;
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, description, concept, allWorkflows);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }

        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }

        Program other = (Program) obj;

        return Objects.equals(this.name, other.name) && Objects.equals(this.description, other.description) &&
                Objects.equals(this.concept, other.concept) && Objects.equals(this.allWorkflows, other.allWorkflows);
    }
}
