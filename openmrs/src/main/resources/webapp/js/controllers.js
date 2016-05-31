(function () {
    'use strict';

    /* Controllers */

    var controllers = angular.module('openmrs.controllers',[]);

    controllers.controller('OpenMRSSettingsCtrl', function($scope, OpenMRSConfig, LoadingModal, ModalFactory) {

        $scope.configs = [];
        $scope.selectedConfig = undefined;
        $scope.originalConfig = undefined;
        $scope.newPatientIdentifierType = {};
        $scope.failureMessage = undefined;
        $scope.successMessage = undefined;

        $scope.onConfigChange = function(selectedConfig, oldConfig) {
            if (!angular.equals(oldConfig, $scope.originalConfig) && selectedConfig) {
                ModalFactory.showConfirm({
                   title: $scope.msg('openMRS.header.confirm'),
                   message: $scope.msg('openMRS.confirm.discardChanges'),
                   type: 'type-warning',
                   callback: function(result) {
                       if (result) {
                           $scope.getConfigs();
                           $scope.clearMessages();
                           $scope.newPatientIdentifierType.name = "";
                       }
                       else {
                           $scope.selectedConfig = $scope.getConfig(oldConfig.name);
                           if (!$scope.selectedConfig) {
                               $scope.selectedConfig = oldConfig;
                           }
                           $scope.$apply();
                       }
                   }
                });
            }
            else {
               $scope.originalConfig = angular.copy($scope.selectedConfig);
               $scope.clearMessages();
               $scope.newPatientIdentifierType.name = "";
            }
        };

        $scope.clearMessages = function() {
            $scope.failureMessage="";
            $scope.successMessage="";
        };

        $scope.getConfig = function(name) {
            for (var i = 0; i < $scope.configs.configs.length; i++) {
                if ($scope.configs.configs[i].name === name) {
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
                    if (!$scope.selectedConfig) {
                        $scope.selectedConfig = $scope.getConfig($scope.configs.defaultConfigName);
                    }
                    else if ($scope.selectedConfig.name != "") {
                        $scope.selectedConfig = $scope.getConfig($scope.selectedConfig.name);
                    }
                    $scope.originalConfig = angular.copy($scope.selectedConfig);
                }
            );
        };

        $scope.getConfigs();

        $scope.addConfig = function(oldConfig) {
            $scope.selectedConfig = {
                'name':'',
                'openMrsUrl':'',
                'username':'',
                'password':'',
                'motechPatientIdentifierTypeName':'',
                'patientIdentifierTypeNames':[]
            };
            $scope.onConfigChange($scope.selectedConfig, oldConfig);
        };

        $scope.draftChanged = function() {
            return !angular.equals($scope.selectedConfig, $scope.originalConfig);
        };

        $scope.patientIdentifierTypeNameExists = function() {
            for (var i=0; i<$scope.selectedConfig.patientIdentifierTypeNames.length; i++) {
                if ($scope.newPatientIdentifierType.name === $scope.selectedConfig.patientIdentifierTypeNames[i]) {
                    return true;
                }
            }
            return false;
        };

        $scope.validatePatientIdentifierType = function() {
            return $scope.newPatientIdentifierType.name
                && !$scope.patientIdentifierTypeNameExists();
        };

        $scope.addPatientIdentifierType = function() {
            $scope.selectedConfig.patientIdentifierTypeNames.push($scope.newPatientIdentifierType.name);
            $scope.newPatientIdentifierType.name = "";
            $scope.draftChanged();
        };

        $scope.removePatientIdentifierType = function(patientIdentifierTypeName) {
            $scope.selectedConfig.patientIdentifierTypeNames.splice($scope.selectedConfig.patientIdentifierTypeNames.indexOf(patientIdentifierTypeName), 1);
            $scope.draftChanged();
        };

        $scope.validateConfig = function() {
            return $scope.selectedConfig['name']
                && $scope.selectedConfig['openMrsUrl']
                && $scope.selectedConfig['username']
                && $scope.selectedConfig['password']
                && $scope.selectedConfig['motechPatientIdentifierTypeName'];
        };

        $scope.validateName = function() {
            //If name contains whitespaces
            if ($scope.selectedConfig.name.indexOf(' ') !== -1) {
                return false;
            }
            //If we create new config and give it an existing name
            else if ($scope.configsWithGivenName() == 1 && $scope.originalConfig.name === '') {
                return false;
            }
            //If we edit saved config and give it another saved config's name
            else if ($scope.configsWithGivenName() == 2) {
                return false;
            }
            else {
                return true;
            }
        };

        $scope.saveAllowed = function() {
            return $scope.validateConfig()
                && $scope.validateName()
                && !angular.equals($scope.selectedConfig, $scope.originalConfig);
        };

        $scope.configsWithGivenName = function() {
            var count = 0;
            for (var i = 0; i < $scope.configs.configs.length; i++) {
                if ($scope.selectedConfig.name === $scope.configs.configs[i].name) {
                    count++;
                }
            }
            return count;
        };

        $scope.saveConfig = function() {
            if (!$scope.configsWithGivenName()) {
                $scope.configs.configs.push($scope.selectedConfig);
            }
            else if ($scope.getConfig($scope.configs.defaultConfigName) === undefined) {
                $scope.configs.defaultConfigName = $scope.selectedConfig.name;
            }
            LoadingModal.open();
            OpenMRSConfig.save($scope.configs,
                function success() {
                    $scope.getConfigs();
                    $scope.clearMessages();
                    LoadingModal.close();
                },
                function failure() {
                    $scope.failureMessage = $scope.msg('openMRS.config.save.failure');
                    $scope.successMessage="";
                    LoadingModal.close();
                }
            );
        };

        $scope.deleteAllowed = function() {
            return angular.equals($scope.selectedConfig, $scope.originalConfig)
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
                    $scope.clearMessages();
                    LoadingModal.close();
                },
                function failure() {
                    $scope.failureMessage = $scope.msg('openMRS.config.delete.failure');
                    $scope.successMessage="";
                    LoadingModal.close();
                }
            );
        };

        $scope.isDefault = function() {
            return $scope.originalConfig.name === $scope.configs.defaultConfigName;
        }

        $scope.makeDefaultAllowed = function() {
            return !$scope.isDefault()
                && angular.equals($scope.selectedConfig, $scope.originalConfig)
                && $scope.validateConfig();
        }

        $scope.makeDefault = function() {
            $scope.configs.defaultConfigName = $scope.selectedConfig.name;
            LoadingModal.open();
            OpenMRSConfig.save($scope.configs,
                function success() {
                    $scope.selectedConfig = undefined;
                    $scope.getConfigs();
                    $scope.clearMessages();
                    LoadingModal.close();
                },
                function failure() {
                    $scope.failureMessage = $scope.msg('openMRS.config.makeDefault.failure');
                    $scope.successMessage="";
                    LoadingModal.close();
                }
            );
        };

        $scope.verifyConfig = function() {
            LoadingModal.open();
            OpenMRSConfig.verify($scope.selectedConfig,
                function success() {
                    $scope.successMessage=$scope.msg('openMRS.config.verify.success');
                    $scope.failureMessage="";
                    LoadingModal.close();
                },
                function failure(response) {
                    $scope.failureMessage=response.data;
                    $scope.successMessage="";
                    LoadingModal.close();
                }
            );
        };
    }
)})();