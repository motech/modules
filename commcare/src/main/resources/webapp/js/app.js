(function () {
    'use strict';

    /* App Module */

    var commcare = angular.module('commcare', ['motech-dashboard', 'commcare.services', 'commcare.controllers',
                    'commcare.directives', 'commcare.filters', 'ngCookies', 'uiServices']);

    commcare.config(['$stateProvider', function ($stateProvider) {
        $stateProvider
            .state('commcare', {
                url: "/commcare",
                abstract: true,
                views: {
                    "moduleToLoad": {
                        templateUrl: "../commcare/resources/index.html"
                    }
                }
            })
            .state('commcare.settings', {
                url: '/settings',
                parent: 'commcare',
                views: {
                    'commcareView': {
                        templateUrl: '../commcare/resources/partials/settings.html',
                        controller: 'CommcareSettingsCtrl'
                    }
                }
            })
            .state('commcare.importForms', {
                url: '/importForms',
                parent: 'commcare',
                views: {
                    'commcareView': {
                        templateUrl: '../commcare/resources/partials/importForms.html',
                        controller: 'CommcareImportFormsCtrl'
                    }
                }
            })
            .state('commcare.importCases', {
                url: '/importCases',
                parent: 'commcare',
                views: {
                    'commcareView': {
                        templateUrl: '../commcare/resources/partials/importCases.html',
                        controller: 'CommcareImportCasesCtrl'
                    }
                }
            })
            .state('commcare.forms', {
                url: '/forms',
                parent: 'commcare',
                views: {
                    'commcareView': {
                        templateUrl: '../commcare/resources/partials/forms.html',
                        controller: 'CommcareModulesCtrl'
                    }
                }
            })
            .state('commcare.cases', {
                url: '/cases',
                parent: 'commcare',
                views: {
                    'commcareView': {
                        templateUrl: '../commcare/resources/partials/cases.html',
                        controller: 'CommcareCaseSchemasCtrl'
                    }
                }
            })
            .state('commcare.reports', {
                url: '/reports',
                parent: 'commcare',
                views: {
                    'commcareView': {
                        templateUrl: '../commcare/resources/partials/reports.html',
                        controller: 'CommcareReportsCtrl'
                    }
                }
            });
    }]);
}());
