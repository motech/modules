(function () {
    'use strict';

    var messageCampaignModule = angular.module('messageCampaign', ['motech-dashboard', 'messageCampaign.services',
    'messageCampaign.controllers', 'ngCookies', 'data-services', 'uiServices']), campaignRecordId, campaignMessageRecordId;

    $.ajax({
        url: '../mds/entities/getEntity/MOTECH Message Campaign/CampaignRecord',
        success:  function(data) {
            campaignRecordId = data.id;
        },
        async: false
    });

    $.ajax({
        url: '../mds/entities/getEntity/MOTECH Message Campaign/CampaignMessageRecord',
        success:  function(data) {
            campaignMessageRecordId = data.id;
        },
        async: false
    });

    messageCampaignModule.config(['$stateProvider', function ($stateProvider) {
        $stateProvider
            .state('messageCampaign', {
                url: "/messageCampaign",
                abstract: true,
                views: {
                    "moduleToLoad": {
                        templateUrl: "../messagecampaign/resources/index.html"
                    }
                },
                resolve: {
                    loadMyService: ['$ocLazyLoad', function($ocLazyLoad) {
                        return $ocLazyLoad.load('data-services');
                    }]
                }
            })
            .state('messageCampaign.campaigns', {
                url: '/campaigns',
                parent: 'messageCampaign',
                views: {
                    'messageCampaignView': {
                        templateUrl: '../messagecampaign/resources/partials/campaigns.html',
                        controller: 'MCCampaignsCtrl'
                    }
                }
            })
            .state('messageCampaign.enrollments', {
                url: '/enrollments/:campaignName',
                parent: 'messageCampaign',
                views: {
                    'messageCampaignView': {
                        templateUrl: '../messagecampaign/resources/partials/enrollments.html',
                        controller: 'MCEnrollmentsCtrl'
                    }
                }
            })
            .state('messageCampaign.settings', {
                url: '/settings',
                parent: 'messageCampaign',
                views: {
                    'messageCampaignView': {
                        templateUrl: '../messagecampaign/resources/partials/settings.html',
                        controller: 'MCSettingsCtrl'
                    }
                }
            })
            .state('messageCampaign.admin', {
                url: '/admin',
                parent: 'messageCampaign',
                views: {
                    'messageCampaignView': {
                        templateUrl: '../messagecampaign/resources/partials/admin.html'
                    }
                }
            })
            .state('messageCampaign.campaignRecord', {
                url: '/campaignRecord/mds/dataBrowser/:entityId/:moduleName',
                parent: 'messageCampaign',
                views: {
                    'messageCampaignView': {
                        templateUrl: '../mds/resources/partials/dataBrowser.html',
                        controller: 'MdsDataBrowserCtrl'
                    }
                },
                resolve:{
                    entityId: ['$stateParams', function ($stateParams) {
                        $stateParams.entityId = campaignRecordId;
                        return campaignRecordId;
                    }],
                    moduleName: ['$stateParams', function ($stateParams) {
                        $stateParams.moduleName = 'messagecampaign';
                        return 'messagecampaign';
                    }]
                }
            })
            .state('messageCampaign.campaignMessageRecord', {
                url: '/campaignMessageRecord/mds/dataBrowser/:entityId/:moduleName',
                parent: 'messageCampaign',
                views: {
                    'messageCampaignView': {
                        templateUrl: '../mds/resources/partials/dataBrowser.html',
                        controller: 'MdsDataBrowserCtrl'
                    }
                },
                resolve:{
                    entityId: ['$stateParams', function ($stateParams) {
                        $stateParams.entityId = campaignMessageRecordId;
                        return campaignMessageRecordId;
                    }],
                    moduleName: ['$stateParams', function ($stateParams) {
                        $stateParams.moduleName = 'messagecampaign';
                        return 'messagecampaign';
                    }]
                }
            })
            .state('mtraining.campaign', {
                url: '/campaign/mds/dataBrowser/:campaignId/:messagecampaign',
                parent: 'messageCampaign',
                views: {
                    'messageCampaignView': {
                        templateUrl: '../mds/resources/partials/dataBrowser.html',
                        controller: 'MdsDataBrowserCtrl'
                    }
                },
                resolve:{
                    entityId: ['$stateParams', function ($stateParams) {
                        $stateParams.campaignId = campaignRecordId;
                        return campaignRecordId;
                    }],
                    moduleName: ['$stateParams', function ($stateParams) {
                        $stateParams.moduleName = 'messagecampaign';
                        return 'messagecampaign';
                    }]
                }
            });
    }]
    );
}());
