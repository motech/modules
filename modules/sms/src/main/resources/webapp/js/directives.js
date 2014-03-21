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

    var directives = angular.module('sms.directives', []);

    directives.directive('jqgridSearch', function () {
        return {
            restrict: 'A',
            link: function (scope, element, attrs) {
                var elem = angular.element(element),
                    table = angular.element('#' + attrs.jqgridSearch),
                    eventType = elem.data('event-type'),
                    timeoutHnd,
                    filter = function (time) {
                        var field = elem.data('search-field'),
                            value = elem.data('search-value'),
                            type = elem.data('field-type') || 'string',
                            url = parseUri(jQuery('#' + attrs.jqgridSearch).jqGrid('getGridParam', 'url')),
                            query = {},
                            params = '?',
                            array = [],
                            prop;

                        for (prop in url.queryKey) {
                            if (prop !== field) {
                                query[prop] = url.queryKey[prop];
                            }
                        }

                        switch (type) {
                        case 'boolean':
                            query[field] = url.queryKey[field].toLowerCase() !== 'true';

                            if (query[field]) {
                                elem.find('i').removeClass('icon-ban-circle').addClass('icon-ok');
                            } else {
                                elem.find('i').removeClass('icon-ok').addClass('icon-ban-circle');
                            }
                            break;
                        case 'array':
                            if (elem.children().hasClass("icon-ok")) {
                                elem.children().removeClass("icon-ok").addClass("icon-ban-circle");
                            } else if (elem.children().hasClass("icon-ban-circle")) {
                                elem.children().removeClass("icon-ban-circle").addClass("icon-ok");
                                array.push(value);
                            }
                            angular.forEach(url.queryKey[field].split(','), function (val) {
                                if (angular.element('#' + val).children().hasClass("icon-ok")) {
                                    array.push(val);
                                }
                            });

                            query[field] = array.join(',');
                            break;
                        default:
                            query[field] = elem.val();
                        }

                        for (prop in query) {
                            params += prop + '=' + query[prop] + '&';
                        }

                        params = params.slice(0, params.length - 1);

                        if (timeoutHnd) {
                            clearTimeout(timeoutHnd);
                        }

                        timeoutHnd = setTimeout(function () {
                            jQuery('#' + attrs.jqgridSearch).jqGrid('setGridParam', {
                                url: '../sms/log' + params
                            }).trigger('reloadGrid');
                        }, time || 0);
                    };

                switch (eventType) {
                case 'keyup':
                    elem.keyup(function () {
                        filter(500);
                    });
                    break;
                case 'change':
                    elem.change(filter);
                    break;
                default:
                    elem.click(filter);
                }
            }
        };
    });

    directives.directive('gridDatePickerFrom', function() {
        return {
            restrict: 'A',
            link: function(scope, element, attrs) {
                var elem = angular.element(element),
                    endDateTextBox = angular.element('#dateTimeTo');

                elem.datetimepicker({
                    dateFormat: "yy-mm-dd",
                    changeMonth: true,
                    changeYear: true,
                    maxDate: +0,
                    timeFormat: "HH:mm:ss",
                    onSelect: function (selectedDateTime){
                        endDateTextBox.datetimepicker('option', 'minDate', elem.datetimepicker('getDate') );
                    }
                });
            }
        };
    });

    directives.directive('gridDatePickerTo', function() {
        return {
            restrict: 'A',
            link: function(scope, element, attrs) {
                var elem = angular.element(element),
                    startDateTextBox = angular.element('#dateTimeFrom');

                elem.datetimepicker({
                    dateFormat: "yy-mm-dd",
                    changeMonth: true,
                    changeYear: true,
                    maxDate: +0,
                    timeFormat: "HH:mm:ss",
                    onSelect: function (selectedDateTime){
                        startDateTextBox.datetimepicker('option', 'maxDate', elem.datetimepicker('getDate') );
                    }
                });
            }
        };
    });

    directives.directive('loggingGrid', function($compile, $http, $templateCache) {
        return {
            restrict: 'A',
            link: function(scope, element, attrs) {
                var elem = angular.element(element), filters;

                elem.jqGrid({
                    url: '../sms/log?deliveryStatus=DISPATCHED,DELIVERY_CONFIRMED,FAILURE_CONFIRMED,RETRYING,ABORTED,PENDING,RECEIVED,SCHEDULED&smsDirection=INBOUND,OUTBOUND',
                    datatype: 'json',
                    jsonReader:{
                        repeatitems:false
                    },
                    prmNames: {
                        sort: 'sortColumn',
                        order: 'sortDirection'
                    },
                    autowidth: true,
                    rowNum: 10,
                    rowList: [10, 20, 50],
                    colModel: [{
                        name: 'config',
                        index: 'config',
                        width: 90
                    }, {
                        name: 'phoneNumber',
                        index: 'phoneNumber',
                        width: 90
                    }, {
                        name: 'messageContent',
                        index: 'messageContent',
                        sortable: false,
                        width: 220
                    }, {
                        name: 'deliveryStatus',
                        index: 'deliveryStatus',
                        width: 100
                    }, {
                        name: 'providerStatus',
                        index: 'providerStatus',
                        width: 100
                    }, {
                        name: 'timestamp',
                        index: 'timestamp',
                        width: 130
                    }, {
                        name: 'smsDirection',
                        index: 'smsDirection',
                        width: 90
                    }, {
                        name: 'motechId',
                        index: 'motechId',
                        width: 240
                    }, {
                        name: 'providerId',
                        index: 'providerId',
                        width: 100
                    }, {
                         name: 'errorMessage',
                         index: 'errorMessage',
                         sortable: false,
                        width: 250
                    }],
                    pager: '#' + attrs.loggingGrid,
                    width: '100%',
                    height: 'auto',
                    sortname: 'timestamp',
                    sortorder: 'desc',
                    viewrecords: true,
                    gridComplete: function () {
                        angular.forEach(['config', 'phoneNumber', 'deliveryStatus', 'providerStatus', 'timestamp',
                            'smsDirection', 'motechId', 'providerId', 'messageContent', 'errorMessage'], function (value) {
                            elem.jqGrid('setLabel', value, scope.msg('sms.log.' + value));
                        });
                        $('#outsideSmsLoggingTable').children('div').width('100%');
                        $('.ui-jqgrid-htable').addClass("table-lightblue");
                        $('.ui-jqgrid-btable').addClass("table-lightblue");
                        $('.ui-jqgrid-htable').width('100%');
                        $('.ui-jqgrid-bdiv').width('100%');
                        $('.ui-jqgrid-hdiv').width('100%');
                        $('.ui-jqgrid-view').width('100%');
                        $('#t_resourceTable').width('auto');
                        $('.ui-jqgrid-pager').width('100%');
                        $('.ui-jqgrid-hbox').css({'padding-right':'0'});
                        $('.ui-jqgrid-hbox').width('100%');
                        $('#outsideSmsLoggingTable').children('div').each(function() {
                            $(this).find('table').width('100%');
                        });
                        var startDateTextBox = angular.element('#dateTimeFrom'),
                            endDateTextBox = angular.element('#dateTimeTo');
                    }
                });
            }
        };
    });

}());
