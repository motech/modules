(function () {
    'use strict';

    var controllers = angular.module('csd.controllers', []);

    /*
     *
     * Settings
     *
     */
    controllers.controller('CsdSettingsCtrl', function ($scope, $http, $timeout) {
        $scope.errors = [];
        $scope.messages = [];

        innerLayout({
            spacing_closed: 30,
            east__minSize: 200,
            east__maxSize: 350
        });

        $http.get('../csd/csd-config')
            .success(function(response){
                var i;
                $scope.config = response;
                $scope.originalConfig = angular.copy($scope.config);
            })
            .error(function(response) {
                $scope.errors.push($scope.msg('csd.web.settings.noConfig', response));
            });

        $scope.reset = function () {
            $scope.config = angular.copy($scope.originalConfig);
            $scope.scrollTop();
        };

        function hideMsgLater(index) {
            return $timeout(function() {
                $scope.messages.splice(index, 1);
            }, 5000);
        }

        $scope.submit = function () {
            $http.post('../csd/csd-config', $scope.config)
                .success(function (response) {
                    $scope.config = response;
                    $scope.originalConfig = angular.copy($scope.config);
                    var index = $scope.messages.push($scope.msg('csd.web.settings.saved'));
                    hideMsgLater(index-1);
                })
                .error (function (response) {
                    //todo: better than that!
                    handleWithStackTrace('csd.error.header', 'csd.error.body', response);
                });
        };
    });

    /*
     *
     * Csd
     *
     */
    controllers.controller('CsdCtrl', function ($scope, $http, $timeout) {
        $scope.errors = [];
        $scope.messages = [];
        $scope.isFetchingCSD = false;

        innerLayout({
            spacing_closed: 30,
            east__minSize: 200,
            east__maxSize: 350
        });

        $http.get('../csd/csd-config')
            .success(function(response){
                var i;
                $scope.config = response;
            })
            .error(function(response) {
                $scope.errors.push($scope.msg('csd.web.settings.noConfig', response));
            });

        function hideMsgLater(index) {
            return $timeout(function() {
                $scope.messages.splice(index, 1);
            }, 5000);
        }

        $scope.fetch = function () {
            $scope.isFetchingCSD = true;
            $http.get('../csd/csd-consume')
                .success(function(response){
                    var index = $scope.messages.push($scope.msg('csd.web.csd.updatedSuccessful'));
                    hideMsgLater(index-1);
                    $scope.isFetchingCSD = false;
                })
                .error(function(response) {
                    $scope.errors.push($scope.msg('csd.web.csd.fetch.error', response));
                    $scope.isFetchingCSD = false;
                });
        };

    });

}());