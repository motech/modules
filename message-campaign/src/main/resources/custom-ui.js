var cronAttached = false;
$scope.unselectInstanceDefault = $scope.unselectInstance;

$scope.backToEntityList = function() {
    cronAttached = false;
    window.location.replace('#/messageCampaign/admin');
};

if ($routeParams.instanceId !== undefined) {
    $scope.unselectInstance = function() {
        cronAttached = false;
        window.location.replace('#/messageCampaign/campaigns');
    };
} else {
    $scope.unselectInstance = function() {
        cronAttached = false;
        $scope.unselectInstanceDefault();
    };
}

$scope.customModals = ['../messagecampaign/resources/partials/modals/edit-campaign.html',
                       '../messagecampaign/resources/partials/modals/delete-campaign.html',
                       '../messagecampaign/resources/partials/modals/campaign-validation-error.html'];

$scope.addEntityInstanceDefault = $scope.addEntityInstance;

$scope.addEntityInstanceModal = function() {
    if ($scope.selectedEntity.name === "CampaignRecord" && $scope.selectedInstance !== undefined) {
        $('#editCampaignModal').modal('show');
    } else {
        $scope.addEntityInstanceDefault();
    }
};

$scope.addEntityInstance = function() {
    if ($scope.selectedEntity.name === "CampaignRecord") {
        $scope.validateCampaignType();
    } else {
        $scope.addEntityInstanceDefault();
    }
};

$scope.deleteSelectedInstanceDefault = $scope.deleteSelectedInstance;

$scope.deleteSelectedInstance = function() {
    if ($scope.selectedEntity.name === "CampaignRecord") {
        $('#deleteCampaignModal').modal('show');
    } else {
        $scope.deleteSelectedInstanceDefault();
    }
};

$scope.loadEditValueForm = function (field) {
    var value = $scope.getTypeSingleClassName(field.type);

    if (value === 'boolean') {

        if (field.value === true) {
            field.value = 'true';

        } else if (field.value === false) {
            field.value = 'false';
        }
    } else if (value === 'combobox' && field.settings[2].value) {
        if (MDSUtils.find(field.settings, [{field: 'name', value: 'mds.form.label.allowMultipleSelections'}], true).value) {
            value = 'combobox-multi';
        }
    } else if (value === 'string' && field.name === 'owner') {
        value = 'string-owner';
    } else if (value === 'string' && field.name === 'cron' && field.nonDisplayable === false) {
        if (!cronAttached) {
            $timeout(function() {
                var initialCronValue = "* * * * *";
                if (field.value !== null && field.value !== undefined && field.value !== "") {
                    initialCronValue = field.value;
                }
                $('#cron-field').cron({
                    initial: initialCronValue,
                    onChange: function() {
                        field.value = $(this).cron("value");
                    }
                });
            }, 500);
            cronAttached = true;
        }
        return '../messagecampaign/resources/partials/widgets/field-edit-Value-cron.html';
    }
    return '../mds/resources/partials/widgets/field-edit-Value-{0}.html'
                  .format(value.substring(value.toLowerCase()));
};

$scope.invalidMessages = [];

$scope.validateCampaignType = function() {
    var campaignType = undefined;
    $scope.invalidMessages = [];

    angular.forEach ($scope.currentRecord.fields, function(field) {
        if (field.name === "campaignType") {
            campaignType = field.value;
        }
    });

    angular.forEach ($scope.currentRecord.fields, function(field) {
        if (field.name === "messages") {
            angular.forEach (field.value, function(value) {
                if (campaignType === "ABSOLUTE" && (value.date === null || value.date === undefined || value.date === "")) {
                    $scope.invalidMessages.push(value);
                } else if (campaignType === "OFFSET" && (value.timeOffset === null || value.timeOffset === undefined || value.timeOffset === "")) {
                    $scope.invalidMessages.push(value);
                } else if (campaignType === "REPEAT_INTERVAL" && (value.repeatEvery === null || value.repeatEvery === undefined || value.repeatEvery === "")) {
                    $scope.invalidMessages.push(value);
                } else if (campaignType === "DAY_OF_WEEK" && (value.repeatOn === null || value.repeatOn === undefined || value.repeatOn.length === 0)) {
                    $scope.invalidMessages.push(value);
                } else if (campaignType === "CRON" && (value.cron === null || value.cron === undefined || value.cron === "")) {
                    $scope.invalidMessages.push(value);
                }
            });
        }
    });

    if ($scope.invalidMessages.length > 0) {
        $('#campaignValidationErrorModal').modal('show');
    } else {
        $scope.addEntityInstanceModal();
    }
};

