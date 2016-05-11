(function () {
    'use strict';

    /* Filters */

    var filters = angular.module('commcare.filters', []);

    filters.filter('filterUnsavedConfig', function() {
        return function(configs, newlyCreatedConfig) {
            var out = [];
            angular.forEach(configs, function(config){
                if (config !== newlyCreatedConfig) {
                    out.push(config);
                }
            });
            return out;
        };
    });
}());