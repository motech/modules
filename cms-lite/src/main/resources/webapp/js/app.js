(function () {
    'use strict';

    /* App Module */

    var cmslite = angular.module('cmslite', ['motech-dashboard', 'cmslite.controllers', 'cmslite.services', 'cmslite.directives',
                                 'ngCookies', 'motech-widgets', 'uiServices']);
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

    cmslite.config(['$stateProvider', function ($stateProvider) {
        $stateProvider
            .state('cmslite', {
                url: "/cmslite",
                abstract: true,
                views: {
                    "moduleToLoad": {
                        templateUrl: "../cmsliteapi/resources/index.html"
                    }
                }
            })
            .state('cmslite.resources', {
                url: '/resources',
                parent: 'cmslite',
                views: {
                    'cmsliteView': {
                        templateUrl: '../cmsliteapi/resources/partials/resources.html',
                        controller: 'CmsResourceCtrl'
                    }
                }
            });
        }
    ]);
}());
