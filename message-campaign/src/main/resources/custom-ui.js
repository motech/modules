$scope.backToEntityList = function() {
    window.location.replace('#/messageCampaign/admin');
};

if ($routeParams.instanceId !== undefined) {
    $scope.unselectInstance = function() {
        window.location.replace('#/messageCampaign/campaigns');
    };
}
