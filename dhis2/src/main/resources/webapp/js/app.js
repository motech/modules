(function () {
    'use strict';

    /* App Module */

    angular.module('dhis2', ['motech-dashboard', 'dhis2.controllers', 'dhis2.directives', 'dhis2.services', 'ngCookies', 'uiServices'])
        .config(['$stateProvider', function ($stateProvider) {
            $stateProvider
                .state('dhis2', {
                    url: "/dhis2",
                    abstract: true,
                    views: {
                        "moduleToLoad": {
                            templateUrl: "../dhis2/resources/index.html"
                        }
                    }
                })
                .state('dhis2.settings', {
                    url: '/settings',
                    parent: 'dhis2',
                    views: {
                        'dhis2View': {
                            templateUrl: '../dhis2/resources/partials/settings.html',
                            controller: 'Dhis2SettingsCtrl'
                        }
                    }
                })
                .state('dhis2.programs', {
                    url: '/programs',
                    parent: 'dhis2',
                    views: {
                        'dhis2View': {
                            templateUrl: '../dhis2/resources/partials/programs.html',
                            controller: 'Dhis2ProgramsCtrl'
                        }
                    }
                })
                .state('dhis2.trackedEntityAttributes', {
                    url: '/trackedEntityAttributes',
                    parent: 'dhis2',
                    views: {
                        'dhis2View': {
                            templateUrl: '../dhis2/resources/partials/trackedEntityAttributes.html',
                            controller: 'Dhis2TrackedEntityAttributesCtrl'
                        }
                    }
                })
                .state('dhis2.trackedEntities', {
                    url: '/trackedEntities',
                    parent: 'dhis2',
                    views: {
                        'dhis2View': {
                            templateUrl: '../dhis2/resources/partials/trackedEntities.html',
                            controller: 'Dhis2TrackedEntitiesCtrl'
                        }
                    }
                })
                .state('dhis2.orgUnits', {
                    url: '/orgUnits',
                    parent: 'dhis2',
                    views: {
                        'dhis2View': {
                            templateUrl: '../dhis2/resources/partials/orgUnits.html',
                            controller: 'Dhis2OrgUnitsCtrl'
                        }
                    }
                })
                .state('dhis2.dataElements', {
                    url: '/dataElements',
                    parent: 'dhis2',
                    views: {
                        'dhis2View': {
                            templateUrl: '../dhis2/resources/partials/dataElements.html',
                            controller: 'Dhis2DataElementsCtrl'
                        }
                    }
                });
        }]);
}());
