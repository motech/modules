(function () {
    'use strict';

    var directives = angular.module('sms.directives', []);
    // from http://stackoverflow.com/questions/14859266/input-autofocus-attribute
    angular.module('ng').directive('ngFocus', function($timeout) {
        return {
            link: function ( scope, element, attrs ) {
                scope.$watch( attrs.ngFocus, function ( val ) {
                    if ( angular.isDefined( val ) && val ) {
                        $timeout( function () { element[0].focus(); } );
                    }
                }, true);

                element.bind('blur', function () {
                    if ( angular.isDefined( attrs.ngFocusLost ) ) {
                        scope.$apply( attrs.ngFocusLost );

                    }
                });
            }
        };
    });

    // TODO: Can be removed after common file upload directive work again. (MOTECH-2265)
    directives.directive('motechFileUploadSms', function($http, $templateCache, $compile) {
        return function(scope, element, attrs) {
            $http.get('../server/resources/partials/motech-file-upload.html', { cache: $templateCache }).success(function(response) {
                var contents = $compile(element.html(response).contents())(scope),
                    input = contents.find("input");
                element.replaceWith(contents);
                if (attrs.accept !== null &&  attrs.accept !== "") {
                    input.attr("accept", attrs.accept);
                }
            });
        };
    });
}());