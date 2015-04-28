$scope.backToEntityList = function() {
    window.location.replace('#/messageCampaign/admin');
};

if ($routeParams.instanceId !== undefined) {
    $scope.unselectInstance = function() {
        window.location.replace('#/messageCampaign/campaigns');
    };
}

$scope.customModals = ['../messagecampaign/resources/partials/modals/edit-campaign.html', '../messagecampaign/resources/partials/modals/delete-campaign.html'];

$scope.addEntityInstanceDefault = $scope.addEntityInstance;

$scope.addEntityInstance = function() {
    if ($scope.selectedEntity.name === "CampaignRecord" && $scope.selectedInstance !== undefined) {
        $('#editCampaignModal').modal('show');
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
}