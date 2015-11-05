package org.motechproject.mtraining.service;

import org.motechproject.mtraining.dto.CourseUnitDto;

import java.util.List;

/**
 * The <code>CourseStructureService</code> contains APIs to updating courses structure from DTO representation. It is used
 * by the tree view.
 */
public interface CourseStructureService {

    /**
     * Updates relations between courses, chapters, lessons and quizzes, it also updates states of given units. When some
     * relations was deleted it also breaks branches for single units, for example when we remove relation between course
     * and chapter then all lessons will be disconnected form chapter(they will be unused).
     *
     * @param courses the list of courses to update
     */
    void updateCourseStructure(List<CourseUnitDto> courses);
}
