(function () {
    'use strict';

    var mtrainingModule =  angular.module('mtraining', ['motech-dashboard', 'mtraining.controllers', 'mtraining.treeviewlib',
        'ngCookies', 'ngResource', 'ui.bootstrap', 'data-services']), idMapping = {};

    $.ajax({
        url: '../mds/entities/getEntitiesByBundle?symbolicName=org.motechproject.mtraining',
        success:  function(data) {
            var i;
            for (i = 0 ; i < data.length ; i += 1) {
                idMapping[data[i].name] = data[i].id;
            }
        },
        async: false
    });

    getAvailableTabs('mtraining', function(data) {
         mtrainingModule.constant('MTRAINING_AVAILABLE_TABS', data);
    });

    mtrainingModule.run(function ($rootScope, MTRAINING_AVAILABLE_TABS) {
        $rootScope.MTRAINING_AVAILABLE_TABS = MTRAINING_AVAILABLE_TABS;
    });

    mtrainingModule.config(function ($stateProvider, MTRAINING_AVAILABLE_TABS) {
        var i, key, tab, setTabsState, setQuizzesState, setTreeView;
        $stateProvider.state('mtraining', {
            url: "/mtraining",
            abstract: true,
            views: {
                "moduleToLoad": {
                    templateUrl: "../mtraining/resources/index.html"
                }
            },
            resolve: {
                loadMyService: ['$ocLazyLoad', function($ocLazyLoad) {
                    return $ocLazyLoad.load('data-services');
                }]
            }
        });

        setTreeView = function () {
            $stateProvider.state('mtraining.treeView', {
                url: '/treeView',
                parent: 'mtraining',
                views: {
                    'mdsEmbeddedView': {
                        templateUrl: '../mtraining/resources/partials/treeView.html',
                        controller: 'TreeViewController'
                    }
                }
            });
        };

        setQuizzesState = function () {
            $stateProvider.state('mtraining.quizzes', {
                url: '/quizzes/mds/dataBrowser/:entityId/:moduleName',
                parent: 'mtraining',
                views: {
                    'mdsEmbeddedView': {
                        templateUrl: '../mds/resources/partials/dataBrowser.html',
                        controller: 'MdsDataBrowserCtrl'
                    }
                },
                resolve: {
                    entityId: ['$stateParams', function ($stateParams) {
                        $stateParams.entityId = idMapping.Quiz;
                        return idMapping.Quiz;
                    }],
                    moduleName: ['$stateParams', function ($stateParams) {
                        $stateParams.moduleName = 'mtraining';
                        return  'mtraining';
                    }]
                }
            });
        };

        setTabsState = function (tab) {
            $stateProvider.state('mtraining.{0}'.format(tab), {
                url: '/{0}/mds/dataBrowser/:entityId/:moduleName'.format(tab),
                parent: 'mtraining',
                views: {
                    'mdsEmbeddedView': {
                        templateUrl: '../mds/resources/partials/dataBrowser.html',
                        controller: 'MdsDataBrowserCtrl'
                    }
                },
                resolve: {
                    entityId: ['$stateParams', function ($stateParams) {
                        $stateParams.entityId = idMapping[tab.capitalize().substring(0, tab.length - 1)];
                        return idMapping[tab.capitalize().substring(0, tab.length - 1)];
                    }],
                    moduleName: ['$stateParams', function ($stateParams) {
                        $stateParams.moduleName = 'mtraining';
                        return 'mtraining';
                    }]
                }
            });
        };

        for (i = 0; i < MTRAINING_AVAILABLE_TABS.length; i = i + 1) {
            tab = MTRAINING_AVAILABLE_TABS[i];
            if (tab === "treeView") {
                setTreeView();
            } else if (tab === "quizzes") {
                setQuizzesState();
            } else {
                setTabsState(tab);
            }
        }
    });
}());
