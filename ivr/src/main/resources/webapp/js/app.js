(function () {
    'use strict';

    var ivrmodule = angular.module('ivr', ['motech-dashboard', 'ivr.controllers', 'ngCookies', 'ui.bootstrap', 'mds']), id;

    $.ajax({
        url: '../mds/entities/getEntity/IVR Module/CallDetailRecord',
        success:  function(data) {
            id = data.id;
        },
        async: false
    });

    ivrmodule.config(
    ['$routeProvider',
        function ($routeProvider) {
            $routeProvider.
                when('/ivr/templates', {templateUrl: '../ivr/resources/partials/templates.html', controller: 'IvrTemplatesCtrl'}).
                when('/ivr/settings', {templateUrl: '../ivr/resources/partials/settings.html',  controller: 'IvrSettingsCtrl'}).
                when('/ivr/logs', {redirectTo: 'mds/dataBrowser/'+id});

    }]);
}());