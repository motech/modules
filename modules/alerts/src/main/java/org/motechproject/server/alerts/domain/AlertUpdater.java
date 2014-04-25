package org.motechproject.server.alerts.domain;

import java.util.HashMap;
import java.util.Map;

public enum AlertUpdater {

    STATUS_UPDATER {
        @Override
        public Alert update(Alert alert, Object newValue) {
            alert.setStatus((AlertStatus) newValue);
            return alert;
        }
    }, NAME_UPDATER {
        @Override
        public Alert update(Alert alert, Object newValue) {
            alert.setName((String) newValue);
            return alert;
        }
    }, DESCRIPTION_UPDATER {
        @Override
        public Alert update(Alert alert, Object newValue) {
            alert.setDescription((String) newValue);
            return alert;
        }
    }, PRIORITY_UPDATER {
        @Override
        public Alert update(Alert alert, Object newValue) {
            alert.setPriority(((Number) newValue).longValue());
            return alert;
        }
    }, DATA_UPDATER {
        @Override
        public Alert update(Alert alert, Object newValue) {
            alert.getData().putAll((Map<String, String>) newValue);
            return alert;
        }
    };

    public abstract Alert update(Alert alert, Object newValue);

    private static Map<UpdateCriterion, AlertUpdater> updateCriterionMap = new HashMap<UpdateCriterion, AlertUpdater>();

    static {
        updateCriterionMap.put(UpdateCriterion.STATUS, AlertUpdater.STATUS_UPDATER);
        updateCriterionMap.put(UpdateCriterion.NAME, AlertUpdater.NAME_UPDATER);
        updateCriterionMap.put(UpdateCriterion.DESCRIPTION, AlertUpdater.DESCRIPTION_UPDATER);
        updateCriterionMap.put(UpdateCriterion.PRIORITY, AlertUpdater.PRIORITY_UPDATER);
        updateCriterionMap.put(UpdateCriterion.DATA, AlertUpdater.DATA_UPDATER);
    }

    public static AlertUpdater get(UpdateCriterion updateCriterion) {
        return updateCriterionMap.get(updateCriterion);
    }
}
