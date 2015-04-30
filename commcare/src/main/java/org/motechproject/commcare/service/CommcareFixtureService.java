package org.motechproject.commcare.service;

import org.motechproject.commcare.domain.CommcareFixture;

import java.util.List;

/**
 * A service to perform queries against CommCareHQ's fixture APIs.
 */
public interface CommcareFixtureService {

    /**
     * Queries CommCareHQ for a list of fixtures(located on the given page) on the configured domain.
     *
     * @param pageSize  the size of the page
     * @param pageNumber  the number of the page
     * @param configName  the name of the configuration used for connecting to CommcareHQ, null means default configuration
     * @return  the list of CommcareFixture that represent the information about each fixture located on the given page
     *          from CommCareHQ
     */
    List<CommcareFixture> getFixtures(Integer pageSize, Integer pageNumber, String configName);

    /**
     * Same as {@link #getFixtures(Integer, Integer, String) getFixtures} but uses default configuration.
     */
    List<CommcareFixture> getFixtures(Integer pageSize, Integer pageNumber);

    /**
     * Queries CommCareHQ for a specific fixture
     *
     * @param id  the id of the fixture to retrieve.
     * @param configName  the name of the configuration used for connecting to CommcareHQ, null means default configuration
     * @return  the CommcareFixture object representing the information about the fixture from CommCareHQ, or null if
     *          that fixture did not exist on that domain.
     */
    CommcareFixture getCommcareFixtureById(String id, String configName);

    /**
     * Same as {@link #getCommcareFixtureById(String, String) getCommcareFixtureById} but uses default configuration.
     */
    CommcareFixture getCommcareFixtureById(String id);
}
