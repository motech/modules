(function () {
    'use strict';

    var controllers = angular.module('csd.controllers', []);

    /*
     *
     * Settings
     *
     */
    controllers.controller('CsdSettingsCtrl', function ($scope, $http, $timeout, ModalFactory) {
        $scope.errors = [];
        $scope.messages = [];
        $scope.dupeUrls = [];

        $scope.timeMultipliers = {
            'days': $scope.msg('csd.web.settings.units.days'),
            'hours': $scope.msg('csd.web.settings.units.hours'),
            'minutes': $scope.msg('csd.web.settings.units.minutes')
        };

        innerLayout({
            spacing_closed: 30,
            east__minSize: 200,
            east__maxSize: 350
        });

        function autoExpandSingleAccordion() {
           if ($scope.accordions.length === 1) {
               $scope.accordions[0] = true;
           }
        }

        function setAccordions(configs) {
             var i;
             $scope.accordions = [];
             $scope.dupeUrls = [];
             for (i = 0; i<configs.length; i = i + 1) {
                 $scope.accordions.push(false);
                 $scope.dupeUrls.push(false);
             }
             autoExpandSingleAccordion();
         }

        $scope.checkForDuplicateUrls = function(index) {
            var i;
            for (i = 0; i < $scope.configs.length; i = i + 1) {
                if (i!==index && $scope.configs[i].xmlUrl === $scope.configs[index].xmlUrl) {
                    $scope.dupeUrls[index] = true;
                    return;
                }
            }
            $scope.dupeUrls[index] = false;
        };

        $scope.anyDuplicateUrls = function() {
            var i;
            for (i = 0; i < $scope.dupeUrls.length; i = i + 1) {
                if ($scope.dupeUrls[i]) {
                    return true;
                }
            }
            return false;
        };

        $http.get('../csd/csd-configs')
            .success(function(response){
                $scope.configs = response;
                $scope.originalConfigs = angular.copy($scope.configs);
                setAccordions($scope.configs);
            })
            .error(function(response) {
                $scope.errors.push($scope.msg('csd.web.settings.noConfig', response));
            });

        $scope.collapseAccordions = function () {
            var key;
            for (key in $scope.accordions) {
                $scope.accordions[key] = false;
            }
            autoExpandSingleAccordion();
        };

        $scope.deleteConfig = function(index) {
            $scope.configs.splice(index, 1);
            $scope.accordions.splice(index, 1);
            $scope.dupeUrls.splice(index, 1);
            autoExpandSingleAccordion();
        };

        $scope.isDirty = function () {
            if ($scope.originalConfigs === null || $scope.configs === null) {
                return false;
            }

            return !angular.equals($scope.originalConfigs, $scope.configs);
        };

        $scope.reset = function () {
            $scope.configs = angular.copy($scope.originalConfigs);
            setAccordions($scope.configs);
        };

        $scope.addConfig = function () {
            var newLength, newConfig = {
                'schedulerEnabled':'false',
                'timePeriod':'1',
                'timePeriodMultiplier':'days',
                'startDate':'',
                'xmlUrl':'',
                'communicationProtocol':'REST',
                'lastModified':null
            };
            newLength = $scope.configs.push(newConfig);
            $scope.accordions.push(true);
            autoExpandSingleAccordion();
            $scope.dupeUrls.push(false);
        };

        function hideMsgLater(index) {
            return $timeout(function() {
                $scope.messages.splice(index, 1);
            }, 5000);
        }

        $scope.submit = function () {
            $http.post('../csd/csd-configs', $scope.configs)
                .success(function (response) {
                    $scope.configs = response;
                    $scope.originalConfigs = angular.copy($scope.configs);
                    var index = $scope.messages.push($scope.msg('csd.web.settings.saved'));
                    hideMsgLater(index-1);
                    $('.ui-layout-content').animate({
                        scrollTop: 0
                    });
                })
                .error (function (response) {
                    ModalFactory.showErrorWithStackTrace('csd.error.body', 'csd.error.header', response);
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
        $scope.hasConfig = false;
        $scope.isFetchingCSD = false;
        $scope.isLoadingXml = false;
        $scope.isDownloadingXml = false;
        $scope.xml = "";
        $scope.xmlUrl = "";

        innerLayout({
            spacing_closed: 30,
            east__minSize: 200,
            east__maxSize: 350
        });

        $http.get('../csd/csd-configs')
            .success(function(response){
                var i;
                $scope.configs = response;
                if ($scope.configs.length > 0) {
                    $scope.xmlUrl = $scope.configs[0].xmlUrl;
                    $scope.hasConfig = true;
                }
            })
            .error(function(response) {
                $scope.errors.push($scope.msg('csd.web.settings.noConfig', response));
            });

        $scope.fetch = function () {
            $scope.isFetchingCSD = true;
            $http.post('../csd/csd-consume', $scope.xmlUrl)
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
            a.style.display = "none";
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
                $http({
                    method: 'GET',
                    url:'../csd/csd-getXml',
                    headers: {
                        'Content-Type': 'application/xml',
                        'Accept': 'application/xml'
                        }
                    })
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