(function () {
    'use strict';
    
    // from http://stackoverflow.com/questions/14859266/input-autofocus-attribute
    angular.module('ng').directive('ngFocus', function($timeout) {
        return {
            link: function ( scope, element, attrs ) {
                scope.$watch( attrs.ngFocus, function ( val ) {
                    if ( angular.isDefined( val ) && val ) {
                        $timeout( function () { element[0].focus(); } );
                    }
                }, true);

                element.bind('blur', function () {
                    if ( angular.isDefined( attrs.ngFocusLost ) ) {
                        scope.$apply( attrs.ngFocusLost );

                    }
                });
            }
        };
    });

    /*smsGridDatePicker in sms module*/
    widgetModule.directive('smsGridDatePicker', function() {
        return {
            restrict: 'A',
            link: function(scope, element, attrs) {
                var elem = angular.element(element),
                    otherDateTextBox = {},
                    curId = attrs.id,
                    curIdLength = curId.length,
                    otherId = '',
                    isStartDate = false;
                     
                if(curId.substr(curIdLength-2,2) === 'To') {
                    otherId = curId.slice(0,curIdLength - 2) + 'From';
                }
                else {
                    otherId = curId.slice(0,curIdLength - 4) + 'To';
                    isStartDate = true;
                }
                otherDateTextBox = angular.element('#' + otherId);

                elem.datetimepicker({
                    dateFormat: "yy-mm-dd",
                    changeMonth: true,
                    changeYear: true,
                    maxDate: +0,
                    timeFormat: "HH:mm:ss",
                    onSelect: function (selectedDateTime){
                        if(isStartDate) {
                            otherDateTextBox.datetimepicker('option', 'minDate', elem.datetimepicker('getDate') );
                        }
                        else {
                            otherDateTextBox.datetimepicker('option', 'maxDate', elem.datetimepicker('getDate') );
                        }
                    }
                });
            }
        };
    });
}());