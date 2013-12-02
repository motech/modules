(function () {
    'use strict';

    /* App Module */

    angular.module('motech-sms', ['motech-dashboard', 'ngCookies', 'ui.bootstrap', 'ngRoute', 'ngSanitize']).config(
    ['$routeProvider',
        function ($routeProvider) {
            $routeProvider.
                when('/send', {templateUrl: '../sms/resources/partials/send.html', controller: 'SendController'}).
                when('/log', {templateUrl: '../sms/resources/partials/log.html', controller: 'LogController'}).
                when('/settings', {templateUrl: '../sms/resources/partials/settings.html',
                        controller: 'SettingsController'}).
                otherwise({redirectTo: '/send'});
    }]);
}());
