package org.motechproject.atomclient.service;

public final class Constants {
    private Constants() { };
    public static final String RAW_CONFIG_FILE = "atom-client-feeds.json";
    public static final String PROPERTIES_FILE = "atom-client-defaults.properties";
    public static final String FETCH_CRON_PROPERTY = "atomclient.feed.cron";
    public static final String BASE_ATOMCLIENT_SUBJECT = "org.motechproject.atomclient";
    public static final String FETCH_MESSAGE = BASE_ATOMCLIENT_SUBJECT + ".fetch";
    public static final String READ_MESSAGE = BASE_ATOMCLIENT_SUBJECT + ".read";
    public static final String FEED_CHANGE_MESSAGE = BASE_ATOMCLIENT_SUBJECT + ".feedchange";
    public static final String RESCHEDULE_FETCH_JOB = BASE_ATOMCLIENT_SUBJECT + ".reschedulefetchjob";
}
