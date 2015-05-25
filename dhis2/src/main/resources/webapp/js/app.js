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
                    }).
                    when('/dhis2/programs', {
                        templateUrl: '../dhis2/resources/partials/programs.html',
                        controller: 'Dhis2ProgramsCtrl'
                    }).
                when('/dhis2/trackedEntityAttributes', {
                    templateUrl: '../dhis2/resources/partials/trackedEntityAttributes.html',
                    controller: 'Dhis2TrackedEntityAttributesCtrl'
                }).
                when('/dhis2/trackedEntities', {
                    templateUrl: '../dhis2/resources/partials/trackedEntities.html',
                    controller: 'Dhis2TrackedEntitiesCtrl'
                }).
                when('/dhis2/orgUnits', {
                    templateUrl: '../dhis2/resources/partials/orgUnits.html',
                    controller: 'Dhis2OrgUnitsCtrl'
                }).
                when('/dhis2/dataElements', {
                    templateUrl: '../dhis2/resources/partials/dataElements.html',
                    controller: 'Dhis2DataElementsCtrl'
                })



            }]);
}());
