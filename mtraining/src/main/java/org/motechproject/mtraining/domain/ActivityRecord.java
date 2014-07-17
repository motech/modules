package org.motechproject.mtraining.domain;

import org.joda.time.DateTime;
import org.motechproject.mds.annotations.Entity;
import org.motechproject.mds.annotations.Field;


/**
 * Log for an instance of course activity for a user identified by externalId
 * This could be used either as a bookmarking system or enrollment system to track progress
 */
@Entity
public class ActivityRecord extends MdsEntity {

    @Field
    private String externalId;

    @Field
    private String courseName;

    @Field
    private String chapterName;

    @Field
    private String lessonName;

    @Field
    private String quizName;

    @Field
    private Double quizScore;

    @Field
    private DateTime startTime;

    @Field
    private DateTime completionTime;

    @Field
    private ActivityState state;

    public ActivityRecord(String externalId, String courseName, String chapterName, String lessonName, DateTime startTime,
                          DateTime completionTime, ActivityState state) {
        this(externalId, courseName, chapterName, lessonName, null, null, startTime, completionTime, state);
    }

    public ActivityRecord(String externalId, String courseName, String chapterName, String lessonName, String quizName,
                          Double quizScore, DateTime startTime, DateTime completionTime, ActivityState state) {
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
