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

    controllers.controller('CommcareImportFormsCtrl', function ($scope, Configurations, $http) {

        $scope.importFormsProgressShow = false;
        $scope.importFormsComplete = false;
        $scope.totalForms = 0;
        $scope.formsImported = 0;
        $scope.statusError = false;
        $scope.errorMsg = 'Unknown error';
        $scope.importInProgress = false;
        $scope.receivedOnStart = null;
        $scope.receivedOnEnd = null;
        $scope.lastFormId = null;
        $scope.lastReceivedOn = null;
        $('#importCompleteAlert').fadeOut("slow");

        $scope.byDateRange = false;
        $scope.importOptions = ['all', 'byDateRange'];
        $scope.selectedImportOption = $scope.importOptions[0];
        $scope.setImportOption = function (index) {
            $scope.resetImportRequest();
            $scope.resetImportValues();
            $scope.selectedImportOption = $scope.importOptions[index];
            if ('byDateRange' === $scope.importOptions[index]) {
                $scope.byDateRange = true;
            } else {
                $scope.byDateRange = false;
            }
        };

        $scope.$watch('selectedConfig', function() {
            if (!$scope.$parent.selectedConfig) {
                return;
            }
        });

        $scope.importFormsStart = function () {
            $scope.updateImportRequest();
            $('#importCommcareForms').modal('show');
            $scope.initImport();
        };

        $scope.importFormsContinue = function () {
            $scope.updateProgress();
            if (!$scope.importFormsProgressShow) {
                $scope.startImport();
            }
            $scope.importFormsProgressShow = true;
        };

        $scope.closeImportForms = function () {
            $('#importCommcareForms').modal('hide');
            $scope.importFormsProgressShow = false;
        };

        $scope.updateProgress = function () {
            var percentage = Math.round(($scope.formsImported / $scope.totalForms) * 100);
            percentage = !isNaN(percentage) && percentage !== Infinity && percentage >= 0 ? percentage : 0;
            $('#commcareImportPercentage').text(percentage + '%').css({width: percentage + '%'}).attr('aria-valuenow', percentage);
        };

        $scope.resetImportValues = function () {
            $scope.initImportComplete = false;
            $scope.importFormsProgressShow = false;
            $scope.importFormsComplete = false;
            $scope.totalForms = 0;
            $scope.formsImported = 0;
            $scope.statusError = false;
            $scope.importInProgress = false;
            $('#importCompleteAlert').fadeOut("slow");
        };

        $scope.importRequest = {
            "config": $scope.selectedConfig !== undefined? $scope.selectedConfig.name : '',
            "receivedOnStart": $scope.receivedOnStart,
            "receivedOnEnd": $scope.receivedOnEnd
        };

        $scope.updateImportRequest = function (nameParameter, valueParameter) {
            $scope.resetImportValues();
            var dValue,
            normalizeDate = function (dateValue) {
                var indexTValue = dateValue.indexOf('T');
                dValue = dateValue.replace(' ', '');
                dValue = dValue.replace(' ', '');
                return  dValue;
            };

            switch (nameParameter) {
            case 'receivedOnStart':
                $scope.importRequest.receivedOnStart = normalizeDate(valueParameter);
                break;
            case 'receivedOnEnd':
                $scope.importRequest.receivedOnEnd = normalizeDate(valueParameter);
                break;
            case 'config':
                $scope.importRequest.config = valueParameter;
                break;
                default:
                break;
            }

        };

        $scope.resetImportRequest = function () {
            $scope.importRequest = {
                "config": $scope.selectedConfig.name = $scope.selectedConfig !== undefined? $scope.selectedConfig.name : '',
                "receivedOnStart": null,
                "receivedOnEnd": null
            };
        };

        $scope.importStatusInterval = {};

        $scope.checkStatus = function () {
            var getStatus = function () {
                $http.get('../commcare/form-import/status').success(function(data) {
                    $scope.statusError = data.error;
                    if (data.formsImported > 0) {
                        $scope.formsImported = data.formsImported;
                    }

                    $scope.lastFormId = data.lastImportFormId;
                    $scope.lastReceivedOn = data.lastImportDate;

                    $scope.updateProgress();

                    if (data.formsImported === data.totalForms) {
                        $scope.importFormsProgressShow = false;
                        $('#importCommcareForms').modal('hide');
                        $scope.importFormsComplete = true;
                        clearInterval($scope.importStatusInterval);
                    }

                    if (!data.error) {
                        $scope.importInProgress = data.importInProgress;
                    } else {
                        $scope.importError(data.errorMsg);
                    }
                }).error(function(data) {
                    $scope.importError(data);
                });
            };
            $scope.importStatusInterval = setInterval(function () {
                getStatus();
            }, 2000);
        };

        $scope.initImport = function () {
            $http.post('../commcare/form-import/init', $scope.importRequest).success( function(data) {
                 $scope.totalForms = data;
                 $scope.initImportComplete = true;
            }).error(function(data) {
                 $scope.importError(data);
            });
        };

        $scope.startImport = function () {
            if (!$scope.importInProgress) {
                $http.post('../commcare/form-import/start', $scope.importRequest).success( function(data) {
                    $scope.importInProgress = true;
                    $scope.lastFormId = null;
                    $scope.lastReceivedOn = null;
                    $scope.checkStatus();
                }).error(function(data) {
                    $scope.importError(data);
                });
            }
        };

        $scope.importError = function (msg) {
            $scope.importFormsProgressShow = false;
            $scope.errorMsg = msg;
            clearInterval($scope.importStatusInterval);
            $scope.statusError = true;
        };

        innerLayout({
            spacing_closed: 30,
            east__minSize: 200,
            east__maxSize: 350
        });
    });

    controllers.controller('CommcareSettingsCtrl', function ($scope, Configurations) {

        $scope.eventStrategyOptions = [ 'minimal', 'partial', 'full' ];

        $scope.selectedConfigBackup = undefined;

        $scope.rollback = false;

        $scope.newConfig = false;

        $scope.configOutdated = false;

        $scope.oldName = "";

        $scope.copyConfig = function(config) {
            if (!config) {
                return;
            }
            var oldName, copy = {};

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
            oldName = copy.name;

            return {
                copy: copy,
                oldName: oldName
            };
        };

        innerLayout({
            spacing_closed: 30,
            east__minSize: 200,
            east__maxSize: 350
        });

        $scope.$watch('selectedConfig', function(newValue, oldValue) {

            if ($scope.newConfig || $scope.configOutdated) {
                if (!$scope.rollback) {
                    BootstrapDialog.confirm({
                        title: $scope.msg('commcare.header.confirm'),
                        message: $scope.msg('commcare.confirm.discardChanges'),
                        type: BootstrapDialog.TYPE_WARNING,
                        callback: function(result) {
                            if (!result) {
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
                        }
                    });
                } else {
                    $scope.rollback = false;
                }
            } else if (newValue !== undefined && newValue.name === "") {
                $scope.newConfig = true;
                $scope.oldName = "";
                $scope.clearMessages();
            } else {
                var copy = {};

                copy = $scope.copyConfig(newValue);
                $scope.selectedConfigBackup = copy.copy;
                $scope.oldName = copy.oldName;
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
            $scope.baseUrl = data.message;
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
                BootstrapDialog.confirm({
                    title: $scope.msg('commcare.header.confirm'),
                    message: $scope.msg('commcare.confirm.deleteConfig'),
                    type: BootstrapDialog.TYPE_WARNING,
                    callback: function(result) {
                        if (result) {
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
                        }
                    }
                });
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

        $scope.syncConfig = function() {
            blockUI();
            Configurations.sync($scope.selectedConfig,
                function success() {
                    $scope.syncSuccessMessage = $scope.msg('commcare.sync.success');
                    $scope.syncSuccess = true;
                    $scope.syncedConfig = $scope.selectedConfig.name;
                    unblockUI();
                },
                function failure(response) {
                    $scope.syncErrorMessage = response.data;
                    $scope.syncSuccess = false;
                    $scope.syncedConfig = $scope.selectedConfig.name;
                    unblockUI();
            });
        };

        $scope.syncAllowed = function() {
            return $scope.validateConfig()
                && !($scope.configOutdated);
        };

        $scope.isSyncSuccess = function() {
            return $scope.syncSuccess === true
                && $scope.syncedConfig === $scope.selectedConfig.name;
        };

        $scope.isSyncError = function() {
            return $scope.syncSuccess === false
                && $scope.syncedConfig === $scope.selectedConfig.name;
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

        $scope.validateUrlProtocol = function () {
            return $scope.selectedConfig.accountConfig.baseUrl.startsWith('https://');
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

        $scope.saveUpdateConfig = function(element) {
            blockUI();
            Configurations.save({
                    oldName: $scope.oldName
                },
                $scope.selectedConfig,
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

        $scope.saveNewConfig = function(element) {
            blockUI();
            Configurations.save(
                $scope.selectedConfig,
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

        $scope.saveConfig = function(element) {
            blockUI();
            if($scope.oldName) {
                $scope.saveUpdatedConfig(element);
            } else {
                $scope.saveNewConfig(element);
            }
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
            $scope.syncSuccess = undefined;
            $scope.syncedConfig = "";
            $scope.syncErrorMessage = "";
            $scope.syncSuccessMessage = "";
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

        innerLayout({
            spacing_closed: 30,
            east__minSize: 200,
            east__maxSize: 350
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

        $scope.$watch('selectedConfig', function() {
            if (!$scope.$parent.selectedConfig) {
                return;
            }
        });

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
