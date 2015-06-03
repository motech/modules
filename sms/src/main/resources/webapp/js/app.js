(function () {
    'use strict';

    /* App Module */

    var smsModule = angular.module('sms', ['motech-dashboard', 'sms.controllers', 'sms.directives', 'ngCookies', 'ui.bootstrap', 'ngSanitize']);

    $.ajax({
        url:      '../sms/available/smsTabs',
        success:  function(data) {
            smsModule.constant('AVAILABLE_TABS', data);
        },
        async:    false
    });

    smsModule.run(function ($rootScope, AVAILABLE_TABS) {
        $rootScope.AVAILABLE_TABS = AVAILABLE_TABS;
    });

    smsModule.config(function ($routeProvider, AVAILABLE_TABS) {
        angular.forEach(AVAILABLE_TABS, function (tab) {
            $routeProvider.when(
                '/sms/{0}'.format(tab),
                {
                    templateUrl: '../sms/resources/partials/{0}.html'.format(tab),
                    controller: 'Sms{0}Ctrl'.format(tab.capitalize())
                }
            );
        });
    });
}());