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

        $scope.timeMultipliers = {
            'minutes': $scope.msg('csd.web.settings.units.minutes'),
            'hours': $scope.msg('csd.web.settings.units.hours'),
            'days': $scope.msg('csd.web.settings.units.days')
        };

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
        $scope.isLoadingXml = false;
        $scope.isDownloadingXml = false;
        $scope.xml = "";

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

        $scope.fetch = function () {
            $scope.isFetchingCSD = true;
            $http.get('../csd/csd-consume')
                .success(function(response){
                    $scope.messages.push($scope.msg('csd.web.csd.updatedSuccessful'));
                    $scope.isFetchingCSD = false;
                    $scope.xml = "";
                })
                .error(function(response) {
                    $scope.errors.push($scope.msg('csd.web.csd.fetch.error', response));
                    $scope.isFetchingCSD = false;
                });
        };

        $scope.showXml = function () {
            if (!$scope.xml && !$scope.isLoadingXml) {
                $scope.isLoadingXml = true;
                $http.get('../csd/csd-getXml')
                    .success(function(response){
                        $scope.xml = response;
                        $scope.isLoadingXml = false;
                    })
                    .error(function(response) {
                        $scope.errors.push($scope.msg('csd.web.csd.showXml.error', response));
                        $scope.isLoadingXml = false;
                    });
            }
        };

        var saveData = (function () {
            var a = document.createElement("a");
            document.body.appendChild(a);
            a.style = "display: none";
            return function (data, fileName) {
                var blob = new Blob([data], {type: "octet/stream"}),
                    url = window.URL.createObjectURL(blob);
                a.href = url;
                a.download = fileName;
                a.click();
            };
        }());

        $scope.downloadXml = function() {
            $scope.isDownloadingXml = true;
            if ($scope.xml) {
                saveData($scope.xml, "CSD.xml");
                $scope.isDownloadingXml = false;
            } else {
                $http.get('../csd/csd-getXml')
                    .success(function(response){
                        $scope.xml = response;
                        saveData(response, "CSD.xml");
                        $scope.isDownloadingXml = false;
                    })
                    .error(function(response) {
                        $scope.errors.push($scope.msg('csd.web.csd.downloadXml.error', response));
                        $scope.isDownloadingXml = false;
                    });
            }
        };

    });

}());