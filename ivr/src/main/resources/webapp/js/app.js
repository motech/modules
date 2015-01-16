(function () {
    'use strict';

    angular.module('ivr', ['motech-dashboard', 'ivr.controllers', 'ngCookies', 'ui.bootstrap']).config(
    ['$routeProvider',
        function ($routeProvider) {
            $routeProvider.
                when('/ivr/templates', {templateUrl: '../ivr/resources/partials/templates.html', controller: 'IvrTemplatesCtrl'}).
                when('/ivr/settings',  {templateUrl: '../ivr/resources/partials/settings.html',  controller: 'IvrSettingsCtrl'});
    }]);
}());
