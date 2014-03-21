(function () {
    'use strict';

    /* App Module */

    angular.module('sms', ['motech-dashboard', 'sms.controllers', 'sms.directives', 'ngCookies', 'ui.bootstrap', 'ngRoute', 'ngSanitize']).config(
    ['$routeProvider',
        function ($routeProvider) {
            $routeProvider.
                when('/sms/send', {templateUrl: '../sms/resources/partials/send.html', controller: 'SendController'}).
                when('/sms/log', {templateUrl: '../sms/resources/partials/log.html', controller: 'LogController'}).
                when('/sms/settings', {templateUrl: '../sms/resources/partials/settings.html', controller: 'SettingsController'});
    }]);
}());
