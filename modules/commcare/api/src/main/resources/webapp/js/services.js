(function () {
    'use strict';

    /* Services */

    angular.module('settingsServices', ['ngResource']).factory('Settings', function($resource) {
        return $resource('../commcare/settings');
    });

    angular.module('modulesServices', ['ngResource']).factory('Schema', function($resource) {
        return $resource('../commcare/schema');
    });

    angular.module('casesServices', ['ngResource']).factory('Cases', function($resource) {
        return $resource('../commcare/caseList');
    });

    angular.module('connectionService', ['ngResource']).factory('Connection', function($resource) {
        return $resource('../commcare/connection/verify', {}, {
            verify: { method: 'POST' }
        });
    });

    angular.module('permissionsServices', ['ngResource']).factory('Permissions', function($resource) {
        return $resource('../commcare/connection/permissions');
    });

}());
