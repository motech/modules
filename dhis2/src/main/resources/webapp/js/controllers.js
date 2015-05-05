(function () {
    'use strict';

    /* Controllers */
    var controllers = angular.module('dhis2.controllers', []);

    controllers.controller('Dhis2SettingsCtrl', function ($scope, $http) {

        $scope.retrievalError = false;
        $scope.updateError = false;
        $http.get('../dhis2/dhis2-settings')
            .success(function (response) {
                $scope.settings = response;
                $scope.originalSettings = angular.copy($scope.settings);
            })
            .error(function (response) {
                $scope.retrievalError = true;
            });

        $scope.sync = function () {
            $scope.blocked = true;
            $scope.success = null;
            $http.get('../dhis2/sync')

                .success(function (response) {
                    $scope.blocked = false;
                    $scope.success = response;
                })

                .error(function (response) {
                    $scope.blocked = false;
                    $scope.success = false;
                })
        };

        $scope.submit = function () {
            $http.post('../dhis2/dhis2-settings', $scope.settings)
                .success(function (response) {
                    $scope.settings = response;
                    $scope.originalSettings = angular.copy($scope.configs);
                })
                .error(function (response) {
                    $scope.updateError = true;
                });
        };
    });

    controllers.controller('Dhis2ProgramsCtrl', function ($scope, Programs) {
        $scope.formError = false;
        blockUI();

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

            unblockUI();
        }, function () {
            $scope.formError = true;
            unblockUI();
        });
        innerLayout({});

        $scope.collapse = function (item) {
            item.collapsed = !item.collapsed;

        };

        $scope.getTypeOfProgram = function(program) {
           if (program.singleEvent) {

               if (program.registration) {
                   return "dhis2.programType.singleEventWithRegistration"

               } else {
                   return "dhis2.programType.singleEventWithoutRegistration";
               }

           } else {
               return "dhis2.programType.MultipleEventsWithRegistration";
           }
        };
    });

    controllers.controller('Dhis2TrackedEntityAttributesCtrl', function($scope, TrackedEntityAttributes) {
        blockUI();
        $scope.trackedEntityAttributes = TrackedEntityAttributes.query(function() {
            unblockUI();
        }, function () {
           unblockUI();
        });
        innerLayout({});
    });

    controllers.controller('Dhis2TrackedEntitiesCtrl', function($scope, TrackedEntities) {
        blockUI();
        $scope.trackedEntities = TrackedEntities.query(function() {
            unblockUI();
        }, function () {
            unblockUI();
        });
        innerLayout({});
    });

    controllers.controller('Dhis2OrgUnitsCtrl', function($scope, OrgUnits) {
        blockUI();
        $scope.orgUnits = OrgUnits.query(function() {
            unblockUI();
        }, function () {
            unblockUI();
        });
        innerLayout({});
    });

    controllers.controller('Dhis2DataElementsCtrl', function($scope, DataElements) {
        blockUI();
        $scope.dataElements = DataElements.query(function() {
            unblockUI();
        }, function () {
            unblockUI();
        });
        innerLayout({});
    });

}());
