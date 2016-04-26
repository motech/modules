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

    ivrmodule.config(function ($stateProvider, IVR_AVAILABLE_TABS) {
        var i, tab, setTabsState, setLogState;

        $stateProvider.state('ivr', {
            url: "/ivr",
            abstract: true,
            views: {
                "moduleToLoad": {
                    templateUrl: "../ivr/resources/index.html"
                }
            },
            resolve: {
                loadMyService: ['$ocLazyLoad', function($ocLazyLoad) {
                    return $ocLazyLoad.load('data-services');
                }]
            }
        });

        setLogState = function () {
            $stateProvider.state('ivr.log', {
                url: '/log/mds/dataBrowser/:entityId/:moduleName',
                parent: 'ivr',
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
                        $stateParams.moduleName = 'ivr';
                        return 'ivr';
                    }]
                }
            });
        };

        setTabsState = function (tab) {
            $stateProvider.state('ivr.{0}'.format(tab), {
                url: '/{0}'.format(tab),
                parent: 'ivr',
                views: {
                    'mdsEmbeddedView': {
                        templateUrl: '../ivr/resources/partials/{0}.html'.format(tab),
                        controller: 'Ivr{0}Ctrl'.format(tab.capitalize())
                    }
                }
            });
        };

        for (i = 0; i < IVR_AVAILABLE_TABS.length; i = i + 1) {

            tab = IVR_AVAILABLE_TABS[i];

            if (tab === "log") {
                setLogState();
            } else {
                setTabsState(tab);
            }
        }
    });
}());
