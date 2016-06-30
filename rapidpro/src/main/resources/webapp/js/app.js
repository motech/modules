(function () {
    'use strict';

    /* App Module */

    angular.module('rapidpro', ['motech-dashboard', 'rapidpro.controllers', 'rapidpro.directives', 'rapidpro.services', 'ngCookies'])
        .config(['$stateProvider',
            function ($stateProvider) {
                $stateProvider
                    .state('rapidpro', {
                        url: "/rapidpro",
                        abstract: true,
                        views: {
                            'moduleToLoad': {
                                templateUrl: '../rapidpro/resources/index.html'
                            }
                        }
                    })
                    .state('rapidpro.settings', {
                        url: '/settings',
                        parent: 'rapidpro',
                        views: {
                            'rapidproView': {
                                templateUrl: '../rapidpro/resources/partials/settings.html',
                                controller: 'SettingsCtrl'
                            }
                        }
                    })
            }]);
}());
