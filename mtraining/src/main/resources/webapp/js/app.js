(function () {
    'use strict';

    var mtrainingModule =  angular.module('mtraining', ['motech-dashboard', 'mtraining.controllers', 'ngCookies', 'ui.bootstrap',
        'mds']), idMapping = {};

    $.ajax({
        url: '../mds/entities/getEntitiesByBundle?symbolicName=org.motechproject.mtraining',
        success:  function(data) {
            var i;
            for (i = 0 ; i < data.length ; i += 1) {
                idMapping[data[i].name] = data[i].id;
            }
        },
        async: false
    });

    getAvailableTabs('mTraining', function(data) {
         mtrainingModule.constant('MTRAINING_AVAILABLE_TABS', data);
    });

    mtrainingModule.run(function ($rootScope, MTRAINING_AVAILABLE_TABS) {
        $rootScope.MTRAINING_AVAILABLE_TABS = MTRAINING_AVAILABLE_TABS;
    });

    mtrainingModule.config(function ($routeProvider, MTRAINING_AVAILABLE_TABS) {
        var i, key, tab;

        for (i = 0; i < MTRAINING_AVAILABLE_TABS.length; i = i + 1) {
            tab = MTRAINING_AVAILABLE_TABS[i];

            if (tab === "treeView") {
                $routeProvider.when('/mtraining/treeView', {templateUrl: '../mtraining/resources/partials/treeView.html', controller: 'TreeViewController'});
            } else if (tab === "quizzes") {
                $routeProvider.when('/mtraining/quizzes', {redirectTo: 'mds/dataBrowser/' + idMapping.Quiz + '/mtraining'});
            } else {
                key = tab.capitalize().substring(0, tab.length - 1);
                $routeProvider.when('/mtraining/{0}'.format(tab), {redirectTo: 'mds/dataBrowser/'+idMapping[key]+'/mtraining'});
            }
        }
    });
}());
