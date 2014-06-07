(function () {
    'use strict';

    /* Services */

    angular.module('cmslite.services', ['ngResource']).factory('Resources', function ($resource) {
        return $resource('../cmsliteapi/resource/:type/:language/:name');
    });

}());
