(function () {
    'use strict';

    /* Directives */

    var directives = angular.module('messageCampaign.directives', []);

    // TODO: Can be removed after common file upload directive work again. (MOTECH-2265)
    directives.directive('motechFileUploadMessageCampaign', function($http, $templateCache, $compile) {
        return function(scope, element, attrs) {
            $http.get('../server/resources/partials/motech-file-upload.html', { cache: $templateCache }).success(function(response) {
                var contents = element.html(response).contents();
                element.replaceWith($compile(contents)(scope));
            });
        };
    });
}());