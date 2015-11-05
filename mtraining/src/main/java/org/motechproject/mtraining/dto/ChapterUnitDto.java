package org.motechproject.mtraining.dto;

import org.motechproject.mtraining.util.Constants;

import java.util.List;

/**
 * Class used to provide data about structure of chapters. It is used by the tree view.
 */
public class ChapterUnitDto extends CourseUnitDto{

    /**
     * Quiz for the Chapter.
     */
    private CourseUnitDto quiz;

    public ChapterUnitDto() {

    }
    /**
     * Constructor with all arguments.
     *
     * @param id the id of the chapter
     * @param name the name of the chapter
     * @param state the status of the chapter
     * @param units the list of the lover level chapter
     * @param quiz the quiz for the chapter
     */
    public ChapterUnitDto(long id, String name, String state, List<CourseUnitDto> units, CourseUnitDto quiz) {
        super(id, name, state, units, Constants.CHAPTER);
        this.quiz = quiz;
    }

    public CourseUnitDto getQuiz() {
        return quiz;
    }

    public void setQuiz(CourseUnitDto quiz) {
        this.quiz = quiz;
    }

}
