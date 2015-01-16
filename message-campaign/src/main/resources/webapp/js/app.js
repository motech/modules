(function () {
    'use strict';

        angular.module('messageCampaign', ['motech-dashboard', 'messageCampaign.services', 'messageCampaign.controllers', 'ngCookies'])
        .config(['$routeProvider', function ($routeProvider) {

            $routeProvider
                .when('/messageCampaign/campaigns', { templateUrl: '../messagecampaign/resources/partials/campaigns.html', controller: 'MCCampaignsCtrl' })
                .when('/messageCampaign/enrollments/:campaignName', { templateUrl: '../messagecampaign/resources/partials/enrollments.html', controller: 'MCEnrollmentsCtrl' })
                .when('/messageCampaign/admin', { templateUrl: '../messagecampaign/resources/partials/admin.html' })
                .when('/messageCampaign/settings', {templateUrl: '../messagecampaign/resources/partials/settings.html', controller: 'MCSettingsCtrl'});
        }]
    );
}());
