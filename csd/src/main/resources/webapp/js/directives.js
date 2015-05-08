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
                            }
                        });
                    });
                }
            };
        });
}());