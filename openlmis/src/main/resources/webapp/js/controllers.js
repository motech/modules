(function () {
    'use strict';

    /* Controllers */
    var controllers = angular.module('open-lmis.controllers', []);

    controllers.controller('OpenLMISSettingsCtrl', function ($scope, $http) {

            $scope.retrievalError = false;
            $scope.updateError = false;
            $scope.configOutdated = true;
            $http.get('../open-lmis/settings')
                .success(function (response) {
                    $scope.settings = response;
                    $scope.originalSettings = angular.copy($scope.settings);
                })
                .error(function (response) {
                    $scope.retrievalError = true;
                });

            $scope.sync = function () {
                $scope.blocked = true;
                $scope.success = null;
                $http.get('../open-lmis/sync')

                    .success(function (response) {
                        $scope.blocked = false;
                        $scope.success = response;
                    })

                    .error(function (response) {
                        $scope.blocked = false;
                        $scope.success = false;
                    })
            };

            $scope.submit = function () {
                $http.post('../open-lmis/settings', $scope.settings)
                    .success(function (response) {
                        $scope.verifySuccessMessage = $scope.msg('open-lmis.web.settings.save.success');
                        $scope.verifyErrorMessage = '';
                        $scope.settings = response;
                        $scope.originalSettings = angular.copy($scope.configs);
                        $scope.configOutdated = false;
                        $scope.updateError = false;
                    })
                    .error(function (response) {
                        $scope.verifySuccessMessage = '';
                        $scope.verifyErrorMessage =  $scope.msg('open-lmis.web.settings.save.fail') + ' ' + response.data;
                        $scope.configOutdated = false;
                        $scope.updateError = true;
                    });
            };

            $scope.isVerifyError = function() {
                return $scope.updateError && !$scope.configOutdated;
            };

            $scope.isVerifySuccess = function() {
                return !$scope.updateError && !$scope.configOutdated;
            };

            $scope.draftChanged = function() {
                $scope.configOutdated = true;
                $scope.clearMessages();
            };

            $scope.clearMessages = function() {
                $scope.updateError = false;
                $scope.verifySuccessMessage = '';
                $scope.verifyErrorMessage = '';
            };
        });

}());
