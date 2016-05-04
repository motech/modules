(function () {
    'use strict';

    /* App Module */

    angular.module('openmrs19', ['motech-dashboard', 'openmrs19.services', 'openmrs19.controllers', 'ngCookies']).config(['$routeProvider',
        function ($routeProvider) {
            $routeProvider.
                when('/openmrs19/settings', {templateUrl: '../openmrs19/resources/partials/settings.html', controller: 'OpenMRSSettingsCtrl' });
    }]);
}());
