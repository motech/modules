package org.motechproject.atomclient.service;

public final class Constants {
    private Constants() { };
    public static final String FETCH_CRON_PROPERTY = "atomclient.feed.cron";
    public static final String BASE_ATOMCLIENT_SUBJECT = "org.motechproject.atomclient";
    public static final String ATOMCLIENT_FEED_CHANGE_MESSAGE = BASE_ATOMCLIENT_SUBJECT + ".feedchange";
    public static final String ATOMCLIENT_FETCH_MESSAGE = BASE_ATOMCLIENT_SUBJECT + ".fetch";
    public static final String ATOMCLIENT_SCHEDULE_FETCH_JOB = BASE_ATOMCLIENT_SUBJECT + ".schedulefetchjob";
}
