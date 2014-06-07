//provides angular directives for call logs, including loading the grid of
//logs and searching the grid.

(function () {
    'use strict';
    var currentPhoneNumber,    //phone number currently displayed and searched on
        overallMaxDuration,    //maximum duration of all call records

        directives = angular.module('ivr.directives', []),

        //takes the given query (a list of pairs) and returns a string
        //that can be used to query the database.
        makeParams = function(query) {
                 var prop,
                     params = '?';
                 for (prop in query) {
                     params += prop + '=' + query[prop] + '&';
                     if (prop === "phoneNumber") {
                        currentPhoneNumber = query[prop];
                     }
                 }

                 params = params.slice(0, params.length - 1);
                 return params;
            },

        //takes a list, a separator, and a separator for the last two elements
        //of the list, and returns a proper english representation of the list.
        //Example: if list = [1, 2, 3], separator = "," and terminalSeparator = "and",
        //will return "1, 2 and 3"
        listToProperString = function(list, separator, terminalSeparator) {
            var s = "";
            $.each(list, function(i, item) {
                s += item;
                if (i < list.length - 2) {
                    s += separator;
                } else if (list.length > 1 && i === list.length - 2) {
                    s += terminalSeparator;
                }
            });
            return s;
        },

        //returns a list of buttons that are currently active,
        //given a list of their id's and a corresponding list of
        //what the buttons should be displayed as. The two lists
        //must be the same size and have the same ordering.
        findActiveButtons = function(options, texts) {
            var i,
                result = [];
            for(i = 0; i < options.length; i+=1) {
                if ($("#" + options[i]).children().hasClass("icon-ok")) {
                    result.push(texts[i]);
                }
            }
            return result;
        },

        setFilterTitle = function(scope) {
            var title = "",
                filters = [],
                startDateTo = $("#startDateTimeTo").val(),
                startDateFrom =  $("#startDateTimeFrom").val(),
                answerDateTo = $("#answerDateTimeTo").val(),
                answerDateFrom = $("#answerDateTimeFrom").val(),
                endDateTo = $("#endDateTimeTo").val(),
                endDateFrom = $("#endDateTimeFrom").val(),
                minDuration = $("#minDuration").val(),
                maxDuration = $("#maxDuration").val(),
                dispositionOptions = ["answered", "busy", "failed", "noAnswer", "unknown"],
                dispositionTexts = [scope.msg('ivr.calllog.answered'), scope.msg('ivr.calllog.busy'),
                 scope.msg('ivr.calllog.failed'), scope.msg('ivr.calllog.noAnswer'), scope.msg('ivr.calllog.unknown')],
                directionOptions = [ scope.msg('ivr.calllog.inbound'),  scope.msg('ivr.calllog.outbound')],
                dispositions,
                directions;
            if(currentPhoneNumber !== "") {
                filters.push(scope.msg('ivr.calllog.phoneNumber') + ": " + currentPhoneNumber);
            }
            if(startDateFrom !== "" && startDateTo !== "") {
                filters.push(scope.msg('ivr.calllog.startDate') + ": " +  startDateFrom + " to " + startDateTo);
            }
            if(answerDateFrom !== "" && answerDateTo !== "") {
                filters.push(scope.msg('ivr.calllog.answerDate') + ": " +  answerDateFrom + " to " + answerDateTo);
            }
            if(endDateFrom !== "" && endDateTo !== "") {
                filters.push(scope.msg('ivr.calllog.endDate') + ": " +  endDateFrom + " to " + endDateTo);
            }
            if(minDuration > 0 || maxDuration < overallMaxDuration) {
                filters.push(scope.msg('ivr.calllog.duration') + ": " + minDuration + " to " + maxDuration + " seconds");
            }
            dispositions = findActiveButtons(dispositionOptions, dispositionTexts);
            if(dispositions.length > 0 && dispositions.length < 5) {
                filters.push(scope.msg('ivr.calllog.disposition') + ": " + listToProperString(dispositions, " or ", " or "));
            }
            directions = findActiveButtons(directionOptions, directionOptions);
            if(directions.length === 1) {
                filters.push(scope.msg('ivr.calllog.callDirection') + ": " + directions[0]);
            }
            if (filters.length > 0) {
                title = "<b>" + scope.msg('ivr.calllog.filteredBy') + "</b> " + listToProperString(filters, ", ", " " + scope.msg('ivr.calllog.and') + " ");
            }
            $('#filter-title').html(title);
        },

        searchGrid = function(grid, time, url, params, timeoutHnd) {
             if (timeoutHnd) {
                 clearTimeout(timeoutHnd);
             }
             return setTimeout(function () {
                 jQuery('#' + grid).jqGrid('setGridParam', {
                     url: '../ivr/api/calllog/search' + params
                 }).trigger('reloadGrid');
             }, time || 0);
        },

        setUpQuery = function(url, field) {
            var prop,
                query = {};
            for (prop in url.queryKey) {
                if (prop !== field) {
                    query[prop] = url.queryKey[prop];
                }
            }
            return query;
        };

    directives.directive('tooltip', function () {

        return {
            restrict:'A',
            link:function (scope, element, attr) {

                var tooltipContainer = $($($(element).parents('div')[0]).children('.tooltip')[0]);

                element.popover({
                    trigger:'click',
                    placement:'bottom',
                    html:true,
                    title:$(tooltipContainer.children()[0]),
                    content:$(tooltipContainer.children()[1])
                });
            }
        };
    });

    directives.directive('typeahead', function () {

        return {
            restrict:'A',
            link:function (scope, element, attr) {
                $.get("../ivr/api/calllog/phone-numbers", function (data) {
                    element.typeahead({
                        source:data,
                        updater: function(item) {
                            //when the user selects a phone number, search
                            //the database with that phone number
                            var timeoutHnd,
                                table = angular.element('#' + attr.jqgridSearch),
                                url = parseUri(jQuery('#' + attr.jqgridSearch).jqGrid('getGridParam', 'url')),
                                query = setUpQuery(url, "phoneNumber"),
                                params,
                                array = [];

                            query.phoneNumber = item;
                            currentPhoneNumber = item;
                            params = makeParams(query);
                            setFilterTitle(scope);
                            timeoutHnd = searchGrid(attr.jqgridSearch, 500, url, params, timeoutHnd);
                            return item;
                        }

                    });
                });
            }
        };
    });



    directives.directive('ngSlider', function (CalllogMaxDuration) {
        return function (scope, element, attributes) {
            CalllogMaxDuration.get(function (data) {

                var sliderElement = $(element),
                    elem = angular.element(element),
                    table = angular.element('#resourceTable'),

                getSliderMin = function () {
                    return sliderElement.slider("values", 0);
                },
                getSliderMax = function () {
                    return sliderElement.slider("values", 1);
                },
                setSliderMin = function (val) {
                    sliderElement.slider("values", 0, val);
                },
                setSliderMax = function (val) {
                    sliderElement.slider("values", 1, val);
                },
                setSliderInputs = function (min, max) {
                    $(".slider-control[slider-point='min']").val(min);
                    $(".slider-control[slider-point='max']").val(max);
                };

                scope.maxDuration = data.maxDuration;
                overallMaxDuration = scope.maxDuration;
                sliderElement.slider({
                    range:true,
                    min:0,
                    max:scope.maxDuration,
                    values:[0, scope.maxDuration],
                    slide:function (event, ui) {
                        setSliderInputs(ui.values[0], ui.values[1]);
                    },
                    change:function (event, ui) {
                        //when the slider is changed, must re-query the database
                        var timeoutHnd,
                            url = parseUri(table.jqGrid('getGridParam', 'url')),
                            query = {},
                            params,
                            array = [],
                            prop;

                        setSliderInputs(ui.values[0], ui.values[1]);
                        for (prop in url.queryKey) {
                            if (prop !== "minDuration" && prop !== "maxDuration") {
                                query[prop] = url.queryKey[prop];
                            }
                        }

                        query.minDuration = ui.values[0];
                        query.maxDuration = ui.values[1];

                        params = makeParams(query);


                        timeoutHnd = searchGrid("resourceTable", 0, url, params, timeoutHnd);
                        setFilterTitle(scope);
                    }
                });
                setSliderInputs(getSliderMin(), getSliderMax());

                $(".slider-control").blur(function (e) {
                    var sliderTextControl = $(e.target),
                        val = parseInt(sliderTextControl.val().match(/\d+/), 10);
                    if (isNaN(val)) {
                        return;
                    }
                    switch (sliderTextControl.attr("slider-point")) {
                        case "min":
                            if (val >= 0 && val < getSliderMax()) {
                                setSliderMin(val);
                            } else if (val >= getSliderMax() && val < scope.maxDuration) {
                                setSliderMin(val);
                                setSliderMax(val);
                            } else {
                                sliderTextControl.val(getSliderMin());
                            }
                            break;
                        case "max":
                            if (val <= scope.maxDuration && val > getSliderMin()) {
                                setSliderMax(val);
                            } else if (val <= getSliderMin()) {
                                setSliderMax(val);
                                setSliderMin(val);
                            } else {
                                sliderTextControl.val(getSliderMax());
                            }
                            break;
                    }
                });



            });

        };
    });

    directives.directive('gridDatePickerFrom', function() {
        return {
            restrict: 'A',
            link: function(scope, element, attrs) {
                var elem = angular.element(element),
                    endDateTextBox = angular.element('#' + attrs.gridDatePickerFrom + 'To');
                elem.datetimepicker({
                    dateFormat: "yy-mm-dd",
                    changeMonth: true,
                    changeYear: true,
                    showSecond: true,
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
                    startDateTextBox = angular.element('#' + attrs.gridDatePickerTo + 'From');

                elem.datetimepicker({
                    dateFormat: "yy-mm-dd",
                    changeMonth: true,
                    changeYear: true,
                    showSecond: true,
                    timeFormat: "HH:mm:ss",
                    onSelect: function (selectedDateTime){
                        startDateTextBox.datetimepicker('option', 'maxDate', elem.datetimepicker('getDate') );
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
                            value = elem.data('search-value'),
                            type = elem.data('field-type') || 'string',
                            url = parseUri(jQuery('#' + attrs.jqgridSearch).jqGrid('getGridParam', 'url')),
                            params,
                            array = [],
                            query = setUpQuery(url, field);

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

                        params = makeParams(query);

                        timeoutHnd = searchGrid(attrs.jqgridSearch, time, url, params, timeoutHnd);
                        setFilterTitle(scope);
                    };


                switch (eventType) {
                case 'keyup':
                    elem.keyup(function () {
                        filter(0);
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



    directives.directive('callGrid', function($compile) {
         return {
            restrict: 'A',
            link: function (scope, element, attrs) {
                var elem = angular.element(element), filters;

                elem.jqGrid({
                    url: '../ivr/api/calllog/search?phoneNumber=&startFromDate=&startToDate=&answerFromDate=&answerToDate=&endFromDate=&endToDate=&answered=true&busy=true&failed=true&noAnswer=true&unknown=true&inbound=true&outbound=true&minDuration=&maxDuration=',
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
                        name: 'phoneNumber',
                        index: 'phoneNumber'
                    }, {
                        name: 'callDirection',
                        index: 'callDirection'
                    }, {
                        name: 'startDate',
                        index: 'startDate',
                        formatter: function(cellValue, options) {
                           if(cellValue) {
                               return $.fmatter.util.DateFormat(
                               '',
                               new Date(+cellValue),
                               'UniversalSortableDateTime',
                               $.extend({}, $.jgrid.formatter.date, options)
                               );
                           } else {
                               return '';
                           }
                       }

                    },
                    {
                        name: 'answerDate',
                        index: 'answerDate',
                        formatter: function(cellValue, options) {
                                       if(cellValue) {
                                           return $.fmatter.util.DateFormat(
                                           '',
                                           new Date(+cellValue),
                                           'UniversalSortableDateTime',
                                           $.extend({}, $.jgrid.formatter.date, options)
                                           );
                                       } else {
                                           return '';
                                       }
                                   }
                    },
                    {
                        name: 'endDate',
                        index: 'endDate',
                        formatter: function(cellValue, options) {
                            if(cellValue) {
                                return $.fmatter.util.DateFormat(
                                '',
                                new Date(+cellValue),
                                'UniversalSortableDateTime',
                                $.extend({}, $.jgrid.formatter.date, options)
                                );
                            } else {
                                return '';
                            }
                        }

                    },
                    {
                        name: 'disposition',
                        index: 'disposition'
                    },
                    {
                        name: 'duration',
                        index: 'duration'
                    }],
                    pager: '#' + attrs.callGrid,
                    width: '100%',
                    height: 'auto',
                    sortname: 'startDate',
                    sortorder: 'desc',
                    viewrecords: true,
                    gridComplete: function () {
                        angular.forEach(['phoneNumber', 'callDirection', 'startDate', 'answerDate', 'endDate', 'disposition', 'duration'], function (value) {

                            elem.jqGrid('setLabel', value, scope.msg('ivr.calllog.' + value));
                        });

                        $('#outsideCalllogTable').children('div').width('100%');
                        $('.ui-jqgrid-htable').addClass("table-lightblue");
                        $('.ui-jqgrid-btable').addClass("table-lightblue");
                        $('.ui-jqgrid-htable').addClass('table-lightblue');
                        $('#outsideCalllogTable').children('div').each(function() {
                            $('table', this).width('100%');
                            $(this).find('#resourceTable').width('100%');
                            $(this).find('table').width('100%');
                       });
                    }
                });

            }
        };
    });

}());
