package org.motechproject.mtraining.domain;

import org.joda.time.DateTime;
import org.motechproject.mds.annotations.Access;
import org.motechproject.mds.annotations.Entity;
import org.motechproject.mds.annotations.Field;
import org.motechproject.mds.domain.MdsEntity;
import org.motechproject.mds.util.SecurityMode;
import org.motechproject.mtraining.util.Constants;


/**
 * Log for an instance of course activity for a user identified by externalId
 * This could be used either as a bookmarking system or enrollment system to track progress
 */
@Entity(nonEditable = true)
@Access(value = SecurityMode.PERMISSIONS, members = {Constants.VIEW_MTRAINING_LOGS})
public class ActivityRecord extends MdsEntity {

    /**
     * External tracking id for user
     */
    @Field
    private String externalId;

    /**
     * Name of the course
     */
    @Field
    private String courseName;

    /**
     * Name of the chapter
     */
    @Field
    private String chapterName;

    /**
     * Name of the lesson
     */
    @Field
    private String lessonName;

    /**
     * Name of the quiz
     */
    @Field
    private String quizName;

    /**
     * Score in quiz
     */
    @Field
    private double quizScore;

    /**
     * start time for activity
     */
    @Field
    private DateTime startTime;

    /**
     * end time for activity
     */
    @Field
    private DateTime completionTime;

    /**
     * state of the activity
     */
    @Field
    private ActivityState state;

    public ActivityRecord() {
    }

    public ActivityRecord(String externalId, String courseName, String chapterName, String lessonName, DateTime startTime,
                          DateTime completionTime, ActivityState state) {
        this(externalId, courseName, chapterName, lessonName, null, 0, startTime, completionTime, state);
    }

    public ActivityRecord(String externalId, String courseName, String chapterName, String lessonName, String quizName,
                          double quizScore, DateTime startTime, DateTime completionTime, ActivityState state) {
        this.externalId = externalId;
        this.courseName = courseName;
        this.chapterName = chapterName;
        this.lessonName = lessonName;
        this.quizName = quizName;
        this.quizScore = quizScore;
        this.startTime = startTime;
        this.completionTime = completionTime;
        this.state = state;
    }

    public String getExternalId() {

        return externalId;
    }

    public void setExternalId(String externalId) {

        this.externalId = externalId;
    }

    public String getCourseName() {

        return courseName;
    }

    public void setCourseName(String courseName) {

        this.courseName = courseName;
    }

    public String getChapterName() {

        return chapterName;
    }

    public void setChapterName(String chapterName) {

        this.chapterName = chapterName;
    }

    public String getLessonName() {

        return lessonName;
    }

    public void setLessonName(String lessonName) {

        this.lessonName = lessonName;
    }

    public String getQuizName() {
        return quizName;
    }

    public void setQuizName(String quizName) {
        this.quizName = quizName;
    }

    public double getQuizScore() {
        return quizScore;
    }

    public void setQuizScore(double quizScore) {
        this.quizScore = quizScore;
    }

    public DateTime getStartTime() {

        return startTime;
    }

    public void setStartTime(DateTime startTime) {

        this.startTime = startTime;
    }

    public DateTime getCompletionTime() {

        return completionTime;
    }

    public void setCompletionTime(DateTime completionTime) {

        this.completionTime = completionTime;
    }

    public ActivityState getState() {

        return state;
    }

    public void setState(ActivityState state) {

        this.state = state;
    }
}
