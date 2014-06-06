(function () {
    'use strict';

    /* Controllers */

    var controllers = angular.module('commcare.controllers', []);

    controllers.controller('SettingsCtrl', function ($scope, Settings, Connection, CommcarePermissions) {

        $scope.permissions = CommcarePermissions.query();

        $scope.eventStrategyOptions = [ 'minimal', 'partial', 'full' ];

        $scope.settings = Settings.get(function success() {
            if (!$scope.settings.eventStrategy) {
                $scope.settings.eventStrategy = $scope.eventStrategyOptions[0];
            }
            $scope.verify();
        });

        $scope.verify = function() {
            blockUI();
            Connection.verify($scope.settings.accountSettings,
                function success()  {
                    $scope.verifySuccessMessage = $scope.msg('commcare.verify.success');
                    $scope.verifyErrorMessage = '';
                    $scope.connectionVerified = true;
                    $scope.getVerifiedSettings();
                    unblockUI();
                },
                function failure(response) {
                    $scope.verifyErrorMessage = response.data.message;
                    $scope.verifySuccessMessage = '';
                    $scope.connectionVerified = false;
                    $('.commcare .switch-small').bootstrapSwitch('setActive', false);
                    unblockUI();
                });
        };


        $scope.isVerifyError = function() {
            return $scope.connectionVerified === false;
        };

        $scope.isVerifySuccess = function() {
            return $scope.canMakeConnection() === true && $scope.connectionVerified === true;
        };

        $scope.hasValue = function(prop) {
            return $scope.settings.hasOwnProperty(prop) && $scope.settings[prop] !== undefined;
        };

        $scope.hasAccountValue = function(prop) {
            return $scope.settings.hasOwnProperty('accountSettings') && $scope.settings.accountSettings[prop] !== undefined;
        };

        $scope.canMakeConnection = function() {
            return $scope.hasAccountValue('commcareBaseUrl') &&
                $scope.hasAccountValue('commcareDomain') &&
                $scope.hasAccountValue('username') &&
                $scope.hasAccountValue('password');
        };

        $scope.saveSettings = function(element) {
            $scope.settings.$save(
                function success() {
                    var controlWrapper = $(element).next('.form-hints');

                    $(controlWrapper).children('.save-status').remove();
                    controlWrapper.append("<span class='save-status form-hint-success'><i class='icon-ok icon-white'></i> {0}</span>".format($scope.msg('commcare.settings.success.value.saved')));

                    $(controlWrapper.children('.save-status')[0]).delay(5000).fadeOut(function() {
                        $(this).remove();
                    });
                },
                function error() {
                    var controlWrapper = $(element).next('.form-hints');

                    $(controlWrapper).children('.save-status').remove();
                    controlWrapper.append("<span class='save-status form-hint'><i class='icon-remove icon-white'></i> {0}</span>".format($scope.msg('commcare.settings.error.value.saved')));

                    $(controlWrapper.children('.save-status')[0]).delay(10000).fadeOut(function() {
                        $(this).remove();
                    });
                });
        };

        /* In CommCare API v0.4 deleting forwarding rules is not supported */
        $scope.blockSwitch = function(element) {
            $(element).bootstrapSwitch('setActive', false);
            $(element).click(function () {
                var controlWrapper = $(element).next('.form-hints');

                $(controlWrapper).children('.save-status').remove();
                controlWrapper.append("<span class='save-status form-hint'><i class='icon-remove icon-white'></i> {0}</span>".format($scope.msg('commcare.settings.error.rule')));

                $(controlWrapper.children('.save-status')[0]).delay(10000).fadeOut(function() {
                    $(this).remove();
                });
            });
        };

        $scope.getVerifiedSettings = function() {
            $scope.settings = Settings.get(function success() {
                if (!$scope.settings.eventStrategy) {
                    $scope.settings.eventStrategy = $scope.eventStrategyOptions[0];
                }
                if($scope.settings.forwardForms) {
                    $('#forwardFormsSwitch').bootstrapSwitch('setState', true);
                    $scope.blockSwitch('#forwardFormsSwitch');
                } else {
                    $('#forwardFormsSwitch').bootstrapSwitch('setState', false);
                    $('#forwardFormsSwitch').bootstrapSwitch('setActive', true);
                }
                if($scope.settings.forwardCases) {
                    $('#forwardCasesSwitch').bootstrapSwitch('setState', true);
                    $scope.blockSwitch('#forwardCasesSwitch');
                } else {
                    $('#forwardCasesSwitch').bootstrapSwitch('setState', false);
                    $('#forwardCasesSwitch').bootstrapSwitch('setActive', true);
                }
                if($scope.settings.forwardFormStubs) {
                    $('#forwardFormStubsSwitch').bootstrapSwitch('setState', true);
                    $scope.blockSwitch('#forwardFormStubsSwitch');
                } else {
                    $('#forwardFormStubsSwitch').bootstrapSwitch('setState', false);
                    $('#forwardFormStubsSwitch').bootstrapSwitch('setActive', true);
                }
                if($scope.settings.forwardAppStructure) {
                    $('#forwardAppStructureSwitch').bootstrapSwitch('setState', true);
                    $scope.blockSwitch('#forwardAppStructureSwitch');
                } else {
                    $('#forwardAppStructureSwitch').bootstrapSwitch('setState', false);
                    $('#forwardAppStructureSwitch').bootstrapSwitch('setActive', true);
                }
            });
        };

        $('#forwardFormsSwitch').change(function () {
            if($scope.settings.forwardForms) {
                $scope.blockSwitch(this);
            }
        });
        $('#forwardCasesSwitch').change(function () {
              if($scope.settings.forwardCases) {
                 $scope.blockSwitch(this);
              }
        });
        $('#forwardFormStubsSwitch').change(function () {
              if($scope.settings.forwardFormStubs) {
                 $scope.blockSwitch(this);
              }
        });
        $('#forwardAppStructureSwitch').change(function () {
              if($scope.settings.forwardAppStructure) {
                 $scope.blockSwitch(this);
              }
        });

    });

    controllers.controller('ModulesCtrl', function ($scope, Schema) {
        $scope.formError = false;
        $scope.formatJson=function(jsonResponse){return JSON.stringify(jsonResponse, null,4);};
        blockUI();
        $scope.applications = Schema.query(function() {
            $scope.formError = false;
            unblockUI();
        }, function() {
            $scope.formError = true;
            unblockUI();
        });
    });

    controllers.controller('CaseSchemasCtrl', function ($scope, Cases) {
        $scope.caseError = false;
        $scope.formatJson=function(jsonResponse){return JSON.stringify(jsonResponse, null,4);};
        blockUI();
        $scope.cases = Cases.query(function() {
            $scope.caseError = false;
            unblockUI();
        }, function() {
            $scope.caseError = true;
            unblockUI();
        });
    });

}());
