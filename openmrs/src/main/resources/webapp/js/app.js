(function () {
    'use strict';

    /* App Module */

    var openmrs = angular.module('openmrs', ['motech-dashboard', 'openmrs.services', 'openmrs.controllers',
                      'ngCookies', 'uiServices']);

    openmrs.config(['$stateProvider', function ($stateProvider) {
        $stateProvider
            .state('openmrs', {
                url: "/openmrs",
                abstract: true,
                views: {
                    "moduleToLoad": {
                        templateUrl: "../openmrs/resources/index.html"
                    }
                }
            })
            .state('openmrs.settings', {
                url: "/settings",
                parent:'openmrs',
                views: {
                    "openmrsView": {
                        templateUrl: "../openmrs/resources/partials/settings.html",
                        controller: 'OpenMRSSettingsCtrl'
                    }
                }
            });
    }]);
}());

