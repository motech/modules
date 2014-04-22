(function () {
    'use strict';

    var services = angular.module('messageCampaign.services', ['ngResource']);

    services.factory('Campaigns', function ($resource) {
        return $resource('../messagecampaign/campaigns');
    });

    services.factory('Enrollments', function ($resource) {
        return $resource('../messagecampaign/enrollments/users?enrollmentStatus=:enrollmentStatus&campaignName=:campaignName',
            {}, { query:{method:'GET', isArray:false}});
    });
}());
