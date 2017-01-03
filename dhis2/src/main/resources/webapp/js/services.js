(function () {
    'use strict';

    /* Services */

    var services = angular.module('dhis2.services', ['ngResource']);

    services.service('Sync', syncService);
    syncService.$inject = ['$http'];

    function syncService($http) {
        var syncing = false;
        var lastSuccessful = null;

        this.sync = sync;
        this.isSyncing = isSyncing;
        this.isLastSuccessful = isLastSuccessful;

        function sync() {
            if (!syncing) {
                syncing = true;

                $http.get('../dhis2/sync').then(function(successful) {
                    lastSuccessful = successful.data;
                }, function() {
                    lastSuccessful = false;
                }).finally(function() {
                    syncing = false;
                })
            }
        }

        function isSyncing() {
            return syncing;
        }

        function isLastSuccessful() {
            return lastSuccessful;
        }
    }

    services.factory('Programs', function ($resource) {
        return $resource('../dhis2/programs');
    });

    services.factory('TrackedEntityAttributes', function($resource) {
        return $resource('../dhis2/trackedEntityAttributes');
    });

    services.factory('TrackedEntities', function($resource) {
        return $resource('../dhis2/trackedEntities');
    });

    services.factory('OrgUnits', function($resource) {
        return $resource('../dhis2/orgUnits');
    });
    services.factory('DataElements', function($resource) {
        return $resource('../dhis2/dataElements');
    });


}());
