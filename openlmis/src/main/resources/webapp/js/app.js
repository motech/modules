(function () {
'use strict';

/* App Module */

angular.module('open-lmis', ['motech-dashboard', 'open-lmis.controllers', 'open-lmis.directives', 'open-lmis.services', 'ngCookies'])
    .config(['$routeProvider', function ($routeProvider) {

        $routeProvider.
            when('/open-lmis/settings', { 
            templateUrl: '../open-lmis/resources/partials/settings.html',
             controller: 'OpenLMISSettingsCtrl' })
    }]);
}());