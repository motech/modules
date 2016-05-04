(function () {
    'use strict';

    /* Controllers */

    var controllers = angular.module('openmrs19.controllers',[]);

    controllers.controller('OpenMRSSettingsCtrl', function($scope, OpenMRSConfig) {

        $scope.configs = undefined;
        $scope.selectedConfig = undefined;
        $scope.configOutdated = false;
        $scope.newPatientIdentifierType={};

        $scope.getDefault = function () {
            var i;
            for (i = 0; i < $scope.configs.configs.length; i += 1) {
                if ($scope.configs.configs[i].name === $scope.configs.defaultConfigName) {
                    return $scope.configs.configs[i];
                }
            }
            return undefined;
        };

         $scope.getConfigs = function () {
             OpenMRSConfig.get(
             function success(data) {
                 $scope.configs = data;
                 $scope.configs.configs.patientIdentifierTypeNames=[];
                 $scope.configOutdated = false;
                 if ($scope.selectedConfig === undefined) {
                    $scope.selectedConfig = $scope.getDefault();
                 }
            });
        };

        $scope.getConfigs();

        $scope.addConfig = function () {
            $scope.selectedConfig = {
                'name':'',
                'openMrsUrl':'',
                'username':'',
                'password':'',
                'motechPatientIdentifierTypeName':'',
                'patientIdentifierTypeNames':[]
            };
        };

        $scope.hasValue = function(prop) {
            return $scope.selectedConfig[prop] !== null &&
                $scope.selectedConfig[prop] !== undefined &&
                $scope.selectedConfig[prop] !== "";
        };

        $scope.draftChanged = function() {
            $scope.configOutdated = true;
        };

        $scope.selectChanged = function() {
            $scope.configOutdated = false;
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
            return $scope.hasValue('name')
                && $scope.hasValue('openMrsUrl')
                && $scope.hasValue('username')
                && $scope.hasValue('password')
                && $scope.hasValue('motechPatientIdentifierTypeName');
        };

        $scope.saveAllowed = function() {
            return $scope.validateConfig()
                && $scope.configOutdated;
        };

        $scope.isNewConfig = function() {
            for (var i=0; i<$scope.configs.configs.length; i++) {
                if ($scope.selectedConfig.name === $scope.configs.configs[i].name) {
                    return false;
                }
            }
            return true;
        };

        $scope.saveConfig = function () {
            if ($scope.isNewConfig()) {
                $scope.configs.configs.push($scope.selectedConfig);
            }
            else if ($scope.getDefault() === undefined) {
                $scope.configs.defaultConfigName = $scope.selectedConfig.name;
            }
            blockUI();
            OpenMRSConfig.save($scope.configs,
                function success() {
                    $scope.configOutdated = false;
                    unblockUI();
                },
                function failure() {
                    unblockUI();
                }
            );
        };

        $scope.deleteAllowed = function() {
            return !$scope.configOutdated
                && $scope.validateConfig();

        };

        $scope.deleteConfig = function() {
            $scope.configs.configs.splice($scope.configs.configs.indexOf($scope.selectedConfig), 1);
            blockUI();
            OpenMRSConfig.save($scope.configs,
                function success() {
                    $scope.selectedConfig = undefined;
                    $scope.getConfigs();
                    unblockUI();
                },
                function failure() {
                    unblockUI()
                }
            );
        };

        $scope.isDefault = function() {
             return $scope.selectedConfig.name === $scope.configs.defaultConfigName;
        }

        $scope.makeDefault = function() {
            $scope.configs.defaultConfigName = $scope.selectedConfig.name;
            blockUI();
            OpenMRSConfig.save($scope.configs,
                function success() {
                    $scope.selectedConfig = undefined;
                    $scope.getConfigs();
                    unblockUI();
                },
                function failure() {
                    unblockUI();
                }
            );
        };
    }
)})();