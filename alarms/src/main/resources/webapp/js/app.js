(function () {
    'use strict';

    /* App Module */

    angular.module('alarms', ['motech-dashboard', 'alarms.controllers', 'ngCookies', 'ui.bootstrap']).config(
    ['$routeProvider',
        function ($routeProvider) {
            $routeProvider.
                when('/alarms/alarms', {templateUrl: '../alarms/resources/partials/alarms.html', controller: 'AlarmsCtrl'}).
                when('/alarms/settings', {templateUrl: '../alarms/resources/partials/settings.html', controller: 'AlarmsSettingsCtrl'});
    }]);
}());
