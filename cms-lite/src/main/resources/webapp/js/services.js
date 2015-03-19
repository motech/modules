(function () {
    'use strict';

    /* Services */
    angular.module('cmslite.services', ['ngResource']).factory('Resources', function ($resource) {
        // Note the extra blank space at the end of the query.
        // It has been added due to a bug with AngularJS, removing the trailing slash.
        // This can be removed once we switch to AngularJS 1.3.X
        return $resource('../cmsliteapi/resource/:type/:language/:name/ ');
    });

}());
