(function () {
    'use strict';

    /* App Module */

    angular.module('dhis2', ['motech-dashboard', 'dhis2.controllers', 'dhis2.directives', 'dhis2.services', 'ngCookies'])
        .config(['$routeProvider',
            function ($routeProvider) {
                $routeProvider.
                    when('/dhis2/settings', {
                        templateUrl: '../dhis2/resources/partials/settings.html',
                        controller: 'Dhis2SettingsCtrl'
                    });

            }]);
}());
