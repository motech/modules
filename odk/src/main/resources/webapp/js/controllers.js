(function () {
    'use strict';

    /* Controllers */
    var controllers = angular.module('odk.controllers', []);

    controllers.controller('SettingsCtrl', function ($scope, $timeout, Config, Verify) {

        $scope.saveSuccess = false;
        $scope.saveError = false;
        $scope.deleteSuccess = false;
        $scope.deleteError = false;
        $scope.getConfigError = false;
        $scope.verifiedSuccess = false;
        $scope.verifiedFail = false;
        $scope.verifiedError = false;
        $scope.configTypes = [{name: "ODK", type: "ODK"}, {name: "Ona", type: "ONA"}, {name: "Kobotoolbox", type: "KOBO"}];
        $scope.url = null;
        var href = window.location.href.split("/server")[0] + "/odk/forms/";


        $scope.getConfigs = function () {
            Config.query(function (configs) {
                $scope.configs = configs;
            }, function (err) {
                console.log(err);
                $scope.getConfigError = true;
                $timeout(function () {
                    $scope.getConfigError = false;
                }, 5000)

            });
        };

        $scope.getConfigs();

        $scope.saveConfig = function () {
            Config.save($scope.selectedConfig, function () {
                $scope.saveSuccess = true;
                $scope.getConfigs();
                $timeout(function () {
                    $scope.saveSuccess = false;
                }, 5000);

            }, function (err) {
                console.log(err);
                $scope.saveError = true;
                $timeout(function () {
                    $scope.saveError = false;
                }, 5000);

            });
        };

        $scope.addConfig = function () {
            $scope.selectedConfig = new Config({verified: false});
        };

        $scope.deleteConfig = function () {

            Config.delete({name: $scope.selectedConfig.name}, function () {
                $scope.selectedConfig = null;
                $scope.getConfigs();
                $scope.deleteSuccess = true;
                $timeout(function () {
                    $scope.deleteSuccess = false;
                }, 5000);
            }, function (error) {
                console.log(error);
                $scope.deleteError = true;
                $timeout(function () {
                    $scope.deleteError = false;
                }, 5000);
            });
        };

        $scope.verifyConfig = function () {
            switch ($scope.selectedConfig.type) {
                case "KOBO":
                    Verify.kobo($scope.selectedConfig, function (data) {
                        success(data)
                    }, function (err) {
                        error(err)
                    });
                    break;

                case "ONA":
                    Verify.ona($scope.selectedConfig, function (data) {
                        success(data)
                    }, function (err) {
                        error(err)
                    });
                    break;

                case "ODK":
                    Verify.odk($scope.selectedConfig, function (data) {
                        success(data)
                    }, function (err) {
                        error(err)
                    });
            }

            var success = function (data) {
                $scope.selectedConfig.verified = data.verified;
                if (data.verified === true) {
                    $scope.verifiedSuccess = true;
                    $timeout(function () {
                        $scope.verifiedSuccess = false;
                    }, 5000);
                } else {
                    $scope.verifiedFail = true;
                    $timeout(function () {
                        $scope.verifiedFail = false;
                    }, 5000);
                }
            };

            var error = function (err) {
                $scope.selectedConfig.verified = false;
                console.log(err);
                $scope.verifiedFail = true;
                $timeout(function () {
                    $scope.verifiedFail = false;
                }, 5000);
            };
        };

        $scope.unverify = function () {
            $scope.selectedConfig.verified = false;
        };

        $scope.changeUrl = function () {
            $scope.url = href + $scope.selectedConfig.name + "/{Form Title}";
        };

    });

    controllers.controller('FormsCtrl', function ($scope, $timeout, Config, Import, FormDefinition) {
        $scope.importSuccess = false;
        $scope.importFail = false;
        $scope.importing = false;
        $scope.formDefinitions = [];
        $scope.href = window.location.href.split("/server")[0] + "/odk/forms/";


        var importFail = function () {
            $scope.importFail = true;
            $timeout(function () {
                $scope.importFail = false;
            }, 5000);
        };

        Config.query(function (configs) {
            $scope.configs = configs;
        }, function (err) {
            console.log(err);
        });


        $scope.import = function () {
            $scope.importing = true;
            Import.get({name: $scope.selectedConfig.name}, function (status) {
                $scope.importing = false;
                if (status.imported == true) {
                    $scope.getFormDefinitions();
                    $scope.importSuccess = true;
                    $timeout(function () {
                        $scope.importSuccess = false;
                    }, 5000);
                } else {
                    importFail();
                }

            }, function (err) {
                console.log(err);
                $scope.importing = false;
                importFail();
            });
        };

        $scope.getFormDefinitions = function () {
            FormDefinition.query({name: $scope.selectedConfig.name}, function (data) {
                $scope.formDefinitions = data;
                flattenFormDefs();
            }, function (err) {
                console.log(err)
            });
        };

        $scope.selectFormDef = function (formDef) {
            $scope.selectedFormDef = formDef;
        };

        var flattenFormDefs = function () {
            for (var i = 0; i < $scope.formDefinitions.length; i++) {
                var formDef = $scope.formDefinitions[i];

                for (var j = 0; j < formDef.formElements.length; j++) {
                    var formElement = formDef.formElements[j];

                    if (formElement.partOfRepeatGroup === false && formElement.children.length > 0) {

                        for (var k = 0; k < formElement.children.length; k++) {
                            var child = formElement.children[k];
                            addElementToFormDef(formDef, child);
                        }
                    }
                }
            }
        };

        var addElementToFormDef = function (formDef, element) {
            if (element.repeatGroup === false) {
                formDef.formElements.push(element)
            }
            if (element.children.length > 0) {
                for (var i = 0; i < element.children.length; i++) {
                    var child = element.children[i];
                    addElementToFormDef(formDef, child);
                }
            }
        };
    });
}());
