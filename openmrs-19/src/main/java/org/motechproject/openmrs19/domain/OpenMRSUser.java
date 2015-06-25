package org.motechproject.openmrs19.domain;

import org.apache.commons.lang.ObjectUtils;

/**
 * Represents a single OpenMRS user. It's a part of the MOTECH model.
 */
public class OpenMRSUser {

    private String id;
    private String systemId;
    private String securityRole;
    private String userName;
    private OpenMRSPerson person;

    /**
     * Sets the ID of this user to {@code id}.
     *
     * @param id  the ID to be set
     * @return the reference to this object
     */
    @Deprecated
    public OpenMRSUser id(String id) {
        this.id = id;
        return this;
    }

    /**
     * Sets the name of this user to {@code userName}.
     *
     * @param userName  the user name to be set
     * @return the reference to this object
     */
    @Deprecated
    public OpenMRSUser userName(String userName) {
        this.userName = userName;
        return this;
    }

    /**
     * Sets the security role of this user to {@code securityRole}.
     *
     * @param securityRole  the security role to be set
     * @return the reference to this object
     */
    @Deprecated
    public OpenMRSUser securityRole(String securityRole) {
        this.securityRole = securityRole;
        return this;
    }

    /**
     * Sets the system ID of this user to {@code systemId}.
     *
     * @param systemId  the system ID to be set
     * @return the reference to this object
     */
    @Deprecated
    public OpenMRSUser systemId(String systemId) {
        this.systemId = systemId;
        return this;
    }

    /**
     * Sets the person of this user to {@code person}.
     *
     * @param person  the person to be set
     * @return the reference to this object
     */
    @Deprecated
    public OpenMRSUser person(OpenMRSPerson person) {
        this.person = person;
        return this;
    }

    public String getUserName() {
        return userName;
    }

    public String getSecurityRole() {
        return securityRole;
    }

    public String getSystemId() {
        return systemId;
    }

    public OpenMRSPerson getPerson() {
        return person;
    }

    public String getUserId() {
        return id;
    }

    public void setUserId(String userId) {
        this.id = userId;
    }

    public void setSystemId(String systemId) {
        this.systemId = systemId;
    }

    public void setSecurityRole(String securityRole) {
        this.securityRole = securityRole;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setPerson(OpenMRSPerson person) {
        this.person = person;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof OpenMRSUser)) {
            return false;
        }
        OpenMRSUser other = (OpenMRSUser) o;
        if (!ObjectUtils.equals(id, other.id)) {
            return false;
        }
        if (!ObjectUtils.equals(systemId, other.systemId)) {
            return false;
        }
        if (!ObjectUtils.equals(securityRole, other.securityRole)) {
            return false;
        }
        if (!ObjectUtils.equals(userName, other.userName)) {
            return false;
        }
        if (!ObjectUtils.equals(person, other.person)) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 1;
        hash = hash * 31 + ObjectUtils.hashCode(id);
        hash = hash * 31 + ObjectUtils.hashCode(systemId);
        hash = hash * 31 + ObjectUtils.hashCode(securityRole);
        hash = hash * 31 + ObjectUtils.hashCode(userName);
        hash = hash * 31 + ObjectUtils.hashCode(person);

        return hash;
    }
}
