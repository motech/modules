(function () {
    'use strict';

    /* App Module */

    angular.module('ivr', ['motech-dashboard', 'motech-widgets', 'ivr.services', 'ivr.directives', 'ivr.controllers',
        'ngCookies', 'ngRoute']).config(['$routeProvider',
        function ($routeProvider) {
            $routeProvider.
                when('/ivr/test-call', {templateUrl:'../ivr/partials/test-call.html'}).
                when('/ivr/call-logs', { templateUrl:'../ivr/partials/call-logs.html' });
        }]);
}());
