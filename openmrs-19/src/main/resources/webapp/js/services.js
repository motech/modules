(function () {
    'use strict';

    /* Services */

    var services = angular.module('openmrs19.services', ['ngResource']);

    services.factory('OpenMRSConfig', function($resource) {
        return $resource('../openmrs19/configs', {}, {});
    });
}());