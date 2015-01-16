(function () {
    'use strict';

    /* App Module */

    var cmslite = angular.module('cmslite', ['motech-dashboard', 'cmslite.controllers', 'cmslite.services', 'cmslite.directives',
                                 'ngCookies', 'motech-widgets']);
    $.ajax({
        url: '../cmsliteapi/resource/all/languages',
        success:  function(data) {
            cmslite.value("usedLanguages", data);
        },
        async: false
    });

    cmslite.run(function ($rootScope, usedLanguages) {
        $rootScope.usedLanguages = usedLanguages;
    });

    cmslite.config(['$routeProvider',
        function ($routeProvider) {
            $routeProvider.when('/cmslite/resources',
                {
                    templateUrl: '../cmsliteapi/resources/partials/resources.html',
                    controller: 'CmsResourceCtrl'
                }
            );
        }
    ]);
}());
