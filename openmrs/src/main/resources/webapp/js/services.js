(function () {
    'use strict';

    /* Services */

    var services = angular.module('openmrs.services', ['ngResource']);

    services.factory('OpenMRSConfig', function($resource) {
        return $resource('../openmrs/configs', {}, {
            verify: {
                method: 'POST',
                url: '../openmrs/configs/verify'
            }}
        );
    });
}());