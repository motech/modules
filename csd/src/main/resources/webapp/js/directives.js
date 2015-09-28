(function () {
    'use strict';

    var directives = angular.module('csd.directives', []);

    directives.directive('csdLastModifiedDatePicker', function($timeout) {
        return {
            restrict: 'A',
            require: 'ngModel',
            transclude: true,
            link: function(scope, element, attrs, ngModel) {
                $timeout(function() {
                    var elem = angular.element(element);

                    elem.datetimepicker({
                        dateFormat: "yy-mm-dd",
                        changeMonth: true,
                        changeYear: true,
                        timeFormat: "HH:mm",
                        onSelect: function (selectedDateTime) {
                            scope.$apply(function() {
                                ngModel.$setViewValue(selectedDateTime);
                            });
                        },
                        onChangeMonthYear: function (year, month, inst) {
                            var curDate = $(this).datepicker("getDate");
                            if (curDate === null) {
                                return;
                            }
                            if (curDate.curDate.getFullYear() !== year || curDate.getMonth() !== month - 1) {
                                curDate.setYear(year);
                                curDate.setMonth(month - 1);
                                $(this).datepicker("setDate", curDate);
                            }
                        },
                        onClose: function () {
                            var viewValue = elem.val();
                            scope.safeApply(function () {
                                ngModel.$setViewValue(viewValue);
                            });
                        }
                    });
                });
            }
        };
    });

    directives.directive('csdSchedulerDatePicker', function($timeout) {
            return {
                restrict: 'A',
                require: 'ngModel',
                transclude: true,
                link: function(scope, element, attrs, ngModel) {
                    $timeout(function() {
                        var elem = angular.element(element);

                        elem.datetimepicker({
                            dateFormat: "yy-mm-dd",
                            changeMonth: true,
                            changeYear: true,
                            minDate: 0,
                            timeFormat: "HH:mm",
                            minTime: 0,
                            onSelect: function (selectedDateTime){
                                scope.$apply(function() {
                                    ngModel.$setViewValue(selectedDateTime);
                                });
                            },
                            onChangeMonthYear: function (year, month, inst) {
                                var curDate = $(this).datepicker("getDate");
                                if (curDate === null) {
                                    return;
                                }
                                if (curDate.getFullYear() !== year || curDate.getMonth() !== month - 1) {
                                    curDate.setYear(year);
                                    curDate.setMonth(month - 1);
                                    $(this).datepicker("setDate", curDate);
                                }
                            },
                            onClose: function () {
                                var viewValue = elem.val();
                                scope.safeApply(function () {
                                    ngModel.$setViewValue(viewValue);
                                });
                            }
                        });
                    });
                }
            };
        });
}());