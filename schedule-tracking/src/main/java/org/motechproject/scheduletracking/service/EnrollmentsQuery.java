package org.motechproject.scheduletracking.service;

import org.joda.time.DateTime;
import org.motechproject.scheduletracking.domain.EnrollmentStatus;
import org.motechproject.scheduletracking.domain.WindowName;
import org.motechproject.scheduletracking.domain.search.CompletedDuringCriterion;
import org.motechproject.scheduletracking.domain.search.Criterion;
import org.motechproject.scheduletracking.domain.search.EndOfWindowCriterion;
import org.motechproject.scheduletracking.domain.search.ExternalIdCriterion;
import org.motechproject.scheduletracking.domain.search.InWindowCriterion;
import org.motechproject.scheduletracking.domain.search.MetadataCriterion;
import org.motechproject.scheduletracking.domain.search.MilestoneCriterion;
import org.motechproject.scheduletracking.domain.search.ScheduleCriterion;
import org.motechproject.scheduletracking.domain.search.StartOfWindowCriterion;
import org.motechproject.scheduletracking.domain.search.StatusCriterion;

import java.util.ArrayList;
import java.util.List;

import static java.util.Arrays.asList;

/**
 * This is the Query builder for retrieving enrollments, provides methods for different query criteria.
 * The order of criteria matters, as the first criterion is used to fetch the result from database
 * and the other criterion are used to filter the results(in the specified sequence) fetched by
 * the first criterion(in memory).
 */
public class EnrollmentsQuery {

    private List<Criterion> criteria = new ArrayList<Criterion>();

    /**
     * This adds the criteria using which enrollments are filtered based on the client external id.
     *
     * @param externalId the client external id
     * @return returns the instance with external id criteria added to the criteria list
     */
    public EnrollmentsQuery havingExternalId(String externalId) {
        criteria.add(new ExternalIdCriterion(externalId));
        return this;
    }

    /**
     * This adds the criteria using which enrollments are filtered based on their schedule name.
     *
     * @param scheduleNames the schedule names
     * @return returns the instance with schedule criteria added to the criteria list
     */
    public EnrollmentsQuery havingSchedule(String... scheduleNames) {
        criteria.add(new ScheduleCriterion(scheduleNames));
        return this;
    }

    /**
     * This adds the criteria using which enrollments are filtered based on their current milestone name.
     *
     * @param milestoneName the milestone name
     * @return returns the instance with current milestone criteria added to the criteria list
     */
    public EnrollmentsQuery havingCurrentMilestone(String milestoneName) {
        criteria.add(new MilestoneCriterion(milestoneName));
        return this;
    }

    /**
     * This adds the criteria using which enrollments are filtered based on their given window start datetime falls
     * during the given period. The start and end dates are inclusive.
     *
     * @param windowName the window name
     * @param start the start date
     * @param end the end date
     * @return returns the instance with window start criteria added to the criteria list
     */
    public EnrollmentsQuery havingWindowStartingDuring(WindowName windowName, DateTime start, DateTime end) {
        criteria.add(new StartOfWindowCriterion(windowName, start, end));
        return this;
    }

    /**
     * This adds the criteria using which enrollments are filtered based on their given window end datetime falls
     * during the given period. The start and end dates are inclusive.
     *
     * @param windowName the window name
     * @param start the start date
     * @param end the end date
     *
     * @return returns the instance with window end criteria added to the criteria list
     */
    public EnrollmentsQuery havingWindowEndingDuring(WindowName windowName, DateTime start, DateTime end) {
        criteria.add(new EndOfWindowCriterion(windowName, start, end));
        return this;
    }

    /**
     * This adds the criteria using which enrollments are filtered based on their current window(of current milestone)
     * is in the given window name list.
     *
     * @param windowNames the window names
     * @return returns the instance with currently in window criteria added to the criteria list
     */
    public EnrollmentsQuery currentlyInWindow(WindowName... windowNames) {
        criteria.add(new InWindowCriterion(asList(windowNames)));
        return this;
    }

    /**
     * This adds the criteria using which enrollments are filtered based on their status.
     *
     * @param enrollmentStatus the enrollment status
     * @return returns the instance with status criteria added to the criteria list
     */
    public EnrollmentsQuery havingState(EnrollmentStatus enrollmentStatus) {
        criteria.add(new StatusCriterion(enrollmentStatus));
        return this;
    }

    /**
     * This adds the criteria using which enrollments are filtered based on them being completed in the given date range.
     * The start and end dates are inclusive.
     *
     * @param start the start date
     * @param end the end date
     * @return returns the instance with completed during criteria added to the criteria list
     */
    public EnrollmentsQuery completedDuring(DateTime start, DateTime end) {
        criteria.add(new CompletedDuringCriterion(start, end));
        return this;
    }

    /**
     * This adds the criteria using which enrollments are filtered based on them having given metadata values in
     * their metadata map.
     *
     * @param key the metadata key
     * @param value the metadata value
     * @return returns the instance with metadata criteria added to the criteria list
     */
    public EnrollmentsQuery havingMetadata(String key, String value) {
        criteria.add(new MetadataCriterion(key, value));
        return this;
    }

    /**
     * This gives all the criterion which are present in the built query.
     *
     * @return the list of the criteria
     */
    public List<Criterion> getCriteria() {
        return criteria;
    }

    /**
     * This gives the primary criterion in the built query, which is used to fetch the results from database.
     *
     * @return the primary criteria
     */
    public Criterion getPrimaryCriterion() {
        return (criteria.size() > 0) ? criteria.get(0) : null;
    }

    /**
     * This gives all the criterion other than primary criterion in the built query, which are used to filter the
     * results of the primary criterion.
     *
     * @return the list of secondary criteria
     */
    public List<Criterion> getSecondaryCriteria() {
        return (criteria.size() > 1) ? criteria.subList(1, criteria.size()) : new ArrayList<Criterion>();
    }
}
