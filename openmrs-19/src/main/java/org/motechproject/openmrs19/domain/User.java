package org.motechproject.openmrs19.domain;

import com.google.gson.annotations.Expose;

import java.util.List;
import java.util.Objects;

/**
 * Represents a single OpenMRS user.
 */
public class User {

    private String uuid;

    @Expose
    private String username;
    @Expose
    private String password;
    @Expose
    private Person person;
    @Expose
    private String systemId;
    @Expose
    private List<Role> roles;

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Person getPerson() {
        return person;
    }

    public void setPerson(Person person) {
        this.person = person;
    }

    public String getSystemId() {
        return systemId;
    }

    public void setSystemId(String systemId) {
        this.systemId = systemId;
    }

    public List<Role> getRoles() {
        return roles;
    }

    public void setRoles(List<Role> roles) {
        this.roles = roles;
    }

    @Override
    public int hashCode() {
        return Objects.hash(uuid, username, password, person, systemId, roles);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }

        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }

        User other = (User) obj;

        return Objects.equals(this.uuid, other.uuid) && Objects.equals(this.username, other.username) && Objects.equals(this.password, other.password) &&
                Objects.equals(this.person, other.person) && Objects.equals(this.systemId, other.systemId) && Objects.equals(this.roles, other.roles);
    }
}
