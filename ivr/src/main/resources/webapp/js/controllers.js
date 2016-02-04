(function () {
    'use strict';

    var controllers = angular.module('ivr.controllers', []);

    /*
     *
     * Templates
     *
     */
    controllers.controller('IvrTemplatesCtrl', function ($scope, $http, $timeout) {
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

        function setAccordions(templates) {
            var i;
            $scope.accordions = [];
            $scope.dupeNames = [];
            for (i = 0 ; i<templates.length ; i = i + 1) {
                $scope.accordions.push(false);
                $scope.dupeNames.push(false);
            }
            autoExpandSingleAccordion();
        }

        $scope.checkForDuplicateNames = function(index) {
            var i;
            for (i = 0 ; i < $scope.templates.length ; i = i + 1) {
                if (i!==index && $scope.templates[i].name === $scope.templates[index].name) {
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

        $http.get('../ivr/ivr-templates')
            .success(function(response){
                $scope.templates = response;
                $scope.originalTemplates = angular.copy($scope.templates);
                setAccordions($scope.templates);
            })
            .error(function(response) {
                $scope.errors.push($scope.msg('ivr.web.templates.noTemplate', response));
            });

        $scope.collapseAccordions = function () {
            var key;
            for (key in $scope.accordions) {
                $scope.accordions[key] = false;
            }
            autoExpandSingleAccordion();
        };

        $scope.deleteTemplate = function(index) {
            $scope.templates.splice(index, 1);
            $scope.accordions.splice(index, 1);
            $scope.dupeNames.splice(index, 1);
            autoExpandSingleAccordion();
        };

        $scope.isDirty = function () {
            if ($scope.originalTemplates === null || $scope.templates === null) {
                return false;
            }

            return !angular.equals($scope.originalTemplates, $scope.templates);
        };

        $scope.reset = function () {
            $scope.templates = angular.copy($scope.originalTemplates);
            setAccordions($scope.templates);
        };

        $scope.addTemplate = function () {
            var newLength, newTemplate = {
                'name':'',
                'value':''
            };
            newLength = $scope.templates.push(newTemplate);
            $scope.accordions.push(true);
            autoExpandSingleAccordion();
            $scope.dupeNames.push(false);
        };

        function hideMsgLater(index) {
            return $timeout(function() {
                $scope.messages.splice(index, 1);
            }, 5000);
        }

        $scope.submit = function () {
            $http.post('../ivr/ivr-templates', $scope.templates)
                .success(function (response) {
                    $scope.templates = response;
                    $scope.originalTemplates = angular.copy($scope.templates);
                    setAccordions($scope.templates);
                    var index = $scope.messages.push($scope.msg('ivr.web.templates.saved'));
                    hideMsgLater(index-1);
                    $('.ui-layout-content').animate({
                        scrollTop: 0
                    });
                })
                .error (function (response) {
                //todo: better than that!
                handleWithStackTrace('ivr.error.header', 'ivr.error.body', response);
            });
        };
    });

    /*
     *
     * Settings
     *
     */
    controllers.controller('IvrSettingsCtrl', function ($scope, $http, $timeout) {
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
                $scope.configs.defaultConfig = $scope.configs.configList[0].name;
                $scope.defaultConfigIndex = 0;
            }
        }

        $scope.setNewDefaultConfig = function() {
            var i;
            for (i = 0 ; i < $scope.configs.configList.length ; i = i + 1) {
                if ($scope.configs.configList[i].name === $scope.configs.defaultConfig) {
                    return;
                }
            }
            if ($scope.configs.configList.length > 0) {
                $scope.configs.defaultConfig = $scope.configs.configList[0].name;
                $scope.defaultConfigIndex = 0;
            } else {
                $scope.configs.defaultConfig = undefined;
            }
        };

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
            for (i = 0 ; i < $scope.configs.configList.length ; i = i + 1) {
                if (i!==index && $scope.configs.configList[i].name === $scope.configs.configList[index].name) {
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
                var i;
                $scope.configs = response;
                $scope.originalConfigs = angular.copy($scope.configs);
                setAccordions($scope.configs.configList);
                for (i = 0 ; i < response.configList.length ; i = i + 1) {
                   if (response.defaultConfig === response.configList[i].name) {
                      $scope.defaultConfigIndex = i;
                         break;
                   }
                }
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

        $scope.setDefaultConfig = function(name, index) {
            $scope.configs.defaultConfig = name;
            $scope.defaultConfigIndex = index;
        };

        $scope.keepDefaultConfig = function() {
            $scope.configs.defaultConfig = $scope.configs.configList[$scope.defaultConfigIndex].name;
        };

        $scope.deleteConfig = function(index) {
            $scope.configs.configList.splice(index, 1);
            $scope.accordions.splice(index, 1);
            $scope.dupeNames.splice(index, 1);
            $scope.setNewDefaultConfig();
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
            setAccordions($scope.configs.configList);
        };

        $scope.addConfig = function () {
            var newLength, newConfig = {
                'name':'',
                'outgoingCallUriTemplate':'',
                'outgoingCallMethod':'GET',
                'ignoredStatusFields':[],
                'statusFieldMapString':'',
                'callStatusMappingString':'',
                'servicesMapString':''
            };
            newLength = $scope.configs.configList.push(newConfig);
            $scope.accordions.push(true);
            autoExpandSingleAccordion();
            $scope.dupeNames.push(false);
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
                    setAccordions($scope.configs.configList);
                    var index = $scope.messages.push($scope.msg('ivr.web.settings.saved'));
                    hideMsgLater(index-1);
                    $('.ui-layout-content').animate({
                        scrollTop: 0
                    });
                })
                .error (function (response) {
                //todo: better than that!
                handleWithStackTrace('ivr.error.header', 'ivr.error.body', response);
            });
        };
    });

}());