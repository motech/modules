(function () {
    'use strict';

    var metrics = angular.module('metrics', ['motech-dashboard', 'metrics.controllers', 'metrics.services', 'ngCookies']);

    metrics.config(['$stateProvider', function ($stateProvider) {
        $stateProvider
            .state('metrics', {
                url: '/metrics',
                abstract: true,
                views: {
                    "moduleToLoad": {
                        templateUrl: '../metrics/resources/index.html'
                    }
                }
            })
            .state('metrics.dashboard', {
                url: '/dashboard',
                parent: 'metrics',
                views: {
                    'metricsView': {
                        templateUrl: '../metrics/resources/partials/metrics.html',
                        controller: 'MetricsCtrl'
                    }
                }
            })
            .state('metrics.settings', {
                url: '/settings',
                parent: 'metrics',
                views: {
                    'metricsView': {
                        templateUrl: '../metrics/resources/partials/settings.html',
                        controller: 'MetricsConfigCtrl'
                    }
                }
            });
    }]);
}());
