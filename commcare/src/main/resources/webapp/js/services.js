(function () {
    'use strict';

    /* Services */

    var services = angular.module('commcare.services', ['ngResource']);

    services.factory('Settings', function($resource) {
        return $resource('../commcare/settings');
    });

    services.factory('Schema', function($resource) {
        return $resource('../commcare/schema');
    });

    services.factory('Cases', function($resource) {
        return $resource('../commcare/caseList');
    });

    services.factory('Connection', function($resource) {
        return $resource('../commcare/connection/verify', {}, {
            verify: { method: 'POST' }
        });
    });

    services.factory('CommcarePermissions', function($resource) {
        return $resource('../commcare/connection/permissions');
    });

}());
