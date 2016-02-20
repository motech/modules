package org.motechproject.atomclient.service;

public class FeedConfig {
    private String url;
    private String regex;

    public FeedConfig() {
        this("", "");
    }

    public FeedConfig(String url, String regex) {
        this.url = url;
        this.regex = regex;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getRegex() {
        return regex;
    }

    public void setRegex(String regex) {
        this.regex = regex;
    }
}