$scope.selectLookup = function(lookup) {
    var i;

    $scope.selectedLookup = lookup;
    $scope.lookupFields = [];
    $scope.lookupBy = {};

    for(i=0; i<$scope.allEntityFields.length; i+=1) {
        if ($.inArray($scope.allEntityFields[i].id, $scope.getLookupIds($scope.selectedLookup.lookupFields)) !== -1) {
            if ($scope.allEntityFields[i].basic.name === "messageType" && $scope.campaignType !== undefined) {
                $scope.lookupBy[$scope.allEntityFields[i].basic.name] = $scope.campaignType;
            } else {
                $scope.lookupFields.push($scope.allEntityFields[i]);
            }
        }
    }
};

$scope.setRelatedEntity = function(field) {
    $('#instanceBrowserModal').on('hide.bs.modal', function () {
        $scope.relatedEntity = undefined;
        $scope.filterBy = [];
    });

    var i, relatedClass;
    if (field.metadata !== undefined && field.metadata !== null && field.metadata.isArray === true) {
        for (i = 0 ; i < field.metadata.length ; i += 1) {
            if (field.metadata[i].key === "related.class") {
                relatedClass = field.metadata[i].value;
                break;
            }
        }
    }
    if (relatedClass !== undefined) {
        LoadingModal.open();
        $http.get('../mds/entities/getEntityByClassName?entityClassName=' + relatedClass).success(function (data) {
            $scope.relatedEntity = data;
            $scope.editedField = field;
            $scope.refreshInstanceBrowserGrid();

            //We need advanced options for related entity e.g. lookups
            Entities.getAdvancedCommited({id: $scope.relatedEntity.id}, function(data) {
                $scope.entityAdvanced = data;

                if (field.name === "messages") {
                    var lookups = $scope.entityAdvanced.indexes;
                    $scope.entityAdvanced.indexes = [];

                    angular.forEach (lookups, function(lookup) {
                        if (lookup.lookupFields.length > 1) {
                            angular.forEach (lookup.lookupFields, function(lookupField) {
                                if (lookupField.name === "messageType") {
                                    $scope.entityAdvanced.indexes.push(lookup);
                                }
                            });
                        }
                    });
                }
            });

            //We need related entity fields for lookups
            Entities.getEntityFields({id: $scope.relatedEntity.id},
                function (data) {
                    $scope.allEntityFields = data;
                },
                function (response) {
                    ModalFactory.handleResponse('mds.error', 'mds.dataBrowsing.error.instancesList', response);
                }
            );
            LoadingModal.close();

        }).error(function(response)
        {
            ModalFactory.handleResponse('mds.error', 'mds.dataBrowsing.error.instancesList', response);
        });
    }

    $scope.campaignType = undefined;

    if (field.name === "messages") {
        angular.forEach ($scope.currentRecord.fields, function(field) {
            if (field.name === "campaignType") {
                if (field.value !== null && field.value !== undefined) {
                    $scope.campaignType = field.value;
                } else {
                    $scope.campaignType = "";
                }
            }
        });

        $scope.filterBy.push({
            field: "messageType",
            values: [ $scope.campaignType ]
        });
    }
};

$scope.messageSelectedType = undefined;
$scope.campaignSelectedType = undefined;
$scope.campaignMessages = undefined;

$scope.messageTypeChanged = function(messageType) {
    angular.forEach ($scope.fields, function(field) {
        if (field.name === "cron") {
            if (messageType === "CRON") {
                field.nonDisplayable = false;
                field.required = true;
            } else {
                field.nonDisplayable = true;
                field.required = false;
                field.value = "";
                cronAttached = false;
            }
        } else if (field.name === "date") {
            if (messageType === "ABSOLUTE") {
                field.nonDisplayable = false;
                field.required = true;
                field.value = "";
            } else {
                field.nonDisplayable = true;
                field.required = false;
                field.value = "";
            }
        } else if (field.name === "timeOffset") {
            if (messageType === "OFFSET") {
                field.nonDisplayable = false;
                field.required = true;
            } else {
                field.nonDisplayable = true;
                field.required = false;
                field.value = "";
            }
        } else if (field.name === "repeatEvery") {
            if (messageType === "REPEAT_INTERVAL") {
                field.nonDisplayable = false;
                field.required = true;
            } else {
                field.nonDisplayable = true;
                field.required = false;
                field.value = "";
            }
        } else if (field.name === "repeatOn") {
            if (messageType === "DAY_OF_WEEK") {
                field.nonDisplayable = false;
                field.required = true;
            } else {
                field.nonDisplayable = true;
                field.required = false;
                field.value = null;
            }
        } else if (field.name === "startTime") {
            if (messageType === "CRON") {
                field.nonDisplayable = true;
                field.required = false;
            } else {
                field.nonDisplayable = false;
                field.required = true;
            }
        }
    });
};

