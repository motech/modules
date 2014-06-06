(function () {
    'use strict';

    /* App Module */

    angular.module('cmslite', ['motech-dashboard', 'cmslite.services', 'cmslite.directives', 'cmslite.controllers',
                                    'ngCookies', 'ngRoute', 'motech-widgets']).config(['$routeProvider',
        function ($routeProvider) {
            $routeProvider.
                when('/cmslite/resources', {templateUrl: '../cmsliteapi/resources/partials/resources.html', controller: 'ResourceCtrl'});
        }]);
}());
