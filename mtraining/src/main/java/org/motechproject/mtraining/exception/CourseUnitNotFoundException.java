package org.motechproject.mtraining.exception;

/**
 * The <code>CourseUnitNotFoundException</code> exception signals a situation in which an course unit does not
 * exist in database.
 */
public class CourseUnitNotFoundException extends RuntimeException {

    private static final long serialVersionUID = 6840168621234574759L;

    public CourseUnitNotFoundException(String message) {
        super(message);
    }
}