$scope.addInstance = function(module, entityName) {
    LoadingModal.open();

    // load the entity if coming from the 'Add' link in the main DataBrowser page
    if (!$scope.selectedEntity) {
        $scope.retrieveAndSetEntityData('../mds/entities/getEntity/' + module + '/' + entityName);
    }

    $scope.instanceEditMode = false;
    cronAttached = false;
    if (!module) {
        if ($scope.selectedEntity.module === null) {
            module = '(No module)';
        } else {
            module = $scope.selectedEntity.module;
        }
    }
    if (!entityName) {
        entityName = $scope.selectedEntity.name;
    }
    $scope.setModuleEntity(module, entityName);
    $scope.addedEntity = Entities.getEntity({
        param:  module,
        params: entityName},
        function () {
            Instances.newInstance({id: $scope.addedEntity.id}, function(data) {
                $scope.currentRecord = data;
                $scope.fields = data.fields;
                angular.forEach($scope.fields, function(field) {
                    if ( field.type.typeClass === "java.util.List" && field.value !== null && field.value.length === 0 ) {
                        field.value = null;
                    }
                    if (entityName === "CampaignMessageRecord" && field.name === "messageType") {
                        $scope.messageSelectedType = field;
                    }
                    if (entityName === "CampaignRecord") {
                        if(field.name === "campaignType") {
                            $scope.campaignSelectedType = field;
                        } else if (field.name === "messages") {
                            $scope.campaignMessages = field;
                        }
                    }
                });
                LoadingModal.close();
            });
        });
};

$scope.editInstance = function(id, module, entityName) {
    LoadingModal.open();
    $scope.setHiddenFilters();
    $scope.instanceEditMode = true;
    cronAttached = false;
    $scope.setModuleEntity(module, entityName);
    $scope.loadedFields = Instances.selectInstance({
        id: $scope.selectedEntity.id,
        param: id
        },
        function (data) {
            $scope.selectedInstance = id;
            $scope.currentRecord = data;
            $scope.fields = data.fields;
            if (entityName === "CampaignMessageRecord") {
                var messageType = undefined;
                angular.forEach ($scope.fields, function(field) {
                    if (field.name === "messageType") {
                        messageType = field.value;
                        field.nonEditable = true;
                    } else if (field.name === "messageKey") {
                        field.nonEditable = true;
                    }
                });

                $scope.messageTypeChanged(messageType);
            } else if (entityName === "CampaignRecord") {
                angular.forEach ($scope.fields, function(field) {
                    if (field.name === "name") {
                        field.nonEditable = true;
                    } else if (field.name === "campaignType") {
                        $scope.campaignSelectedType = field;
                    } else if (field.name === "messages") {
                        $scope.campaignMessages = field;
                    }
                });
            }

            LoadingModal.close();
        }, ModalFactory.angularHandler('mds.error', 'mds.error.cannotUpdateInstance'));
};

$scope.$watch('messageSelectedType.value', function(newValue) {
    $scope.messageTypeChanged(newValue);
});

$scope.$watch('campaignSelectedType.value', function(newVal, oldVal) {
    if ($scope.campaignMessages) {
        var messages = $scope.campaignMessages.value;
        $scope.campaignMessages.value = [];
        if (newVal !== null && newVal !== undefined) {
            angular.forEach (messages, function(message) {
                if (newVal === "ABSOLUTE" && message.date !== null && message.date !== undefined && message.date !== "") {
                    $scope.campaignMessages.value.push(message);
                } else if (newVal === "OFFSET" && message.timeOffset !== null && message.timeOffset !== undefined && message.timeOffset !== "") {
                    $scope.campaignMessages.value.push(message);
                } else if (newVal === "REPEAT_INTERVAL" && message.repeatEvery !== null && message.repeatEvery !== undefined && message.repeatEvery !== "") {
                    $scope.campaignMessages.value.push(message);
                } else if (newVal === "DAY_OF_WEEK" && message.repeatOn !== null && message.repeatOn !== undefined && message.repeatOn.length > 0) {
                    $scope.campaignMessages.value.push(message);
                } else if (newVal === "CRON" && message.cron !== null && message.cron !== undefined && message.cron !== "") {
                    $scope.campaignMessages.value.push(message);
                }
            });
        }
        if ($scope.campaignMessages.value.length === 0) {
            $scope.campaignMessages.value = null;
        }
    }
});
