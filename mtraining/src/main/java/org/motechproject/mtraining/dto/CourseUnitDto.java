package org.motechproject.mtraining.dto;

import org.codehaus.jackson.map.annotate.JsonDeserialize;

import java.util.List;

/**
 * Class used to provide data about structure of courses, chapters, lessons and quizzes. It is used by the tree view.
 *
 * @see org.motechproject.mtraining.web.TreeViewController
 */
public class CourseUnitDto {

    /**
     * Id of the course unit.
     */
    private long id;

    /**
     * Name of the course unit.
     */
    private String name;

    /**
     * Status of the course unit.
     */
    private String state;

    /**
     *
     */
    private List<CourseUnitDto> units;


    /**
     * Type of the unit(for example course or chapter).
     */
    private String type;

    public CourseUnitDto() {

    }

    /**
     * Constructor with all arguments.
     *
     * @param id the id of the unit
     * @param name the name of the unit
     * @param state the status of the unit
     * @param units the list of the lover level units
     * @param type the type of the unit
     */
    public CourseUnitDto(long id, String name, String state, List<CourseUnitDto> units, String type) {
        this.id = id;
        this.name = name;
        this.state = state;
        this.units = units;
        this.type = type;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public List<CourseUnitDto> getUnits() {
        return units;
    }

    @JsonDeserialize(using = CourseUnitDeserializer.class)
    public void setUnits(List<CourseUnitDto> units) {
        this.units = units;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
