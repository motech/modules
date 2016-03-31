(function () {
    'use strict';

    /* App Module */

    var smsModule = angular.module('sms', ['motech-dashboard', 'sms.controllers', 'ngCookies', 'ui.bootstrap', 'ngSanitize',
                                   'data-services']), id;

    $.ajax({
        url: '../mds/entities/getEntity/SMS Module/SmsRecord',
        success:  function(data) {
            id = data.id;
        },
        async: false
    });

    getAvailableTabs('motech-sms', function(data) {
         smsModule.constant('SMS_AVAILABLE_TABS', data);
    });

    smsModule.run(function ($rootScope, SMS_AVAILABLE_TABS) {
        $rootScope.SMS_AVAILABLE_TABS = SMS_AVAILABLE_TABS;
    });

    smsModule.config(function ($stateProvider, SMS_AVAILABLE_TABS) {
        var setTabsState, setLogState;

        $stateProvider.state('sms', {
            url: "/sms",
            abstract: true,
            views: {
                "moduleToLoad": {
                    templateUrl: "../sms/resources/index.html"
                }
            },
            resolve: {
                loadMyService: ['$ocLazyLoad', function($ocLazyLoad) {
                    return $ocLazyLoad.load('data-services');
                }]
            }
        });

        setLogState = function () {
            $stateProvider.state('sms.log', {
                url: '/log/mds/dataBrowser/:entityId/:moduleName',
                parent: 'sms',
                views: {
                    'mdsEmbeddedView': {
                        templateUrl: '../mds/resources/partials/dataBrowser.html',
                        controller: 'MdsDataBrowserCtrl'
                    }
                },
                resolve:{
                    entityId: ['$stateParams', function ($stateParams) {
                        $stateParams.entityId = id;
                        return id;
                    }],
                    moduleName: ['$stateParams', function ($stateParams) {
                        $stateParams.moduleName = 'sms';
                        return 'sms';
                    }]
                }
            });
        };

        setTabsState = function (tab) {
            $stateProvider.state('sms.{0}'.format(tab), {
                url: '/{0}'.format(tab),
                parent: 'sms',
                views: {
                    'mdsEmbeddedView': {
                        templateUrl: '../sms/resources/partials/{0}.html'.format(tab),
                        controller: 'Sms{0}Ctrl'.format(tab.capitalize())
                    }
                }
            });
        };

        angular.forEach(SMS_AVAILABLE_TABS, function (tab) {
            if (tab === "log") {
                setLogState();
            } else {
                setTabsState(tab);
            }
        });
    });
}());