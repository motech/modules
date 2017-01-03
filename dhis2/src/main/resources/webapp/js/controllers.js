(function () {
    'use strict';

    /* Controllers */
    var controllers = angular.module('dhis2.controllers', []);

    controllers.controller('Dhis2SettingsCtrl', function ($scope, Sync, $http) {

            $scope.retrievalError = false;
            $scope.updateError = false;
            $scope.configOutdated = true;
            $http.get('../dhis2/dhis2-settings')
                .success(function (response) {
                    $scope.settings = response;
                    $scope.originalSettings = angular.copy($scope.settings);
                })
                .error(function (response) {
                    $scope.retrievalError = true;
                });

            $scope.sync = Sync.sync;
            $scope.isSyncing = Sync.isSyncing;
            $scope.success = Sync.isLastSuccessful;

            $scope.submit = function () {
                if(!$scope.settings.serverURI.match("^https?:\/\/.+")) {
                    $scope.settings.serverURI = "http://" + $scope.settings.serverURI;
                }

                $http.post('../dhis2/dhis2-settings', $scope.settings)
                    .success(function (response) {
                        $scope.verifySuccessMessage = $scope.msg('dhis2.web.settings.save.success');
                        $scope.verifyErrorMessage = '';
                        $scope.settings = response;
                        $scope.originalSettings = angular.copy($scope.configs);
                        $scope.configOutdated = false;
                        $scope.updateError = false;
                    })
                    .error(function (response) {
                        $scope.verifySuccessMessage = '';
                        $scope.verifyErrorMessage =  $scope.msg('dhis2.web.settings.save.fail') + ' ' + response.data;
                        $scope.configOutdated = false;
                        $scope.updateError = true;
                    });
            };

            $scope.isVerifyError = function() {
                return $scope.updateError && !$scope.configOutdated;
            };

            $scope.isVerifySuccess = function() {
                return !$scope.updateError && !$scope.configOutdated;
            };

            $scope.draftChanged = function() {
                $scope.configOutdated = true;
                $scope.clearMessages();
            };

            $scope.clearMessages = function() {
                $scope.updateError = false;
                $scope.verifySuccessMessage = '';
                $scope.verifyErrorMessage = '';
            };
        });

    controllers.controller('Dhis2ProgramsCtrl', function ($scope, Programs, LoadingModal) {
        $scope.formError = false;
        LoadingModal.open();

        $scope.programs = Programs.query(function () {
            $scope.formError = false;

            angular.forEach($scope.programs, function(program) {
                program.collapsed = true;
                program.attributes.collapsed = true;
                program.stages.collapsed = true;

                angular.forEach(program.stages, function(stage) {
                   stage.collapsed= true;
                });

            });

            LoadingModal.close();
        }, function () {
            $scope.formError = true;
            LoadingModal.close();
        });
        innerLayout({});

        $scope.collapse = function (item) {
            item.collapsed = !item.collapsed;

        };

        $scope.getTypeOfProgram = function(program) {
            if (program.programType !== undefined && program.programType !== null) {
                return program.programType === 'WITH_REGISTRATION' ? "dhis2.programType.withRegistration" : "dhis2.programType.withoutRegistration"
            } else if (program.singleEvent) {
                return program.registration ? "dhis2.programType.singleEventWithRegistration" : "dhis2.programType.singleEventWithoutRegistration"
            } else {
                return "dhis2.programType.MultipleEventsWithRegistration";
            }
        };
    });

    controllers.controller('Dhis2TrackedEntityAttributesCtrl', function($scope, TrackedEntityAttributes, LoadingModal) {
        LoadingModal.open();
        $scope.trackedEntityAttributes = TrackedEntityAttributes.query(function() {
            LoadingModal.close();
        }, function () {
           LoadingModal.close();
        });
        innerLayout({});
    });

    controllers.controller('Dhis2TrackedEntitiesCtrl', function($scope, TrackedEntities, LoadingModal) {
        LoadingModal.open();
        $scope.trackedEntities = TrackedEntities.query(function() {
            LoadingModal.close();
        }, function () {
            LoadingModal.close();
        });
        innerLayout({});
    });

    controllers.controller('Dhis2OrgUnitsCtrl', function($scope, OrgUnits, LoadingModal) {
        LoadingModal.open();
        $scope.orgUnits = OrgUnits.query(function() {
            LoadingModal.close();
        }, function () {
            LoadingModal.close();
        });
        innerLayout({});
    });

    controllers.controller('Dhis2DataElementsCtrl', function($scope, DataElements, LoadingModal) {
        LoadingModal.open();
        $scope.dataElements = DataElements.query(function() {
            LoadingModal.close();
        }, function () {
            LoadingModal.close();
        });
        innerLayout({});
    });

}());
