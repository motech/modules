package org.motechproject.http.agent.domain;

/**
 * The <code>Credentials</code> class is used to represent user credentials,
 * when a http request requires an authentication
 */
public class Credentials {

    private String username;
    private String password;

    /**
     * Constructor.
     * @param username the username
     * @param password the user password
     */
    public Credentials(String username, String password) {
        this.username = username;
        this.password = password;
    }

    /**
     * @return the username
     */
    public String getUsername() {
        return username;
    }

    /**
     * @param username the username
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * @return the user password
     */
    public String getPassword() {
        return password;
    }

    /**
     * @param password the user password
     */
    public void setPassword(String password) {
        this.password = password;
    }
}

