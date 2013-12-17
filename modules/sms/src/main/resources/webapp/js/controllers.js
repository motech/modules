(function () {
    'use strict';

    var smsModule = angular.module('motech-sms');

    /*
     *
     * Send
     *
     */
    smsModule.controller('SendController', function ($log, $scope, $timeout, $http) {
        $scope.sms = {};
        $scope.dt = "now";
        $scope.messages = [];
        $scope.error = "";
        $scope.innerLayout.hide('east');

        $http.get('../sms/configs')
        .success(function(response) {
            $scope.config = response;
            $scope.sms.config = $scope.config.defaultConfigName;
        })
        .error(function(response) {
            $scope.error = $scope.msg('sms.settings.validate.no_config', response);
        });

        function hideMsgLater(index) {
            return $timeout(function() {
                $scope.messages.splice(index, 1);
            }, 5000);
        }

        $scope.sendSms = function () {
            var now = new Date(), then;
            $scope.error = null;

            switch ($scope.dt)
            {
            case "10sec":
                then = new Date(now.getTime() + 10*1000);
                break;
            case "1min":
                then = new Date(now.getTime() + 60*1000);
                break;
            case "1hour":
                then = new Date(now.getTime() + 3600*1000);
                break;
            // This covers 'now'
            default:
                then = null;
            }
            $scope.sms.deliveryTime = then;
            $http.post('../sms/send', $scope.sms)
                .success(function(response) {
                    var index = $scope.messages.push(response);
                    hideMsgLater(index-1);
                })
                .error(function(response) {
                    $scope.error = response;
                });
        };
    });

    /*
     *
     * Log
     *
     */

    smsModule.controller('LogController', function ($scope, $http) {
        $scope.log = [];
        $scope.innerLayout.show('east');
        $scope.innerLayout.addToggleBtn("#sms-logging-filters", "east");
    });

    /*
     *
     * Settings
     *
     */
    smsModule.controller('SettingsController', function ($scope, $http, $timeout) {
        $scope.errors = [];
        $scope.messages = [];
        $scope.dupeNames = [];
        $scope.innerLayout.hide('east');

        $http.get('../sms/templates')
            .success(function(response){
                $scope.templates = response;
            })
            .error(function(response) {
                 $scope.errors.push($scope.msg('sms.settings.validate.no_templates', response));
             });

        function autoExpandSingleAccordion() {
            if ($scope.accordions.length === 1) {
                $scope.accordions[0] = true;
                $scope.config.defaultConfigName = $scope.config.configs[0].name;
                $scope.defaultConfigIndex = 0;
            }
        }

        function setAccordions(configs) {
            var i;
            $scope.accordions = [];
            $scope.dupeNames = [];
            for (i = 0 ; i<configs.length ; i = i + 1) {
                $scope.accordions.push(false);
                $scope.dupeNames.push(false);
            }
            autoExpandSingleAccordion();
        }

        $scope.checkForDuplicateNames = function(index) {
            var i;
            for (i = 0 ; i < $scope.config.configs.length ; i = i + 1) {
                if (i!==index && $scope.config.configs[i].name === $scope.config.configs[index].name) {
                    $scope.dupeNames[index] = true;
                    return;
                }
            }
            $scope.dupeNames[index] = false;
        };

        $scope.anyDuplicateNames = function() {
            var i;
            for (i = 0 ; i < $scope.dupeNames.length ; i = i + 1) {
                if ($scope.dupeNames[i]) {
                    return true;
                }
            }
            return false;
        };

        $http.get('../sms/configs')
            .success(function(response){
                var i;
                $scope.config = response;
                $scope.originalConfig = angular.copy($scope.config);
                setAccordions($scope.config.configs);
                for (i = 0 ; i < response.configs.length ; i = i + 1) {
                    if (response.defaultConfigName === response.configs[i].name) {
                        $scope.defaultConfigIndex = i;
                        break;
                    }
                }
            })
            .error(function(response) {
                $scope.errors.push($scope.msg('sms.settings.validate.no_config', response));
            });

        $scope.collapseAccordions = function () {
            var key;
            for (key in $scope.accordions) {
                $scope.accordions[key] = false;
            }
            autoExpandSingleAccordion();
        };

        /*  TODO
            This replaces the configuration's properties with the ones from the selected template.
            Do we want to 'remember' the old template properties in case the user chooses to select the old template
            back from the dropdown?
        */
        $scope.changeTemplateProperties = function (config) {
            var i, configurables = $scope.templates[config.templateName].configurables;
            config.props = [];
            for (i = 0 ; i < configurables.length ; i = i + 1) {
                config.props.push({"name": configurables[i], "value": ""});
            }
        };

        $scope.setNewDefaultConfig = function() {
            var i;
            for (i = 0 ; i < $scope.config.configs.length ; i = i + 1) {
                if ($scope.config.configs[i].name === $scope.config.defaultConfigName) {
                    return;
                }
            }
            if ($scope.config.configs.length > 0) {
                $scope.config.defaultConfigName = $scope.config.configs[0].name;
                $scope.defaultConfigIndex = 0;
            }
        };

        $scope.setDefaultConfig = function(name, index) {
            $scope.config.defaultConfigName = name;
            $scope.defaultConfigIndex = index;
        };

        $scope.keepDefaultConfig = function() {
            $scope.config.defaultConfigName = $scope.config.configs[$scope.defaultConfigIndex].name;
        };

        $scope.deleteConfig = function(index) {
            $scope.config.configs.splice(index, 1);
            $scope.accordions.splice(index, 1);
            $scope.dupeNames.splice(index, 1);
            $scope.setNewDefaultConfig();
            autoExpandSingleAccordion();
        };

        $scope.isDirty = function () {
            if ($scope.originalConfig === null || $scope.config === null) {
                return false;
            }

            return !angular.equals($scope.originalConfig, $scope.config);
        };

        $scope.reset = function () {
            $scope.config = angular.copy($scope.originalConfig);
            $scope.setNewDefaultConfig();
            setAccordions($scope.config.configs);
        };

        $scope.addConfig = function () {
            var firstTemplateName = Object.keys($scope.templates)[0], newLength, newConfig;
            newConfig = {
                'name':'',
                'templateName':firstTemplateName,
                'maxRetries':parseInt($scope.msg('sms.settings.max_retries.default'), 10),
                'splitHeader':$scope.msg('sms.settings.split_header.default'),
                'splitFooter':$scope.msg('sms.settings.split_footer.default'),
                'excludeLastFooter':Boolean($scope.msg('sms.settings.split_exclude.default'))
                };
            newLength = $scope.config.configs.push(newConfig);
            $scope.accordions.push(true);
            autoExpandSingleAccordion();
            $scope.dupeNames.push(false);
            if ($scope.config.configs.length === 1) {
                $scope.config.defaultConfigName = $scope.config.configs[0].name;
            }
            $scope.changeTemplateProperties($scope.config.configs[newLength-1]);
        };

        $scope.betterMsg = function (name, templateName) {
            var ret, key;
            if (templateName !== null) {
                key = 'sms.settings.prop.' + name + '.' + templateName;
                ret = $scope.msg(key);
                if (ret !== '[' + key + ']') {
                    return ret;
                }
            }
            key = 'sms.settings.prop.' + name;
            ret = $scope.msg(key);
            if (ret === '[' + key + ']') {
                 ret = name;
            }
            return ret;
        };

        $scope.tooltipOrBlank = function (name, templateName) {
            var key, ret;
            if (templateName !== null) {
                key = "sms.settings.prop." + name + '.' + templateName + ".tooltip";
                ret = $scope.msg(key);
                if (ret !== "[" + key + "]") {
                    return ret;
                }
            }
            key = "sms.settings.prop." + name + ".tooltip";
            ret = $scope.msg(key);
            if (ret === "[" + key + "]") {
                ret = "";
            }
            return ret;
        };

        function hideMsgLater(index) {
            return $timeout(function() {
                $scope.messages.splice(index, 1);
            }, 5000);
        }

        $scope.submit = function () {
            $http.post('../sms/configs', $scope.config)
                .success(function (response) {
                    $scope.config = response;
                    $scope.originalConfig = angular.copy($scope.config);
                    setAccordions($scope.config.configs);
                    var index = $scope.messages.push($scope.msg('sms.settings.saved'));
                    hideMsgLater(index-1);
                })
                .error (function (response) {
                    //todo: better than that!
                    handleWithStackTrace('sms.error.header', 'sms.error.body', response);
                });
        };
    });
}());