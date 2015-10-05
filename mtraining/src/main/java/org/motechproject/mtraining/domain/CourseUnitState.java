package org.motechproject.mtraining.domain;

/**
 * Course state enum
 * This is used to mark the course unit (eg - Course, Chapter, Lesson, etc)
 * as being Active, Pending or Inactive. This helps an implementation filter the
 * list of courses/chapter/lesson according to their business logic.
 */
public enum CourseUnitState {
    Active,
    Pending,
    Inactive;
}
