(function () {
    'use strict';

    var mtrainingModule =  angular.module('mtraining', ['motech-dashboard', 'mtraining.controllers', 'ngCookies', 'ui.bootstrap',
        'mds']), entities;

    $.ajax({
        url: '../mds/entities/getEntitiesByBundle?symbolicName=org.motechproject.mtraining',
        success:  function(data) {
            entities = data;
        },
        async: false
    });

    mtrainingModule.config(['$routeProvider',
        function ($routeProvider) {
            var i, idMapping = {};
            $routeProvider.when('/mtraining/treeView', {templateUrl: '../mtraining/resources/partials/treeView.html', controller: 'TreeViewController'});

            for (i = 0 ; i < entities.length ; i += 1) {
                idMapping[entities[i].name] = entities[i].id;
            }

            $routeProvider.when('/mtraining/courses', {redirectTo: 'mds/dataBrowser/' + idMapping.Course + '/mtraining'});
            $routeProvider.when('/mtraining/chapters', {redirectTo: 'mds/dataBrowser/' + idMapping.Chapter + '/mtraining'});
            $routeProvider.when('/mtraining/quizzes', {redirectTo: 'mds/dataBrowser/' + idMapping.Quiz + '/mtraining'});
            $routeProvider.when('/mtraining/lessons', {redirectTo: 'mds/dataBrowser/' + idMapping.Lesson + '/mtraining'});
            $routeProvider.when('/mtraining/bookmarks', {redirectTo: 'mds/dataBrowser/' + idMapping.Bookmark + '/mtraining'});
            $routeProvider.when('/mtraining/activityRecords', {redirectTo: 'mds/dataBrowser/' + idMapping.ActivityRecord + '/mtraining'});
    }]);
}());
