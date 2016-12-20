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
        $scope.formId = null;
        $scope.lastReceivedOn = null;
        $('#importCompleteAlert').fadeOut("slow");

        $scope.byDateRange = false;
        $scope.byFormId = false;
        $scope.importOptions = ['all', 'byDateRange', 'byFormId'];
        $scope.selectedImportOption = $scope.importOptions[0];
        $scope.setImportOption = function (index) {
            $scope.resetImportRequest();
            $scope.resetImportValues();
            $scope.selectedImportOption = $scope.importOptions[index];
            if ('byDateRange' === $scope.importOptions[index]) {
                $scope.byDateRange = true;
                $scope.byFormId = false;
            } else if ('byFormId' === $scope.importOptions[index]) {
                $scope.byFormId = true;
                $scope.byDateRange = false;
            } else {
                $scope.byDateRange = false;
                $scope.byFormId = false;
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
            "receivedOnEnd": $scope.receivedOnEnd,
            "formId": $scope.formId
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
            case 'formId':
                $scope.importRequest.formId = valueParameter;
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
                "receivedOnEnd": null,
                "formId": null
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
                        $('#importCompleteAlert').fadeIn("slow");
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

    controllers.controller('CommcareImportCasesCtrl', function ($scope, $http, LoadingModal, ModalFactory) {
        $scope.importOptions = ['all', 'byDateRange', 'byId'];
        $scope.importCasesProgressShow = false;
        $scope.importCasesComplete = false;
        $scope.totalCases = 0;
        $scope.casesImported = 0;
        $scope.statusError = false;
        $scope.errorMsg = 'Unknown error';
        $scope.importInProgress = false;
        $scope.modifiedDateStart = null;
        $scope.modifiedDateEnd = null;
        $scope.caseId = null;
        $scope.lastCaseId = null;
        $scope.lastReceivedOn = null;
        $('#importCompleteAlert').fadeOut("slow");

        $scope.byDateRange = false;
        $scope.byId = false;
        $scope.importRequest = {
                    "config": $scope.selectedConfig !== undefined? $scope.selectedConfig.name : '',
                    "caseId": $scope.caseId,
                    "modifiedDateStart": $scope.modifiedDateStart,
                    "modifiedDateEnd": $scope.modifiedDateEnd
        };
        $scope.selectedImportOption = $scope.importOptions[0];
        $scope.updateProgress = function () {
                    var percentage = Math.round(($scope.casesImported / $scope.totalCases) * 100);
                    percentage = !isNaN(percentage) && percentage !== Infinity && percentage >= 0 ? percentage : 0;
                    $('#commcareImportPercentage').text(percentage + '%').css({width: percentage + '%'}).attr('aria-valuenow', percentage);
                };
        $scope.setImportOption = function (index) {
                    $scope.resetImportRequest();
                    $scope.resetImportValues();
                    $scope.selectedImportOption = $scope.importOptions[index];
                    if ('byDateRange' === $scope.importOptions[index]) {
                        $scope.byDateRange = true;
                    } else {
                        $scope.byDateRange = false;
                    }
                    if ('byId' === $scope.importOptions[index]) {
                        $scope.byId = true;
                    } else {
                        $scope.byId = false;
                    }
                };
        $scope.resetImportRequest = function () {
                    $scope.importRequest = {
                        "config": $scope.selectedConfig.name = $scope.selectedConfig !== undefined? $scope.selectedConfig.name : '',
                        "caseId": null,
                        "modifiedDateStart": null,
                        "modifiedDateEnd": null
                    };
                };
        $scope.resetImportValues = function () {
                    $scope.initImportComplete = false;
                    $scope.importCasesProgressShow = false;
                    $scope.importCasesComplete = false;
                    $scope.totalCases = 0;
                    $scope.casesImported = 0;
                    $scope.statusError = false;
                    $scope.importInProgress = false;
                    $('#importCompleteAlert').fadeOut("slow");
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
                    case 'modifiedDateStart':
                        $scope.importRequest.modifiedDateStart = normalizeDate(valueParameter);
                        break;
                    case 'modifiedDateEnd':
                        $scope.importRequest.modifiedDateEnd = normalizeDate(valueParameter);
                        break;
                    case 'config':
                        $scope.importRequest.config = valueParameter;
                        break;
                    case 'caseId':
                        $scope.importRequest.caseId = valueParameter;
                        break;
                    default:
                        break;
                    }

                };
        $scope.importCasesStart = function () {
                    $scope.updateImportRequest();
                    $('#importCommcareCases').modal('show');
                    $scope.initImport();
                };
        $scope.initImport = function () {
                    $http.post('../commcare/case-import/init', $scope.importRequest).success( function(data) {
                         $scope.totalCases = data;
                         $scope.initImportComplete = true;
                    }).error(function(data) {
                         $scope.importError(data);
                    });
                };
        $scope.closeImportCases = function () {
                    $('#importCommcareCases').modal('hide');
                    $scope.importCasesProgressShow = false;
                };
        $scope.importCasesContinue = function () {
                    $scope.updateProgress();
                    if (!$scope.importCasesProgressShow) {
                        $scope.startImport();
                    }
                    $scope.importCasesProgressShow = true;
                };
        $scope.startImport = function () {
                    if (!$scope.importInProgress) {
                        var url = $scope.byId ? "../commcare/case-import/import-by-id" : "../commcare/case-import/start";
                        $http.post(url, $scope.importRequest).success( function(data) {
                            $scope.importInProgress = true;
                            $scope.lastCaseId = null;
                            $scope.lastReceivedOn = null;
                            $scope.checkStatus();
                        }).error(function(data) {
                            $scope.importError(data);
                        });
                    }
                };
        $scope.checkStatus = function () {
                    var getStatus = function () {
                        $http.get('../commcare/case-import/status').success(function(data) {
                            $scope.statusError = data.error;
                            if (data.casesImported > 0) {
                                $scope.casesImported = data.casesImported;
                            }

                            $scope.lastCaseId = data.lastImportCaseId;
                            $scope.lastReceivedOn = data.lastImportDate;

                            $scope.updateProgress();

                            if (data.casesImported === data.totalCases) {
                                $scope.importCasesProgressShow = false;
                                $('#importCommcareCases').modal('hide');
                                $scope.importCasesComplete = true;
                                $('#importCompleteAlert').fadeIn("slow");
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
        $scope.importError = function (msg) {
                    $scope.importCasesProgressShow = false;
                    $scope.errorMsg = msg;
                    clearInterval($scope.importStatusInterval);
                    $scope.statusError = true;
                };
    });

    controllers.controller('CommcareSettingsCtrl', function ($scope, Configurations, ModalFactory, LoadingModal, $timeout) {

        $scope.eventStrategyOptions = [ 'minimal', 'partial', 'full' ];

        $scope.selectedConfigBackup = undefined;

        $scope.rollback = false;

        $scope.newConfig = false;

        $scope.configOutdated = false;

        $scope.oldName = "";

        $scope.copyConfig = function(config) {

            if (!config) {
                $scope.configSaved = false;
                $scope.verified = false;
                return;
            }

            $scope.verified = true;
            $scope.configSaved = true;
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
                    ModalFactory.showConfirm({
                        title: $scope.msg('commcare.header.confirm'),
                        message: $scope.msg('commcare.confirm.discardChanges'),
                        type: 'type-warning',
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
                if(copy) {
                    $scope.selectedConfigBackup = copy.copy;
                    $scope.oldName = copy.oldName;
                }
                $scope.configOutdated = false;
                $scope.newConfig = false;
                $scope.clearMessages();
            }
        });

        $scope.draftChanged = function() {
            $scope.configOutdated = true;
            $scope.clearMessages();
            $scope.configSaved = false;
            $scope.verified = false;
        };


        Configurations.getBaseEndpointUrl(function(data) {
            $scope.baseUrl = data.message;
        });

        $scope.isDefaultConfig = function() {
            return $scope.selectedConfig.name === $scope.configurations.defaultConfigName;
        };

        $scope.addConfig = function() {
            LoadingModal.open();
            $scope.configSaved = false;
            $scope.verified = false;
            Configurations.create(
                function success(data) {
                    $scope.$parent.configurations.configs.push(data);
                    $scope.$parent.selectedConfig = data;
                    $scope.$parent.newlyCreatedConfig = data;
                    if ($scope.$parent.selectedConfig.eventStrategy === "") {
                        $scope.$parent.selectedConfig.eventStrategy = $scope.eventStrategyOptions[0];
                    }
                    LoadingModal.close();
                },
                function failure() {
                    LoadingModal.close();
                }
            );
        };

        $scope.deleteConfig = function() {
            if (!$scope.newConfig) {
                ModalFactory.showConfirm({
                    title: $scope.msg('commcare.header.confirm'),
                    message: $scope.msg('commcare.confirm.deleteConfig'),
                    type: 'type-warning',
                    callback: function(result) {
                        if (result) {
                            LoadingModal.open();
                            Configurations.remove({'name': $scope.selectedConfig.name},
                                function success() {
                                    $scope.$parent.selectedConfig = undefined;
                                    $scope.getConfigurations();
                                    LoadingModal.close();
                                },
                                function failure() {
                                    LoadingModal.close();
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
                && $scope.configSaved === false
                && $scope.verified === true;
        };

        $scope.syncConfig = function() {
            LoadingModal.open();
            Configurations.sync($scope.selectedConfig,
                function success() {
                    $scope.syncSuccessMessage = $scope.msg('commcare.sync.success');
                    $scope.syncSuccess = true;
                    $scope.syncedConfig = $scope.selectedConfig.name;
                    LoadingModal.close();
                },
                function failure(response) {
                    $scope.syncErrorMessage = response.data.message;
                    $scope.syncSuccess = false;
                    $scope.syncedConfig = $scope.selectedConfig.name;
                    LoadingModal.close();
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
            LoadingModal.open();
            Configurations.verify($scope.selectedConfig,
                function success()  {
                    $scope.verifySuccessMessage = $scope.msg('commcare.verify.success');
                    $scope.verifyErrorMessage = '';
                    $scope.connectionVerified = true;
                    $scope.verified = true;
                    LoadingModal.close();
                },
                function failure(response) {
                    $scope.verifyErrorMessage = response.data.message;
                    $scope.verifySuccessMessage = '';
                    $scope.connectionVerified = false;
                    $scope.verified = false;
                    LoadingModal.close();
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
            LoadingModal.open();
            Configurations.makeDefault($scope.selectedConfig.name,
                function success() {
                    $scope.getConfigurations();
                    LoadingModal.close();
                },
                function failure() {
                    LoadingModal.close();
                }
            );
        };

        $scope.saveUpdateConfig = function(element) {
            LoadingModal.open();
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
                    $scope.$parent.newlyCreatedConfig = undefined;
                    LoadingModal.close();
                },
                function failure(response) {
                    $scope.verifySuccessMessage = '';
                    $scope.verifyErrorMessage =  response.data.message;
                    $scope.connectionVerified = false;
                    LoadingModal.close();
                });
        };

        $scope.saveNewConfig = function(element) {
            LoadingModal.open();
            Configurations.save(
                $scope.selectedConfig,
                function success(data) {
                    $scope.verifySuccessMessage = $scope.msg('commcare.save.success');
                    $scope.verifyErrorMessage = '';
                    $scope.connectionVerified = true;
                    if (!$scope.configurations.defaultConfigName) {
                        $scope.configurations.defaultConfigName = $scope.selectedConfig.name;
                    }
                    $scope.updateConfig(data);
                    $scope.newConfig = false;
                    $scope.configOutdated = false;
                    $scope.$parent.newlyCreatedConfig = undefined;
                    LoadingModal.close();
                },
                function failure(response) {
                    $scope.verifySuccessMessage = '';
                    $scope.verifyErrorMessage =  response.data.message;
                    $scope.connectionVerified = false;
                    LoadingModal.close();
                });
        };

        $scope.saveConfig = function(element) {
            LoadingModal.open();
            if($scope.oldName) {
                $scope.saveUpdateConfig(element);
            } else {
                $scope.saveNewConfig(element);
            }
            $scope.configSaved = true;
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

    controllers.controller('CommcareModulesCtrl', function ($scope, Schema, LoadingModal) {

        $scope.formError = false;

        $scope.downloadingCases = false;

        $scope.formatJson = function(jsonResponse) {
            return JSON.stringify(jsonResponse, null, 4);
        };

        $scope.$watch('selectedConfig', function() {
            if (!$scope.$parent.selectedConfig) {
                return;
            }
            if ($scope.$parent.selectedConfig === $scope.$parent.newlyCreatedConfig) {
                return;
            }
            LoadingModal.open();
            $scope.applications = Schema.query({name: $scope.selectedConfig.name}, function() {
                $scope.formError = false;
                LoadingModal.close();
            }, function() {
                $scope.formError = true;
                LoadingModal.close();
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

    controllers.controller('CommcareReportsCtrl', function ($scope, $http, Reports, LoadingModal) {

            $scope.reportError = false;

            $scope.formatJson = function(jsonResponse) {
              return JSON.stringify(jsonResponse, null, 4);
            };

            $scope.$watch('selectedConfig', function() {
                if (!$scope.$parent.selectedConfig) {
                    return;
                }
                if ($scope.$parent.selectedConfig === $scope.$parent.newlyCreatedConfig) {
                    return;
                }
                LoadingModal.open();

                $scope.reports = Reports.query({name: $scope.selectedConfig.name}, function() {
                    $scope.reportError = false;
                    LoadingModal.close();
                }, function() {
                    $scope.reportError = true;
                    LoadingModal.close();
                });

                LoadingModal.close();
            });

            innerLayout({
                spacing_closed: 30,
                east__minSize: 200,
                east__maxSize: 350
            });
        });

}());
