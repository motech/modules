(function () {
    'use strict';

    /* App Module */

    angular.module('odk', ['motech-dashboard', 'odk.controllers', 'odk.directives', 'odk.services', 'ngCookies'])
        .config(['$routeProvider',
            function ($routeProvider) {
                $routeProvider.
                    when('/odk/settings', {
                        templateUrl: '../odk/resources/partials/settings.html',
                        controller: 'SettingsCtrl'
                    }).
                    when('/odk/forms', {
                        templateUrl: '../odk/resources/partials/forms.html',
                        controller: 'FormsCtrl'
                    });
            }]);
}());
