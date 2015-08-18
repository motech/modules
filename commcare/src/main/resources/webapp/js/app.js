(function () {
    'use strict';

    /* App Module */

    angular.module('commcare', ['motech-dashboard', 'commcare.services', 'commcare.controllers', 'commcare.directives', 'ngCookies']).config(['$routeProvider',
        function ($routeProvider) {
            $routeProvider.
                when('/commcare/settings', {templateUrl: '../commcare/resources/partials/settings.html', controller: 'CommcareSettingsCtrl' }).
                when('/commcare/import', {templateUrl: '../commcare/resources/partials/importForms.html', controller: 'CommcareImportFormsCtrl' }).
                when('/commcare/forms', {templateUrl: '../commcare/resources/partials/forms.html', controller: 'CommcareModulesCtrl' }).
                when('/commcare/cases', {templateUrl: '../commcare/resources/partials/cases.html', controller: 'CommcareCaseSchemasCtrl' });
    }]);
}());
