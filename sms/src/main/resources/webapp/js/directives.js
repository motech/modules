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

    directives.directive('smsJqgridSearch', function () {
        return {
            restrict: 'A',
            link: function (scope, element, attrs) {
                var elem = angular.element(element),
                    table = angular.element('#' + attrs.smsJqgridSearch),
                    eventType = elem.data('event-type'),
                    timeoutHnd,
                    filter = function (time) {
                        var field = elem.data('search-field'),
                            value = elem.data('search-value'),
                            type = elem.data('field-type') || 'string',
                            url = parseUri(jQuery('#' + attrs.smsJqgridSearch).jqGrid('getGridParam', 'url')),
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
                                elem.find('i').removeClass('fa-square-o').addClass('fa-check-square-o');
                            } else {
                                elem.find('i').removeClass('fa-check-square-o').addClass('fa-square-o');
                            }
                            break;
                        case 'array':
                            if (elem.children().hasClass("fa-check-square-o")) {
                                elem.children().removeClass("fa-check-square-o").addClass("fa-square-o");
                            } else if (elem.children().hasClass("fa-square-o")) {
                                elem.children().removeClass("fa-square-o").addClass("fa-check-square-o");
                                array.push(value);
                            }
                            angular.forEach(url.queryKey[field].split(','), function (val) {
                                if (angular.element('#' + val).children().hasClass("fa-check-square-o")) {
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
                            jQuery('#' + attrs.smsJqgridSearch).jqGrid('setGridParam', {
                                page: 1,
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

    directives.directive('smsLoggingGrid', function($compile, $http, $templateCache) {
        return {
            restrict: 'A',
            link: function(scope, element, attrs) {
                try {
                    if (typeof($('#outsideSmsLoggingTable')[0].grid) !== 'undefined') {
                        return;
                    }
                }
                catch (e) {
                    return;
                }

                var elem = angular.element(element), filters;

                elem.jqGrid({
                    url: '../sms/log?deliveryStatus=DISPATCHED,DELIVERY_CONFIRMED,FAILURE_CONFIRMED,RETRYING,ABORTED,PENDING,RECEIVED,SCHEDULED&smsDirection=INBOUND,OUTBOUND',
                    datatype: 'json',
                    jsonReader:{
                        repeatitems:false
                    },
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
                    pager: '#' + attrs.smsLoggingGrid,
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
                        $('#t_smsLoggingTable').width('auto');
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