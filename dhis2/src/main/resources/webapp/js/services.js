(function () {
    'use strict';

    /* Services */

    var services = angular.module('dhis2.services', ['ngResource']);

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
