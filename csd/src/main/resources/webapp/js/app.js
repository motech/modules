(function () {
    'use strict';

    /* App Module */

    angular.module('csd', ['motech-dashboard', 'csd.controllers', 'csd.directives', 'ngCookies', 'ui.bootstrap', 'uiServices']).config(
    ['$routeProvider',
        function ($routeProvider) {
            $routeProvider.
                when('/csd/csd', {templateUrl: '../csd/resources/partials/csd.html', controller: 'CsdCtrl'}).
                when('/csd/settings', {templateUrl: '../csd/resources/partials/settings.html', controller: 'CsdSettingsCtrl'});
    }]);
}());
