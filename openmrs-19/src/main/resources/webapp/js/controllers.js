(function () {
    'use strict';

    /* Controllers */

    var controllers = angular.module('openmrs19.controllers',[]);

    controllers.controller('OpenMRSSettingsCtrl', function($scope, OpenMRSConfig, LoadingModal) {

        $scope.configs = [];
        $scope.selectedConfig = undefined;
        $scope.configOutdated = false;
        $scope.newPatientIdentifierType = {};
        $scope.failureMessage = undefined;

        $scope.getDefault = function () {
            for (var i = 0; i < $scope.configs.configs.length; i++) {
                if ($scope.configs.configs[i].name === $scope.configs.defaultConfigName) {
                    return $scope.configs.configs[i];
                }
            }
            return undefined;
        };

        $scope.getConfigs = function() {
            OpenMRSConfig.get(
                function success(data) {
                    $scope.configs = data;
                    $scope.configs.configs.patientIdentifierTypeNames=[];
                    $scope.configOutdated = false;
                    if (!$scope.selectedConfig) {
                        $scope.selectedConfig = $scope.getDefault();
                    }
                }
            );
        };

        $scope.getConfigs();

        $scope.addConfig = function() {
            $scope.selectedConfig = {
                'name':'',
                'openMrsUrl':'',
                'username':'',
                'password':'',
                'motechPatientIdentifierTypeName':'',
                'patientIdentifierTypeNames':[]
            };
        };

        $scope.draftChanged = function() {
            $scope.configOutdated = true;
        };

        $scope.selectChanged = function() {
            $scope.configOutdated = false;
            $scope.newPatientIdentifierType.name = "";
            $scope.failureMessage="";
        };

        $scope.addPatientIdentifierType = function() {
            $scope.selectedConfig.patientIdentifierTypeNames.push($scope.newPatientIdentifierType.name);
            $scope.newPatientIdentifierType.name = "";
            $scope.configOutdated = true;
        };

        $scope.removePatientIdentifierType = function(patientIdentifierTypeName) {
            $scope.selectedConfig.patientIdentifierTypeNames.splice($scope.selectedConfig.patientIdentifierTypeNames.indexOf(patientIdentifierTypeName), 1);
            $scope.configOutdated = true;
        };

        $scope.validateConfig = function() {
            return $scope.selectedConfig['name']
                && $scope.selectedConfig['openMrsUrl']
                && $scope.selectedConfig['username']
                && $scope.selectedConfig['password']
                && $scope.selectedConfig['motechPatientIdentifierTypeName'];
        };

        $scope.saveAllowed = function() {
            return $scope.validateConfig()
                && $scope.configOutdated;
        };

        $scope.isNewConfig = function() {
            for (var i = 0; i < $scope.configs.configs.length; i++) {
                if ($scope.selectedConfig.name === $scope.configs.configs[i].name) {
                    return false;
                }
            }
            return true;
        };

        $scope.saveConfig = function() {
            if ($scope.isNewConfig()) {
                $scope.configs.configs.push($scope.selectedConfig);
            }
            else if ($scope.getDefault() === undefined) {
                $scope.configs.defaultConfigName = $scope.selectedConfig.name;
            }
            LoadingModal.open();
            OpenMRSConfig.save($scope.configs,
                function success() {
                    $scope.configOutdated = false;
                    $scope.failureMessage="";
                    LoadingModal.close();
                },
                function failure() {
                    $scope.failureMessage = $scope.msg('openMRS.config.save.failure');
                    LoadingModal.close();
                }
            );
        };

        $scope.deleteAllowed = function() {
            return !$scope.configOutdated
                && $scope.validateConfig();

        };

        $scope.deleteConfig = function() {
            $scope.configs.configs.splice($scope.configs.configs.indexOf($scope.selectedConfig), 1);
            if ($scope.selectedConfig.name === $scope.configs.defaultConfigName) {
                $scope.configs.defaultConfigName = undefined;
            }
            LoadingModal.open();
            OpenMRSConfig.save($scope.configs,
                function success() {
                    $scope.selectedConfig = undefined;
                    $scope.getConfigs();
                    $scope.failureMessage="";
                    LoadingModal.close();
                },
                function failure() {
                    $scope.failureMessage = $scope.msg('openMRS.config.delete.failure');
                    LoadingModal.close();
                }
            );
        };

        $scope.isDefault = function() {
             return $scope.selectedConfig.name === $scope.configs.defaultConfigName;
        }

        $scope.makeDefault = function() {
            $scope.configs.defaultConfigName = $scope.selectedConfig.name;
            LoadingModal.open();
            OpenMRSConfig.save($scope.configs,
                function success() {
                    $scope.selectedConfig = undefined;
                    $scope.getConfigs();
                    $scope.failureMessage="";
                    LoadingModal.close();
                },
                function failure() {
                    $scope.failureMessage = $scope.msg('openMRS.config.makeDefault.failure');
                    LoadingModal.close();
                }
            );
        };
    }
)})();