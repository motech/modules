(function () {
    'use strict';

    /* App Module */

    angular.module('odk', ['motech-dashboard', 'odk.controllers', 'odk.directives', 'odk.services', 'ngCookies'])
        .config(['$stateProvider', function ($stateProvider) {
            $stateProvider
            .state('odk', {
                url: "/odk",
                abstract: true,
                views: {
                    'moduleToLoad': {
                        templateUrl: '../odk/resources/index.html'
                    }
                }
            })
            .state('odk.settings', {
                url: '/settings',
                parent: 'odk',
                views: {
                    'odkView': {
                        templateUrl: '../odk/resources/partials/settings.html',
                        controller: 'SettingsCtrl'
                    }
                }
            })
            .state('odk.forms', {
                url: '/forms',
                parent: 'odk',
                views: {
                    'odkView': {
                        templateUrl: '../odk/resources/partials/forms.html',
                        controller: 'FormsCtrl'
                    }
                }
            });
        }]);
}());
