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

    directives.directive('commcareCaseJqgrid', function ($compile) {
        return {
            restrict: 'A',
            link: function (scope, element, attrs) {
                var elem = angular.element(element);

                elem.jqGrid({
                    url: '../commcare/caseList',
                    datatype: 'json',
                    jsonReader:{
                        repeatitems:false
                    },
                    prmNames: {
                        sort: 'sortColumn',
                        order: 'sortDirection'
                    },
                    shrinkToFit: true,
                    autowidth: true,
                    rownumbers: true,
                    rowNum: 10,
                    rowList: [10, 20, 50],
                    colModel: [{
                        label: 'Case Name',
                        name: 'caseName',
                        index: 'caseName',
                        width: '200'
                    }, {
                        label: 'Case Type',
                        name: 'caseType',
                        index: 'caseType',
                        sortable: false,
                        width: '200'
                    }, {
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
                            title = '<h4 class="modal-title">' + scope.msg('commcare.case.name') + ': <em>' + rowObject.caseName + '</em></h4>',
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
                    width: '100%',
                    height: 'auto',
                    sortname: 'caseName',
                    sortorder: 'asc',
                    viewrecords: true,
                    gridComplete: function () {
                        angular.forEach(elem.find('button'), function(value) {
                            $compile(value)(scope);
                        });
                        $('#commcare-case').children('div').width('100%');
                        $('.ui-jqgrid-htable').addClass("table-lightblue");
                        $('.ui-jqgrid-btable').addClass("table-lightblue");
                        $('.ui-jqgrid-htable').width('100%');
                        $('.ui-jqgrid-bdiv').width('100%');
                        $('.ui-jqgrid-hdiv').width('100%');
                        $('.ui-jqgrid-view').width('100%');
                        $('#t_commcare-case').width('auto');
                        $('.ui-jqgrid-pager').width('100%');
                        $('.ui-jqgrid-hbox').css({'padding-right':'0'});
                        $('.ui-jqgrid-hbox').width('100%');
                        $('#commcare-case').children('div').each(function() {
                            $(this).find('table').width('100%');
                        });
                    }
                });

            }
        };
    });

}());
