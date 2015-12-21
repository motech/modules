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

    directives.directive('commcareCaseJqgrid', function ($compile) {
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

                if (scope.$parent.selectedConfig) {
                    params.url = '../commcare/caseList/' + scope.$parent.selectedConfig.name + '?caseName=&dateModifiedStart=&dateModifiedEnd=';
                }

                if (scope.$parent.selectedConfig !== undefined) {
                    scope.downloadingCases = true;
                    scope.$apply();
                }

                elem.jqGrid(params);
            }
        };
    });

}());
