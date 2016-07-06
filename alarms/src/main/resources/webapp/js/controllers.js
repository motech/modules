(function () {
    'use strict';

    var controllers = angular.module('alarms.controllers', []);

    /*
     *
     * Settings
     *
     */
    controllers.controller('AlarmsSettingsCtrl', function ($scope, $http) {
        $scope.errors = [];
        $scope.messages = [];

        $http.get('../alarms/alarms-config')
            .success(function(response){
                $scope.config = response;
                $scope.originalConfig = angular.copy($scope.config);
            })
            .error(function(response) {
                $scope.errors.push($scope.msg('alarms.web.settings.noConfig', response));
            });

        $scope.reset = function () {
            $scope.config = angular.copy($scope.originalConfig);
        };

        function hideMsgLater(index) {
            return $timeout(function() {
                $scope.messages.splice(index, 1);
            }, 5000);
        }

        $scope.submit = function () {
            $http.post('../alarms/alarms-config', $scope.config)
                .success(function (response) {
                    $scope.config = response;
                    $scope.originalConfig = angular.copy($scope.config);
                    var index = $scope.messages.push($scope.msg('alarms.web.settings.saved'));
                    hideMsgLater(index-1);
                })
                .error (function (response) {
                    motechAlert('alarms.web.settings.save.error.msg', 'alarms.web.settings.save.error', response);
                });
        };
    });

    /*
     *
     * Alarms
     *
     */
    controllers.controller('AlarmsCtrl', function ($scope, $http) {

        $scope.accordions = [];
        $scope.recipients = [];
        $scope.alarms = [];
        $scope.originalAlarms = [];

        $scope.newRecipient = {};
        $scope.addRecipientMsg = null;

        function autoExpandSingleAccordion() {
            if ($scope.accordions.length === 1) {
                $scope.accordions[0] = true;
            }
        }

        function setAccordions(alarms) {
            var i;
            $scope.accordions = [];
            for (i = 0; i < alarms.length; i = i + 1) {
                $scope.accordions.push(false);
            }
            autoExpandSingleAccordion();
        }

        $http.get('../alarms/getAlarms')
        .success(function(response) {
            $scope.alarms = response;
            $scope.originalAlarms = angular.copy($scope.alarms);
            setAccordions($scope.alarms);
        })
        .error(function(response) {
            motechAlert('alarms.web.alarms.getAlarms.error', 'alarms.web.alarms.error', response);
        });

        $http.get('../alarms/getEmailRecipients')
        .success(function(response) {
            $scope.recipients = response;
        })
        .error(function(response) {
            motechAlert('alarms.web.alarms.getRecipients.error', 'alarms.web.alarms.error', response);
        });

        $scope.collapseAccordions = function () {
            var key;
            for (key in $scope.accordions) {
                $scope.accordions[key] = false;
            }
            autoExpandSingleAccordion();
        };

        $scope.deleteAlarm = function(index) {
            var alarmId = $scope.alarms[index].id;
            if (alarmId === undefined || alarmId === null || alarmId === '') {
                $scope.removeAlarm(index);
            } else {
                motechConfirm("alarms.web.alarms.deleteAlarm.ConfirmMsg", "alarms.web.alarms.deleteAlarm.ConfirmTitle",
                    function (response) {
                        if (!response) {
                            return;
                        } else {
                            $http.post('../alarms/deleteAlarm', alarmId)
                            .success(function (response) {
                                $scope.removeAlarm(index);
                            })
                            .error (function (response) {
                                motechAlert('alarms.web.alarms.deleteAlarm.error', 'alarms.web.alarms.error', response);
                            });
                        }
                    });
            }
        };

        $scope.removeAlarm = function(index) {
            $scope.alarms.splice(index, 1);
            $scope.originalAlarms.splice(index, 1);
            $scope.accordions.splice(index, 1);
            autoExpandSingleAccordion();
        };

        $scope.isDirty = function (index) {
            if ($scope.originalAlarms[index] === null || $scope.alarms[index] === null) {
                return false;
            }

            return !angular.equals($scope.originalAlarms[index], $scope.alarms[index]);
        };

        $scope.reset = function (index) {
            $scope.alarms[index] = angular.copy($scope.originalAlarms[index]);
        };

        $scope.addAlarm = function () {
            var newAlarm = {
                'name': '',
                'subject': '',
                'messageContent': '',
                'recipients': [],
                'schedulePeriod': '',
                'status': 'ACTIVE'
            };

            $scope.alarms.push(newAlarm);
            $scope.originalAlarms.push(angular.copy(newAlarm));
            $scope.accordions.push(true);
            autoExpandSingleAccordion();
        };

        $scope.saveAlarm = function (index) {
            $http.post('../alarms/saveAlarm', $scope.alarms[index])
            .success(function (response) {
                $scope.alarms[index] = response;
                $scope.originalAlarms[index] = angular.copy($scope.alarms[index]);

                motechAlert('alarms.web.alarms.saved.successMsg', 'alarms.web.alarms.saved.successTitle');
            })
            .error (function (response) {
                motechAlert('alarms.web.alarms.saveAlarm.error', 'alarms.web.alarms.error', response);
            });
        };

        $scope.enableAlarm = function(index) {
            var alarmId = $scope.alarms[index].id;
            if (alarmId === undefined || alarmId === null || alarmId === '') {
                $scope.alarms[index].status = 'ACTIVE';
                $scope.originalAlarms[index].status = 'ACTIVE';
            } else {
                $http.post('../alarms/enableAlarm', alarmId)
                .success(function (response) {
                    $scope.alarms[index].status = 'ACTIVE';
                    $scope.originalAlarms[index].status = 'ACTIVE';
                    motechAlert('alarms.web.alarms.enabled.successMsg', 'alarms.web.alarms.enabled.successTitle');
                })
                .error (function (response) {
                    motechAlert('alarms.web.alarms.enableAlarm.error', 'alarms.web.alarms.error', response);
                });
            }
        };

        $scope.disableAlarm = function(index) {
            var alarmId = $scope.alarms[index].id;
            if (alarmId === undefined || alarmId === null || alarmId === '') {
                $scope.alarms[index].status = 'DISABLED';
                $scope.originalAlarms[index].status = 'DISABLED';
            } else {
                $http.post('../alarms/disableAlarm', alarmId)
                .success(function (response) {
                    $scope.alarms[index].status = 'DISABLED';
                    $scope.originalAlarms[index].status = 'DISABLED';
                    motechAlert('alarms.web.alarms.disabled.successMsg', 'alarms.web.alarms.disabled.successTitle');
                })
                .error (function (response) {
                    motechAlert('alarms.web.alarms.disableAlarm.error', 'alarms.web.alarms.error', response);
                });
            }
        };

        $scope.addRecipientModalShow = function () {
            $scope.addRecipientMsg = null;
            $scope.newRecipient = {};
            $('#addRecipientModal').modal('show');
        };

        $scope.closeAddRecipientModal = function () {
            $('#addRecipientForm').resetForm();
            $('#addRecipientModal').modal('hide');
        };

        $scope.saveNewRecipient = function () {
            $http.post('../alarms/addRecipient', $scope.newRecipient)
            .success(function (response) {
                $scope.newRecipient = response;
                $scope.recipients.push($scope.newRecipient);
                $scope.addRecipientMsg = $scope.msg('alarms.web.alarms.addRecipient.success');
            })
            .error (function (response) {
                $scope.addRecipientMsg = $scope.msg('alarms.web.alarms.addRecipient.error', response);
            });
        }

    });

}());