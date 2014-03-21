(function () {
    'use strict';

    /* Directives */

    var directives = angular.module('cmslite.directives', []);

    directives.directive('clearForm', function () {
        return {
            restrict: 'A',
            link: function (scope, element, attrs) {
                $(element).on('hidden', function () {
                    $('#' + attrs.clearForm).clearForm();
                });
            }
        };
    });

    directives.directive('autoComplete', function () {
        return {
            restrict: 'A',
            link: function (scope, element, attrs) {
                angular.element(element).autocomplete({
                    minLength: 2,
                    source: function (request, response) {
                        $.getJSON('../cmsliteapi/resource/available/' + attrs.autoComplete, request, function (data) {
                            response(data);
                        });
                    }
                });
            }
        };
    });

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
                            type = elem.data('field-type') || 'string',
                            url = parseUri(jQuery('#' + attrs.jqgridSearch).jqGrid('getGridParam', 'url')),
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
                            angular.forEach(url.queryKey[field].split(','), function (value) {
                                if (elem.not(':checked') && elem.val() !== value && value !== '') {
                                    array.push(value);
                                }
                            });

                            if (elem.is(':checked')) {
                                array.push(elem.val());
                            }

                            query[field] = array.join(',');
                            break;
                        default:
                            query[field] = elem.val();
                        }

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
                            jQuery('#' + attrs.jqgridSearch).jqGrid('setGridParam', {
                                url: '../cmsliteapi/resource' + params
                            }).trigger('reloadGrid');
                        }, time || 0);
                    };

                switch (eventType) {
                case 'keyup':
                    elem.keyup(function () {
                        filter(500);
                    });
                    break;
                default:
                    elem.click(filter);
                }
            }
        };
    });

    directives.directive('resourcesGrid', function ($compile) {
        return {
            restrict: 'A',
            link: function (scope, element, attrs) {
                var elem = angular.element(element);

                elem.jqGrid({
                    url: '../cmsliteapi/resource?name=&string=true&stream=true&languages=',
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
                        name: 'name',
                        index: 'name',
                        width: '200'
                    }, {
                        name: 'languages',
                        index: 'languages',
                        width: '200',
                        sortable: false,
                        formatter: function (array, options, data) {
                            var ul = $('<ul>');

                            angular.forEach(array, function (value) {
                                ul.append($('<li>').append($('<a>')
                                    .append(value)
                                    .attr('ng-click', 'showResource("{0}", "{1}", "{2}")'.format(data.type, value, data.name))
                                    .css('cursor', 'pointer')
                                ));
                            });

                            return '<ul>' + ul.html() + '</ul>';
                        }
                    }, {
                        name: 'type',
                        index: 'type',
                        width: '100',
                        align: 'center',
                        formatter: function (value) {
                            return scope.msg('cmslite.resource.type.' + value);
                        }
                    }],
                    pager: '#' + attrs.resourcesGrid,
                    width: '100%',
                    height: 'auto',
                    sortname: 'name',
                    sortorder: 'asc',
                    viewrecords: true,
                    gridComplete: function () {
                        $.ajax({
                            url: '../server/lang/locate',
                            success:  function() {},
                            async: false
                        });

                        angular.forEach(elem.find('ul'), function(value) {
                            $compile(value)(scope);
                        });

                        angular.forEach(['name', 'languages', 'type'], function (value) {
                            elem.jqGrid('setLabel', value, scope.msg('cmslite.resource.' + value));
                        });

                        $('#outsideResourceTable').children('div').width('100%');
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
                        $('#outsideResourceTable').children('div').each(function() {
                            $(this).find('table').width('100%');
                        });
                    }
                });

            }
        };
    });
}());
