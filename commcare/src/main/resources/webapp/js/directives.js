(function () {
    'use strict';

    var directives = angular.module('commcare.directives', []);

    directives.directive('autosave', function() {

        return {
            restrict: 'A',
            priority: 0,
            link: function(scope, element, attrs) {
                element.on(attrs.autosave, function(e) {
                    scope.saveSettings(element);
                });
            }
        };
    });

    directives.directive('importDateTimePickerFrom', function() {
        return {
            restrict: 'A',
            require: 'ngModel',
            link: function(scope, element, attrs, ngModel) {
                var elem = angular.element(element),
                    endDateTextBox = angular.element('#commcareDateTimeTo');

                elem.datetimepicker({
                    dateFormat: "yy-mm-dd",
                    changeMonth: true,
                    changeYear: true,
                    timeFormat: "HH:mm:ss",
                    separator: ' T ',
                    onSelect: function (selectedDateTime) {
                        endDateTextBox.datetimepicker('option', 'minDate', elem.datetimepicker('getDate'));
                        endDateTextBox.datetimepicker('option', 'minDateTime', elem.datetimepicker('getDate'));
                        $(this).change();
                    },
                    onClose: function (dateText, inst) {
                        var viewValue = elem.val(), testStartDate, testEndDate;
                        if (endDateTextBox.val() !== '') {
                            testStartDate = elem.datetimepicker('getDate');
                            testEndDate = endDateTextBox.datetimepicker('getDate');
                            if (testStartDate > testEndDate && testEndDate !== null) {
                                endDateTextBox.datetimepicker('setDate', testStartDate);
                                endDateTextBox.datetimepicker('option', 'minDateTime', elem.datetimepicker('getDate'));
                            }
                        }
                        if (viewValue === '') {
                            endDateTextBox.datetimepicker('option', 'minDate', null);
                        }
                        scope.safeApply(function () {
                            ngModel.$setViewValue(viewValue);
                        });
                        scope.updateImportRequest('receivedOnStart', viewValue);
                        scope.updateImportRequest('modifiedDateStart', viewValue);
                    },
                    onChangeMonthYear: function (year, month, inst) {
                        var curDate = elem.datetimepicker("getDate");
                        if (curDate === null) {
                            return;
                        }
                        if (curDate.getYear() !== year || curDate.getMonth() !== month - 1) {
                            curDate.setYear(year);
                            curDate.setMonth(month - 1);
                            endDateTextBox.datetimepicker('option', 'minDate', curDate);
                            $(this).datetimepicker("setDate", curDate);
                            $(this).change();
                        }
                    }
                });

                $("#selectImportOption").children("ul").on("click", function () {
                    scope.receivedOnStart = null;
                    elem.datetimepicker('setTime', null);
                    elem.datetimepicker('setDate', null);
                    elem.datetimepicker('option', 'maxDate', null);
                    elem.datetimepicker('option', 'maxDateTime', null);
                });
            }
        };
    });

    directives.directive('importDateTimePickerTo', function() {
        return {
            restrict: 'A',
            require: 'ngModel',
            link: function(scope, element, attrs, ngModel) {
                var elem = angular.element(element),
                    startDateTextBox = angular.element('#commcareDateTimeFrom');

                elem.datetimepicker({
                    dateFormat: "yy-mm-dd",
                    changeMonth: true,
                    changeYear: true,
                    timeFormat: "HH:mm:ss",
                    separator: ' T ',
                    onSelect: function (selectedDateTime) {
                        startDateTextBox.datetimepicker('option', 'maxDate', elem.datetimepicker('getDate'));
                        startDateTextBox.datetimepicker('option', 'maxDateTime', elem.datetimepicker('getDate'));
                        $(this).change();
                    },
                    onClose: function (dateText, inst) {
                        var viewValue = elem.val(), testStartDate, testEndDate;
                        if (startDateTextBox.val() !== '') {
                            testStartDate = startDateTextBox.datetimepicker('getDate');
                            testEndDate = elem.datetimepicker('getDate');
                            if (testStartDate > testEndDate && testEndDate !== null) {
                                startDateTextBox.datetimepicker('setDate', testEndDate);
                            }
                        }
                        if (viewValue === '') {
                            startDateTextBox.datetimepicker('option', 'maxDate', null);
                        }
                        scope.safeApply(function () {
                            ngModel.$setViewValue(viewValue);
                        });
                        scope.updateImportRequest('receivedOnEnd', viewValue);
                        scope.updateImportRequest('modifiedDateEnd', viewValue);
                    },
                    onChangeMonthYear: function (year, month, inst) {
                        var curDate = $(this).datetimepicker("getDate");
                        if (curDate === null) {
                            return;
                        }
                        if (curDate.getYear() !== year || curDate.getMonth() !== month - 1) {
                            curDate.setYear(year);
                            curDate.setMonth(month - 1);
                            startDateTextBox.datetimepicker('option', 'maxDate', curDate);
                            $(this).datetimepicker("setDate", curDate);
                            $(this).change();
                        }
                    }
                });

                $("#selectImportOption").children("ul").on("click", function () {
                    scope.receivedOnEnd = null;
                    elem.datetimepicker('setTime', null);
                    elem.datetimepicker('setDate', null);
                    elem.datetimepicker('option', 'minDate', null);
                    elem.datetimepicker('option', 'minDateTime', null);
                });
            }
        };
    });

    directives.directive('switch', function() {

        return {
            restrict: 'A',
            priority: 1,
            link: function(scope, element, attrs) {

                var property, deregister;

                property = $(attrs['switch']).attr('ng-model');
                element.on('switch-change', function(e, data) {
                    scope.settings[property.split('.')[1]] = data.value;
                });

                deregister = scope.$watch(property, function(value) {
                    if (value !== undefined) {
                        element.bootstrapSwitch();
                        deregister();
                    }
                });
            }
        };
    });

    directives.directive('showmodal', function () {
        return {
            restrict: 'A',
            link: function (scope, element, attrs, ctrl) {
                var elm = angular.element(element),
                divModal = $('#case-schema-modal');

                elm.on('click', function () {
                    divModal.find('.modal-body').children().remove();
                    divModal.find('.modal-title').remove();
                    divModal.find('.modal-header').append(attrs.datatitle);
                    divModal.find('.modal-body').append(attrs.showmodal);
                    divModal.modal('show');
                });
            }
        };
    });

    directives.directive('commcareGridDatePickerFrom', function() {
        return {
            restrict: 'A',
            link: function (scope, element, attrs) {
                var elem = angular.element(element),
                endDateTextBox = angular.element('#commcareDateTo');

                elem.datepicker({
                    changeYear: true,
                    changeMonth: true,
                    showButtonPanel: true,
                    dateFormat: 'yy-mm-dd',
                    onSelect: function (selectedDate) {
                        endDateTextBox.datepicker('option', 'minDate', elem.datepicker('getDate'));
                        $(this).change();
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
                            $(this).change();
                        }
                    },
                    onClose: function () {
                        var viewValue = $(this).val();
                        if (viewValue === '') {
                            endDateTextBox.datetimepicker('option', 'minDate', null);
                        } else {
                            endDateTextBox.datepicker('option', 'minDate', elem.datepicker('getDate'));
                        }
                    }
                });
            }
        };
    });

    directives.directive('commcareGridDatePickerTo', function() {
        return {
            restrict: 'A',
            link: function (scope, element, attrs) {
                var elem = angular.element(element),
                startDateTextBox = angular.element('#commcareDateFrom');

                elem.datepicker({
                    changeYear: true,
                    changeMonth: true,
                    showButtonPanel: true,
                    dateFormat: 'yy-mm-dd',
                    onSelect: function (selectedDate) {
                        startDateTextBox.datepicker('option', 'maxDate', elem.datepicker('getDate'));
                        $(this).change();
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
                            $(this).change();
                        }
                    },
                    onClose: function () {
                        var viewValue = $(this).val();
                        if (viewValue === '') {
                            startDateTextBox.datetimepicker('option', 'maxDate', null);
                        } else {
                            startDateTextBox.datepicker('option', 'maxDate', elem.datepicker('getDate'));
                        }
                    }
                });
            }
        };
    });

    directives.directive('commcareCaseJqgridSearch', function () {
        return {
            restrict: 'A',
            link: function (scope, element, attrs) {
                var elem = angular.element(element),
                    eventType = elem.data('event-type'),
                    timeoutHnd,
                    filter = function (time) {
                        var field = elem.data('search-field'),
                            type = elem.data('field-type') || 'string',
                            url = parseUri(jQuery('#' + attrs.commcareCaseJqgridSearch).jqGrid('getGridParam', 'url')),
                            query = {},
                            params = '?',
                            array = [],
                            prop;

                        // copy existing url parameters
                        for (prop in url.queryKey) {
                            if (prop !== field) {
                                query[prop] = url.queryKey[prop];
                            }
                        }

                        // set parameter for given element
                        query[field] = elem.val();

                        // create raw parameters
                        for (prop in query) {
                            params += prop + '=' + query[prop] + '&';
                        }

                        // remove last '&'
                        params = params.slice(0, params.length - 1);

                        if (timeoutHnd) {
                            clearTimeout(timeoutHnd);
                        }

                        timeoutHnd = setTimeout(function () {
                            jQuery('#' + attrs.commcareCaseJqgridSearch).jqGrid('setGridParam', {
                                page: 1,
                                url: '../commcare/caseList/' + scope.selectedConfig.name + params
                            }).trigger('reloadGrid');
                        }, time || 0);
                    };

                switch (eventType) {
                case 'change':
                    elem.change(function () {
                        filter(1000);
                    });
                    break;
                case 'click':
                    $('#search-case').click(function () {
                        filter(500);
                    });
                    break;
                default:
                }
            }
        };
    });

    directives.directive('commcareCaseJqgrid', function ($compile, $timeout) {
        return {
            restrict: 'A',
            link: function (scope, element, attrs) {
                var params, elem = angular.element(element);

                scope.$watch('$parent.selectedConfig', function() {
                    scope.downloadingCases = true;
                    scope.loadingError = undefined;
                    if (scope.$parent.selectedConfig !== undefined && scope.$parent.selectedConfig.name !== undefined) {
                        elem.jqGrid('setGridParam', {
                            url: '../commcare/caseList/' + scope.$parent.selectedConfig.name + '?caseName=&dateModifiedStart=&dateModifiedEnd=',
                            viewrecords: false
                        }).trigger('reloadGrid');
                    }
                });

                params = {
                    datatype: 'json',
                    jsonReader:{
                        repeatitems:false
                    },
                    rownumbers: true,
                    colModel: [
                        {
                            label: 'Case Name',
                            name: 'caseName',
                            index: 'caseName',
                            sortable: false,
                            width: '200'
                        },
                        {
                            label: 'Case Type',
                            name: 'caseType',
                            index: 'caseType',
                            sortable: false,
                            width: '200'
                        },
                        {
                            label: 'Action',
                            name: 'action',
                            sortable: false,
                            index: 'action',
                            align: 'center',
                            title: false,
                            formatter: function (cellVal, options, rowObject) {
                                var div = $('<div>'),
                                button1 = $('<button>'),
                                button2 = $('<button>'),
                                title = '<h4 class="modal-title">' + scope.msg('commcare.caseName') + ': <em>' + rowObject.caseName + '</em></h4>',
                                contentView = '<div class="form-horizontal list-lightblue">' + scope.formatModalContent(rowObject) + '</div>',
                                contentJson = '<pre>' + scope.formatJson(rowObject) + '</pre>';
                                button1
                                    .append(scope.msg('commcare.view'))
                                    .attr('datatitle', title)
                                    .attr('showmodal', contentView)
                                    .addClass('btn btn-default btn-xs');
                                button2
                                    .append(scope.msg('commcare.json'))
                                    .attr('datatitle', title)
                                    .attr('showmodal', contentJson)
                                    .addClass('btn btn-default btn-xs');
                                div
                                    .addClass('button-group')
                                    .append(button1)
                                    .append(button2);
                                return '<div>' + div.html() + '</div>';
                            }
                    }],
                    pager: '#' + attrs.commcareCaseJqgrid,
                    sortname: 'caseName',
                    viewrecords: true,
                    gridComplete: function () {
                        angular.forEach(elem.find('button'), function(value) {
                            $compile(value)(scope);
                        });
                        $('#commcareCase').children().width('100%');
                        $('#commcareCase .ui-jqgrid-htable').addClass('table-lightblue');
                        $('#commcareCase .ui-jqgrid-btable').addClass("table-lightblue");
                        $('#commcareCase .ui-jqgrid-htable').width('100%');
                        $('#commcareCase .ui-jqgrid-btable').width('100%');
                        $('#commcareCase .ui-jqgrid-bdiv').width('100%');
                        $('#commcareCase .ui-jqgrid-hdiv').width('100%').show();
                        $('#commcareCase .ui-jqgrid-hbox').css({'padding-right':'0'});
                        $('#commcareCase .ui-jqgrid-hbox').width('100%');
                        $('#commcareCase .ui-jqgrid-view').width('100%');
                        $('#commcareCase .ui-jqgrid-pager').width('100%');
                        scope.downloadingCases = false;
                        scope.$apply();
                    },
                    loadError: function(request, status, error) {
                        scope.downloadingCases = false;
                        scope.loadingError = request.responseText;
                        elem.jqGrid('clearGridData');
                        scope.$apply();
                    }
                };

                if(scope.$parent.getDefault().name === "") {
                    scope.$parent.selectedConfig = undefined;
                }
                else if(scope.$parent.selectedConfig.name === "") {
                    scope.$parent.selectedConfig = scope.$parent.getDefault();
                }

                if (scope.$parent.selectedConfig) {
                    params.url = '../commcare/caseList/' + scope.$parent.selectedConfig.name + '?caseName=&dateModifiedStart=&dateModifiedEnd=';
                    scope.downloadingCases = true;
                }

                $timeout(function() {}, 0).then(elem.jqGrid(params));

            }
        };
    });

}());
