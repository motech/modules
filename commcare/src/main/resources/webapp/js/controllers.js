(function () {
    'use strict';

    /* Controllers */

    var controllers = angular.module('commcare.controllers', []);

    controllers.controller('CommcareBaseCtrl', function($scope, Configurations) {

        $scope.getDefault = function() {
            var i;

            for (i = 0; i < $scope.configurations.configs.length; i += 1) {
                if ($scope.configurations.configs[i].name === $scope.configurations.defaultConfigName) {
                    return $scope.configurations.configs[i];
                }
            }

            return undefined;
        };

        $scope.getConfigurations = function () {
            $scope.configurations = Configurations.get(function success(data) {
                var i;

                for (i = 0; i < data.configs.length; i += 1) {
                    if (data.configs[i].name === data.defaultConfigName) {
                        $scope.selectedConfig = data.configs[i];
                        return;
                    }
                }
            });
        };

        $scope.getConfigurations();
    });

    controllers.controller('CommcareSettingsCtrl', function ($scope, Configurations) {

        $scope.eventStrategyOptions = [ 'minimal', 'partial', 'full' ];

        $scope.selectedConfigBackup = undefined;

        $scope.rollback = false;

        $scope.newConfig = false;

        $scope.configOutdated = false;

        $scope.copyConfig = function(config) {
            var copy = {};

            copy.name = config.name;
            copy.accountConfig = {};
            copy.accountConfig.baseUrl = config.accountConfig.baseUrl;
            copy.accountConfig.domain = config.accountConfig.domain;
            copy.accountConfig.username = config.accountConfig.username;
            copy.accountConfig.password = config.accountConfig.password;
            copy.eventStrategy = config.eventStrategy;
            copy.forwardForms = config.forwardForms;
            copy.forwardSchema = config.forwardSchema;
            copy.forwardStubs = config.forwardStubs;
            copy.forwardCases = config.forwardCases;

            return copy;
        };

        $scope.$watch('selectedConfig', function(newValue, oldValue) {

            if ($scope.newConfig || $scope.configOutdated) {
                if (!$scope.rollback) {
                    jConfirm(jQuery.i18n.prop('commcare.confirm.discardChanges'), jQuery.i18n.prop("commcare.header.confirm"), function (val) {
                        if (!val) {
                            $scope.rollback = true;
                            $scope.$parent.selectedConfig = $scope.$parent.configurations.configs[$scope.$parent.configurations.configs.indexOf(oldValue)];
                            $scope.$parent.$apply();
                        } else if ($scope.newConfig) {
                            $scope.$parent.configurations.configs.splice($scope.$parent.configurations.configs.indexOf(oldValue), 1);
                            $scope.$parent.$apply();
                            $scope.configOutdated = false;
                            if (newValue === undefined || newValue.name !== "") {
                                $scope.newConfig = false;
                            }
                            $scope.clearMessages();
                        } else if ($scope.configOutdated) {
                            $scope.$parent.configurations.configs[$scope.$parent.configurations.configs.indexOf(oldValue)] = $scope.selectedConfigBackup;
                            $scope.configOutdated = false;
                            if (newValue.name === "") {
                                $scope.newConfig = true;
                            }
                            $scope.clearMessages();
                        }
                    });
                } else {
                    $scope.rollback = false;
                }
            } else if (newValue.name === "") {
                $scope.newConfig = true;
                $scope.clearMessages();
            } else {
                $scope.selectedConfigBackup = $scope.copyConfig(newValue);
                $scope.configOutdated = false;
                $scope.newConfig = false;
                $scope.clearMessages();
            }
        });

        $scope.draftChanged = function() {
            $scope.configOutdated = true;
            $scope.clearMessages();
        };


        Configurations.getBaseEndpointUrl(function(data) {
            if (data.message !== "null/module/commcare/" && data.message !=="/module/commcare/") {
                $scope.baseUrl = data.message;
            }
        });

        $scope.isDefaultConfig = function() {
            return $scope.selectedConfig.name === $scope.configurations.defaultConfigName;
        };

        $scope.addConfig = function() {
            blockUI();
            Configurations.create(
                function success(data) {
                    $scope.$parent.configurations.configs.push(data);
                    $scope.$parent.selectedConfig = data;
                    if ($scope.$parent.selectedConfig.eventStrategy === "") {
                        $scope.$parent.selectedConfig.eventStrategy = $scope.eventStrategyOptions[0];
                    }
                    unblockUI();
                },
                function failure() {
                    unblockUI();
                }
            );
        };

        $scope.deleteConfig = function() {
            if (!$scope.newConfig) {
                blockUI();
                Configurations.remove({'name': $scope.selectedConfig.name},
                    function success() {
                        $scope.$parent.selectedConfig = undefined;
                        $scope.getConfigurations();
                        unblockUI();
                    },
                    function failure() {
                        unblockUI();
                    }
                );
            } else {
                $scope.$parent.selectedConfig = $scope.getDefault();
            }
        };

        $scope.saveAllowed = function() {
            return $scope.validateConfig()
                && $scope.validateConfigName()
                && $scope.validateUrlAndDomain()
                && $scope.configOutdated;
        };

        $scope.verify = function() {
            blockUI();
            Configurations.verify($scope.selectedConfig,
                function success()  {
                    $scope.verifySuccessMessage = $scope.msg('commcare.verify.success');
                    $scope.verifyErrorMessage = '';
                    $scope.connectionVerified = true;
                    unblockUI();
                },
                function failure(response) {
                    $scope.verifyErrorMessage = response.data;
                    $scope.verifySuccessMessage = '';
                    $scope.connectionVerified = false;
                    $('.commcare .switch-small').bootstrapSwitch('setActive', false);
                    unblockUI();
                });
        };

        $scope.validateConfig = function() {
            return $scope.hasValue('name')
                && $scope.hasValue('eventStrategy')
                && $scope.hasValue('forwardCases')
                && $scope.hasValue('forwardForms')
                && $scope.hasValue('forwardStubs')
                && $scope.hasValue('forwardSchema')
                && $scope.hasAccountValue('baseUrl')
                && $scope.hasAccountValue('domain')
                && $scope.hasAccountValue('username')
                && $scope.hasAccountValue('password');
        };

        $scope.validateConfigName = function() {

            if ($scope.selectedConfig.name.indexOf(' ') !== -1) {
                return false;
            }

            var i, count = 0;

            for (i = 0; i < $scope.configurations.configs.length; i += 1) {
                if ($scope.selectedConfig.name === $scope.configurations.configs[i].name) {
                    if (count > 0) {
                        return false;
                    } else {
                        count += 1;
                    }
                }
            }

            return true;
        };

        $scope.validateUrlAndDomain = function () {

            var i, count = 0;

            for (i = 0; i < $scope.configurations.configs.length; i += 1) {
                if ($scope.selectedConfig.accountConfig.baseUrl === $scope.configurations.configs[i].accountConfig.baseUrl &&
                    $scope.selectedConfig.accountConfig.domain === $scope.configurations.configs[i].accountConfig.domain) {

                    if (count > 0) {
                        return false;
                    } else {
                        count += 1;
                    }
                }
            }

            return true;
        };

        $scope.isVerifyError = function() {
            return $scope.validateConfig() === true && $scope.connectionVerified === false;
        };

        $scope.isVerifySuccess = function() {
            return $scope.validateConfig() === true && $scope.connectionVerified === true;
        };

        $scope.hasAccountValue = function(prop) {
            return $scope.selectedConfig.accountConfig[prop] !== null &&
                $scope.selectedConfig.accountConfig[prop] !== undefined &&
                $scope.selectedConfig.accountConfig[prop] !== "";
        };

        $scope.hasValue = function(prop) {
            return $scope.selectedConfig[prop] !== null &&
                $scope.selectedConfig[prop] !== undefined &&
                $scope.selectedConfig[prop] !== "";
        };

        $scope.makeDefault = function() {
            blockUI();
            Configurations.makeDefault($scope.selectedConfig.name,
                function success() {
                    $scope.getConfigurations();
                    unblockUI();
                },
                function failure() {
                    unblockUI();
                }
            );
        };

        $scope.saveConfig = function(element) {
            blockUI();
            Configurations.save($scope.selectedConfig,
                function success(data) {
                    $scope.verifySuccessMessage = $scope.msg('commcare.save.success');
                    $scope.verifyErrorMessage = '';
                    $scope.connectionVerified = true;
                    if ($scope.configurations.defaultConfigName === "") {
                        $scope.configurations.defaultConfigName = $scope.selectedConfig.name;
                    }
                    $scope.updateConfig(data);
                    $scope.newConfig = false;
                    $scope.configOutdated = false;
                    unblockUI();
                },
                function failure(response) {
                    $scope.verifySuccessMessage = '';
                    $scope.verifyErrorMessage =  response.data;
                    $scope.connectionVerified = false;
                    unblockUI();
                });
        };

        $scope.updateConfig = function (config) {
            var i;

            for (i = 0; i < $scope.$parent.configurations.configs.length; i += 1) {
                if ($scope.$parent.configurations.configs[i].name === config.name) {
                    $scope.$parent.configurations.configs[i] = config;
                    $scope.$parent.selectedConfig = $scope.configurations.configs[i];
                    return;
                }
            }
        };

        $scope.clearMessages = function() {
            $scope.connectionVerified = undefined;
            $scope.verifySuccessMessage = "";
            $scope.verifyErrorMessage = "";
        };
    });

    controllers.controller('CommcareModulesCtrl', function ($scope, Schema) {

        $scope.formError = false;

        $scope.downloadingCases = false;

        $scope.formatJson = function(jsonResponse) {
            return JSON.stringify(jsonResponse, null,4);
        };

        $scope.$watch('selectedConfig', function() {
            if (!$scope.$parent.selectedConfig) {
                return;
            }
            blockUI();
            $scope.applications = Schema.query({name: $scope.selectedConfig.name}, function() {
                $scope.formError = false;
                unblockUI();
            }, function() {
                $scope.formError = true;
                unblockUI();
            });
        });
    });

    controllers.controller('CommcareCaseSchemasCtrl', function ($scope, Cases, Configurations) {

        innerLayout({
            spacing_closed: 30,
            east__minSize: 200,
            east__maxSize: 350
        }, {
            show: true,
            button: '#commcare-case-filters'
        });

        $scope.formatJson=function(jsonResponse) {
            return JSON.stringify(jsonResponse, null, 4);
        };

        $scope.formatModalContent = function (rowObject) {
            var contentView = '';
            $.each(rowObject, function( key, value ) {
                if (key !== 'xformIds' && key !== 'indices' && key !== 'fieldValues') {
                    contentView = contentView + '<div><label class="col-md-4 control-label">'
                        + $scope.msg("commcare.case.field." + key) + ':</label><div class="input-xlarge-fluid form-list-inline">' + value + '</div></div>';
                } else {
                    var strValue = '';
                    $.each(value, function( key2, value2 ) {
                        strValue = strValue + '<div>' + key2 + ': ' + value2 + '</div>';
                    });
                    contentView = contentView + '<div><label class="col-md-4 control-label">'
                    + $scope.msg("commcare.case.field." + key) + ':</label><div class="input-xlarge-fluid form-list-inline">' + strValue + '</div></div>';
                }
            });
            return contentView;
        };
    });

}());
