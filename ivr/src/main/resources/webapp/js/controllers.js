(function () {
    'use strict';

    var controllers = angular.module('ivr.controllers', []);

    /*
     *
     * Settings
     *
     */
    controllers.controller('SettingsController', function ($scope, $http, $timeout) {
        $scope.errors = [];
        $scope.messages = [];
        $scope.dupeNames = [];

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
            $scope.dupeNames = [];
            for (i = 0 ; i<configs.length ; i = i + 1) {
                $scope.accordions.push(false);
                $scope.dupeNames.push(false);
            }
            autoExpandSingleAccordion();
        }

        $scope.checkForDuplicateNames = function(index) {
            var i;
            for (i = 0 ; i < $scope.configs.length ; i = i + 1) {
                if (i!==index && $scope.configs[i].name === $scope.configs[index].name) {
                    $scope.dupeNames[index] = true;
                    return;
                }
            }
            $scope.dupeNames[index] = false;
        };

        $scope.anyDuplicateNames = function() {
            var i;
            for (i = 0 ; i < $scope.dupeNames.length ; i = i + 1) {
                if ($scope.dupeNames[i]) {
                    return true;
                }
            }
            return false;
        };

        $http.get('../ivr/ivr-configs')
            .success(function(response){
                $scope.configs = response;
                $scope.originalConfigs = angular.copy($scope.configs);
                setAccordions($scope.configs);
            })
            .error(function(response) {
                $scope.errors.push($scope.msg('ivr.web.settings.noConfig', response));
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
            $scope.dupeNames.splice(index, 1);
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
                'name':'',
                'outgoingCallUriTemplate':'',
                'outgoingCallMethod':'GET',
                'ignoredStatusFields':[],
                'statusFieldMapString':''
                };
            newLength = $scope.configs.push(newConfig);
            $scope.accordions.push(true);
            autoExpandSingleAccordion();
            $scope.dupeNames.push(false);
        };

        $scope.tooltipOrBlank = function (name, templateName) {
            var key, ret;
            if (templateName !== null) {
                key = "ivr.settings.prop." + name + '.' + templateName + ".tooltip";
                ret = $scope.msg(key);
                if (ret !== "[" + key + "]") {
                    return ret;
                }
            }
            key = "ivr.settings.prop." + name + ".tooltip";
            ret = $scope.msg(key);
            if (ret === "[" + key + "]") {
                ret = "";
            }
            return ret;
        };

        function hideMsgLater(index) {
            return $timeout(function() {
                $scope.messages.splice(index, 1);
            }, 5000);
        }

        $scope.submit = function () {
            $http.post('../ivr/ivr-configs', $scope.configs)
                .success(function (response) {
                    $scope.configs = response;
                    $scope.originalConfigs = angular.copy($scope.configs);
                    setAccordions($scope.configs);
                    var index = $scope.messages.push($scope.msg('ivr.web.settings.saved'));
                    hideMsgLater(index-1);
                })
                .error (function (response) {
                    //todo: better than that!
                    handleWithStackTrace('ivr.error.header', 'ivr.error.body', response);
                });
        };
    });
}());