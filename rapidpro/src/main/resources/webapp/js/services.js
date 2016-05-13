(function () {
    'use strict';

    /* Services */

    var services = angular.module('rapidpro.services', ['ngResource']);

    services.factory('Settings', function ($resource) {
        return $resource('../rapidpro/settings');
    });
}());
