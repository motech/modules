(function () {
    'use strict';

    /* Directives */

    var directives = angular.module('cmslite.directives', []);

    directives.directive('cmsLiteAutoComplete', function () {
        return {
            restrict: 'A',
            link: function (scope, element, attrs) {
                angular.element(element).autocomplete({
                    minLength: 2,
                    source: function (request, response) {
                        $.getJSON('../cmsliteapi/resource/available/' + attrs.cmsLiteAutoComplete, request, function (data) {
                            response(data);
                        });
                    }
                });
            }
        };
    });

    directives.directive('cmsLiteJqgridSearch', function () {
        return {
            restrict: 'A',
            link: function (scope, element, attrs) {
                var elem = angular.element(element),
                    eventType = elem.data('event-type'),
                    timeoutHnd,
                    filter = function (time) {
                        var field = elem.data('search-field'),
                            type = elem.data('field-type') || 'string',
                            url = parseUri(jQuery('#' + attrs.cmsLiteJqgridSearch).jqGrid('getGridParam', 'url')),
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
                            if (elem.children().hasClass("icon-ok")) {
                                elem.children().removeClass("icon-ok").addClass("icon-ban-circle");
                            } else if (elem.children().hasClass("icon-ban-circle")) {
                                elem.children().removeClass("icon-ban-circle").addClass("icon-ok");
                            }

                            angular.forEach(scope.usedLanguages, function (value) {
                                if (angular.element('#' + value).children().hasClass("icon-ok") && value !== '') {
                                    array.push(value);
                                }
                            });

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
                            jQuery('#' + attrs.cmsLiteJqgridSearch).jqGrid('setGridParam', {
                                page: 1,
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

    directives.directive('cmsLiteGrid', function ($compile) {
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
                    rownumbers: true,
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
                    pager: '#' + attrs.cmsLiteGrid,
                    sortname: 'name',
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

                        $('#outside-cms-lite-table').children('div').width('100%');
                        $('.ui-jqgrid-htable').addClass("table-lightblue");
                        $('.ui-jqgrid-btable').addClass("table-lightblue");
                        $('.ui-jqgrid-htable').width('100%');
                        $('.ui-jqgrid-bdiv').width('100%');
                        $('.ui-jqgrid-hdiv').width('100%');
                        $('.ui-jqgrid-view').width('100%');
                        $('#t_cms-lite-table').width('auto');
                        $('.ui-jqgrid-pager').width('100%');
                        $('.ui-jqgrid-hbox').css({'padding-right':'0'});
                        $('.ui-jqgrid-hbox').width('100%');
                        $('#outside-cms-lite-table').children('div').each(function() {
                            $(this).find('table').width('100%');
                        });
                    }
                });

            }
        };
    });
}());