(function () {
    'use strict';

    var services = angular.module('ivr.services', ['ngResource']);

    services.factory('Provider', function($resource) {
        return $resource('../ivr/api/providers', {}, {
            all: {
                method: 'GET',
                params: {},
                isArray: true
            }
        });
    });

    services.factory('Call', function($resource) {
        return $resource('../ivr/api/test-call', {}, {
            dial: {
                method: 'POST'
            }
        });
    });


    services.factory('CalllogSearch', function ($resource) {
        return $resource('../ivr/api/calllog/search');
    });

    services.factory('CalllogCount', function ($resource) {
        return $resource('../ivr/api/calllog/count', {}, { query:{method:'GET', isArray:false}});
    });

    services.factory('CalllogMaxDuration', function ($resource) {
        return $resource('../ivr/api/calllog/maxduration', {}, { query:{method:'GET', isArray:false}});
    });

    services.factory('CalllogPhoneNumber', function($resource) {
        return $resource('../ivr/api/calllog/phone-numbers',{}, { query: {method: 'GET', isArray: true}});
    });
}());
