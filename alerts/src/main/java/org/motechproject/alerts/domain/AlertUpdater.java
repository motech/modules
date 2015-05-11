package org.motechproject.alerts.domain;

import java.util.HashMap;
import java.util.Map;

/**
 * An enum that represents an update action on alert.
 * Each enum value corresponds to an alert field that will be updated
 * using the {@link #update(Alert, Object)} method.
 */
public enum AlertUpdater {

    /**
     * Updates the status of the alert.
     */
    STATUS_UPDATER {
        @Override
        public Alert update(Alert alert, Object newValue) {
            alert.setStatus((AlertStatus) newValue);
            return alert;
        }
    },
    /**
     * Updates the name of the alert.
     */
    NAME_UPDATER {
        @Override
        public Alert update(Alert alert, Object newValue) {
            alert.setName((String) newValue);
            return alert;
        }
    },
    /**
     * Updates the description of the alert.
     */
    DESCRIPTION_UPDATER {
        @Override
        public Alert update(Alert alert, Object newValue) {
            alert.setDescription((String) newValue);
            return alert;
        }
    },
    /**
     * Updates the priority of the alert.
     */
    PRIORITY_UPDATER {
        @Override
        public Alert update(Alert alert, Object newValue) {
            alert.setPriority(((Number) newValue).longValue());
            return alert;
        }
    },
    /**
     * Updates the data of the alert, by putting all the new data in the map.
     */
    DATA_UPDATER {
        @Override
        public Alert update(Alert alert, Object newValue) {
            alert.getData().putAll((Map<String, String>) newValue);
            return alert;
        }
    };

    /**
     * Updates the field of the alert with the provided new value. Each enum value
     * updates a different a field. This method only changes the provided alert instance,
     * it does not execute any database operations on itself.
     * @param alert the alert to update
     * @param newValue the new value of the field
     * @return the updated alert
     */
    public abstract Alert update(Alert alert, Object newValue);

    private static Map<UpdateCriterion, AlertUpdater> updateCriterionMap = new HashMap<UpdateCriterion, AlertUpdater>();

    static {
        updateCriterionMap.put(UpdateCriterion.STATUS, AlertUpdater.STATUS_UPDATER);
        updateCriterionMap.put(UpdateCriterion.NAME, AlertUpdater.NAME_UPDATER);
        updateCriterionMap.put(UpdateCriterion.DESCRIPTION, AlertUpdater.DESCRIPTION_UPDATER);
        updateCriterionMap.put(UpdateCriterion.PRIORITY, AlertUpdater.PRIORITY_UPDATER);
        updateCriterionMap.put(UpdateCriterion.DATA, AlertUpdater.DATA_UPDATER);
    }

    /**
     * Retrieves an updater instance that corresponds to the provided {@link org.motechproject.alerts.domain.UpdateCriterion}.
     * @param updateCriterion the criterion to fetch the updater for
     * @return the updater for the criterion
     */
    public static AlertUpdater get(UpdateCriterion updateCriterion) {
        return updateCriterionMap.get(updateCriterion);
    }
}
