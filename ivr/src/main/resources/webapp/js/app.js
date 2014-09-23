(function () {
    'use strict';

    angular.module('ivr', ['motech-dashboard', 'ivr.controllers', 'ivr.directives', 'ngCookies', 'ui.bootstrap']).config(
    ['$routeProvider',
        function ($routeProvider) {
            $routeProvider.
                when('/ivr', {templateUrl: '../ivr/resources/partials/settings.html',
                    controller: 'SettingsController'}).
                when('/ivr/settings', {templateUrl: '../ivr/resources/partials/settings.html',
                controller: 'SettingsController'});
    }]);
}());
