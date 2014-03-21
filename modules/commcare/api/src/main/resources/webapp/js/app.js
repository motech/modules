(function () {
    'use strict';

    /* App Module */

    angular.module('commcare', ['motech-dashboard', 'commcare.services', 'commcare.controllers', 'commcare.directives', 'ngCookies', 'ngRoute']).config(['$routeProvider',
        function ($routeProvider) {
            $routeProvider.
                when('/commcare/settings', {templateUrl: '../commcare/resources/partials/settings.html', controller: 'SettingsCtrl' }).
                when('/commcare/forms', {templateUrl: '../commcare/resources/partials/forms.html', controller: 'ModulesCtrl' }).
                when('/commcare/cases', {templateUrl: '../commcare/resources/partials/cases.html', controller: 'CaseSchemasCtrl' });
    }]);
}());
