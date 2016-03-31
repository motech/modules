(function () {
    'use strict';

    /* App Module */

    var csd = angular.module('csd', ['motech-dashboard', 'csd.controllers', 'csd.directives', 'ngCookies', 'ui.bootstrap']);

        csd.config(['$stateProvider', function ($stateProvider) {
            $stateProvider
                .state('csd', {
                    url: "/csd",
                    abstract: true,
                    views: {
                        "moduleToLoad": {
                            templateUrl: "../csd/resources/index.html"
                        }
                    }
                })
                .state('csd.dashboard', {
                    url: '/dashboard',
                    parent: 'csd',
                    views: {
                        'csdView': {
                            templateUrl: '../csd/resources/partials/csd.html',
                            controller: 'CsdCtrl'
                        }
                    }
                })
                .state('csd.settings', {
                    url: '/settings',
                    parent: 'csd',
                    views: {
                        'csdView': {
                            templateUrl: '../csd/resources/partials/settings.html',
                            controller: 'CsdSettingsCtrl'
                        }
                    }
                });
        }]);
}());
