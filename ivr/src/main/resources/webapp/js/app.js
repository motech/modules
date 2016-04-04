(function () {
    'use strict';

    var ivrmodule = angular.module('ivr', ['motech-dashboard', 'ivr.controllers', 'ngCookies', 'ui.bootstrap', 'data-services', 'uiServices']), id;

    $.ajax({
        url: '../mds/entities/getEntity/IVR Module/CallDetailRecord',
        success:  function(data) {
            id = data.id;
        },
        async: false
    });

    getAvailableTabs('motech-ivr', function(data) {
         ivrmodule.constant('IVR_AVAILABLE_TABS', data);
    });

    ivrmodule.run(function ($rootScope, IVR_AVAILABLE_TABS) {
        $rootScope.IVR_AVAILABLE_TABS = IVR_AVAILABLE_TABS;
    });

    ivrmodule.config(function ($routeProvider, IVR_AVAILABLE_TABS) {

        var i, tab;

        for (i = 0; i < IVR_AVAILABLE_TABS.length; i = i + 1) {

            tab = IVR_AVAILABLE_TABS[i];

            if (tab === "log") {
                $routeProvider.when('/ivr/{0}'.format(tab), {redirectTo: 'mds/dataBrowser/'+id+'/ivr'});
            } else {
                $routeProvider.when('/ivr/{0}'.format(tab),
                    {
                        templateUrl: '../ivr/resources/partials/{0}.html'.format(tab),
                        controller: 'Ivr{0}Ctrl'.format(tab.capitalize())
                    }
                );
            }
        }
    });
}());
