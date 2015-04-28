(function () {
    'use strict';

    var controllers = angular.module('messageCampaign.controllers', []);

    controllers.controller('MCCampaignsCtrl', function ($scope, Campaigns) {

        $scope.$on('$viewContentLoaded', function () {
            $scope.campaigns = Campaigns.query();
        });

        $scope.deleteCampaign = function(campaignName) {
            motechConfirm("msgCampaign.campaign.deleteConfirmMsg", "msgCampaign.campaign.deleteConfirmTitle",
                function (response) {
                    if (!response) {
                        return;
                    }
                    jQuery.ajax({
                        type: "DELETE",
                        url: "../messagecampaign/campaigns/" + campaignName,
                        success: function() {
                            window.location.reload(true);
                        }
                    });
                });
        };

        $scope.editCampaign = function(campaignName) {
            jQuery.ajax({
                type: "GET",
                url: "../messagecampaign/campaign-record/" + campaignName,
                success: function(response) {
                    window.location.replace('#/messageCampaign/campaigns/' + response.id);
                }
            });
        };
    });

     controllers.controller('MCEnrollmentsCtrl', function ($scope, $routeParams, Enrollments) {

        $scope.campaignName = $routeParams.campaignName;

        function getPanelWidth() {
            return document.getElementById("main-content").offsetWidth-13;
        }

        jQuery(window).bind('resize', function() {
            jQuery("#enrollmentsTable").jqGrid('setGridWidth', getPanelWidth());
        }).trigger('resize');

        function validateExternalId(value, columnName) {
            if (value.length > 0) {
                return [true, ""];
            }
            return [false, "msgCampaign.enrollment.emptyExternalId"];
        }

        $scope.$on('$viewContentLoaded', function () {

            var createOrUpdateEnrollementUrl = "../messagecampaign/enrollments/" + $scope.campaignName + "/users",
                getEnrollementsUrl = "../messagecampaign/enrollments/users?campaignName=" + $scope.campaignName,
                deleteEnrollementUrl = "../messagecampaign/enrollments/" + $scope.campaignName + "/users/",
                newRowPrefix = "NEW_ROW_",
                isNewRow = function (rowId) {
                    return rowId.startsWith(newRowPrefix);
                };

            function deleteFormatter(cellvalue, options, rowObject) {
                return (
                    "<div style='margin-left:8px;'>" +
                    "<div " +
                    "class='ui-pg-div ui-inline-del'" +
                    "onmouseout='jQuery(this).removeClass(\"ui-state-hover\");'" +
                    "onmouseover='jQuery(this).addClass(\"ui-state-hover\");'" +
                    "onclick='deleteRows([\"" + options.rowId + "\"]);'" +
                    "style='float:left;margin-left:5px;'" +
                    "title='Delete selected row'>" +
                    "<span class='ui-icon ui-icon-trash'></span>" +
                    "</div>" +
                    "</div>").toString();
            }

            jQuery("#enrollmentsTable").jqGrid({
                caption:"Enrollments for Campaign - " + $scope.campaignName,
                url:getEnrollementsUrl,
                datatype:"json",
                jsonReader:{
                    root:"enrollments",
                    id:"0",
                    repeatitems:false
                },
                colNames:['Enrollment ID', 'ID', 'Edit', 'Delete'],
                colModel:[
                    {name:'enrollmentId', index:'enrollmentId', hidden:true, editable:true},
                    {
                        name:'externalId',
                        index:'externalId',
                        sortable:false,
                        editable:true,
                        editrules:{
                            custom:true,
                            custom_func:validateExternalId
                        }
                    },
                    {
                        name:'edit',
                        formatter:'actions',
                        formatoptions:{
                            keys:true,
                            editbutton:true,
                            delbutton:false,
                            url:createOrUpdateEnrollementUrl,
                            mtype:"POST",
                            onSuccess:function () {
                                jQuery("#enrollmentsTable").trigger('reloadGrid');
                            },
                            afterRestore:function (rowId) {
                                var grid = jQuery("#enrollmentsTable"),
                                    externalId = grid.jqGrid('getRowData', rowId).externalId;
                                if (!externalId) {
                                    grid.jqGrid('delRowData', rowId);
                                }
                            }
                        }
                    },
                    {
                        name:'delete',
                        formatter:deleteFormatter
                    }
                ],
                width:getPanelWidth(),
                height:"auto",
                multiselect:true
            });

            function deleteRows(rowIds) {
                if (rowIds.length === 0) {
                    motechAlert("msgCampaign.enrollment.noUserSelected", "msgCampaign.enrollment.invalidAction");
                    return;
                }
                motechConfirm("msgCampaign.enrollment.deleteConfirmMsg", "msgCampaign.enrollment.deleteConfirmTitle",
                    function (response) {
                    var i, rowData, grid = jQuery("#enrollmentsTable"),
                        refresh, rowId,
                        successFun = function (){
                                         if(refresh) {
                                             jQuery("#enrollmentsTable").trigger('reloadGrid');
                                         }
                                     };
                        if (!response) {
                            return;
                        }
                        for (i = 0; i < rowIds.length; i+=1) {
                            refresh = (i === rowIds.length - 1);
                            rowId = rowIds[i];
                            if (isNewRow(rowId)) {
                                grid.jqGrid('delRowData', rowId);
                            }
                            else {
                                rowData = grid.jqGrid("getRowData", rowId);
                                jQuery.ajax({
                                    type:"DELETE",
                                    url:deleteEnrollementUrl + rowData.externalId,
                                    success: successFun
                                });
                            }
                        }
                    });
            }

            jQuery("#deleteEnrollments").click(function () {
                var grid = jQuery("#enrollmentsTable"),
                rowIds = grid.jqGrid('getGridParam', 'selarrrow');
                deleteRows(rowIds);
            });

            jQuery("#addEnrollment").click(function () {
                var i, grid = jQuery("#enrollmentsTable"),
                    rowIds = grid.jqGrid('getDataIDs'),
                    rowId, row;
                for (i = 0; i < rowIds.length; i+=1) {
                    if (isNewRow(rowIds[i])) {
                        motechAlert("msgCampaign.enrollment.unsavedEnrollement", "msgCampaign.enrollment.invalidAction");
                        return;
                    }
                }
                rowId = newRowPrefix + Math.round(Math.random() * 10000);
                grid.jqGrid('addRowData', rowId, {});
                row = jQuery(grid.jqGrid('getInd', rowId, true));
                row.find('.ui-inline-edit').click();
                row.find('input').focus();
            });

            $.extend($.jgrid, {
                info_dialog:function (caption, content, c_b, modalopt) {
                    setTimeout(function () {
                        motechAlert(content.trim(), "msgCampaign.enrollment.invalidAction");
                    }, 0);
                }
            });
        });
    });

    controllers.controller('MCSettingsCtrl', function ($scope) {
        $scope.uploadSettings = function () {
            blockUI();
            $("#messageCampaignSettingsForm").ajaxSubmit({
                success: function() {
                    motechAlert('msgCampaign.settings.success.saved', 'msgCampaign.saved');
                    unblockUI();
                },
                error: function(error) {
                    if (error.status === 403) {
                        motechAlert('msgCampaign.settings.error.permission', 'msgCampaign.error');
                    } else {
                        jAlert(error.responseText, $scope.msg('msgCampaign.error'));
                    }
                    unblockUI();
                }
            });
        };

        $("#messageCampaigns").change(function() {
            if(!$('#messageCampaigns').val()) {
                $('input[type="button"]').attr('disabled','disabled');
            }
            else if($('#messageCampaigns').val().split(".").pop().toLowerCase() === "json") {
                $('input[type="button"]').removeAttr('disabled');
            }
            else {
                $('input[type="button"]').attr('disabled','disabled');
                motechAlert('msgCampaign.settings.notSupported', 'msgCampaign.error');
            }


        });
    });

}());
