(function() {
    'use strict';

    /* Controllers */
    var controllers = angular.module('mtraining.controllers', []);

    controllers.controller('TreeViewController', function($scope, $http) {

        innerLayout({
            spacing_closed: 30,
            east__minSize: 200,
            east__maxSize: 350
        });

    });
}());
