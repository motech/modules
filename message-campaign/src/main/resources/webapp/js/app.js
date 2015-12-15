(function () {
    'use strict';

    var messageCampaignModule = angular.module('messageCampaign', ['motech-dashboard', 'messageCampaign.services', 'messageCampaign.controllers', 'ngCookies', 'data-services']), campaignRecordId, campaignMessageRecordId;

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

    messageCampaignModule.config(['$routeProvider', function ($routeProvider) {
        $routeProvider
            .when('/messageCampaign/campaigns', { templateUrl: '../messagecampaign/resources/partials/campaigns.html', controller: 'MCCampaignsCtrl' })
            .when('/messageCampaign/enrollments/:campaignName', { templateUrl: '../messagecampaign/resources/partials/enrollments.html', controller: 'MCEnrollmentsCtrl' })
            .when('/messageCampaign/admin', { templateUrl: '../messagecampaign/resources/partials/admin.html' })
            .when('/messageCampaign/campaignRecord', { redirectTo: '/mds/dataBrowser/' + campaignRecordId + '/messagecampaign' })
            .when('/messageCampaign/campaignMessageRecord', { redirectTo: '/mds/dataBrowser/' + campaignMessageRecordId + '/messagecampaign' })
            .when('/messageCampaign/settings', {templateUrl: '../messagecampaign/resources/partials/settings.html', controller: 'MCSettingsCtrl'})
            .when('/messageCampaign/campaigns/:campaignId', { redirectTo: '/mds/dataBrowser/' + campaignRecordId + '/:campaignId/messagecampaign' });
    }]
    );
}());
