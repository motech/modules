(function () {
    'use strict';

    /* App Module */

    var smsModule = angular.module('sms', ['motech-dashboard', 'sms.controllers', 'ngCookies',
                'ui.bootstrap', 'ngSanitize', 'data-services', 'uiServices']), id;

    $.ajax({
        url: '../mds/entities/getEntity/SMS Module/SmsRecord',
        success:  function(data) {
            id = data.id;
        },
        async: false
    });

    getAvailableTabs('motech-sms', function(data) {
         smsModule.constant('AVAILABLE_TABS', data);
    });

    smsModule.run(function ($rootScope, AVAILABLE_TABS) {
        $rootScope.AVAILABLE_TABS = AVAILABLE_TABS;
    });

    smsModule.config(function ($routeProvider, AVAILABLE_TABS) {
        angular.forEach(AVAILABLE_TABS, function (tab) {
            if (tab === "log") {
                $routeProvider.when('/sms/{0}'.format(tab), {redirectTo: 'mds/dataBrowser/'+id+'/sms'});
            } else {
                $routeProvider.when(
                    '/sms/{0}'.format(tab),
                        {
                            templateUrl: '../sms/resources/partials/{0}.html'.format(tab),
                            controller: 'Sms{0}Ctrl'.format(tab.capitalize())
                        }
                );
            }
        });
    });
}());