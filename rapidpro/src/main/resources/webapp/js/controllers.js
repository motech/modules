(function () {
    'use strict';

    /* Controllers */
    var controllers = angular.module('rapidpro.controllers', []);

    controllers.controller('SettingsCtrl', function ($scope, Settings) {
        $scope.versions = [{value: 'v1', label: "1"}];
        $scope.settings = Settings.get();

        $scope.updateSettings = function () {
            $scope.settings.$save();
        };
    });
}());
