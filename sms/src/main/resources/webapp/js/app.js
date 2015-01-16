(function () {
    'use strict';

    /* App Module */

    angular.module('sms', ['motech-dashboard', 'sms.controllers', 'sms.directives', 'ngCookies', 'ui.bootstrap', 'ngSanitize']).config(
    ['$routeProvider',
        function ($routeProvider) {
            $routeProvider.
                when('/sms/send', {templateUrl: '../sms/resources/partials/send.html', controller: 'SmsSendCtrl'}).
                when('/sms/log', {templateUrl: '../sms/resources/partials/log.html', controller: 'SmsLogCtrl'}).
                when('/sms/settings', {templateUrl: '../sms/resources/partials/settings.html', controller: 'SmsSettingsCtrl'});
    }]);
}());
