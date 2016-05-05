(function () {
    'use strict';

    /* App Module */

    var openmrs19 = angular.module('openmrs19', ['motech-dashboard', 'openmrs19.services', 'openmrs19.controllers',
                      'ngCookies', 'uiServices']);

    openmrs19.config(['$stateProvider', function ($stateProvider) {
        $stateProvider
            .state('openmrs19', {
                url: "/openmrs19",
                abstract: true,
                views: {
                    "moduleToLoad": {
                        templateUrl: "../openmrs19/resources/index.html"
                    }
                }
            })
            .state('openmrs19.settings', {
                url: "/settings",
                parent:'openmrs19',
                views: {
                    "openmrs19View": {
                        templateUrl: "../openmrs19/resources/partials/settings.html",
                        controller: 'OpenMRSSettingsCtrl'
                    }
                }
            });
    }]);
}());

