package org.motechproject.atomclient.service;


/**
 * Represents the information needed for the module to fetch an atom feed (the URL) and extract a specific part of the
 * feed content (the regex)
 */
public class FeedConfig {

    /**
     * The atom feed URL
     */
    private String url;

    /**
     * The regex expression used to extract specific parts of the feed content, see <a href="http://stackoverflow.com/questions/237061/using-regular-expressions-to-extract-a-value-in-java">stackoverflow sample</a> and {@link java.util.regex.Matcher}
     */
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
